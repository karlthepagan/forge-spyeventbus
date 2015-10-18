package karl.codes.minecraft.spyeventbus.config;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.eventhandler.Event;
import karl.codes.minecraft.spyeventbus.action.EventAction;
import karl.codes.minecraft.spyeventbus.action.EventRule;

import java.util.Map;

import static karl.codes.minecraft.spyeventbus.action.DefaultRules.*;

/**
 * Created by karl on 10/15/2015.
 */
public class ConfigManager {
    private ListMultimap<Class<? extends Event>,EventRule> rules;

    public ConfigManager() {
        rules = ArrayListMultimap.create();

        applyDefaults(this);
    }

    protected void applyDefaults(ConfigManager target) {
        Builder b = Builder.with(target.rules);

        b.event(net.minecraftforge.client.event.TextureStitchEvent.Pre.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.GuiOpenEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Pre.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.sound.PlaySoundEvent17.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.sound.PlayStreamingSourceEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.event.entity.player.PlayerDestroyItemEvent.class)
                .rule(LOG.DEBUG);
    }

    public void populate(Config config) {
    }

    public void apply(Event event) {
        Class<? extends Event> type = event.getClass();


        Boolean show = INTERESTING.get(type);
        if(show == null) {
            show = SEEN.putIfAbsent(type,Boolean.TRUE) == null;
        }

        if(show) {
            LOG.info("EVENTSPY\n.put({}.class,false)",event.getClass().getCanonicalName());
        }
    }

    private static class Builder {
        private Multimap<Class<? extends Event>,EventRule> target;
        private Class<? extends Event> event;
        private EventRule rule;

        public Builder commit() {
            target.put(event,rule);

            this.event = null;
            this.rule = null;

            return this;
        }

        public static Builder with(Multimap<Class<? extends Event>,EventRule> target) {
            Builder builder = new Builder();

            builder.target = target;

            return builder;
        }

        public Builder event(Class<? extends Event> event) {
            if(this.event != null) {
                commit();
            }

            this.event = event;

            return this;
        }

        public Builder rule(EventRule rule) {
            this.rule = rule;

            return this;
        }
    }
}
