package karl.codes.minecraft.spyeventbus.action;

import karl.codes.minecraft.spyeventbus.action.log.LogAction;
import karl.codes.minecraft.spyeventbus.action.log.LogOnceAction;
import karl.codes.minecraft.spyeventbus.action.log.LogSummaryAction;
import net.minecraftforge.fml.common.eventhandler.Event;
import karl.codes.minecraft.spyeventbus.SpyEventBus;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by karl on 10/18/2015.
 */
public class DefaultActions {
    public static final EventAction IGNORE = new IgnoreAction(EventAction.Result.CANCEL);
    public static final EventAction ABORT = new IgnoreAction(EventAction.Result.ABORT); // TODO abort might be missed
    public static final class LOGALWAYS {
        public static final EventAction INFO = new LogAction(Level.INFO);
        public static final EventAction DEBUG = new LogAction(Level.DEBUG);
    }
    public static final class LOGFIRST {
        public static final EventAction INFO = new LogOnceAction(Level.INFO);
        public static final EventAction DEBUG = new LogOnceAction(Level.DEBUG);
    }
    public static final class LOGSUMMARY {
        public static final EventAction INFO = new LogSummaryAction(Level.INFO);
        public static final EventAction DEBUG = new LogSummaryAction(Level.DEBUG);
    }

    private static final class IgnoreAction implements EventAction {
        private final Result result;

        public IgnoreAction(Result result) {
            this.result = result;
        }

        @Nullable
        @Override
        public Result apply(Event input, ConcurrentMap<Object,Object> memory, Result lastResult) {
            if(lastResult != Result.MISS) return lastResult;

            return result;
        }
    }

    public static final String DEFAULT_MESSAGE = "[" + SpyEventBus.class.getSimpleName() + "] {}";

    public static final String DEFAULT_MEMORY = "0";

}
