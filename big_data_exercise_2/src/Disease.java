import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Disease implements Runnable {
    private static final Logger logger = Logger.getLogger(Disease.class.getName());
    private final CasesHandler casesHandler;
    private final Random random = new Random();

    public Disease(CasesHandler casesHandler) {
        this.casesHandler = casesHandler;
    }

    public void run() {
        for (int i = 0; i < CasesHandler.ITERATIONS; i++) {
            int newCases = random.nextInt(CasesHandler.K + 1);
            logger.log(Level.INFO, "Generated " + newCases + " new cases.");

            int remainingCases = newCases;
            for (Hospital hospital : casesHandler.getHospitals()) {
                remainingCases = hospital.admitCases(remainingCases);
                if (remainingCases == 0) break;
            }

            if (remainingCases > 0) {
                casesHandler.incrementRejectedCases(remainingCases);
                logger.log(Level.WARNING, "Some cases were rejected due to hospital capacity limits.");
            }

            try {
                Thread.sleep(1000); // 1-second interval
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Disease thread interrupted", e);
            }
        }
    }
}
