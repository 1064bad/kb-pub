package library.lv4.controller.tgbot;

import library.AppException;
import library.lv4.controller.Controller;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
public class TelegramBotController implements Controller {
    public void start() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot(System.getenv("CLEAN_TG_BOT_TOKEN")));
            log.info("tg bot started");
        }
        catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }
}
