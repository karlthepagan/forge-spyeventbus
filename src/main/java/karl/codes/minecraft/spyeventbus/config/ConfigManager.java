package karl.codes.minecraft.spyeventbus.config;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import cpw.mods.fml.common.eventhandler.Event;
import karl.codes.minecraft.spyeventbus.action.EventRule;

import java.util.Map;

import static karl.codes.minecraft.spyeventbus.action.DefaultActions.*;

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
        target.rules.put(net.minecraftforge.client.event.TextureStitchEvent.Pre.class, );
        target.rules.put(net.minecraftforge.client.event.GuiOpenEvent.class, false);
        target.rules.put(net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre.class, false);
        target.rules.put(net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post.class, false);
        target.rules.put(net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Pre.class, false);
        target.rules.put(net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post.class, false);
        target.rules.put(net.minecraftforge.client.event.sound.PlaySoundEvent17.class, false);
        target.rules.put(net.minecraftforge.client.event.sound.PlayStreamingSourceEvent.class, false);
        target.rules.put(net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre.class, false);

        target.rules.put(PlayerDestroyItemEvent.class, true);
    }

    public void populate(Config config) {
    }

    private static class Builder {
        private Map<Class,EventRule> target;
        private Class<? extends Event> event;
        private EventRule rule;

        public Builder commit() {
            target.put(event,rule);
        }

        public static Builder with(Map<Class,EventRule> target) {
            Builder builder = new Builder();

            builder.target = target;

            return builder;
        }

        public Builder event(Class<? extends Event> event) {
            if(event != null) {
                commit();
            }

        }
    }
}
