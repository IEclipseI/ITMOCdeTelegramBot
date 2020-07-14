package bot;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

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

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(System.getenv("PORT"))), 20);
            HttpContext context = server.createContext("/");
            context.setHandler(Deployer::handleRequest);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("App down");
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String response = "This is the response at " + requestURI;
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
