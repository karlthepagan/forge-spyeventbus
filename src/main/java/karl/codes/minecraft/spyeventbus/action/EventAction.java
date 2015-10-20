package karl.codes.minecraft.spyeventbus.action;

import com.google.common.base.Function;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by karl on 10/18/2015.
 */
public interface EventAction {
    @Nullable
    public Object apply(Event event, ConcurrentMap<String,Object> memory);

    public default boolean creatememory() {
        return false;
    }
}
