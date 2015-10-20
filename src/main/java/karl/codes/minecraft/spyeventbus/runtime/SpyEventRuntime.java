package karl.codes.minecraft.spyeventbus.runtime;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
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

    private ConcurrentMap<EventRule,ConcurrentMap<String,Object>> workingMemory;

    public SpyEventRuntime() {
        runtime = new ConcurrentHashMap<Class<? extends Event>, List<EventRule>>();

        workingMemory = new ConcurrentHashMap<EventRule, ConcurrentMap<String, Object>>();
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

    public ConcurrentMap<String,Object> memory(EventRule rule) {
        return workingMemory.computeIfAbsent(rule,(EventRule r) -> new ConcurrentHashMap<String, Object>());
    }

    public void expire(EventRule rule) {
        workingMemory.remove(rule);
    }

    public void apply(Event event) {
        Class<? extends Event> type = event.getClass();

        List<EventRule> rules = runtime.get(event);

        if(rules == null) return;

        for(EventRule rule : rules) {
            rule.execute(event);
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