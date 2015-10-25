package karl.codes.minecraft.spyeventbus.action;

import karl.codes.minecraft.spyeventbus.config.ConfigManager;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by karl on 10/22/2015.
 */
public abstract class EventActionBuilder<T extends EventActionBuilder<T>> {
    public T event(Class<? extends Event> event) {
        return (T)this;
    }

}
