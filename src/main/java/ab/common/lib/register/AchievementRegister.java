package ab.common.lib.register;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import ab.api.AchievementAB;
import ab.api.AdvancedBotanyAPI;
// import ab.utils.*;

public class AchievementRegister {

    public static Achievement relicSlingshot;
    public static Achievement relicPocketArmor;
    public static Achievement relicItemChest;
    public static Achievement fateBoard;
    public static Achievement hornPlenty;
    public static Achievement sphereNavigation;

    public static void init() {
        relicSlingshot = new AchievementAB("relicSlingshot", 0, 2, new ItemStack(ItemListAB.itemFreyrSlingshot), null);
        relicPocketArmor = new AchievementAB(
                "relicPocketArmor",
                0,
                -2,
                new ItemStack(ItemListAB.itemPocketWardrobe),
                null);
        relicItemChest = new AchievementAB(
                "relicItemChest",
                2,
                1,
                new ItemStack(ItemListAB.itemTalismanHiddenRiches),
                null);
        hornPlenty = new AchievementAB("relicHornPlenty", 2, -1, new ItemStack(ItemListAB.itemHornPlenty), null);
        sphereNavigation = new AchievementAB(
                "relicSphereNavigation",
                -2,
                1,
                new ItemStack(ItemListAB.itemSphereNavigation),
                null);
        fateBoard = new AchievementAB("fateBoard", 0, 0, new ItemStack(BlockListAB.blockBoardFate, 1, 1), null)
                .setSpecial();
        AchievementPage page = new AchievementPage(
                "Advanced Botany",
                AdvancedBotanyAPI.achievements
                        .<Achievement>toArray(new Achievement[AdvancedBotanyAPI.achievements.size()]));
        AchievementPage.registerAchievementPage(page);
    }
}
