package pulxes.ab.common.core.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pulxes.ab.AdvancedBotany;

public class AdvBotanyConfigHandler {
	
	public static Configuration config;
	public static boolean useManaChargerAnimation = true;
	public static int maxContainerMana = 64000000;
	public static double protectionFactorNebula = 1.0f;
	public static double damageFactorSpaceSword = 1.0f;
	public static int nebulaWandCooldownTick = 18;
//	public static int spreaderMaxMana = 128000;
//	public static int spreaderBurstMana = 32000;
//	public static String[] lockEntityListToHorn = new String[] {};
	public static String[] lockWorldNameNebulaRod = new String[] {};
	public static int limitXZCoords = 30000000;

	public static void loadConfig(File configFile) {
	    config = new Configuration(configFile);
	    config.load();
	    load();
	    FMLCommonHandler.instance().bus().register(new ChangeListener());
	}
	
	public static void load() {
		String desc = "Maximum mana capacity for Mana Container (Diluted Mana Container will receive an eighth of the set value)";
		maxContainerMana = loadPropInt("Mana Container capacity", desc, maxContainerMana);
		desc = "Activating the charging animation for the Mana Charger";
		useManaChargerAnimation = loadPropBool("Mana Charger lighting", desc, useManaChargerAnimation);
		desc = "Protection factor for nebula armour";
		protectionFactorNebula = loadPropDouble("Nebula Armor Protection Factor", desc, protectionFactorNebula);
		desc = "Damage factor for Space Blade";
		damageFactorSpaceSword = loadPropDouble("Space Sword Damage Factor", desc, damageFactorSpaceSword);
		desc = "The number of ticks needed to restore 1 unit of the Rod of Nebula strength.";
		nebulaWandCooldownTick = loadPropInt("Rod of Nebula Cooldown", desc, nebulaWandCooldownTick);
//		desc = "Maximum amount of mana held in a mana spreader";
//		spreaderMaxMana = loadPropInt("Spreader Max Mana", desc, spreaderMaxMana);
//		desc = "Amount of Mana in a Mana Burst";
//		spreaderBurstMana = loadPropInt("Spreader Burst Mana", desc, spreaderBurstMana);
//		desc = "To block a creature, type it's class name";
//		lockEntityListToHorn = loadPropString("Blocked creatures for double drop", desc, lockEntityListToHorn);
		desc = "Enter the name of the World you want to add to the blacklist";
		lockWorldNameNebulaRod = loadPropString("Locking Worlds for Teleportation with Nebula Rod", desc, lockWorldNameNebulaRod);
		desc = "Limitation on X Z coordinates for teleportation, do not increase the default value";
		limitXZCoords = loadPropInt("Restriction on X Z coordinates for Rod of Nebula", desc, limitXZCoords);
		if(config.hasChanged())
			config.save(); 
	}
	
	public static void loadPostInit() {
		if(config.hasChanged())
			config.save(); 
	}
	
	public static String[] loadPropString(String propName, String desc, String[] default_) {
		Property prop = config.get("general", propName, default_);
		prop.setComment(desc);
		return prop.getStringList();
	}
	
	public static boolean loadPropBool(String propName, String desc, boolean default_) {
		Property prop = config.get("general", propName, default_);
		prop.setComment(desc);
		return prop.getBoolean(default_);
	}
	
	public static int loadPropInt(String propName, String desc, int default_) {
		Property prop = config.get("general", propName, default_);
		prop.setComment(desc);
		return prop.getInt(default_);
	}
	
	public static double loadPropDouble(String propName, String desc, double default_) {
		Property prop = config.get("general", propName, default_);
		prop.setComment(desc);
		return prop.getDouble(default_);
	}
	
	public static class ChangeListener {
		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
			if(eventArgs.getModID().equals(AdvancedBotany.MODID))
				AdvBotanyConfigHandler.load(); 
		}
	}
}