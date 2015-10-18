package karl.codes.minecraft.spyeventbus.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import cpw.mods.fml.common.eventhandler.Event;
import karl.codes.jackson.DelegatingDeserializer;
import karl.codes.minecraft.spyeventbus.action.EventAction;

/**
 * Created by karl on 10/15/2015.
 */
public class EventRule {
    private JsonNode eventSpec;
    private EventAction actionImpl;

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
}
