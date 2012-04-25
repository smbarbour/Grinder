package grinder;

import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.MinecraftForge;

public enum ServerClientProxy {
	CLIENT("grinder.ClientProxy"),
	SERVER("grinder.ServerProxy");
	
	private String className;
	private ServerClientProxy(String proxyClassName) {
		className=proxyClassName;
	}
	
	private IProxy buildProxy() {
		try {
			return (IProxy) Class.forName(className).newInstance();
		} catch (Exception e) {
			ModLoader.getLogger().severe("A fatal error has occurred while initializing Grinder");
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}
	public static IProxy getProxy() {
		if (MinecraftForge.isClient()) {
			return CLIENT.buildProxy();
		} else {
			return SERVER.buildProxy();
		}
	}
}
