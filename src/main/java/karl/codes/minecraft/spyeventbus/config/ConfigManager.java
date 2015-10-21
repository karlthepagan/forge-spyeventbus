package karl.codes.minecraft.spyeventbus.config;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import karl.codes.minecraft.spyeventbus.action.EventRule;
import net.minecraftforge.fml.common.eventhandler.Event;

import static karl.codes.minecraft.spyeventbus.action.DefaultRules.*;

/**
 * Created by karl on 10/15/2015.
 */
public class ConfigManager {
    private ListMultimap<Class<? extends Event>,EventRule> rules;

    public ConfigManager() {
        this.rules = ArrayListMultimap.create();

        applyLogSummaryDefaults(this);
    }

    protected void applyLogFirstDefaults(ConfigManager target) {
        Builder b = Builder.with(target.rules);

        b.event(net.minecraftforge.client.event.TextureStitchEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.GuiOpenEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.GuiScreenEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.sound.SoundEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.event.world.WorldEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.event.entity.EntityEvent.class)
                .rule(IGNORE);

//        b.event(net.minecraftforge.event.entity.EntityJoinWorldEvent.class)
//                .rule(LOGALWAYS.INFO);

        b.event(net.minecraftforge.client.event.EntityViewRenderEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.DrawBlockHighlightEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.RenderWorldLastEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.RenderHandEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.RenderGameOverlayEvent.class)
                .rule(IGNORE);

//        b.event(net.minecraftforge.client.event.ClientChatReceivedEvent.class)
//                .rule(LOGALWAYS.INFO);

        b.event(net.minecraftforge.client.event.MouseEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.FOVUpdateEvent.class)
                .rule(IGNORE);

        // TODO need to un-ignore specific children
        b.event(net.minecraftforge.event.terraingen.BiomeEvent.GetGrassColor.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.event.terraingen.BiomeEvent.GetWaterColor.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.RenderLivingEvent.class)
                .rule(IGNORE);

//        b.event(net.minecraftforge.event.terraingen.BiomeEvent.class)
//                .rule(IGNORE);

//        b.event(net.minecraftforge.event.entity.player.PlayerDestroyItemEvent.class)
//                .rule(DefaultActions.LOGALWAYS.INFO);

        b.event(Event.class)
                .rule(LOGFIRST.INFO);

        b.commit();
    }

    protected void applyLogSummaryDefaults(ConfigManager target) {
        Builder b = Builder.with(target.rules);

        b.event(net.minecraftforge.client.event.TextureStitchEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.GuiOpenEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.GuiScreenEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.EntityViewRenderEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.DrawBlockHighlightEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.RenderWorldLastEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.RenderHandEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.RenderGameOverlayEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.MouseEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.FOVUpdateEvent.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.client.event.RenderLivingEvent.class)
                .rule(IGNORE);

        // spammy entity events
        b.event(net.minecraftforge.event.entity.EntityEvent.EntityConstructing.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent.class)
                .rule(IGNORE);

        // spammy biome events
        b.event(net.minecraftforge.event.terraingen.BiomeEvent.GetGrassColor.class)
                .rule(IGNORE);

        b.event(net.minecraftforge.event.terraingen.BiomeEvent.GetWaterColor.class)
                .rule(IGNORE);

        b.event(Event.class)
                .rule(LOG.INFO);

        b.commit();
    }

    public void populate(Config config) {
        // clear
        // apply defaults
        // apply config
        // register runtime
    }

    public ListMultimap<Class<? extends Event>, EventRule> getRules() {
        return rules;
    }

    private static class Builder {
        private Multimap<Class<? extends Event>,EventRule> target;
        private Class<? extends Event> event;
        private EventRule rule;
        private ListMultimap<Class<? extends Event>, EventRule> rules;

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
