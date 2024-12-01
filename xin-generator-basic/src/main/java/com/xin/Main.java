package com.xin;

import com.xin.cli.CommandExecutor;


//12
public class Main {
    public static void main(String[] args) {
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}