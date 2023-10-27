package net.superkat.pumpkinpal.forge;

import net.minecraftforge.fml.loading.FMLPaths;
import net.superkat.pumpkinpal.ExampleExpectPlatform;

import java.nio.file.Path;

public class ExampleExpectPlatformImpl {
    /**
     * This is our actual method to {@link ExampleExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}