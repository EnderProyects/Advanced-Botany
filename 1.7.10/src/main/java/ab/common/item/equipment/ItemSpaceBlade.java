package ab.common.item.equipment;

import java.awt.Color;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Multimap;

import ab.AdvancedBotany;
import ab.api.AdvancedBotanyAPI;
import ab.api.IRankItem;
import ab.common.core.ConfigABHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILensEffect;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityManaBurst;

public class ItemSpaceBlade extends ItemSword implements IRankItem, ILensEffect, IManaUsingItem {
	
	private static final IIcon[] icons = new IIcon[3];
	private static final int recharge = 36;
	
	public static final int[] LEVELS = new int[] { 0, 10000, 1000000, 10000000, 100000000, 1000000000 };
	private static final int[] CREATIVE_MANA = new int[] { 9999, 999999, 9999999, 99999999, 999999999, 2147483646 };
	
	public ItemSpaceBlade() {
		super(AdvancedBotanyAPI.mithrilToolMaterial);
		this.setCreativeTab(AdvancedBotany.tabAB);
		this.setUnlocalizedName("spaceBlade");
	}
	
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int mana : CREATIVE_MANA) {
			ItemStack stack = new ItemStack(item);
			setMana(stack, mana);
			list.add(stack);
		} 
	}
	
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if(!player.worldObj.isRemote) {
			ItemNBTHelper.setInt(stack, "postAttackTick", 3);
			if(this.getLevel(stack) >= 3 && this.isEnabledMode(stack)) {
				float size = this.getLevel(stack) >= 4 ? (this.getLevel(stack) >= 5 ? 3.2f : 2.2f) : 1.2f; 
				AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(size, 1.0f, size);
				List<EntityLivingBase> entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axis);
				for (EntityLivingBase living : entities) {
					if (living instanceof EntityPlayer && (((EntityPlayer)living).getCommandSenderName().equals(player.getCommandSenderName()) || (MinecraftServer.getServer() != null && !MinecraftServer.getServer().isPVPEnabled())))
						continue; 
					if (living.hurtTime == 0) {
						float damage = 4.0F + AdvancedBotanyAPI.mithrilToolMaterial.getDamageVsEntity();
						living.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
					}
				}
			}
		}		
		return super.onLeftClickEntity(stack, player, entity);
	}
	
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
		int tick = ItemNBTHelper.getInt(stack, "tick", 0);
		if(tick > 0) 
			ItemNBTHelper.setInt(stack, "tick", tick - 1);
		if(!world.isRemote) {
			int postAttackTick = ItemNBTHelper.getInt(stack, "postAttackTick", 0);
			if(postAttackTick > 0) 
				ItemNBTHelper.setInt(stack, "postAttackTick", postAttackTick - 1);
			if(entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)entity;
				PotionEffect haste = player.getActivePotionEffect(Potion.digSpeed);
		        float check = (haste == null) ? 0.16666667F : ((haste.getAmplifier() == 1) ? 0.5F : 0.4F);
		        if(player.getCurrentEquippedItem() == stack && player.swingProgress == check && this.getLevel(stack) >= 1 && postAttackTick == 0 && ManaItemHandler.requestManaExactForTool(stack, player, 120, true)) {
		        	EntityManaBurst burst = getBurst(player, stack);
		        	world.spawnEntityInWorld((Entity)burst);
		        }
			}
		} else {
			if(tick > 0 && par5) {
				for(int i = 0; i < 9; i++) {
					float r = world.rand.nextBoolean() ? (225.0f / 255.0f) : (101.0f / 255.0f);
					float g = world.rand.nextBoolean() ? (67.0f / 255.0f) : (209.0f / 255.0f);
					float b = world.rand.nextBoolean() ? (240.0f / 255.0f) : (225.0f / 255.0f);
					Botania.proxy.sparkleFX(world, entity.posX + (Math.random() - 0.5D), entity.posY + (Math.random() - 0.5D) - 0.4f, entity.posZ + (Math.random() - 0.5D), r + (float)(Math.random() / 4 - 0.125D), g + (float)(Math.random() / 4 - 0.125D), b + (float)(Math.random() / 4 - 0.125D), 1.2F * (float)(Math.random() - 0.5D), 2);
				}
			}
		}
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(!player.isUsingItem() && !player.isSneaking() && ItemNBTHelper.getInt(stack, "tick", 0) == 0 && this.getLevel(stack) >= 2) {
			double posX = player.posX;
			double posY = player.posY;
			double posZ = player.posZ;					  
			Vec3 vec3 = player.getLook(1.0F).normalize();
			player.motionX += vec3.xCoord * 2.5f;
			player.motionY += (vec3.yCoord / 1.8f);
			player.motionZ += vec3.zCoord * 2.5f;
			ItemNBTHelper.setInt(stack, "tick", recharge);
			return stack;					      	
		}
		else if(player.isSneaking() && this.getLevel(stack) >= 3) {
			ItemNBTHelper.setBoolean(stack, "isEnabledMode", !this.isEnabledMode(stack));
		}
		return super.onItemRightClick(stack, world, player);
	}
	
	public EntityManaBurst getBurst(EntityPlayer player, ItemStack stack) {
		EntityManaBurst burst = new EntityManaBurst(player);
		float motionModifier = 8.0F;
		burst.setColor(player.worldObj.rand.nextBoolean() ? 0x78dde6 : 0xe143f0);
		burst.setMana(100);
		burst.setStartingMana(100);
		burst.setMinManaLoss(40);
		burst.setManaLossPerTick(5.0F);
		burst.setGravity(0.0F);
		burst.setMotion(burst.motionX * motionModifier, burst.motionY * motionModifier, burst.motionZ * motionModifier);
		ItemStack lens = stack.copy();
		ItemNBTHelper.setString(lens, "attackerUsername", player.getCommandSenderName());
		burst.setSourceLens(lens);
		return burst;
	}
	
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4) {
		String rankFormat = StatCollector.translateToLocal("botaniamisc.toolRank");
		String rank = StatCollector.translateToLocal("botania.rank" + getLevel(stack));
		list.add(String.format(rankFormat, new Object[] { rank }).replaceAll("&", "\u00A7"));
		if (getMana(stack) == Integer.MAX_VALUE)
			list.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("abmisc.swordFull")); 
		if (GuiScreen.isShiftKeyDown()) {
			int level = (getLevel(stack));
			list.add((level >= 1 ? EnumChatFormatting.GREEN : "") + StatCollector.translateToLocal("abmisc.swordInfo.1"));
			list.add((level >= 2 ? EnumChatFormatting.GREEN : "") + StatCollector.translateToLocal("abmisc.swordInfo.2"));
			list.add((level >= 3 ? EnumChatFormatting.GREEN : "") + StatCollector.translateToLocal(("abmisc.swordInfo.LEVEL").replaceAll("LEVEL", "" + (level >= 3 ? level : 3))));
		} else {
			if(this.getLevel(stack) != 0)
				list.add(StatCollector.translateToLocal("botaniamisc.shiftinfo").replaceAll("&", "\u00A7"));
		} 
	}
	
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}
	
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int par2) {
		return (par2 == 1) ? Color.HSBtoRGB(0.836F, 1.0f - (float)getDurabilityForDisplay(stack), 1.0F) : 16777215;
	}
	
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean showDurabilityBar(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, "tick", 0) != 0;
	}
	
	@SideOnly(Side.CLIENT)
	public double getDurabilityForDisplay(ItemStack stack) {
		int tick = ItemNBTHelper.getInt(stack, "tick", 0);
		return (double)(tick) / (double)recharge;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ir) {
		this.icons[0] = ir.registerIcon("ab:itemMithrillSword_mode_0");
		this.icons[1] = ir.registerIcon("ab:itemMithrillSword_gem");
		this.icons[2] = ir.registerIcon("ab:itemMithrillSword_mode_1");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		int iconID = Math.min(1, pass);
		if(isEnabledMode(stack) && iconID == 0) {
			iconID = 2;
		}
		return this.icons[iconID];
	}
	
	private boolean isEnabledMode(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, "isEnabledMode", false);
	}
	
	public int[] getLevels() {
		return LEVELS;
	}
	
	public int getLevel(ItemStack stack) {
		int mana = getMana_(stack);
		for (int i = LEVELS.length - 1; i > 0; i--) {
			if (mana >= LEVELS[i])
				return i;
		    } 
		return 0;
	}
	
	public static int getMana_(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, "mana", 0);
	}
	
	public static void setMana(ItemStack stack, int mana) {
		ItemNBTHelper.setInt(stack, "mana", mana);
	}

	public void addMana(ItemStack stack, int mana) {
		setMana(stack, Math.min(getMana(stack) + mana, 2147483647));
	}

	public boolean canExportManaToItem(ItemStack stack, ItemStack stack1) {
		return false;
	}

	public boolean canExportManaToPool(ItemStack stack, TileEntity tile) {
		return false;
	}

	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return !(otherStack.getItem() instanceof vazkii.botania.api.mana.IManaGivingItem);
	}

	public boolean canReceiveManaFromPool(ItemStack stack, TileEntity tile) {
		return true;
	}

	public int getMana(ItemStack stack) {
		return getMana_(stack);
	}

	public int getMaxMana(ItemStack stack) {
		return Integer.MAX_VALUE;
	}

	public boolean isNoExport(ItemStack stack) {
		return true;
	}

	public void apply(ItemStack stack, BurstProperties burst) {}

	public boolean collideBurst(IManaBurst burst, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		return dead;
	}

	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		return true;
	}

	public void updateBurst(IManaBurst burst, ItemStack stack) {		
		EntityThrowable entity = (EntityThrowable)burst;
		String attacker = ItemNBTHelper.getString(burst.getSourceLens(), "attackerUsername", "");		
		AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(1.0D, 1.0D, 1.0D);
		List<EntityLivingBase> entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axis);
		for (EntityLivingBase living : entities) {
			if (living instanceof EntityPlayer && (((EntityPlayer)living).getCommandSenderName().equals(attacker) || (MinecraftServer.getServer() != null && !MinecraftServer.getServer().isPVPEnabled())))
				continue; 
			if (living.hurtTime == 0) {
				int cost = 33;
				int mana = burst.getMana();
		        if (mana >= cost) {
		        	burst.setMana(mana - cost);
		        	float damage = getSwordDamage(stack);
		        	if (!burst.isFake() && !entity.worldObj.isRemote) {
		        		EntityPlayer player = living.worldObj.getPlayerEntityByName(attacker);
		        		living.attackEntityFrom((player == null) ? DamageSource.magic : DamageSource.causePlayerDamage(player), damage);
		        		entity.setDead();
		        		break;
		        	} 
		        } 
			} 
		} 
	}
	
	public boolean usesMana(ItemStack stack) {
        return true;
    }
	
	private float getSwordDamage(ItemStack stack) {
		int level = this.getLevel(stack);
		return (float)Math.round((AdvancedBotanyAPI.mithrilToolMaterial.getDamageVsEntity() + (level * level / 1.5f)) * ConfigABHandler.damageFactorSpaceSword);
	}
	
	public Multimap getAttributeModifiers(ItemStack stack) {
		Multimap multimap = this.getItemAttributeModifiers();
		multimap.clear();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", getSwordDamage(stack), 0));
		multimap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon speed", 0.1f, 0));
		return multimap;
	}
}