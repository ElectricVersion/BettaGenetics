package elecvrsn.GeneticBettas.ai.brain;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class ConditionalRun<E extends LivingEntity> implements BehaviorControl<E> {
    private Behavior.Status status;
    private Predicate<E> startCondition;
    private BehaviorControl<? super E> behaviorControl;

    public ConditionalRun(Predicate<E> startCondition, BehaviorControl<? super E> behaviorControl) {
        this.status = Behavior.Status.STOPPED;
        this.startCondition = startCondition;
        this.behaviorControl = behaviorControl;
    }

    @Override
    public Behavior.Status getStatus() {
        return status;
    }

    @Override
    public boolean tryStart(ServerLevel serverLevel, E entity, long gameTime) {
        if (startCondition.test(entity)) {
            return behaviorControl.tryStart(serverLevel, entity, gameTime);
        }
        return false;
    }

    @Override
    public void tickOrStop(ServerLevel serverLevel, E entity, long gameTime) {
        if (behaviorControl.getStatus() == Behavior.Status.STOPPED) {
            this.doStop(serverLevel, entity, gameTime);
        }
    }

    @Override
    public void doStop(ServerLevel serverLevel, E entity, long gameTime) {
        this.status = Behavior.Status.STOPPED;
        if (behaviorControl instanceof BehaviorControl<?>) {
            behaviorControl.doStop(serverLevel, entity, gameTime);
        }
    }

    @Override
    public @NotNull String debugString() {
        return "ConditionalRun";
    }
}
