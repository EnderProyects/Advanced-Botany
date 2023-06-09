package ab.client.nei;

import net.minecraft.item.ItemStack;

import ab.AdvancedBotany;
import ab.common.lib.register.BlockListAB;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;

public class NEIConfig implements IConfigureNEI {

    public String getName() {
        return AdvancedBotany.modid;
    }

    public String getVersion() {
        return AdvancedBotany.version;
    }

    public void loadConfig() {
        API.registerRecipeHandler((ICraftingHandler) new RecipeHandlerAlphirine());
        API.registerUsageHandler((IUsageHandler) new RecipeHandlerAlphirine());
        API.registerRecipeHandler((ICraftingHandler) new RecipeHandlerAdvancedPlate());
        API.registerUsageHandler((IUsageHandler) new RecipeHandlerAdvancedPlate());

        API.hideItem(new ItemStack(BlockListAB.blockAntigravitation));
    }
}
