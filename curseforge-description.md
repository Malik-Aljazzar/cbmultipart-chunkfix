# CBMultipart Chunk Scheduler Fix

Fixes a crash in [CBMultipart](https://www.curseforge.com/minecraft/mc-mods/cbmultipart) where the server throws `"Chunk already loaded?"` from `WorldTickScheduler$ChunkScheduler.onChunkLoad`.

## The bug

CBMultipart keeps a stale reference to a chunk's scheduler after that scheduler finishes ticking but before the chunk actually unloads. When the chunk loads again, CBMultipart finds the old reference still there and crashes.

Confirmed against CBMultipart's real bytecode, not guessed. Upstream report: [TheCBProject/CBMultipart#197](https://github.com/TheCBProject/CBMultipart/issues/197) (open, unfixed).

## What this does

A small mixin clears the stale scheduler reference when the chunk it points to is no longer loaded, before CBMultipart gets a chance to crash on it.

## Requirements

- Minecraft 1.21.1, NeoForge
- CBMultipart 3.5.0.155
- **Server-side only.** Nothing client-visible depends on this.

## Compatibility

Narrow mixin targeting one specific method. No other interactions with CBMultipart or other mods.
