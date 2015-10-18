package karl.codes.minecraft.spyeventbus;

import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
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

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void event(Event event) {
        Class<? extends Event> type = event.getClass();
        Boolean show = INTERESTING.get(type);
        if(show == null) {
            show = SEEN.putIfAbsent(type,Boolean.TRUE) == null;
        }

        if(show) {
            LOG.info("EVENTSPY\n.put({}.class,false)",event.getClass().getCanonicalName());
        }
    }
}
