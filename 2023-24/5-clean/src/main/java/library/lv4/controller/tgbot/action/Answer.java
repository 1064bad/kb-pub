package library.lv4.controller.tgbot.action;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Data
public class Answer {
    private final List<SendMessage> messages;
    private final Action nextAction;
    private final boolean redirect;
}
