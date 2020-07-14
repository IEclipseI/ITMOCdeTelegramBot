package bot;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Deployer {
    private static final Logger logger = LogManager.getLogger(Rosmira.class);

    public static void main(String[] args) {
        logger.info("Maven works for Rosmira!!!");
        System.out.println("println qwerty!!!");

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new Rosmira());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        try (ServerSocket serverSocket = new ServerSocket(Integer.valueOf(System.getenv("PORT")))) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
