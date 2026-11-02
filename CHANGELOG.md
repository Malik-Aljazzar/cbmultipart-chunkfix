# Changelog

All notable changes to CBMultipart Chunk Scheduler Fix are documented in this file.

## [1.0.0]

### Fixes
- Fixed CBMultipart's `"Chunk already loaded?"` crash during chunk generation/loading (confirmed via our own upstream report, [TheCBProject/CBMultipart#197](https://github.com/TheCBProject/CBMultipart/issues/197), still open and unfixed). Root cause: a `ChunkScheduler` that finishes its tick cycle leaves CBMultipart's internal `ticking` list before its chunk unloads, so `WorldTickScheduler.onChunkUnload` never clears its cached chunk reference; the scheduler itself is never removed from the chunk-keyed cache, so the next time that exact chunk loads, the stale reference makes CBMultipart throw instead of accepting the new chunk. This mod clears a provably-stale reference (the old chunk object's own `loaded` flag is false) right before CBMultipart's check runs, so it no longer throws - a genuine double-load (the old reference still actually loaded) is untouched and still throws as before.
