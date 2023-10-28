package net.superkat.pumpkinpal.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.superkat.pumpkinpal.entity.custom.PumpkinPalEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PumpkinPalRenderer extends GeoEntityRenderer<PumpkinPalEntity> {
    public PumpkinPalRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PumpkinPalModel());
    }

//    @Override
//    public RenderType getRenderType(PumpkinPalEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
//        return RenderType.entityCutoutNoCull(texture);
//    }

    //    @Override
//    public ResourceLocation getTextureLocation(PumpkinPalEntity entity) {
//        return new ResourceLocation(PumpkinPal.MOD_ID, "textures/entity/pumpkinpal.png");
//    }
//
//    @Override
//    public void render(PumpkinPalEntity entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
//        if(entity.isBaby()) {
//            poseStack.scale(0.35f, 0.35f, 0.35f);
//        }
//
//        super.render(entity, f, g, poseStack, multiBufferSource, i);
//    }
}
