package bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Deployer {
    private static final Logger logger = LogManager.getLogger(Rosmira.class);

    public static void main(String[] args) {
        logger.info("App started");

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new Rosmira());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(System.getenv("PORT")))) {
            while (true) {
                //Hack for Heroku, need reworking
                Socket clientSocket = serverSocket.accept();
                clientSocket.close();
                logger.info("Socket closed: " + clientSocket.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("App down");
    }
}
