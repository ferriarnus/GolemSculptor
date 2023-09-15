package com.example.examplemod.job;

import com.example.examplemod.ai.SculptorAI;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.coremod.colony.jobs.AbstractJob;

public class JobSculptor extends AbstractJob<SculptorAI, JobSculptor> {
    public JobSculptor(ICitizenData entity) {
        super(entity);
    }

    @Override
    public SculptorAI generateAI() {
        return new SculptorAI(this);
    }
}
