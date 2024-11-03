import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(DiseaseSimulation.class.getName());

    public static void main(String[] args) {
        DiseaseSimulation simulation = new DiseaseSimulation();
        Thread diseaseThread = new Thread(simulation.new Disease());
        Thread hospitalThread = new Thread(simulation.new Hospital());

        diseaseThread.start();
        hospitalThread.start();

        try {
            diseaseThread.join();
            hospitalThread.join();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Error joining the threads", e);
        }

        System.out.println("Total recovered cases: " + simulation.getRecoveredCases());
        System.out.println("Total rejected cases: " + simulation.getCasesRejected());
    }
}
