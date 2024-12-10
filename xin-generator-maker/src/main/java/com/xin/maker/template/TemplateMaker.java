package com.xin.maker.template;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.xin.maker.meta.Meta;
import com.xin.maker.meta.Meta.FileConfig.FileInfo;
import com.xin.maker.meta.Meta.ModelConfig.ModelInfo;
import com.xin.maker.meta.enunms.FileGenerateEnum;
import com.xin.maker.meta.enunms.FileTypeEnum;
import com.xin.maker.template.enums.FileFilterRangeEnum;
import com.xin.maker.template.enums.FileFilterRuleEnum;
import com.xin.maker.template.model.FileFilterConfig;
import com.xin.maker.template.model.TemplateMakerFileConfig;
import com.xin.maker.template.model.TemplateMakerFileConfig.FileGroupConfig;
import com.xin.maker.template.model.TemplateMakerFileConfig.FileInfoConfig;
import com.xin.maker.template.model.TemplateMakerModelConfig.ModelInfoConfig;
import com.xin.maker.template.model.TemplateMakerModelConfig;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
//48-5

/**
 * 模板制作工具
 */
public class TemplateMaker {
    /**
     * 制作模板
     * @param meta
     * @param originProjectPath
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param id
     * @return
     */
    private static long makeTemplate(Meta meta, String originProjectPath, TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig, Long id) {
        //没有的id则生成
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();
        }
        
