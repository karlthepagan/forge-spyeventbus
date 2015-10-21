package karl.codes.minecraft.spyeventbus.action;

/**
 * Created by karl on 10/18/2015.
 */
public class DefaultRules {
    public static final EventRule IGNORE = new EventRule(DefaultActions.IGNORE);
    public static final class LOGFIRST {
        public static final EventRule INFO = new EventRule(DefaultActions.LOGFIRST.INFO);
        public static final EventRule DEBUG = new EventRule(DefaultActions.LOGFIRST.DEBUG);
    }
    public static final class LOGALWAYS {
        public static final EventRule INFO = new EventRule(DefaultActions.LOGALWAYS.INFO);
        public static final EventRule DEBUG = new EventRule(DefaultActions.LOGALWAYS.DEBUG);
    }

    /**
     * Default logging rules:
     * - every few seconds log a summary of events which occurred
     */
    public static final class LOG {
        public static final EventRule INFO = new EventRule(DefaultActions.LOGSUMMARY.INFO);
        public static final EventRule DEBUG = new EventRule(DefaultActions.LOGSUMMARY.DEBUG); // debug should report faster!
    }
}
