import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HashServer {
    private static final Logger logger = Logger.getLogger(DiseaseHandler.class.getName());
    // Helper function that sets default values to env vars that are not configured
    private static int getEnvVarOrDefault() {
        String value = System.getenv("SERVER_PORT");
        if (value == null || value.isEmpty()) {
            logger.log(Level.WARNING,"Environment variable \"" + "SERVER_PORT" + "\" not set, using default value: " + 9003);
            return 9003;
        }
        return Integer.parseInt(value);
    }

    private static final int port = getEnvVarOrDefault();         // Port of the server
    private final HashMap<Integer, Integer> hashTable;
    private static final int TABLE_SIZE = 1 << 20;

    public HashServer() {
        this.hashTable = new HashMap<>(TABLE_SIZE);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                     System.out.println("Client connected from IP:" + clientSocket.getInetAddress());
                     handleClient(in, out);
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(1);
        }
    }

    private void handleClient(BufferedReader in, PrintWriter out) throws IOException {
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
                    hashTable.put(key, value);
                    out.println(1);
                    break;
                case 2:
                    out.println(hashTable.remove(key) != null ? 1 : 0);
                    break;
                case 3:
                    Integer result = hashTable.get(key);
                    out.println(result != null ? result : 0);
                    break;
                default:
                    System.out.println("Invalid operation number received: " + operation);
                    out.println(0);
            }
        }
    }

    public static void main(String[] args) {
        new HashServer().start();
    }
}
