package bot;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Log4j2
public class Rosmira extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = System.getenv("BOT_TOKEN");

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Received update: " + update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                execute(message);
                log.debug("Answer sent for message: " + update.getMessage().getMessageId());
            } catch (TelegramApiException e) {
                log.error("Error on response for message: " + update.getMessage().getMessageId());
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
