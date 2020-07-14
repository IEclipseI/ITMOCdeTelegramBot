package bot;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Log4j2
public class Deployer {
    public static void main(String[] args) {
        log.info("App started");
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
                clientSocket.getOutputStream().write(200);
                clientSocket.close();
                log.info("Socket closed: " + clientSocket.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("App down");
    }
}
