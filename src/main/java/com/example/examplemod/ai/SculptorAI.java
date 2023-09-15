package com.example.examplemod.ai;

import com.example.examplemod.building.BuildingSculptor;
import com.example.examplemod.job.JobSculptor;
import com.minecolonies.coremod.entity.ai.basic.AbstractEntityAISkill;
import org.jetbrains.annotations.NotNull;

public class SculptorAI extends AbstractEntityAISkill<JobSculptor, BuildingSculptor> {

    public SculptorAI(@NotNull JobSculptor job) {
        super(job);
    }

    @Override
    public Class<BuildingSculptor> getExpectedBuildingClass() {
        return BuildingSculptor.class;
    }
}
