package karl.codes.minecraft.spyeventbus.action;

import com.google.common.base.Function;
import cpw.mods.fml.common.eventhandler.Event;

import javax.annotation.Nullable;

/**
 * Created by karl on 10/18/2015.
 */
public interface EventAction extends Function<Event,Object> {
}
