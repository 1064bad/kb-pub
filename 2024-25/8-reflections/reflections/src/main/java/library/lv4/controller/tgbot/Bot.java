package library.lv4.controller.tgbot;

import library.lv4.controller.tgbot.action.Action;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final Map<String, Action> currentActions = new ConcurrentHashMap<>();

    public Bot(String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var redirected = true;
            while (redirected) {
                var chatId = update.getMessage().getChatId().toString();
                var currentAction = currentActions.getOrDefault(chatId, Action.DEFAULT);
                var answer = currentAction.act(chatId, update.getMessage().getText());
                currentActions.put(chatId, answer.getNextAction());
                redirected = answer.isRedirect();
                try {
                    for (var message : answer.getMessages())
                        sendApiMethod(message);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "clean_bot";
        // clean_kjhadfkjhsdf_bot
    }


}
