package karl.codes.minecraft.spyeventbus.config;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import karl.codes.minecraft.spyeventbus.action.EventAction;
import karl.codes.minecraft.spyeventbus.action.EventRule;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static karl.codes.minecraft.spyeventbus.action.DefaultRules.*;

/**
 * Created by karl on 10/15/2015.
 */
public class ConfigManager {
    private ListMultimap<Class<? extends Event>,EventRule> rules;

    public ConfigManager() {
        this.rules = ArrayListMultimap.create();

        applyVerboseSummaryDefaults(this);
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

    protected void applyVerboseSummaryDefaults(ConfigManager target) {
        Builder b = Builder.with(target.rules);

        b.event(Event.class)
                .rule(LOG.INFO);

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

        b.event(ClientChatReceivedEvent.class)
                .rule(new EventRule(new EventAction<ClientChatReceivedEvent>() {
                            @Nullable
                            @Override
                            public Result apply(ClientChatReceivedEvent event, ConcurrentMap<Object, Object> memory, Result last) {

                                List<IChatComponent> siblings = event.message.getSiblings();
                                int size = siblings.size();

                                if(size > 30 || size < 1) {
                                    // 37 siblings ???
                                    return Result.MISS;
                                }

                                String chat0 = siblings.get(0).getUnformattedText();
                                String chatLast = siblings.get(size-1).getUnformattedText();

                                if(chat0.startsWith("[ClearLag]")) {
                                    // ClearLag - '[ClearLag]', 'Warning Ground items will be removed in ', '60', 'seconds!'
                                    // possibly useful - on-screen indicator?
                                    return Result.MISS;
                                }

                                if(chat0.startsWith("[mcMMO]")) {
                                /*
                                0 = {net.minecraft.util.ChatComponentText@6981} "TextComponent{text='[mcMMO] Running version ', siblings=[], style=Style{hasParent=true, color=§6, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}"
1 = {net.minecraft.util.ChatComponentText@6982} "TextComponent{text='1.5.00-b3547', siblings=[], style=Style{hasParent=true, color=§3, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}"
                                 */
                                /*
                                0 = {net.minecraft.util.ChatComponentText@7004} "TextComponent{text='[mcMMO] ', siblings=[], style=Style{hasParent=true, color=§6, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}"
1 = {net.minecraft.util.ChatComponentText@7005} "TextComponent{text='http://dev.bukkit.org/server-mods/mcmmo/', siblings=[], style=Style{hasParent=true, color=§a, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=ClickEvent{action=OPEN_URL, value='http://dev.bukkit.org/server-mods/mcmmo/'}, hoverEvent=null, insertion=null}}"
2 = {net.minecraft.util.ChatComponentText@7006} "TextComponent{text=' - mcMMO Website', siblings=[], style=Style{hasParent=true, color=§e, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}"
                                 */
                                    return Result.MISS;
                                }

                                if(chat0.startsWith("<")) {
                                    // factions nametag chat?
                                    return Result.MISS;
                                }

                                if(chat0.startsWith("----")) {
                                    // hbar - waste of space
                                    /*
                                    TextComponent{text='-----------------------------------------------------', siblings=[], style=Style{hasParent=true, color=§6, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}
                                     */
                                    event.setCanceled(true);
                                    return Result.MISS;
                                }

                                if(chat0.startsWith("You have not voted ") || chat0.startsWith("Vote for us every day ")) {
                                    /*
                                    TextComponent{text='You have not voted recently, please vote to support the server', siblings=[], style=Style{hasParent=true, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}
                                     */
                                    /*
                                    TextComponent{text='Vote for us every day for in game rewards and extras', siblings=[], style=Style{hasParent=true, color=null, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}
                                     */
                                    event.setCanceled(true);
                                    return Result.MISS;
                                }

                                if(chat0.equals("[") || chat0.equals("You currently have ")) {
                                    chat0 = chat0 + siblings.get(1).getUnformattedText();
                                }

                                if(chat0.startsWith("[Links]")) {
                                    // links spam
                                /*
                                0 = {net.minecraft.util.ChatComponentText@6765} "TextComponent{text='[', siblings=[], style=Style{hasParent=true, color=§9, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}"
1 = {net.minecraft.util.ChatComponentText@6766} "TextComponent{text='Links] ', siblings=[], style=Style{hasParent=true, color=§9, bold=true, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}"
2 = {net.minecraft.util.ChatComponentText@6767} "TextComponent{text=' ', siblings=[], style=Style{hasParent=true, color=§c, bold=null, italic=null, underlined=null, obfuscated=true, clickEvent=null, hoverEvent=null, insertion=null}}"
3 = {net.minecraft.util.ChatComponentText@6768} "TextComponent{text=' ', siblings=[], style=Style{hasParent=true, color=§c, bold=null, italic=null, underlined=null, obfuscated=true, clickEvent=null, hoverEvent=null, insertion=null}}"
4 = {net.minecraft.util.ChatComponentText@6769} "TextComponent{text=' ', siblings=[], style=Style{hasParent=true, color=§c, bold=null, italic=null, underlined=null, obfuscated=true, clickEvent=null, hoverEvent=null, insertion=null}}"
5 = {net.minecraft.util.ChatComponentText@6770} "TextComponent{text='Want To Get Some Keys And Ranks? Click Here: ', siblings=[], style=Style{hasParent=true, color=§6, bold=true, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}"
6 = {net.minecraft.util.ChatComponentText@6771} "TextComponent{text='http://mzgamerbros.buycraft.net/', siblings=[], style=Style{hasParent=true, color=§6, bold=true, italic=null, underlined=null, obfuscated=null, clickEvent=ClickEvent{action=OPEN_URL, value='http://mzgamerbros.buycraft.net/'}, hoverEvent=null, insertion=null}}"
7 = {net.minecraft.util.ChatComponentText@6772} "TextComponent{text='    ', siblings=[], style=Style{hasParent=true, color=§c, bold=null, italic=null, underlined=null, obfuscated=true, clickEvent=null, hoverEvent=null, insertion=null}}"
                                 */
                                    event.setCanceled(true);
                                    return Result.MISS;
                                }

                                if(chat0.endsWith("Votes")) {
                                    // vote spam
                                /*
                                0 = {net.minecraft.util.ChatComponentText@6843} "TextComponent{text='You currently have ', siblings=[], style=Style{hasParent=true, color=§b, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}"
1 = {net.minecraft.util.ChatComponentText@6844} "TextComponent{text='0 Votes', siblings=[], style=Style{hasParen9t=true, color=§a, bold=null, italic=null, underlined=null, obfuscated=null, clickEvent=null, hoverEvent=null, insertion=null}}"
                                 */
                                    event.setCanceled(true);
                                    return Result.MISS;
                                }

                                return Result.MISS;
                            }
                        })
                );

        b.event(CommandEvent.class)
                .rule(new EventRule(new EventAction() {
                    @Nullable
                    @Override
                    public Result apply(Event event, ConcurrentMap memory, Result last) {
                        return Result.MISS;
                    }
                }));

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
