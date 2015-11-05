package karl.codes.minecraft.spyeventbus.action.log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Objects;
import karl.codes.minecraft.spyeventbus.SpyEventBus;
import karl.codes.minecraft.spyeventbus.action.DefaultActions;
import karl.codes.minecraft.spyeventbus.action.EventAction;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by karl on 10/20/2015.
 */
public final class LogSummaryAction implements EventAction<Event> {
    private static final Logger LOG = LogManager.getLogger(SpyEventBus.class);

    // TODO inject from environment
    private static final ScheduledExecutorService EXEC = new ScheduledThreadPoolExecutor(1);
    private static final Object SCHEDULE_KEY = new Object();

    private final Level level;
    private final String format;
    /**
     * Dummy variable used to affect hashCode / equals. This allows similar actions to hold independent working memory maps.
     */
    private final String memory;

    @JsonCreator
    public LogSummaryAction(Level level) {
        this(level, DefaultActions.DEFAULT_MESSAGE + " ({})", DefaultActions.DEFAULT_MEMORY);
    }

    @JsonCreator
    public LogSummaryAction(Level level, String format, String memory) {
        this.level = level;
        this.format = format;
        this.memory = memory;
    }

    public boolean creatememory() {
        return true;
    }

    @Nullable
    @Override
    public Result apply(Event input, ConcurrentMap<Object, Object> memory, Result lastResult) {
        if (lastResult != Result.MISS) return lastResult;

        Summary data = getEventData(input, memory);

        Summary.COUNT_UPDATER.incrementAndGet(data);

        updateSchedule(memory);

        return Result.OK;
    }

    private void updateSchedule(final ConcurrentMap<Object, Object> memory) {
        // double-check (usually faster than a raw lambda with this escaping reference)
        if(memory.get(SCHEDULE_KEY) == null) {
            memory.computeIfAbsent(SCHEDULE_KEY, (Object o) -> this.startReportSchedule(memory));
        }
    }

    private ScheduledFuture<?> startReportSchedule(final ConcurrentMap<Object, Object> memory) {
        return EXEC.scheduleAtFixedRate(()->{
            for(Map.Entry<?,?> e : memory.entrySet()) {
                if(e.getKey() instanceof Class) {
                    Summary data = (Summary)e.getValue();
                    int count = data.count;

                    // TODO sort entrySet based on count
                    if(count > 0)
                        LOG.log(level,format,e.getKey(),count);

                    Summary.COUNT_UPDATER.addAndGet(data,-count);
                }
            }
        },10,10,TimeUnit.SECONDS);
    }

    private Summary getEventData(Event input, ConcurrentMap<Object, Object> memory) {
        Summary data = (Summary)memory.putIfAbsent(input.getClass(),new Summary());
        if(data == null) {
            data = (Summary)memory.get(input.getClass());
        }
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogSummaryAction that = (LogSummaryAction) o;
        return Objects.equal(level, that.level) &&
                Objects.equal(format, that.format) &&
                Objects.equal(memory, that.memory);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(level, format, memory);
    }

    private static final class Summary {
        private static final AtomicIntegerFieldUpdater<Summary> COUNT_UPDATER = AtomicIntegerFieldUpdater.newUpdater(Summary.class,"count");
        private volatile int count;
    }
}
