package com.xin.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.xin.generator.MainGenerator;
import com.xin.model.MainTemplateConfig;
import freemarker.template.TemplateException;
import lombok.Data;
import picocli.CommandLine;

import java.io.IOException;
import java.util.concurrent.Callable;
@Data
@CommandLine.Command(name = "generate", mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {
    /**
     * 作者注释
     */
    @CommandLine.Option(names = {"-a", "--author"}, description = "作者名称", arity = "0..1", interactive = true, echo = true)
    private String author = "xin";
    /**
     * 输出信息
     */
    @CommandLine.Option(names = {"-o", "--output"}, description = "输出文本", arity = "0..1", interactive = true, echo = true)
    private String outputText = "结果：";
    /**
     * 是否循环
     */
    @CommandLine.Option(names = {"-l", "--loop"}, description = "是否循环", arity = "0..1", interactive = true, echo = true)
    private boolean loop = true;

    @Override
    public Integer call() throws TemplateException, IOException {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        BeanUtil.copyProperties(this, mainTemplateConfig);
        MainGenerator.doGenerator(mainTemplateConfig);
        return 0;
    }
}
