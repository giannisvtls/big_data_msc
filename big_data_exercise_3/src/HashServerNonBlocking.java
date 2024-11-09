import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashServerNonBlocking {
    private static final Logger logger = Logger.getLogger(HashServerNonBlocking.class.getName());
    private static int getEnvVarOrDefault() {
        String value = System.getenv("SERVER_PORT");
        if (value == null || value.isEmpty()) {
            logger.log(Level.WARNING,"Environment variable \"" + "SERVER_PORT" + "\" not set, using default value: " + 9003);
            return 9003;
        }
        return Integer.parseInt(value);
    }

    private static final int port = getEnvVarOrDefault();
    private final HashMap<Integer, Integer> hashTable;
    private static final int TABLE_SIZE = 1 << 20;

    public HashServerNonBlocking() {
        this.hashTable = new HashMap<>(TABLE_SIZE);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from IP:" + clientSocket.getInetAddress());

                // Handle each new client in a new thread
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(1);
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String[] parts = inputLine.split(",");
                    int operation = Integer.parseInt(parts[0]);

                    if (operation == 0) {
                        System.out.println("Client requested to Exit");
                        break;
                    }
                    int key = Integer.parseInt(parts[1]);
                    switch (operation) {
                        case 1:
                            int value = Integer.parseInt(parts[2]);
                            synchronized (hashTable) {
                                hashTable.put(key, value);
                            }
                            out.println(1);
                            break;
                        case 2:
                            synchronized (hashTable) {
                                out.println(hashTable.remove(key) != null ? 1 : 0);
                            }
                            break;
                        case 3:
                            synchronized (hashTable) {
                                Integer result = hashTable.get(key);
                                out.println(result != null ? result : 0);
                            }
                            break;
                        default:
                            System.out.println("Invalid operation number received: " + operation);
                            out.println(0);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        new HashServerNonBlocking().start();
    }
}