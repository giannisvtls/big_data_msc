import java.net.*;
import java.io.*;
import java.util.HashMap;


public class HashServer {
    private final int port;
    private final HashMap<Integer, Integer> hashTable;
    //private static final int TABLE_SIZE = Math.pow(2, 20);
    private static final int TABLE_SIZE = 1 << 20;

    public HashServer(int port) {
        this.port = port;
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
            int key = Integer.parseInt(parts[1]);

            if (operation == 0) {
                System.out.println("Client requested to Exit");
                break;
            }
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
        int port = 9003; // random available port
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new HashServer(port).start();
    }
}
