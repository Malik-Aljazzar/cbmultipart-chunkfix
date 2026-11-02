package dev.malik.cbmultipartchunkfix.mixin;

import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fixes CBMultipart's "Chunk already loaded?" crash
 * (https://github.com/TheCBProject/CBMultipart/issues/197).
 *
 * WorldTickScheduler.onChunkUnload only nulls a ChunkScheduler's {@link #chunk} reference for
 * schedulers still in its `ticking` list at unload time - one that already finished ticking this
 * server tick has already left that list, so its reference survives the unload. The scheduler
 * itself stays cached forever in WorldTickScheduler's ChunkPos-keyed map, so the next load of
 * that exact chunk hits onChunkLoad with a stale non-null reference and CBMultipart's own check
 * throws instead of accepting the new chunk.
 *
 * Verified against the real CBMultipart 3.5.0.155 bytecode (javap -c), not guessed: the stale
 * LevelChunk itself still carries a `loaded` field that goes false once it's actually unloaded,
 * even though this ChunkScheduler was never told. Clearing a reference that is provably stale
 * (non-null but no longer loaded) before CBMultipart's own check runs turns the false-positive
 * crash into a no-op; a genuine double-load (the old reference still actually loaded) still
 * throws exactly as before.
 */
@Mixin(targets = "codechicken.multipart.util.WorldTickScheduler$ChunkScheduler")
public abstract class ChunkSchedulerMixin {

    @Shadow(remap = false)
    private LevelChunk chunk;

    @Inject(method = "onChunkLoad(Lnet/minecraft/world/level/chunk/LevelChunk;)V", at = @At("HEAD"), remap = false)
    private void dev$clearStaleChunkReference(LevelChunk newChunk, CallbackInfo ci) {
        if (this.chunk != null && !this.chunk.loaded) {
            this.chunk = null;
        }
    }
}
