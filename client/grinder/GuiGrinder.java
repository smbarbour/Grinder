package grinder;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;
import net.minecraft.src.World;

public class GuiGrinder extends GuiContainer {
	private TileGrinder tileEntity;

	public GuiGrinder(InventoryPlayer invPlayer, World world, TileGrinder tile) {
		super(new ContainerGrinder(invPlayer,tile));
		this.tileEntity = tile;
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString("Grinder", 60, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int var4 = this.mc.renderEngine.getTexture("/grinder/sprites/GUIGrinder.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		int xOrigin = (this.width - this.xSize) / 2;
		int yOrigin = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(xOrigin, yOrigin, 0, 0, this.xSize, this.ySize);
		int progress;
		if (this.tileEntity.power > 0) {
			progress = this.tileEntity.gaugePowerScaled(14);
			this.drawTexturedModalRect(xOrigin + 25, yOrigin + 33 + 14 - progress, 176, 12 - progress, 14, progress + 2);
		}
		
		progress = this.tileEntity.gaugeProgressScaled(24);
		this.drawTexturedModalRect(xOrigin + 79, yOrigin + 34, 176, 14, progress + 1, 16);
	}

}
