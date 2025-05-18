package net.ui;

import net.core.command.Connect;
import net.core.command.Send;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ConsoleUITest {

    private ConsoleUI ui;

    private ConsoleConnector console;

    @BeforeEach
    void setUp() {
        console = mock(ConsoleConnector.class);
        doNothing().when(console).write(any());
        ui = spy(new ConsoleUI(console));
    }

    @AfterEach
    void tearDown() {
        reset(console, ui);
    }

    @Test
    void givenConnectLine_whenParseCommand_thenReturnsCommandObject() {
        var line = "connect";

        var result = ui.parseCommand(line);

        assertThat(result).isEqualTo(new Connect());
    }

    @Test
    void givenSpacesUppercaseConnectLine_whenParseCommand_thenReturnsCommandObject() {
        var line = "  CONNECT    ";

        var result = ui.parseCommand(line);

        assertThat(result).isEqualTo(new Connect());
    }

    @Test
    void givenSpacesUppercaseSendLine_whenParseCommand_thenReturnsSendObjectWithMessage() {
        var message = "This is Message!";
        var line = "  Send " + message + "    ";

        var result = ui.parseCommand(line);

        assertThat(result).isEqualTo(new Send(message));
    }

    @Test
    void givenSendOnlyLine_whenParseCommand_thenReturnsNull() {
        var line = "  Send     ";

        var result = ui.parseCommand(line);

        assertThat(result).isNull();
    }

    @Test
    void givenSomeCommandLine_whenGetCommand_callParseCommand() {
        var line = "asend msg";
//        var line = " some command  ";
        when(console.read())
                .thenReturn(line)
                .thenThrow(new RuntimeException("duplicate call"));
        when(ui.parseCommand(line))
                .thenAnswer(inv -> {
                    System.out.println("INVOKED");
                    return new Connect();
                });
//                .thenReturn(new Connect());

        ui.getCommand();

        verify(ui, times(1)).parseCommand(line);
    }

}