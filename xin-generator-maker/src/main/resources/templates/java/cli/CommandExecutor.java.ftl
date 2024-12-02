package ${basePackage}.cli;

import ${basePackage}.cli.command.ConfigCommand;
import ${basePackage}.cli.command.GenerateCommand;
import ${basePackage}.cli.command.ListCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine;
@Command(name= "xin-gen", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable {
    private final CommandLine commandLine;

    {
        commandLine = new CommandLine(this)
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand());
    }
    @Override
    public void run() {
        //不输入子命令时，给出友好提示
        System.out.println("请输入具体命令，或者输入 --help 查看命令提示！");
    }

    public Integer doExecute(String[] args) {
        return commandLine.execute(args);
    }
}
