package karl.codes.minecraft.spyeventbus.action;

import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by karl on 10/18/2015.
 */
public interface EventAction {
    public static enum Result {
        ABORT, // HARD cancel, stop processing!
        CANCEL, // cancel downstream actions
        OK, // continue
        MISS, // pretend this rule hit did not happen
    }

    @Nullable
    public Result apply(Event event, ConcurrentMap<Object,Object> memory, Result last);

    public default boolean creatememory() {
        return false;
    }
}
