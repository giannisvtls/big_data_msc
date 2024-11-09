import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CasesHandler {
    private static final Logger logger = Logger.getLogger(CasesHandler.class.getName());

    protected static final int K = EnvUtil.getEnvVarOrDefault("MAX_NEW_CASES", 10);
    protected static final int H = EnvUtil.getEnvVarOrDefault("MAX_RECOVERIES", 5);
    protected static final int ITERATIONS = EnvUtil.getEnvVarOrDefault("ITERATIONS", 20);
    private static final int HOSPITAL_NUM = EnvUtil.getEnvVarOrDefault("NUM_HOSPITALS", 3);

    private int recoveredCases = 0;
    private int rejectedCases = 0;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final List<Hospital> hospitals;

    public CasesHandler() {
        hospitals = new ArrayList<>();
        for (int i = 0; i < HOSPITAL_NUM; i++) {
            hospitals.add(new Hospital(this)); // Pass the capacity to each hospital
        }
    }

    public List<Hospital> getHospitals() {
        return hospitals;
    }

    public void incrementRecoveredCases(int count) {
        recoveredCases += count;
    }

    public void incrementRejectedCases(int count) {
        rejectedCases += count;
    }

    public int getRecoveredCases() {
        return recoveredCases;
    }

    public int getRejectedCases() {
        return rejectedCases;
    }

    public Lock getLock() {
        return lock;
    }

    public Condition getCondition() {
        return condition;
    }
}
