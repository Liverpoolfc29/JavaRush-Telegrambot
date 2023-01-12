package com.github.liverpoolfc29.jrtb.command;


import org.junit.jupiter.api.DisplayName;

import static com.github.liverpoolfc29.jrtb.command.AdminHelpCommand.ADMIN_HELP_MESSAGE;
import static com.github.liverpoolfc29.jrtb.command.CommandName.ADMIN_HELP;

@DisplayName("Unit-level testing for AdminHelpCommand")
public class AdminHelpCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return ADMIN_HELP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return ADMIN_HELP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new AdminHelpCommand(sendBotMessageService);
    }
}