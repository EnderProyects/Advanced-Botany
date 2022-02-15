package ab.common.block.tile;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import ab.api.IRenderHud;
import ab.client.core.ClientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.core.helper.Vector3;

public class TileManaCharger extends TileSimpleInventory implements ISidedInventory, IRenderHud, IWandBindable {

	private static final int MANA_SPEED = 11240;
	
	public boolean requestsClientUpdate = false;
	int clientMana = -1;
	int receiverPosX = -1;
	int receiverPosY = -1;
	int receiverPosZ = -1;
	public int clientTick[] = new int[] {0, 0, 3, 12, 6};
	
	public void updateEntity() {
		if(requestsClientUpdate && !this.worldObj.isRemote && this.worldObj.getTotalWorldTime() % 15 == 0) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			requestsClientUpdate = false;
		}
		ISparkAttachable receiver = this.getReceiver();
		if(receiver == null)
			return;
		for(int i = 0; i < this.getSizeInventory(); i++) {
			ItemStack stack = this.getStackInSlot(i);
			if(stack == null)
				continue;
			else if(stack.getItem() instanceof IManaItem) {
				IManaItem mana = (IManaItem)stack.getItem();
				if(i == 0) {
					if(mana.getMana(stack) > 0 && !receiver.isFull() && mana.canExportManaToPool(stack, (TileEntity)receiver)) {
						int availableMana = ((ISparkAttachable)receiver).getAvailableSpaceForMana();
						int manaVal = Math.min(Math.min(mana.getMaxMana(stack) / 256, MANA_SPEED) * 3, Math.min(availableMana, mana.getMana(stack)));
						if(!this.worldObj.isRemote) {
							mana.addMana(stack, -manaVal);
							requestsClientUpdate = true;
						} else {
							clientTick[i]++;
						}
						receiver.recieveMana(manaVal);
					}
					continue;
				}
				else if(receiver.getCurrentMana() > 0 && mana.getMana(stack) < mana.getMaxMana(stack) && mana.canReceiveManaFromPool(stack, (TileEntity)receiver)) {
					int manaVal = Math.min(Math.min(mana.getMaxMana(stack) / 256, MANA_SPEED), Math.min(receiver.getCurrentMana(), mana.getMaxMana(stack) - mana.getMana(stack)));
					if(!this.worldObj.isRemote) {
						mana.addMana(stack, manaVal);
						requestsClientUpdate = true;
					} else {
						clientTick[i]++;
					}
					receiver.recieveMana(-manaVal);
				}
			}
		}
	}
	
	public ISparkAttachable getReceiver() {
		if(this.worldObj != null && this.receiverPosY != -1) {
			TileEntity tile = this.worldObj.getTileEntity(receiverPosX, receiverPosY, receiverPosZ);
			if(tile != null && tile instanceof ISparkAttachable) {
				return (ISparkAttachable)tile;
			}
		}
		return null;
	}
	
	public boolean canSelect(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		return true;
	}
	
	public boolean bindTo(EntityPlayer player, ItemStack wand, int x, int y, int z, int side) {
		TileEntity tile = this.worldObj.getTileEntity(x, y, z);
		boolean isFar = Math.abs(this.xCoord - x) >= 16 || Math.abs(this.yCoord - y) >= 16 || Math.abs(this.zCoord - z) >= 16;
		if(isFar)
			return false;
		else if(tile instanceof ISparkAttachable) {
			if(!this.worldObj.isRemote) {
				this.receiverPosX = tile.xCoord;
				this.receiverPosY = tile.yCoord;
				this.receiverPosZ = tile.zCoord;
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
			return true;
		}
	    return false;
	}
	
	public ChunkCoordinates getBinding() {
		IManaReceiver receiver = this.getReceiver();
		if (receiver == null)
			return null; 
		TileEntity tile = (TileEntity)receiver;
		return new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	public static float getManaPercent(ItemStack stack) {
		if(!(stack.getItem() instanceof IManaItem))
			return 0.0f;
		IManaItem mana = (IManaItem)stack.getItem();
		return (float)mana.getMana(stack) / (mana.getMaxMana(stack) / 100.0f);
	}
	
	public void renderHud(Minecraft mc, ScaledResolution res) {		        
		int xc = res.getScaledWidth() / 2;
	    int yc = res.getScaledHeight() / 2;
	    int radius = 42;
	    int amt = 0;
	    for(int i = 0; i < getSizeInventory(); i++)
	    	if(getStackInSlot(i) != null)
	    		amt++;
	    float angle = -90.0f;
	    if(amt >= 0) {
			for (int i = 0; i < getSizeInventory(); i++) {
				ItemStack stack = getStackInSlot(i);
				if(stack == null)
					continue;
				float anglePer = 360.0F / amt;
				double xPos = xc + Math.cos(angle * Math.PI / 180.0D) * radius - 8.0D;
		        double yPos = yc + Math.sin(angle * Math.PI / 180.0D) * radius - 8.0D;
		        RenderHelper.enableGUIStandardItemLighting();
		        GL11.glPushMatrix();
		        GL11.glEnable(3042);
		        GL11.glBlendFunc(770, 771);
		        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		        GL11.glTranslated(xPos, yPos, 0.0D);
				vazkii.botania.client.core.helper.RenderHelper.renderProgressPie(0, 0, getManaPercent(stack) / 100.0f, stack);
				if(i == 0) {
					GL11.glScalef(0.75f, 0.75f, 0.75f);
		        	GL11.glTranslated(11.0D, 10.0D, 0.0D);
		        	RenderHelper.enableGUIStandardItemLighting();
		        	RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(ModBlocks.pool), 0, 0);
		        }
				GL11.glDisable(2896);
				GL11.glDisable(3042);
				GL11.glPopMatrix();
				RenderHelper.disableStandardItemLighting();
		        angle += anglePer;
			} 
	    }
	}
	
	public void onWanded(EntityPlayer player, ItemStack wand) {
		ISparkAttachable reciever = getReceiver();
		if(player == null)
			return; 
		if(this.worldObj.isRemote && reciever != null)
			clientMana = reciever.getCurrentMana();
		this.worldObj.playSoundAtEntity((Entity)player, "botania:ding", 0.11F, 1.0F);
	}
	
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		ISparkAttachable reciever = getReceiver();
		if (reciever != null) {
			String name = StatCollector.translateToLocal("ab.manaCharger.wandHud");
			TileEntity receiverTile = (TileEntity)reciever;
			ItemStack recieverStack = new ItemStack(this.worldObj.getBlock(receiverTile.xCoord, receiverTile.yCoord, receiverTile.zCoord), 1, receiverTile.getBlockMetadata());
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			if (recieverStack != null && recieverStack.getItem() != null) {
				String stackName = recieverStack.getDisplayName();
				int width = 16 + mc.fontRenderer.getStringWidth(stackName) / 2;
				int x = res.getScaledWidth() / 2 - width;
				int y = res.getScaledHeight() / 2 + 48;
				mc.fontRenderer.drawStringWithShadow(stackName, x + 20, y + 5, 0xf4f4f4);
				RenderHelper.enableGUIStandardItemLighting();
				RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, recieverStack, x, y);
				RenderHelper.disableStandardItemLighting();
			} 
			GL11.glDisable(2896);
			GL11.glDisable(3042);
			ClientHelper.drawPoolManaHUD(res, name, clientMana, reciever.getAvailableSpaceForMana() + reciever.getCurrentMana(), 0xb9bbae);
		}
	}
	
	public int getSizeInventory() {
		return 5;
	}
	
	public int getInventoryStackLimit() {
		return 1;
	}

	public String getInventoryName() {
		return "ab.manaCharger";
	}
	
	public void writeCustomNBT(NBTTagCompound nbtt) {
		super.writeCustomNBT(nbtt);
		nbtt.setInteger("bindingX", this.receiverPosX);
		nbtt.setInteger("bindingY", this.receiverPosY);
		nbtt.setInteger("bindingZ", this.receiverPosZ);
		nbtt.setBoolean("requestUpdate", this.requestsClientUpdate);
		this.requestsClientUpdate = false;
	}
	
	public void readFromNBT(NBTTagCompound nbtt) {
		super.readFromNBT(nbtt);
		this.requestsClientUpdate = nbtt.getBoolean("requestUpdate");
	}
	
	public void readCustomNBT(NBTTagCompound nbtt) {
		super.readCustomNBT(nbtt);
		this.receiverPosX = nbtt.getInteger("bindingX");
		this.receiverPosY = nbtt.getInteger("bindingY");
		this.receiverPosZ = nbtt.getInteger("bindingZ");
	}

	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[0];
	}

	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return false;
	}

	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}
}