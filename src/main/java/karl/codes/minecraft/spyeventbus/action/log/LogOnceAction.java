package karl.codes.minecraft.spyeventbus.action.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import karl.codes.minecraft.spyeventbus.SpyEventBus;
import karl.codes.minecraft.spyeventbus.action.DefaultActions;
import karl.codes.minecraft.spyeventbus.action.EventAction;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by karl on 10/20/2015.
 */
public final class LogOnceAction implements EventAction<Event> {
    private static final Logger LOG = LogManager.getLogger(SpyEventBus.class);

    private final Level level;
    private final String format;

    @JsonCreator
    public LogOnceAction(Level level) {
        this(level, DefaultActions.DEFAULT_MESSAGE);
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
    public Result apply(Event input, ConcurrentMap<Object, Object> memory, Result lastResult) {
        if (lastResult != Result.MISS) return lastResult;

        if (memory.putIfAbsent(input.getClass(), Boolean.TRUE) == null) {
            LOG.log(level, format, input);
        }

        return Result.OK;
    }
}
