package ab.client.core;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.ModBlocks;

public class ClientHelper {
	
	public static final ResourceLocation miscHuds = new ResourceLocation("ab:textures/misc/miscHuds.png");
	private static final ResourceLocation field_147529_c = new ResourceLocation("textures/environment/end_sky.png");
	private static final ResourceLocation field_147526_d = new ResourceLocation("textures/entity/end_portal.png");	  
	private static final Random field_147527_e = new Random(31100L);
	public static Minecraft mc = Minecraft.getMinecraft();
	private static FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);
	
	public static void renderCosmicBackground() {
		GL11.glDisable(GL11.GL_LIGHTING);
		field_147527_e.setSeed(31100L);
	    float f4 = 0.24F;
	    for (int i = 0; i < 16; i++) {
	    	GL11.glPushMatrix();
	    	float f5 = (16 - i);
	    	float f6 = 0.0625F;
	    	float f7 = 1.0F / (f5 + 1.0F);
	    	if (i == 0) {
	    		mc.renderEngine.bindTexture(field_147529_c);
	    		f7 = 0.1F;
	    		f5 = 65.0F;
	    		f6 = 0.125F;
	    		GL11.glEnable(GL11.GL_BLEND);
	    		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    	} 
	    	if (i == 1) {
	    		mc.renderEngine.bindTexture(field_147526_d);
	    		GL11.glEnable(GL11.GL_BLEND);
	    		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
	    		f6 = 0.5F;
	    	} 	
	    	GL11.glTranslatef(0.0f, 1.5f, 0.0f);
	    	GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
	    	GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
	    	GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
	    	GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
	    	GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
	    	GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
	    	GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
	    	GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
	    	GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
	    	GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
	    	GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
	    	GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
	    	GL11.glPopMatrix();
	    	GL11.glMatrixMode(GL11.GL_TEXTURE);
	    	GL11.glPushMatrix();
	    	GL11.glLoadIdentity();
	    	GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 20000L) / 20000.0F, 0.0F);
	    	GL11.glScalef(f6, f6, f6);
	    	GL11.glTranslatef(0.5F, 0.5F, 0.0F);
	    	GL11.glRotatef((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
	    	GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
	    	Tessellator tessellator = Tessellator.instance;
	    	tessellator.startDrawingQuads();
	    	Color color = Color.getHSBColor((float)Minecraft.getSystemTime() / 20.0F % 360.0F / 360.0F, 1.0F, 1.0F);
	    	float f11 = color.getRed() / 255.0F;
	    	float f12 = color.getGreen() / 255.0F;
	    	float f13 = color.getBlue() / 255.0F;
	    	tessellator.setBrightness(180);
	    	tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
	    	tessellator.addVertex(0.0D, f4, 0.0D);
	    	tessellator.addVertex(0.0D, f4, 1.0D);
	    	tessellator.addVertex(1.0D, f4, 1.0D);
	    	tessellator.addVertex(1.0D, f4, 0.0D);
	    	tessellator.draw();
	    	GL11.glPopMatrix();
	    	GL11.glMatrixMode(GL11.GL_MODELVIEW);
	    } 
	    GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
        GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	private static FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_) {
		field_147528_b.clear();
		field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
		field_147528_b.flip();
		return field_147528_b;
	}
	
	public static void drawChanceBar(int x, int y, int chance) {
		Minecraft mc = Minecraft.getMinecraft();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0f);
		mc.renderEngine.bindTexture(miscHuds);
		vazkii.botania.client.core.helper.RenderHelper.drawTexturedModalRect(x, y, 0.0F, 0, 0, 57, 6);
		int chancePercentage = Math.max(0, (int)((chance / 100.f) * 55.0D));
		vazkii.botania.client.core.helper.RenderHelper.drawTexturedModalRect(x + 1, y + 1, 0.0F, 0, 6, 55, 4);
		Color color = new Color(Color.HSBtoRGB((chance) / 360.0f, ((float)Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2D) + 1.0F) * 0.3F + 0.4F, 1.0f));
		GL11.glColor4ub((byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue(), (byte)255);
		vazkii.botania.client.core.helper.RenderHelper.drawTexturedModalRect(x + 1, y + 1, 0.0F, 0, 6, Math.min(55, chancePercentage), 4);
	}
	
	public static void renderPoolManaBar(int x, int y, int color, float alpha, int mana) {
		Minecraft mc = Minecraft.getMinecraft();
		int poolCount = (int)Math.floor(mana / 1000000.0D);
		if(poolCount < 0)
			poolCount = 0;
		int onePoolMana = mana - (poolCount * 1000000);
		String strPool = poolCount + "x";
		int xc = x - mc.fontRenderer.getStringWidth(strPool) / 2;
	    int yc = y;
		GL11.glPushMatrix();
		GL11.glTranslatef(xc + 42.0f, yc + 5.0f, 0.0f);
		RenderHelper.enableGUIStandardItemLighting();
    	RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(ModBlocks.pool), 0, 0);
    	RenderHelper.disableStandardItemLighting();
    	GL11.glTranslatef(18.0f, 5.0f, 300.0f);
    	mc.fontRenderer.drawString(strPool, 0, 0, color);
    	GL11.glPopMatrix();
    	if((poolCount * 1000000) == mana)
    		onePoolMana = poolCount * 1000000;
		HUDHandler.renderManaBar(x, y, color, alpha, onePoolMana, 1000000);
	}
	
	public static void drawPoolManaHUD(ScaledResolution res, String name, int mana, int maxMana, int color) {
		Minecraft mc = Minecraft.getMinecraft();
		int poolCount = (int)Math.floor(mana / 1000000.0D);
		int maxPoolCount = (int)Math.floor(maxMana / 1000000.0D);
		if(poolCount < 0)
			poolCount = 0;
		if(maxPoolCount < 0)
			maxPoolCount = 0;
		int onePoolMana = mana - (poolCount * 1000000);
		String strPool = poolCount + "x / " + maxPoolCount + "x";
		int xc = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(strPool) / 2 - 3;
	    int yc = res.getScaledHeight() / 2;
		GL11.glPushMatrix();
		GL11.glTranslatef(xc - 6.0f, yc + 30.0f, 0.0f);
		RenderHelper.enableGUIStandardItemLighting();
    	RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(ModBlocks.pool), 0, 0);
    	RenderHelper.disableStandardItemLighting();
    	GL11.glTranslatef(18.0f, 4.5f, 300.0f);
    	mc.fontRenderer.drawStringWithShadow(strPool, 0, 0, color);
    	GL11.glPopMatrix();
    	if((poolCount * 1000000) == mana)
    		onePoolMana = poolCount * 1000000;
		HUDHandler.drawSimpleManaHUD(color, onePoolMana, 1000000, name, res);
	}
}