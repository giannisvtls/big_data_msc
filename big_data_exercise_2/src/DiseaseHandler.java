import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiseaseHandler {
    private static final Logger logger = Logger.getLogger(DiseaseHandler.class.getName());

    // Helper function that sets default values to env vars that are not configured
    private static int getEnvVarOrDefault(String envVar, int defaultValue) {
        String value = System.getenv(envVar);
        if (value == null || value.isEmpty()) {
            logger.log(Level.WARNING,"Environment variable \"" + envVar + "\" not set, using default value: " + defaultValue);
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    private static final int K = getEnvVarOrDefault("MAX_NEW_CASES", 10);         // Max number of new cases
    private static final int H = getEnvVarOrDefault("MAX_RECOVERIES", 5);         // Max number of recoveries
    private static final int E = getEnvVarOrDefault("MAX_ICU_CAPACITY", 20);      // Total ICU capacity
    private static final int ITERATIONS = getEnvVarOrDefault("ITERATIONS", 20);   // Number of iterations
    private static final int HOSPITAL_THREAD_SLEEP_TIME_MS = getEnvVarOrDefault("HOSPITAL_THREAD_SLEEP_TIME_MS", 5000); // Hospital thread sleep time in ms
    private static final int DISEASE_THREAD_SLEEP_TIME_MS = getEnvVarOrDefault("DISEASE_THREAD_SLEEP_TIME_MS", 3000);   // Disease thread sleep time in ms

    private int bedsOccupied = 0;
    private int recoveredCases = 0;
    private int casesRejected = 0;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Random random = new Random();

    // Thread that generates new cases
    class Disease implements Runnable {
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {

                // Lock the thread while doing operations on the shared variables
                lock.lock();
                try {
                    int newCases = random.nextInt(K + 1);
                    logger.log(Level.INFO,"Generated " + newCases + " new cases.");

                    // Calculate if the ICU capacity can handle the new cases
                    if (bedsOccupied + newCases > E) {
                        // If more cases than available beds, fill the available beds and reject the rest of the cases
                        casesRejected += (bedsOccupied + newCases - E);
                        bedsOccupied = E;
                    } else {
                        // If available beds than new cases, add the new cases to the ICU
                        bedsOccupied += newCases;
                    }

                    logger.log(Level.INFO,"Occupied beds: " + bedsOccupied);
                    condition.signalAll();
                } finally {
                    // Finally unlock the thread when all the operations are complete
                    lock.unlock();
                }

                try {
                    Thread.sleep(DISEASE_THREAD_SLEEP_TIME_MS);
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Error in Disease thread", e);
                }
            }
        }
    }

    // Thread that recovers cases and frees up beds
    class Hospital implements Runnable {
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                lock.lock();
                try {
                    int recoveries = random.nextInt(H + 1);
                    int actualRecoveries = Math.min(recoveries, bedsOccupied);

                    // Recovered patients free up some beds
                    bedsOccupied -= actualRecoveries;
                    recoveredCases += actualRecoveries;

                    logger.log(Level.INFO,"Recovered: " + actualRecoveries + ", Occupied beds: " + bedsOccupied);
                    condition.signalAll();
                } finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(HOSPITAL_THREAD_SLEEP_TIME_MS);
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Error in Hospital thread", e);
                }
            }
        }
    }

    public int getRecoveredCases() {
        return recoveredCases;
    }

    public int getCasesRejected() {
        return casesRejected;
    }
}
