import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsumerClient {
    private static final int[] CONSUMER_PORTS = {9991, 9992, 9993};
    private static final Logger logger = Logger.getLogger(ConsumerClient.class.getName());

    public static void main(String[] args) {
        String serverHostname = "127.0.0.1";
        Random random = new Random();

        while (!Thread.currentThread().isInterrupted()) {
            int port = CONSUMER_PORTS[random.nextInt(CONSUMER_PORTS.length)];
            try (Socket socket = new Socket(serverHostname, port);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                int amount = random.nextInt(91) + 10;
                out.println("REMOVE " + amount);
                System.out.println("Consumer sent REMOVE " + amount + " to port " + port);

                String response = in.readLine();
                System.out.println("Server response: " + response);

                try {
                    Thread.sleep((random.nextInt(10) + 1) * 1000);  // Random delay between 1-10 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // Restore the interrupted status
                    break;  // Exit the loop
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Consumer error: ", e);
            }
        }
    }
}
