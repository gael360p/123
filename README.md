# ChunkData Client Profiler (Fabric)

Client-side Fabric mod that estimates “chunk stored data” by measuring the **ChunkData packet payload size**.
Works on servers (client-only).

## Controls
- Hold **H** to show the heatmap (top-left)

## Commands
- `/chunkdata heatmap on|off|toggle`
- `/chunkdata heatmap abnormalonly on|off|toggle` (green-only mode; only ABNORMAL chunks shown)
- `/chunkdata top [count]`
- `/chunkdata here`
- `/chunkdata clear`

## Build (GitHub Actions)
Push to `main` and download the built jar from **Actions → workflow run → Artifacts**.
