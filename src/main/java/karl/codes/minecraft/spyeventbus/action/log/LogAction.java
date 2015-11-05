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
public final class LogAction implements EventAction<Event> {
    private static final Logger LOG = LogManager.getLogger(SpyEventBus.class);

    private final Level level;
    private final String format;

    @JsonCreator
    public LogAction(Level level) {
        this(level, DefaultActions.DEFAULT_MESSAGE);
    }

    @JsonCreator
    public LogAction(Level level, String format) {
        this.level = level;
        this.format = format;
    }

    @Nullable
    @Override
    public Result apply(Event input, ConcurrentMap<Object, Object> memory, Result lastResult) {
        // logalways will ignore cancels but not parent hits
        // TODO this violates least surprise see EventAction.Result comments
        if (lastResult == Result.OK) return lastResult;

        LOG.log(level, format, input);
        return Result.OK;
    }
}
