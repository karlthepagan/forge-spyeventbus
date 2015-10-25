package karl.codes.minecraft.spyeventbus.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;
import karl.codes.minecraft.spyeventbus.runtime.SpyEventRuntime;
import net.minecraftforge.fml.common.eventhandler.Event;
import karl.codes.jackson.DelegatingDeserializer;

/**
 * Created by karl on 10/15/2015.
 */
public class EventRule {
    private JsonNode eventSpec;
    // TODO consider separating out rule-action binding
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
        return true;
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

    public EventAction.Result execute(Event event, EventAction.Result lastResult) {
        if(!testEvent(event)) return lastResult;

        EventAction action = getAction();

        return action.apply(
                event,
                action.creatememory()?
                        getRuntime().get().memory(this):null,
                lastResult);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRule eventRule = (EventRule) o;
        return Objects.equal(actionImpl, eventRule.actionImpl);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(actionImpl);
    }
}
