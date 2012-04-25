package grinder;

import java.io.File;

import net.minecraft.src.EntityItem;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.forge.IGuiHandler;

public interface IProxy extends IGuiHandler {
	
	public abstract void registerRenderInformation();
	public abstract void registerTileEntities();
	public abstract boolean isRemote();
	public abstract World getCurrentWorld();
	public abstract File getMinecraftDir();

}
