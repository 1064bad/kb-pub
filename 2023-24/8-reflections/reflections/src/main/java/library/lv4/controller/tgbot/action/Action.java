package library.lv4.controller.tgbot.action;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Action {
    Action DEFAULT = new RouterAction();

    Answer act(String charId, String text);
}
