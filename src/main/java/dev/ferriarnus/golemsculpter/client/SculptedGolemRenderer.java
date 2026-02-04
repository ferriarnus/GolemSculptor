package dev.ferriarnus.golemsculpter.client;

import dev.ferriarnus.golemsculpter.entity.SculptedGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SculptedGolemRenderer extends MobRenderer<SculptedGolemEntity, SculptedGolemModel<SculptedGolemEntity>> {

    private static final ResourceLocation GOLEM_LOCATION = ResourceLocation.parse("textures/entity/iron_golem/iron_golem.png");

    public SculptedGolemRenderer(EntityRendererProvider.Context p_174188_) {
        super(p_174188_, new SculptedGolemModel(p_174188_.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
        //this.addLayer(new IronGolemCrackinessLayer(this));
        //this.addLayer(new IronGolemFlowerLayer(this, p_174188_.getBlockRenderDispatcher()));
    }

    public ResourceLocation getTextureLocation(SculptedGolemEntity p_115012_) {
        return GOLEM_LOCATION;
    }

    @Override
    protected void setupRotations(SculptedGolemEntity entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        if (!((double)entity.walkAnimation.speed() < 0.01)) {
            float $$5 = 13.0F;
            float $$6 = entity.walkAnimation.position(scale) + 6.0F;
            float $$7 = (Math.abs($$6 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            poseStack.mulPose(Axis.ZP.rotationDegrees(6.5F * $$7));
        }
    }
}
