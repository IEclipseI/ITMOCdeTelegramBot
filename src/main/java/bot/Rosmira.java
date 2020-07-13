package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;


public class Rosmira extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = System.getenv("BOT_TOKEN");
    private static final Logger logger = LogManager.getLogger(Rosmira.class);

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("Received message: " + update.getMessage());
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                execute(message); // Call method to send the message
                logger.info("Answer sent");
            } catch (TelegramApiException e) {
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
