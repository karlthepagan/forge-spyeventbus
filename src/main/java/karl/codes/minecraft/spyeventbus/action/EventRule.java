package karl.codes.minecraft.spyeventbus.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import karl.codes.minecraft.spyeventbus.runtime.SpyEventRuntime;
import net.minecraftforge.fml.common.eventhandler.Event;
import karl.codes.jackson.DelegatingDeserializer;
import karl.codes.minecraft.spyeventbus.action.EventAction;

/**
 * Created by karl on 10/15/2015.
 */
public class EventRule {
    private JsonNode eventSpec;
    private EventAction actionImpl;
    private SpyEventRuntime.StateProxy runtime;

    public EventRule() {
    }

    public EventRule(EventAction action) {
        actionImpl = action;
    }

    @JsonDeserialize(using = DelegatingDeserializer.class)
    public void setEvent(JsonNode event) {
        eventSpec = event;
    }

    public boolean testEvent(Event event) {
        return false;
    }

//    @JsonDeserialize(using = ActionLookupDeserializer.class)
    public void setAction(String action) {
    }

    public EventAction getAction() {
        return actionImpl;
    }

    public SpyEventRuntime.StateProxy getRuntime() {
        return runtime;
    }

    public void setRuntime(SpyEventRuntime.StateProxy runtime) {
        this.runtime = runtime;
    }

    public void execute(Event event) {
        if(!testEvent(event)) return;

        EventAction action = getAction();

        action.apply(event,
                action.creatememory()?
                        getRuntime().get().memory(this):null);
    }
}
