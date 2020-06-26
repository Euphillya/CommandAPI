package dev.jorel.commandapi.exceptions;

/**
 * An exception caused when using the BiomeArgument on Minecraft version < 1.16
 */
@SuppressWarnings("serial")
public class BiomeArgumentException extends RuntimeException {

	public BiomeArgumentException() {
		super("The BiomeArgument is only compatible with Minecraft 1.16 or later");
	}
	
}
