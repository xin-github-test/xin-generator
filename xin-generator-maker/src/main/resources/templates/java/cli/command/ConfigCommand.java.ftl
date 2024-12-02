package ${basePackage}.cli.command;

import cn.hutool.core.util.ReflectUtil;
import ${basePackage}.model.DataModel;
import picocli.CommandLine;

import java.lang.reflect.Field;

@CommandLine.Command(name = "config", mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable {

    /**
     * 输出配置信息
     */
    @Override
    public void run() {
        Field[] fields = ReflectUtil.getFields(DataModel.class);
        for (Field field : fields) {
            System.out.println("字段类型："+field.getType());
            System.out.println("字段名称："+field.getName());
        }
    }
}