        //复制目录
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originProjectPath, templatePath, true);
        }

        //处理模型信息
        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();
        //转换为配置文件可接收的对象
        List<Meta.ModelConfig.ModelInfo> inputModelInfoList = models.stream()
        .map(modelInfoConfig -> {
            Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelInfoConfig, modelInfo);
            return modelInfo;
        })
        .collect(Collectors.toList());
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>();
        //如果是模型组
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if (modelGroupConfig != null) {
            String condition = modelGroupConfig.getCondition();
            String groupKey = modelGroupConfig.getGroupKey();
            String groupName = modelGroupConfig.getGroupName();

            ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();
            groupModelInfo.setCondition(condition);
            groupModelInfo.setGroupKey(groupKey);
            groupModelInfo.setGroupName(groupName);
            //将这一次处理的模型放在同一个分组内
            groupModelInfo.setModels(inputModelInfoList);
            newModelInfoList = new ArrayList<>();
            newModelInfoList.add(groupModelInfo);
        } else {
            newModelInfoList.addAll(inputModelInfoList);
        }


        //2.输入文件信息
        //要挖坑的项目的目录
        File templatePathFile = new File(templatePath);
        templatePath = templatePathFile.getAbsolutePath();
        String sourcePath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath));
        sourcePath = sourcePath.replace("\\", "/");
        
        //遍历输入文件
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateMakerFileConfig.getFiles();
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {
            String inputFilePath = fileInfoConfig.getPath();
            String inputFileAbsPath = sourcePath + "/" + inputFilePath;

            //传入绝对路径,得到过滤后的文件列表
            List<File> fileList = FileFilter.doFilter(inputFileAbsPath, fileInfoConfig.getFilterConfigList());;
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig, sourcePath, file);
                newFileInfoList.add(fileInfo);
            }
        }

        //如果是文件组
        FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if (fileGroupConfig != null) {
            String condition = fileGroupConfig.getCondition();
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();

            FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
            groupFileInfo.setCondition(condition);
            groupFileInfo.setGroupKey(groupKey);
            groupFileInfo.setGroupName(groupName);
            //将这一次处理的文件放在同一个分组内
            groupFileInfo.setFiles(newFileInfoList);
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);
        }


        //三、生成配置文件
        String metaOutputPath = sourcePath + File.separator + "meta.json";
        
        //已有 meta 文件,不是第一次制作, 则在 meta 文件的基础上修改
        if (FileUtil.exist(metaOutputPath)) {
            meta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            //1.追加配置参数
            List<FileInfo> fileInfoList = meta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);

            List<ModelInfo> modelInfoList = meta.getModelConfig().getModels();
            modelInfoList.addAll(newModelInfoList);

            //配置去重
            meta.getFileConfig().setFiles(distinctFils(fileInfoList));
            meta.getModelConfig().setModels(distinctModels(modelInfoList));
            
        } else {
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            meta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourcePath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);

            
            fileInfoList.addAll(newFileInfoList);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            meta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.addAll(newModelInfoList);
        
        }
        //2.生成元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(meta), metaOutputPath);
        return id;
    }
    /**
     * 制作文件模板 
     * @param templateMakerModelConfig
     * @param sourcePath
     * @param inputFile
     * @return
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, String sourcePath, File inputFile) {
        //要挖坑的文件
        //得到相对路径
        String fileInputAbsPath = inputFile.getAbsolutePath().replace("\\", "/");
        String inputFilePath = fileInputAbsPath.replace(sourcePath + "/", "");
        String outputFilePath = inputFilePath + ".ftl";

        //二、使用字符串替换,生成模板文件
        String fileOutputAbsPath = fileInputAbsPath + ".ftl";

        String fileContent;
        //如果已有模板文件,表示不是第一次制作,则在原有模板的基础上再挖坑
        if (FileUtil.exist(fileOutputAbsPath)) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsPath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsPath);
        }

        //支持多个模型，对于同一个文件的内容，遍历模型进行多轮替换
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        String newFileContent = fileContent;
        String replacement;
        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {
            //模型配置
            //不是分组
            String fieldName = modelInfoConfig.getFieldName();
            if (modelGroupConfig == null) {
                replacement = String.format("${%s}", fieldName);
            } else {
                String groupKey = modelGroupConfig.getGroupKey();
                replacement = String.format("${%s.%s}", groupKey, fieldName);
            }

            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), replacement);
        }

        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(inputFilePath);
        fileInfo.setOutputPath(outputFilePath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateEnum.DYNAMIC.getValue());

        //判断新文件内容和源文件内容是否一致
        if (newFileContent.equals(fileContent)) {
            fileInfo.setOutputPath(inputFilePath);
            fileInfo.setGenerateType(FileGenerateEnum.STATIC.getValue());
        } else {
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsPath);
        }
        
        return fileInfo;
    }
     
    /**
     * 去重模型信息 
     * @param modelInfoList
     * @return
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
           
        //增加分组后，去重功能需要更新
        //1.将所有配置模型（modelInfo）分为有分组和无分组
        //以组为单位的划分
        Map<String, List<Meta.ModelConfig.ModelInfo>> groupKeyModelInfoListMap = modelInfoList.stream()
        .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
        .collect(Collectors.groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey));
        //合并后的对象 Map
        Map<String, Meta.ModelConfig.ModelInfo> groupKeyMergedModelInfoMap = new HashMap<>();
        //2.对于有分组的模型配置，若有相同分组则进行合并，不同则保留
        //同组内配置合并
        for (Map.Entry<String, List<Meta.ModelConfig.ModelInfo>> entry : groupKeyModelInfoListMap.entrySet()) {
            List<Meta.ModelConfig.ModelInfo> tempModelInfoList = entry.getValue();
            
            List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(tempModelInfoList.stream()
            .flatMap(modelInfo -> modelInfo.getModels().stream())
            .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r))
            .values());

            //使用新的组配置覆盖旧的组配置
            Meta.ModelConfig.ModelInfo newModelInfo = CollUtil.getLast(tempModelInfoList);
            newModelInfo.setModels(newModelInfoList);

            String groupKey = entry.getKey();
            groupKeyMergedModelInfoMap.put(groupKey, newModelInfo);
        }

        //3.创建新的模型配置列表（结果列表），先将合并后的分组添加到结果列表
        ArrayList<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(groupKeyMergedModelInfoMap.values());
        //4.将无分组的模型配置列表添加到结果列表
        
        
        resultList.addAll(new ArrayList<>(modelInfoList.stream()
        .filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
        .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)).values()));
        return resultList;
    }

    /**
     * 去重文件信息 
     * @param fileInfoList
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> distinctFils(List<Meta.FileConfig.FileInfo> fileInfoList) {
        
        //增加分组后，去重功能需要更新
        //1.将所有配置文件（fileInfo）分为有分组和无分组
        //以组为单位的划分
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoListMap = fileInfoList.stream()
        .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
        .collect(Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey));
        //合并后的对象 Map
        Map<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        //2.对于有分组的文件配置，若有相同分组则进行合并，不同则保留
        //同组内配置合并
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()) {
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
            .flatMap(fileInfo -> fileInfo.getFiles().stream())
            .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r))
            .values());

            //使用新的组配置覆盖旧的组配置
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            newFileInfo.setFiles(newFileInfoList);

            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey, newFileInfo);
        }

        //3.创建新的文件配置列表（结果列表），先将合并后的分组添加到结果列表
        ArrayList<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());
        //4.将无分组的文件配置列表添加到结果列表
        
        
        resultList.addAll(new ArrayList<>(fileInfoList.stream()
        .filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
        .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)).values()));
        return resultList;
    }

    public static void main(String[] args) {
        //1. 项目的基本信息
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM 示例模板生成器");

        //指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "xin-generator-demo-projects/springboot-init";
        originProjectPath = originProjectPath.replace("\\", "/");

        //指定路径
        String inputFilePath = "src/main/java/com/yupi/springbootinit/common";
        String inputFilePath1 = "src/main/resources/application.yml";


        List<String> inputFilePathList = new ArrayList<>();
        inputFilePathList.add(inputFilePath);

        //3.输入模型参数信息(第一次)
/*         Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("outputText");
        modelInfo.setType("String");
        modelInfo.setDescription("输出文本");
        modelInfo.setDefaultValue("sum = ");

        String searchStr = "Sum: "; */

        //3.输入模型参数信息(第二次)
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("className");
        modelInfo.setType("String");
        modelInfo.setDescription("类名");
        modelInfo.setDefaultValue("MainTemplate");

        // String searchStr = "MainTemplate";
        String searchStr = "BaseResponse";

        // 文件过滤配置
        FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath);

        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
        .range(FileFilterRangeEnum.FILE_NAME.getValue())
        .rule(FileFilterRuleEnum.CONTAINS.getValue())
        .value("Base")
        .build();
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig1.setFilterConfigList(fileFilterConfigList);

        FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(inputFilePath1);

        List<FileInfoConfig> fileInfoConfigList = Arrays.asList(fileInfoConfig1, fileInfoConfig2);

        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        templateMakerFileConfig.setFiles(fileInfoConfigList);

        //分组配置
        FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setCondition("outputText");
        fileGroupConfig.setGroupKey("test1");
        fileGroupConfig.setGroupName("测试分组2");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);

        //模型组配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();

        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
        modelGroupConfig.setGroupKey("mysql");
        modelGroupConfig.setGroupName("数据库配置");
        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);

        //模型配置
        ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDescription("url");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("host");  
        
        ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig2.setFieldName("username");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDescription("用户名");
        modelInfoConfig2.setDefaultValue("root");
        modelInfoConfig2.setReplaceText("root");

        List<ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1, modelInfoConfig2);
        templateMakerModelConfig.setModels(modelInfoConfigList);

        long id = makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1866440169054879744L);
        System.out.println(id);
    }
}
