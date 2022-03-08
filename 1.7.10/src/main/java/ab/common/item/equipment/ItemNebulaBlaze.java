package ab.common.item.equipment;

import ab.common.entity.EntityNebulaBlaze;
import ab.common.item.ItemMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

public class ItemNebulaBlaze extends ItemMod implements IManaUsingItem {

	public ItemNebulaBlaze() {
		super("nebulaBlaze");
		this.setMaxStackSize(1);
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		return stack;
	}
	
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		if(player.worldObj.isRemote)
			return;
		else if(count % 5 == 2 && ManaItemHandler.requestManaExactForTool(stack, player, 125, true)) {
			EntityNebulaBlaze blaze = new EntityNebulaBlaze(player.worldObj, player);
			blaze.setAttacker(player.getCommandSenderName());
			player.worldObj.spawnEntityInWorld(blaze);
		}
	}
	
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.block;
    }
    
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    public boolean usesMana(ItemStack stack) {
        return true;
    }
}