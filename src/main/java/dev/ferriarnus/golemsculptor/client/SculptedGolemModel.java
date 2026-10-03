package dev.ferriarnus.golemsculptor.client;

import dev.ferriarnus.golemsculptor.GolemSculptor;
import dev.ferriarnus.golemsculptor.entity.SculptedGolemEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SculptedGolemModel extends DefaultedEntityGeoModel<SculptedGolemEntity> {

    public SculptedGolemModel() {
        super(ResourceLocation.fromNamespaceAndPath(GolemSculptor.MODID, "golem"));
    }
}
