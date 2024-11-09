import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hospital implements Runnable {
    private static final Logger logger = Logger.getLogger(Hospital.class.getName());

    // Max ICU capacity per hospital, if we have 1 hospital total capacity is "MAX_ICU_CAPACITY", for 2 hospitals "MAX_ICU_CAPACITY*2" capacity etc.
    private static final int maxIcuCapacity = EnvUtil.getEnvVarOrDefault("MAX_ICU_CAPACITY", 20);

    private static int hospitalIdCount = 0;
    private final int currentId;
    private final CasesHandler casesHandler;
    private int bedsOccupied = 0;
    private final Random random = new Random();

    public Hospital(CasesHandler diseaseHandler) {
        this.casesHandler = diseaseHandler;
        currentId = hospitalIdCount;
        hospitalIdCount++;

    }

    public int admitCases(int cases) {
        casesHandler.getLock().lock();
        try {
            int availableBeds = maxIcuCapacity - bedsOccupied;
            int admittedCases = Math.min(cases, availableBeds);
            bedsOccupied += admittedCases;
            casesHandler.getCondition().signalAll();

            if (admittedCases > 0) {
                logger.log(Level.INFO, "Admitted " + admittedCases + " cases to hospital with ID: " + this.getHospitalId() + " (Occupied beds: " + bedsOccupied + "/" + maxIcuCapacity +")");
            }
            return cases - admittedCases;
        } finally {
            casesHandler.getLock().unlock();
        }
    }

    public void run() {
        for (int i = 0; i < CasesHandler.ITERATIONS; i++) {
            int recoveries = random.nextInt(CasesHandler.H + 1);

            casesHandler.getLock().lock();
            try {
                int actualRecoveries = Math.min(recoveries, bedsOccupied);
                bedsOccupied -= actualRecoveries;
                casesHandler.incrementRecoveredCases(actualRecoveries);
                casesHandler.getCondition().signalAll();

                if (actualRecoveries > 0) {
                    logger.log(Level.INFO, "For hospital ID: " + this.getHospitalId() + "\nRecovered " + actualRecoveries + " cases (Remaining occupied beds: " + bedsOccupied + "/" + maxIcuCapacity +")");
                }
            } finally {
                casesHandler.getLock().unlock();
            }

            try {
                Thread.sleep(5000); // 5-second interval
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Hospital thread interrupted", e);
            }
        }
    }

    public int getHospitalId() {
        return currentId;
    }
}
