package com.hrznstudio.sandbox.ragdoll.ragdolls.vanilla;

/**
 * Created by sekawh on 8/7/2015.
 */
public class ZombieRagdoll extends BipedRagdoll {

    public ZombieRagdoll() {
        super();

        rightArm.setPosition(-6f, -2f, 8f);
        rightArm.shiftPositionToModelScale();

        leftArm.setPosition(6f, -2f, 8f);
        leftArm.shiftPositionToModelScale();
    }
}
