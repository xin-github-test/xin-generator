package com.xin.maker.template;

import com.xin.maker.meta.Meta;
import com.xin.maker.template.model.TemplateMakerConfig;
import com.xin.maker.template.model.TemplateMakerFileConfig;
import com.xin.maker.template.model.TemplateMakerModelConfig;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TemplateMakerTest {
    /**
     * bug1 当一个动态生成文件第二次执行后，会变成static（已修复）
     */
    @Test
    public void testMakeTemplateBug1() {
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

        List<String> inputFilePathList = new ArrayList<>();
        inputFilePathList.add(inputFilePath);


        // 文件过滤配置
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath);

        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = Arrays.asList(fileInfoConfig1);

        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        templateMakerFileConfig.setFiles(fileInfoConfigList);


        //模型组配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        //模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("className");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDescription("url");
        modelInfoConfig1.setReplaceText("BaseResponse");


        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1);
        templateMakerModelConfig.setModels(modelInfoConfigList);

        long id = TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1L);
        System.out.println(id);
    }

    /**
     * bug2 当已经存在ftl文件后，再一次执行会把ftl文件也当作普通文件进行扫描
     * 实际我们只需要针对普通文件进行扫描，跳过生成的ftl文件(已修复)
     */ 
    @Test
    public void testMakeTemplateBug2() {
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

        List<String> inputFilePathList = new ArrayList<>();
        inputFilePathList.add(inputFilePath);


        // 文件过滤配置
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath);

        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = Arrays.asList(fileInfoConfig1);

        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        templateMakerFileConfig.setFiles(fileInfoConfigList);


        //模型组配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        //模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("className");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDescription("url");
        modelInfoConfig1.setReplaceText("BaseResponse");


        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1);
        templateMakerModelConfig.setModels(modelInfoConfigList);

        long id = TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1L);
        System.out.println(id);
    }

    /**
     * bug3 meta.json文件中的inputPath和outputPath的顺序应该调换，应该是根据ftl文件生成对应的java文件(已修复)
     */
    @Test
    public void testMakeTemplateBug3() {
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

        List<String> inputFilePathList = new ArrayList<>();
        inputFilePathList.add(inputFilePath);


        // 文件过滤配置
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath);

        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = Arrays.asList(fileInfoConfig1);

        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        templateMakerFileConfig.setFiles(fileInfoConfigList);


        //模型组配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        //模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("className");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDescription("url");
        modelInfoConfig1.setReplaceText("BaseResponse");


        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1);
        templateMakerModelConfig.setModels(modelInfoConfigList);

        long id = TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1L);
        System.out.println(id);
    }

    /**
     * bug4 当生成的目录指定为整个项目时，会将meta.json文件当成普通文件并生成对应的ftl文件（已修复）
     */
    @Test
    public void testMakeTemplateBug4() {
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

        List<String> inputFilePathList = new ArrayList<>();
        inputFilePathList.add(inputFilePath);


        // 文件过滤配置
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath);

        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = Arrays.asList(fileInfoConfig1);

        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        templateMakerFileConfig.setFiles(fileInfoConfigList);


        //模型组配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        //模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("className");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDescription("url");
        modelInfoConfig1.setReplaceText("BaseResponse");


        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1);
        templateMakerModelConfig.setModels(modelInfoConfigList);

        long id = TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1L);
        System.out.println(id);
    }

    /**
     * 通过json文件进行配置，并用对应的类进行接收
     */
    @Test
    public void testMakeTemplateWithJSON() {
        String configStr = ResourceUtil.readUtf8Str("templateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        long id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
    }
}