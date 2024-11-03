package elecvrsn.GeneticBettas.ai.brain.betta;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class SetWalkTargetAwayFromWithExpiry<T> extends Behavior<PathfinderMob> {
    private final MemoryModuleType<T> walkAwayFromMemory;
    private final float speedModifier;
    private final int desiredDistance;
    private final int expirationTime;
    private final Function<T, Vec3> toPosition;

    public SetWalkTargetAwayFromWithExpiry(MemoryModuleType<T> memory, float speedMod, int dist, boolean registered, int expiry, Function<T, Vec3> func) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, registered ? MemoryStatus.REGISTERED : MemoryStatus.VALUE_ABSENT, memory, MemoryStatus.VALUE_PRESENT));
        this.walkAwayFromMemory = memory;
        this.speedModifier = speedMod;
        this.desiredDistance = dist;
        this.expirationTime = expiry;
        this.toPosition = func;
    }

    public static SetWalkTargetAwayFromWithExpiry<? extends Entity> entity(MemoryModuleType<? extends Entity> memoryModuleType, float speedMod, int dist, boolean registered, int expiry) {
        return new SetWalkTargetAwayFromWithExpiry<>(memoryModuleType, speedMod, dist, registered, expiry, Entity::position);
    }

    protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob mob) {
        return !this.alreadyWalkingAwayFromPosWithSameSpeed(mob) && mob.position().closerThan(this.getPosToAvoid(mob), (double) this.desiredDistance);
    }

    private Vec3 getPosToAvoid(PathfinderMob mob) {
        return this.toPosition.apply(mob.getBrain().getMemory(this.walkAwayFromMemory).get());
    }

    private boolean alreadyWalkingAwayFromPosWithSameSpeed(PathfinderMob mob) {
        if (!mob.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
            return false;
        } else {
            WalkTarget walktarget = mob.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get();
            if (walktarget.getSpeedModifier() != this.speedModifier) {
                return false;
            } else {
                Vec3 vec3 = walktarget.getTarget().currentPosition().subtract(mob.position());
                Vec3 vec31 = this.getPosToAvoid(mob).subtract(mob.position());
                return vec3.dot(vec31) < 0.0D;
            }
        }
    }

    protected void start(ServerLevel level, PathfinderMob mob, long gameTime) {
        moveAwayFrom(mob, this.getPosToAvoid(mob), this.speedModifier);
    }

    private void moveAwayFrom(PathfinderMob mob, Vec3 pos, float speedMod) {
        for(int i = 0; i < 10; ++i) {
            Vec3 vec3 = LandRandomPos.getPosAway(mob, 16, 7, pos);
            if (vec3 != null) {
                mob.getBrain().setMemoryWithExpiry(MemoryModuleType.WALK_TARGET, new WalkTarget(vec3, speedMod, 0), this.expirationTime);
                return;
            }
        }

    }
}