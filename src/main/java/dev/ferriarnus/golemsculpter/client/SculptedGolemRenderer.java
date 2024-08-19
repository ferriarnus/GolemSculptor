package dev.ferriarnus.golemsculpter.client;

import dev.ferriarnus.golemsculpter.entity.SculptedGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SculptedGolemRenderer extends MobRenderer<SculptedGolem, SculptedGolemModel<SculptedGolem>> {

    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");

    public SculptedGolemRenderer(EntityRendererProvider.Context p_174188_) {
        super(p_174188_, new SculptedGolemModel(p_174188_.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
        //this.addLayer(new IronGolemCrackinessLayer(this));
        //this.addLayer(new IronGolemFlowerLayer(this, p_174188_.getBlockRenderDispatcher()));
    }

    public ResourceLocation getTextureLocation(SculptedGolem p_115012_) {
        return GOLEM_LOCATION;
    }

    protected void setupRotations(SculptedGolem p_115014_, PoseStack p_115015_, float p_115016_, float p_115017_, float p_115018_) {
        super.setupRotations(p_115014_, p_115015_, p_115016_, p_115017_, p_115018_);
        if (!((double)p_115014_.walkAnimation.speed() < 0.01)) {
            float $$5 = 13.0F;
            float $$6 = p_115014_.walkAnimation.position(p_115018_) + 6.0F;
            float $$7 = (Math.abs($$6 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            p_115015_.mulPose(Axis.ZP.rotationDegrees(6.5F * $$7));
        }
    }
}
