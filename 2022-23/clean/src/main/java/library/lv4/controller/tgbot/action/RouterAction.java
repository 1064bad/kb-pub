package library.lv4.controller.tgbot.action;

import java.util.List;

public class RouterAction implements Action {
    @Override
    public Answer act(String chatId, String text) {
        var nextAction = switch (text) {
            case "book ls" -> GetAllBooksAction.Factory.get();
            default -> new EchoAction();
        };
        return new Answer(List.of(), nextAction, true);
    }
}
