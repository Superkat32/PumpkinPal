package net.superkat.pumpkinpal.forge;

import net.minecraftforge.fml.loading.FMLPaths;
import net.superkat.pumpkinpal.PumpkinPalPlatform;

import java.nio.file.Path;

public class PumpkinPalPlatformImpl {
    /**
     * This is our actual method to {@link PumpkinPalPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
