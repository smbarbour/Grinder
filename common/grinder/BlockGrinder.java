package grinder;

import buildcraft.api.IPipeConnection;
import buildcraft.api.Orientations;
import buildcraft.api.Position;
import buildcraft.core.Utils;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Container;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityFurnace;
import net.minecraft.src.World;
import net.minecraft.src.mod_Grinder;
import net.minecraft.src.forge.ITextureProvider;

public class BlockGrinder extends BlockContainer implements ITextureProvider, IPipeConnection {

	int textureTop;
	int textureFront;
	int textureSide;
	
	public BlockGrinder(int blockId) {
		super(blockId,Material.iron);
		
		this.setResistance(10F);
		this.setHardness(2.0F);
		this.setStepSound(soundMetalFootstep);
		this.setBlockName("Grinder");
		
		textureSide = 0;
		textureFront = 1;
		textureTop = 2;
	}
	
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityLiving) {
		super.onBlockPlacedBy(world, i, j, k, entityLiving);
		
		Orientations orientation = Utils.get2dOrientation(new Position(entityLiving.posX, entityLiving.posY, entityLiving.posZ), new Position(i,j,k));
		
		world.setBlockMetadataWithNotify(i, j, k, orientation.reverse().ordinal());
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if (j == 0 && i == 3) {
			return textureFront;
		}
		
		if (i == j) {
			return textureFront;
		}
		
		switch (i) {
		case 1:
			return textureTop;
		default:
			return textureSide;				
		}
	}
	
	@Override
	public String getTextureFile() {
		return "/grinder/sprites/block_textures.png";
	}

	@Override
	public TileEntity getBlockEntity() {
		return new TileGrinder();
	}

	@Override
	public boolean isPipeConnected(IBlockAccess blockAccess, int x1, int y1, int z1, int x2, int y2, int z2) {
		return true;
	}
	
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer)
    {
    	TileEntity tile = world.getBlockTileEntity(x, y, z);
    	
    	if (tile == null || !(tile instanceof TileGrinder)) {
    		return true;
    	}

    	if (world.isRemote)
        {
            return true;
        }
        
    	entityPlayer.openGui(mod_Grinder.instance, 0, world, x, y, z);
        return true;
    }
}
