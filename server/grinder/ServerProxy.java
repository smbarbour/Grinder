package grinder;

import java.io.File;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class ServerProxy implements IProxy {

	@Override
	public Object getGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile != null && tile instanceof TileGrinder) {
			TileGrinder teGrinder = (TileGrinder) tile;
			return new ContainerGrinder(player.inventory,teGrinder);
		} else {
			return null;
		}
	}

	@Override
	public void registerRenderInformation() {
		return;
	}

	@Override
	public void registerTileEntities() {
		ModLoader.registerTileEntity(TileGrinder.class, "Grinder");
	}

	@Override
	public boolean isRemote() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public World getCurrentWorld() {
		return null;
	}

	@Override
	public File getMinecraftDir() {
		return new File(".");
	}

}
