package net.superkat.pumpkinpal.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.superkat.pumpkinpal.entity.client.PumpkinPalRenderer;

public class PumpkinPalFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(PumpkinPalFabric.PUMPKIN_PAL, PumpkinPalRenderer::new);
    }
}
