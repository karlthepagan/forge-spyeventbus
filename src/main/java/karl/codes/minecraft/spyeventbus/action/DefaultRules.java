package karl.codes.minecraft.spyeventbus.action;

/**
 * Created by karl on 10/18/2015.
 */
public class DefaultRules {
    public static final EventRule IGNORE = new EventRule(DefaultActions.IGNORE);
    public static final class LOG {
        public static final EventRule INFO = new EventRule(DefaultActions.LOG.INFO);
        public static final EventRule DEBUG = new EventRule(DefaultActions.LOG.DEBUG);
    }
}
