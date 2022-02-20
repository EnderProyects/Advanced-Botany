package ab;

import ab.common.core.proxy.CommonProxy;
import ab.common.integration.corporea.EventCorporeaHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

@Mod (modid = AdvancedBotany.modid, name="Advanced Botany", version = AdvancedBotany.version, dependencies = "required-after:Botania")
public class AdvancedBotany {
	
	public static final String modid = "AdvancedBotany";
	public static final String version = "1.0.1.1";

	@Mod.Instance("AdvancedBotany")
    public static AdvancedBotany instance;
	
	@SidedProxy(clientSide = "ab.client.core.proxy.ClientProxy", serverSide = "ab.common.core.proxy.CommonProxy")
    public static CommonProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {		
		proxy.init(event);
		MinecraftForge.EVENT_BUS.register(new EventCorporeaHandler());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	public static final CreativeTabs tabAB = new AdvancedBotanyTab("tabAB");
}
