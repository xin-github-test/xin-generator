package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;
import lombok.Data;
import picocli.CommandLine;

import java.io.IOException;
import java.util.concurrent.Callable;

<#-- 生成选项 -->
<#macro generateOption indent modelInfo>
${indent}@CommandLine.Option(names = {<#if modelInfo.abbr??>"-${modelInfo.abbr}",</#if> "--${modelInfo.fieldName}"}, <#if modelInfo.description??>description = "${modelInfo.description}",</#if> arity = "0..1", interactive = true, echo = true)
${indent}private ${modelInfo.type} ${modelInfo.fieldName} <#if modelInfo.defaultValue??>= ${modelInfo.defaultValue?c}</#if>;
</#macro>

<#macro generateCondition indent modelInfo>
${indent}System.out.println("输入${modelInfo.groupName}配置：");
${indent}CommandLine ${modelInfo.groupKey}CommandLine = new CommandLine(${modelInfo.type}Command.class);
${indent}${modelInfo.groupKey}CommandLine.execute(${modelInfo.allArgsStr});
</#macro>
@Data
@CommandLine.Command(name = "generate", description = "生成代码", mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {

<#list modelConfig.models as modelInfo>
    <#--有分组 -->
    <#if modelInfo.groupKey??>
    /**
     * ${modelInfo.groupName}
     */
    static DataModel.${modelInfo.type} ${modelInfo.groupKey} = new DataModel.${modelInfo.type}();
    <#--生成命令类 -->
    @CommandLine.Command(name = "${modelInfo.groupName}", description = "${modelInfo.description}")
    public static class ${modelInfo.type}Command implements Runnable {
        <#list modelInfo.models as subModelInfo>
            <@generateOption indent="        " modelInfo=subModelInfo />
        </#list>
        @Override
        public void run() {
            <#list modelInfo.models as subModelInfo>
            ${modelInfo.groupKey}.${subModelInfo.fieldName} = ${subModelInfo.fieldName};
            </#list>
        }
    }
    <#else>
        <@generateOption indent="    " modelInfo=modelInfo />
    </#if>  
</#list>

    @Override
    public Integer call() throws TemplateException, IOException {
        <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
        <#if modelInfo.groupKey??>
        if (${modelInfo.condition}) {
            <@generateCondition indent="            " modelInfo=modelInfo />
        }
        <#else>
        <@generateCondition indent="        " modelInfo=modelInfo />
        </#if>
        </#if>
        </#list>

        <#-- 填充数据模型对象 -->
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
        dataModel.${modelInfo.groupKey} = ${modelInfo.groupKey};
        </#if>
        </#list>
        MainGenerator.doGenerate(dataModel);
        return 0;
    }
}
