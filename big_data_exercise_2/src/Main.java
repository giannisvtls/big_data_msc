import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        CasesHandler casesHandler = new CasesHandler();

        // Create 1 thread to generate diseases
        Thread diseaseThread = new Thread(new Disease(casesHandler));

        // Create and start multiple Hospital threads
        List<Thread> hospitalThreads = new ArrayList<>();
        var hospitals = casesHandler.getHospitals();
        for (Hospital hospital : hospitals) {
            var hospitalThread = new Thread(hospital);
            hospitalThreads.add(hospitalThread);
        }

        // Start the disease thread that will generate cases in random intervals
        diseaseThread.start();

        // Start one or multiple hospital threads, each one will recover cases in random intervals
        for (Thread hospitalThread : hospitalThreads) {
            hospitalThread.start();
        }

        try {
            diseaseThread.join();
            for (Thread hospitalThread : hospitalThreads) {
                hospitalThread.join();
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Thread interrupted", e);
        }

        // Final results
        logger.log(Level.INFO, "Total recovered cases: " + casesHandler.getRecoveredCases());
        logger.log(Level.INFO, "Total rejected cases: " + casesHandler.getRejectedCases());
    }
}
