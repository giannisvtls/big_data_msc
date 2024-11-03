import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        DiseaseHandler diseaseHandler = new DiseaseHandler();

        // Create a new thread instance for each service
        Thread diseaseThread = new Thread(diseaseHandler.new Disease());
        Thread hospitalThread = new Thread(diseaseHandler.new Hospital());

        // Start the threads
        diseaseThread.start();
        hospitalThread.start();

        try {
            diseaseThread.join();
            hospitalThread.join();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Error joining the threads", e);
        }

        logger.log(Level.INFO,"Total recovered cases: " + diseaseHandler.getRecoveredCases());
        logger.log(Level.INFO,"Total rejected cases: " + diseaseHandler.getCasesRejected());
    }
}
