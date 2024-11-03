import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiseaseSimulation {
    private static final Logger logger = Logger.getLogger(DiseaseSimulation.class.getName());
    private static final int K = Integer.parseInt(System.getenv("MAX_NEW_CASES"));  // Max number of new cases
    private static final int H = Integer.parseInt(System.getenv("MAX_RECOVERIES"));;  // Max number of recoveries
    private static final int E = Integer.parseInt(System.getenv("MAX_ICU_CAPACITY"));  // Total ICU capacity
    private static final int ROUNDS = Integer.parseInt(System.getenv("ITERATIONS"));;// Number of rounds
    private static final int HOSPITAL_THREAD_SLEEP_TIME_MS = Integer.parseInt(System.getenv("HOSPITAL_THREAD_SLEEP_TIME_MS"));
    private static final int DISEASE_THREAD_SLEEP_TIME_MS = Integer.parseInt(System.getenv("DISEASE_THREAD_SLEEP_TIME_MS"));

    private int bedsOccupied = 0;
    private int recoveredCases = 0;
    private int casesRejected = 0;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Random random = new Random();

    // Thread that generates new cases
    class Disease implements Runnable {
        public void run() {
            for (int i = 0; i < ROUNDS; i++) {
                lock.lock();
                try {
                    int newCases = random.nextInt(K + 1);
                    System.out.println("Generated " + newCases + " new cases.");

                    if (bedsOccupied + newCases > E) {
                        casesRejected += (bedsOccupied + newCases - E);
                        bedsOccupied = E;
                    } else {
                        bedsOccupied += newCases;
                    }

                    System.out.println("Occupied beds: " + bedsOccupied);
                    condition.signalAll();
                } finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(DISEASE_THREAD_SLEEP_TIME_MS); // 1 second
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Error in Disease thread", e);
                }
            }
        }
    }

    // Thread that recovers cases and frees up beds
    class Hospital implements Runnable {
        public void run() {
            for (int i = 0; i < ROUNDS; i++) {
                lock.lock();
                try {
                    int recoveries = random.nextInt(H + 1);
                    int actualRecoveries = Math.min(recoveries, bedsOccupied);

                    bedsOccupied -= actualRecoveries;
                    recoveredCases += actualRecoveries;

                    System.out.println("Recovered: " + actualRecoveries + ", Occupied beds: " + bedsOccupied);
                    condition.signalAll();
                } finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(HOSPITAL_THREAD_SLEEP_TIME_MS); // 5 seconds
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
