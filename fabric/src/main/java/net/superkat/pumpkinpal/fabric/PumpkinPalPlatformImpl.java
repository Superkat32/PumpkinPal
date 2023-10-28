package net.superkat.pumpkinpal.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.superkat.pumpkinpal.PumpkinPalPlatform;

import java.nio.file.Path;

public class PumpkinPalPlatformImpl {
    /**
     * This is our actual method to {@link PumpkinPalPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
