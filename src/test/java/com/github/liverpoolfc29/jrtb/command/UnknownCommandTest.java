package com.github.liverpoolfc29.jrtb.command;

import static com.github.liverpoolfc29.jrtb.command.UnknownCommand.UNKNOWN_MESSAGE;

public class UnknownCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return "sds";
    }

    @Override
    String getCommandMessage() {
        return UNKNOWN_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UnknownCommand(sendBotMessageService);
    }
}
