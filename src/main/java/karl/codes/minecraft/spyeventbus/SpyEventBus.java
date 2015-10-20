package karl.codes.minecraft.spyeventbus;

import com.google.common.collect.ImmutableMap;
import karl.codes.minecraft.spyeventbus.runtime.SpyEventRuntime;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import karl.codes.minecraft.spyeventbus.config.ConfigManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Mod(
        useMetadata = true,
        modid = SpyEventBus.MODID,
        acceptableRemoteVersions = "*",
        acceptedMinecraftVersions = "[1.7.0,)",
        canBeDeactivated = true)
@SideOnly(Side.CLIENT)
public class SpyEventBus
{
    public static final String MODID = "spyeventbus";

    // https://dl.dropboxusercontent.com/s/h777x7ugherqs0w/forgeevents.html
    private static final Logger LOG = LogManager.getLogger(SpyEventBus.class);

    private static final ConcurrentMap<Class,Boolean> SEEN = new ConcurrentHashMap<Class, Boolean>();

    private final ConfigManager config;
    private final SpyEventRuntime runtime;

    public SpyEventBus() {
        runtime = new SpyEventRuntime();
        config = new ConfigManager(runtime);

        runtime.update(config.getRules());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void event(Event event) {
        runtime.apply(event);
    }
}
