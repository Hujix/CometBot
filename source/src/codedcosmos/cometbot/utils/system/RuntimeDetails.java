package codedcosmos.cometbot.utils.system;

public class RuntimeDetails {
	public static int getHeapSize() {
		return convertBytesToMegabytes(Runtime.getRuntime().totalMemory());
	}
	
	public static int getHeapMaxSize() {
		return convertBytesToMegabytes(Runtime.getRuntime().maxMemory());
	}
	
	public static int getHeapFreeSize() {
		return convertBytesToMegabytes(Runtime.getRuntime().freeMemory());
	}
	
	public static int getHeapUsage() {
		return getHeapSize()-getHeapFreeSize();
	}
	
	public static int convertBytesToMegabytes(long bytes) {
		return (int)(bytes/1024/1024);
	}
}
