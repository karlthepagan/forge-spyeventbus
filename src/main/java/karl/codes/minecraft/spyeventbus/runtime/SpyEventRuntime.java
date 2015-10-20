package karl.codes.minecraft.spyeventbus.runtime;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import karl.codes.java.ClassAncestry;
import karl.codes.minecraft.spyeventbus.action.EventAction;
import karl.codes.minecraft.spyeventbus.action.EventRule;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by karl on 10/19/2015.
 */
public class SpyEventRuntime {
    private ConcurrentMap<Class<? extends Event>,List<EventRule>> runtime;

    private ConcurrentMap<EventRule,ConcurrentMap<Object,Object>> workingMemory;

    public SpyEventRuntime() {
        runtime = new ConcurrentHashMap<Class<? extends Event>, List<EventRule>>();

        workingMemory = new ConcurrentHashMap<EventRule, ConcurrentMap<Object, Object>>();
    }

    public void update(ListMultimap<Class<? extends Event>,EventRule> rules) {
        stateDeath:
        for(List<EventRule> dyingChain : runtime.values()) {
            for(EventRule dyingRule : dyingChain) {
                dyingRule.getRuntime().kill();
                break stateDeath;
            }
        }

        StateProxy proxy = new StateProxy();

        // remove keys not present from runtime
        runtime.keySet().retainAll(rules.keySet());

        // TODO expire removed entries (memory leak)

        for(Map.Entry<Class<? extends Event>,Collection<EventRule>> e : rules.asMap().entrySet()) {
            List<EventRule> ruleChain = ImmutableList.copyOf(e.getValue());

            // roll out the state machine backwards!
            for(int i = ruleChain.size() - 1; i >= 0; i--) {
                ruleChain.get(i).setRuntime(proxy);
            }

            runtime.put(e.getKey(), ruleChain);
        }
    }

    public ConcurrentMap<Object,Object> memory(EventRule rule) {
        return workingMemory.computeIfAbsent(rule,(EventRule r) -> new ConcurrentHashMap<Object, Object>());
    }

    public void expire(EventRule rule) {
        workingMemory.remove(rule);
    }

    public void apply(Event event) {
        List<EventRule> rules;
        EventAction.Result lastResult = EventAction.Result.MISS;

        ruleProcessing:
        for(Class<? extends Event> type : getAncestry(event,Event.class)) {
            rules = runtime.get(type);

            if(rules == null) continue;

            for(EventRule rule : rules) {
                lastResult = rule.execute(event,lastResult);
                if(lastResult == EventAction.Result.ABORT) break ruleProcessing;
            }

    //        Boolean show = INTERESTING.get(type);
    //        if(show == null) {
    //            show = SEEN.putIfAbsent(type,Boolean.TRUE) == null;
    //        }
    //
    //        if(show) {
    //            LOG.info("EVENTSPY\n.put({}.class,false)",event.getClass().getCanonicalName());
    //        }
        }
    }

    private <T,U extends T> Iterable<Class<? extends T>> getAncestry(U event, Class<T> rootClass) {
        return new ClassAncestry<T>((Class<U>)event.getClass(),rootClass);
    }

    public class StateProxy {
        private volatile boolean living = true;

        public boolean isLiving() {
            return living;
        }

        public void kill() {
            living = false;
        }

        public SpyEventRuntime get() {
            return SpyEventRuntime.this;
        }
    }
}
