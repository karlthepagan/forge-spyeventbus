package karl.codes.minecraft.spyeventbus.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import cpw.mods.fml.common.eventhandler.Event;
import karl.codes.minecraft.spyeventbus.SpyEventBus;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

/**
 * Created by karl on 10/18/2015.
 */
public class DefaultActions {
    private static final Logger LOG = LogManager.getLogger(SpyEventBus.class);

    public static final EventAction IGNORE = new IgnoreAction();
    public static final class LOG {
        public static final EventAction INFO = new LogAction(Level.INFO, "info");
        public static final EventAction DEBUG = new LogAction(Level.DEBUG, "debug");
    }

    private static final class IgnoreAction implements EventAction {
        @Nullable
        @Override
        public Object apply(Event input) {
            return null;
        }
    }

    public static final class LogAction implements EventAction {
        private final Level level;
        private final String format;

        @JsonCreator
        public LogAction(Level level, String format) {
            this.level = level;
            this.format = format;
        }

        @Nullable
        @Override
        public Object apply(Event input) {
            LOG.log(level,format,input);
            return null;
        }
    }
}
