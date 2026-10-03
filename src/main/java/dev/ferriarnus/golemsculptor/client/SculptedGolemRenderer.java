package dev.ferriarnus.golemsculptor.client;

import dev.ferriarnus.golemsculptor.GolemSculptor;
import dev.ferriarnus.golemsculptor.entity.SculptedGolemEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SculptedGolemRenderer extends GeoEntityRenderer<SculptedGolemEntity>  {

    public SculptedGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new SculptedGolemModel());
    }

    @Override
    public ResourceLocation getTextureLocation(SculptedGolemEntity animatable) {
        return switch (animatable.getGolemType()) {
            case ANDESITE -> ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "textures/entity/andesite_golem.png");
            case GRANITE -> ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "textures/entity/granite_golem.png");
            case QUARTZ -> ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "textures/entity/quartz_golem.png");
            case PRISMARINE -> ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "textures/entity/prismarine_golem.png");
            case OBSIDIAN -> ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "textures/entity/obsidian_golem.png");
        };
    }
}
