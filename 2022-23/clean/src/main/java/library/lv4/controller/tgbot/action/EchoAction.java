package library.lv4.controller.tgbot.action;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class EchoAction implements Action {
    @Override
    public Answer act(String charId, String text) {
        var message = new SendMessage(charId, "echo new: " + text);
        return new Answer(List.of(message), Action.DEFAULT, false);
    }
}
