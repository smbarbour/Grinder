package net.minecraft.src;

import grinder.BlockGrinder;
import grinder.DefaultProps;
import grinder.IProxy;
import grinder.PacketHandler;
import grinder.ServerClientProxy;
import grinder.TileGrinder;

import java.io.File;
import java.lang.reflect.Array;

import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.forge.Property;

public class mod_Grinder extends NetworkMod {
	public static mod_Grinder instance;
	public static Configuration config;
	public static Block blockGrinder;
	
	public static int[] guiIds = new int[1];
	private static IProxy proxy;
			
	@Override
	public String getVersion() {
		return DefaultProps.VERSION;
	}

	@Override
	public void load() {
		MinecraftForge.versionDetect("Grinder", 3, 0, 1);
		instance = this;
		proxy = ServerClientProxy.getProxy();
		BuildCraftCore.initialize();
		try {
			config = new Configuration(new File(proxy.getMinecraftDir(), "config/BC_Grinder.cfg"));
			config.load();
			blockGrinder = new BlockGrinder(getBlockId("blockGrinder", 175));
		}
		catch (Exception ex1) {
			System.out.println("[Grinder] Error while trying to access configuration: " + ex1);
			config = null;
		} finally {
			config.save();
		}
				
		ModLoader.registerBlock(blockGrinder);
		proxy.registerTileEntities();
		//ModLoader.addName(blockGrinder, "Grinder");
				
/*
  		try {
			guiIds[0] = getConfigInt("guiIdGrinder", 50, Configuration.CATEGORY_GENERAL, false,"");
		} catch (Exception ex) {
			System.out.println("[Grinder] Error while trying to access config, wasn\'t loaded properly!");
			guiIds[0] = 50;
		}
*/
		CraftingManager.getInstance().addRecipe(new ItemStack(blockGrinder, 1), new Object[] {"GSG", "SPS", "GSG", Character.valueOf('G'), BuildCraftCore.diamondGearItem, Character.valueOf('S'), Item.ingotIron, Character.valueOf('P'), Item.pickaxeDiamond});
		
		MinecraftForge.setGuiHandler(this, proxy);
		MinecraftForge.registerConnectionHandler(new PacketHandler());
		proxy.registerRenderInformation();
	}

	public static int getBlockId(String blockName, int defaultValue) {
		if (config == null) {
			return defaultValue;
		} else {
			try {
				return Integer.valueOf(config.getOrCreateBlockIdProperty(blockName, defaultValue).value).intValue();
			} catch (Exception ex1) {
				System.out.println("[Grinder] Error while trying to access config, wasn\'t loaded properly!");
				return defaultValue;
			}
		}
	}

    public static int getConfigInt(String stringValue, int defaultValue, String category, boolean useComment, String comment)
    {
        if (config == null)
        {
            return defaultValue;
        } else
        {
            try
            {
            	if(useComment)
            	{
            		Property prop = config.getOrCreateIntProperty(stringValue, category, defaultValue);
            		prop.comment = comment;
            		return Integer.valueOf(prop.value).intValue();
            	}
                return Integer.valueOf(config.getOrCreateIntProperty(stringValue, category, defaultValue).value).intValue();
            }
            catch (Exception var3)
            {
                System.out.println("[Grinder] Error while trying to access config, wasn\'t loaded properly!");
                return defaultValue;
            }
        }
    }
    
	@Override
	public boolean clientSideRequired() {
		return true;
	}

	@Override
	public boolean serverSideRequired() {
		return false;
	}

}
