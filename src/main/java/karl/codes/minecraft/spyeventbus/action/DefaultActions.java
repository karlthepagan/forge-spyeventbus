package karl.codes.minecraft.spyeventbus.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.minecraftforge.fml.common.eventhandler.Event;
import karl.codes.minecraft.spyeventbus.SpyEventBus;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by karl on 10/18/2015.
 */
public class DefaultActions {
    private static final Logger LOG = LogManager.getLogger(SpyEventBus.class);

    public static final EventAction IGNORE = new IgnoreAction(EventAction.Result.CANCEL);
    public static final EventAction ABORT = new IgnoreAction(EventAction.Result.ABORT); // TODO abort might be missed
    public static final class LOGALWAYS {
        public static final EventAction INFO = new LogAction(Level.INFO);
        public static final EventAction DEBUG = new LogAction(Level.DEBUG);
    }
    public static final class LOG {
        public static final EventAction INFO = new LogOnceAction(Level.INFO);
        public static final EventAction DEBUG = new LogOnceAction(Level.DEBUG);
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

    public static final class LogAction implements EventAction {
        private final Level level;
        private final String format;

        @JsonCreator
        public LogAction(Level level) {
            this(level,DEFAULT_MESSAGE);
        }

        @JsonCreator
        public LogAction(Level level, String format) {
            this.level = level;
            this.format = format;
        }

        @Nullable
        @Override
        public Result apply(Event input, ConcurrentMap<Object,Object> memory, Result lastResult) {
            if(lastResult != Result.MISS) return lastResult;

            LOG.log(level,format,input);
            return Result.OK;
        }
    }

    public static final class LogOnceAction implements EventAction {
        private final Level level;
        private final String format;

        @JsonCreator
        public LogOnceAction(Level level) {
            this(level,DEFAULT_MESSAGE);
        }

        @JsonCreator
        public LogOnceAction(Level level, String format) {
            this.level = level;
            this.format = format;
        }

        public boolean creatememory() {
            return true;
        }

        @Nullable
        @Override
        public Result apply(Event input, ConcurrentMap<Object,Object> memory, Result lastResult) {
            if(lastResult != Result.MISS) return lastResult;

            if(memory.putIfAbsent(input.getClass(),Boolean.TRUE) == null) {
                LOG.log(level, format, input);
            }

            return Result.OK;
        }
    }
}
