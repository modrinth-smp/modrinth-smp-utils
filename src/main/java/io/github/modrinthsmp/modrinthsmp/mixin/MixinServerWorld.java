package io.github.modrinthsmp.modrinthsmp.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld {
    @Shadow public abstract BlockPos getSpawnPos();

    @Shadow public abstract MinecraftServer getServer();

    @Unique
    private double msu$msw$explosionX;
    @Unique
    private double msu$msw$explosionZ;

    @Inject(
        method = "createExplosion",
        at = @At("HEAD")
    )
    private void storeExplosionCoords(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, Explosion.DestructionType destructionType, CallbackInfoReturnable<Explosion> cir) {
        msu$msw$explosionX = x;
        msu$msw$explosionZ = z;
    }

    @ModifyVariable(
        method = "createExplosion",
        at = @At("HEAD"),
        ordinal = 0,
        argsOnly = true
    )
    private boolean noFireAtSpawn(boolean createFire) {
        return createFire && !spawnProtected();
    }

    @ModifyVariable(
        method = "createExplosion",
        at = @At("HEAD"),
        ordinal = 0,
        argsOnly = true
    )
    private Explosion.DestructionType noExplosionsAtSpawn(Explosion.DestructionType type) {
        return spawnProtected() ? Explosion.DestructionType.NONE : type;
    }

    private boolean spawnProtected() {
        final BlockPos spawnPos = getSpawnPos();
        final double distanceX = Math.abs(msu$msw$explosionX - spawnPos.getX());
        final double distanceZ = Math.abs(msu$msw$explosionZ - spawnPos.getZ());
        final double distance = Math.max(distanceX, distanceZ);
        return distance <= getServer().getSpawnProtectionRadius();
    }
}
