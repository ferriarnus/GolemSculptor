package dev.ferriarnus.golemsculptor.job;

import com.minecolonies.core.colony.jobs.AbstractJob;
import dev.ferriarnus.golemsculptor.ai.SculptorAI;
import com.minecolonies.api.colony.ICitizenData;

public class JobSculptor extends AbstractJob<SculptorAI, JobSculptor> {
    public JobSculptor(ICitizenData entity) {
        super(entity);
    }

    @Override
    public SculptorAI generateAI() {
        return new SculptorAI(this);
    }
}
