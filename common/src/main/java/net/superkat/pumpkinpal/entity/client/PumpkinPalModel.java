package net.superkat.pumpkinpal.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.superkat.pumpkinpal.PumpkinPal;
import net.superkat.pumpkinpal.entity.custom.PumpkinPalEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class PumpkinPalModel extends DefaultedEntityGeoModel<PumpkinPalEntity> {
    public PumpkinPalModel() {
        super(new ResourceLocation(PumpkinPal.MOD_ID, "pumpkinpal"));
    }

//    @Override
//    public ResourceLocation getModelResource(PumpkinPalEntity animatable) {
//        return new ResourceLocation(PumpkinPal.MOD_ID, "geo/entity/pumpkinpal.geo.json");
//    }
//
//    @Override
//    public ResourceLocation getTextureResource(PumpkinPalEntity animatable) {
//        return new ResourceLocation(PumpkinPal.MOD_ID, "textures/entity/pumpkinpal.png");
//    }
//
//    @Override
//    public ResourceLocation getAnimationResource(PumpkinPalEntity animatable) {
//        return new ResourceLocation(PumpkinPal.MOD_ID, "animations/entity/pumpkinpal.animation.json");
//    }

//    @Override
//    public void setCustomAnimations(PumpkinPalEntity animatable, long instanceId, AnimationState<PumpkinPalEntity> animationState) {
////        super.setCustomAnimations(animatable, instanceId, animationState);
//        CoreGeoBone head = getAnimationProcessor().getBone("top");
//
//        if(head != null) {
//            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
//
//            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
//            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
//        }
//    }
}
