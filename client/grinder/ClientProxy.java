package grinder;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiChest;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.IGuiHandler;
import net.minecraft.src.forge.MinecraftForgeClient;

public class ClientProxy implements IProxy {

	@Override
	public GuiScreen getGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		
		if (tile != null && tile instanceof TileGrinder) {
			return new GuiGrinder(player.inventory, world, (TileGrinder)tile);
		} else {
			return null;
		}
	}

	@Override
	public void registerRenderInformation() {
		MinecraftForgeClient.preloadTexture("/grinder/block_textures.png");
	}

	@Override
	public void registerTileEntities() {
		ModLoader.registerTileEntity(TileGrinder.class, "Grinder");		
	}

	@Override
	public boolean isRemote() {
		return ModLoader.getMinecraftInstance().theWorld.isRemote;
	}

	@Override
	public World getCurrentWorld() {
		return ModLoader.getMinecraftInstance().theWorld;
	}

	@Override
	public File getMinecraftDir() {
		return Minecraft.getMinecraftDir();
	}
}
