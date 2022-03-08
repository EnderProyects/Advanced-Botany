package ab.common.core.proxy;

import ab.common.core.ConfigABHandler;
import ab.common.lib.register.BlockListAB;
import ab.common.lib.register.EntityListAB;
import ab.common.lib.register.FlowerRegister;
import ab.common.lib.register.ItemListAB;
import ab.common.lib.register.RecipeListAB;
import ab.common.minetweaker.MineTweakerConfig;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ConfigABHandler.loadConfig(event.getSuggestedConfigurationFile());
		BlockListAB.init();
		ItemListAB.init();
		EntityListAB.init();
		FlowerRegister.init();
	}
 
	public void init(FMLInitializationEvent event) {
		RecipeListAB.init();
	}
	 
	public void postInit(FMLPostInitializationEvent event) {
		ConfigABHandler.loadPostInit();
		if(Loader.isModLoaded("MineTweaker3")) 		
			MineTweakerConfig.registerMT();
	}
}