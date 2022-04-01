package ab.common.lib.register;

import java.util.ArrayList;
import java.util.List;

import ab.api.AchievementAB;
import ab.api.AdvancedBotanyAPI;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;

public class AchievementRegister {
	
	public static Achievement relicSlingshot;
	public static Achievement relicPocketArmor;
	public static Achievement relicItemChest;
	public static Achievement fateBoard;

	public static void init() {
		relicSlingshot = new AchievementAB("relicSlingshot", 0, 2, new ItemStack(ItemListAB.itemFreyrSlingshot), null);
		relicPocketArmor = new AchievementAB("relicPocketArmor", 0, -2, new ItemStack(ItemListAB.itemPocketWardrobe), null);
		relicItemChest = new AchievementAB("relicItemChest", 2, 1, new ItemStack(ItemListAB.itemTalismanHiddenRiches), null);
		fateBoard = new AchievementAB("fateBoard", 0, 0, new ItemStack(BlockListAB.blockBoardFate, 1, 1), null).setSpecial();
		AchievementPage page = new AchievementPage("Advanced Botany", AdvancedBotanyAPI.achievements.<Achievement>toArray(new Achievement[AdvancedBotanyAPI.achievements.size()]));
		AchievementPage.registerAchievementPage(page);
	}
}
