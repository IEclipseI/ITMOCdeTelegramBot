package bot;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
                try (Socket clientSocket = serverSocket.accept()) {
                    try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
//                        String word = in.readLine(); // ждём пока клиент что-нибудь нам напишет
                        System.out.println("YEEEEEEEEEEEEEEEEEEEEEEEH");
                        // не долго думая отвечает клиенту
                        String response = "HTTP/1.1 200 OK\n"
                                + "Content-Type: text/html\n"
                                + "Content-Length: 11\n"
                                + "\n"
                                + "hello world";
                        out.write(response);
                        out.flush(); // выталкиваем все из буфера
                        log.info("Socket closed: " + clientSocket.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("App down");
    }
}
