package bot;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Deployer {
    private static final Logger logger = LogManager.getLogger(Rosmira.class);

    public static void main(String[] args) {
        logger.info("Maven works for Rosmira!!!");

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new Rosmira());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
