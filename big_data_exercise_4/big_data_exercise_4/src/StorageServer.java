
import java.net.*;
import java.io.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageServer {
    private static final int MAX_STORAGE = 1000;
    private static final int MIN_STORAGE = 1;
    private int storage;
    private final int producerPort;
    private final int consumerPort;
    private static final Logger logger = Logger.getLogger(ProducerClient.class.getName());

    public StorageServer(int producerPort, int consumerPort) {
        this.producerPort = producerPort;
        this.consumerPort = consumerPort;
        this.storage = new Random().nextInt(1000) + 1;
    }

    public void start() {
        new Thread(new ClientHandler(producerPort, "Producer")).start();
        new Thread(new ClientHandler(consumerPort, "Consumer")).start();
    }

    private synchronized String handleRequest(String command, int amount) {
        if (command.equalsIgnoreCase("ADD")) {
            if (storage + amount > MAX_STORAGE) {
                return "Storage limit exceeded. Operation not applied. Current storage: " + storage;
            } else {
                storage += amount;
                return "Added " + amount + " to storage. Current storage: " + storage;
            }
        } else if (command.equalsIgnoreCase("REMOVE")) {
            if (storage - amount < MIN_STORAGE) {
                return "Insufficient storage. Operation not applied. Current storage: " + storage;
            } else {
                storage -= amount;
                return "Removed " + amount + " from storage. Current storage: " + storage;
            }
        }
        return "Invalid command.";
    }

    private class ClientHandler implements Runnable {
        private final int port;
        private final String clientType;

        public ClientHandler(int port, String clientType) {
            this.port = port;
            this.clientType = clientType;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println(clientType + " Server started on port " + port + " with initial storage: " + storage);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new ClientProcessor(clientSocket)).start();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not listen on port: " + port);
            }
        }
    }

    private class ClientProcessor implements Runnable {
        private final Socket clientSocket;

        public ClientProcessor(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String[] parts = inputLine.split(" ");
                    String command = parts[0];
                    int amount = Integer.parseInt(parts[1]);

                    String response = handleRequest(command, amount);
                    out.println(response);
                    System.out.println("Request: " + inputLine + " | Response: " + response);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error communicating with the server: " + e);
            }
        }
    }

    public static void main(String[] args) {
        StorageServer server1 = new StorageServer(8881, 9991);
        StorageServer server2 = new StorageServer(8882, 9992);
        StorageServer server3 = new StorageServer(8883, 9993);

        server1.start();
        server2.start();
        server3.start();
    }
}
