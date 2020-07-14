package bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Rosmira extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = System.getenv("BOT_TOKEN");
    private static final Logger logger = LogManager.getLogger(Rosmira.class);

    @Override
    public void onUpdateReceived(Update update) {
        logger.debug("Received message: " + update.getMessage());
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                execute(message);
                logger.debug("Answer sent for message: " + update.getMessage().getMessageId());
            } catch (TelegramApiException e) {
                logger.error("Error on response for message: " + update.getMessage().getMessageId());
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "RosmiraBot";
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
