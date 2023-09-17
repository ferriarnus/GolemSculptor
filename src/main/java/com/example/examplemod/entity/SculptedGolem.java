package com.example.examplemod.entity;

import com.example.examplemod.blockentity.SculptorBlockEntity;
import com.example.examplemod.building.BuildingSculptor;
import com.minecolonies.api.entity.mobs.AbstractEntityMinecoloniesMob;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SculptedGolem extends AbstractGolem {

    public BuildingSculptor building;

    protected SculptedGolem(EntityType<? extends AbstractGolem> p_27508_, Level p_27509_) {
        super(EntityRegistry.GOLEM.get(), p_27509_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 0.6D, false));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        //this.targetSelector.addGoal(1, new DefendVillageTargetGoal(this)); //TODO custom Goal
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractEntityMinecoloniesMob.class, 5, false, false, (p_28879_) -> p_28879_ instanceof Enemy));
    }

    public void setBuilding(BuildingSculptor building) {
        this.building = building;
    }

    @Override
    public void onRemovedFromWorld() {
        building.removeGolem(this);
        super.onRemovedFromWorld();
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!building.getEntityUUID().contains(this.getUUID())) {
            this.remove(RemovalReason.DISCARDED);
        } else {
            building.addGolem(this);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putLong("building", building.getPosition().asLong());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        BlockPos pos = BlockPos.of(p_21450_.getLong("building"));
        BlockEntity be = level().getBlockEntity(pos);
        if (be instanceof SculptorBlockEntity sculptorBlock) {
            this.building = (BuildingSculptor) sculptorBlock.getBuilding();
        }
    }
}
