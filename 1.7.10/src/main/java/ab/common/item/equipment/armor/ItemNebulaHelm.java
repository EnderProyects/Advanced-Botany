package ab.common.item.equipment.armor;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class ItemNebulaHelm extends ItemNebulaArmor implements IManaDiscountArmor, IManaGivingItem {

	private static final String TAG_COSMIC_FACE = "enableCosmicFace";
	
	public ItemNebulaHelm() {
		this("nebulaHelm");
	}
	
	public ItemNebulaHelm(String str) {
		super(0, str);
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(player.isSneaking())
			setCosmicFace(stack, !enableCosmicFace(stack));
		else 
			super.onItemRightClick(stack, world, player);
		return stack;
	}
	
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		super.addInformation(stack, player, list, adv);
		if(!enableCosmicFace(stack))
			addStringToTooltip(EnumChatFormatting.GREEN + StatCollector.translateToLocal("ab.nebulaHelm.mask"), list);
	}
	
	public boolean enableCosmicFace(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_COSMIC_FACE, true);
	}
	
	public void setCosmicFace(ItemStack stack, boolean enable) {
		ItemNBTHelper.setBoolean(stack, TAG_COSMIC_FACE, enable);
	}
	
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
	    super.onArmorTick(world, player, stack);
	    if(hasArmorSet(player)) {
	    	int food = player.getFoodStats().getFoodLevel();
	    	if (food > 0 && food < 18 && player.shouldHeal() && player.ticksExisted % 80 == 0)
	    		player.heal(1.0F);
	    	ManaItemHandler.dispatchManaExact(stack, player, 5, true);
	    } 
	}
	
	public float getDiscount(ItemStack stack, int slot, EntityPlayer player) {
	    return hasArmorSet(player) ? 0.30F : 0.0F;
	}
	
	public Multimap getAttributeModifiers(ItemStack stack) {
		HashMultimap hashMultimap = HashMultimap.create();
	    UUID uuid = new UUID(getUnlocalizedName().hashCode(), 0L);
	    hashMultimap.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier(uuid, "NebulaHelm modifier", 20.0D * (1.0f - (this.getDamage(stack) / 1000.0f)), 0));
	    return (Multimap)hashMultimap;
	}
}