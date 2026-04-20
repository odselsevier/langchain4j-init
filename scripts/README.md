# Video Render Scripts

This folder contains scripts to render the terminal demo video.

## Prerequisites

- macOS with Swift available
- `ffmpeg` installed

Check:

```bash
swift --version
ffmpeg -version
```

## Quick Start (Recommended)

From repo root (`/Users/dykyio/Documents/GitHub/langchain4j-init`):

```bash
TS="$(date +%Y%m%d-%H%M%S)"
OUT="media/${TS}-langchain4j-init-terminal-cinematic-15s.mp4"

mkdir -p tmp/.swift-module-cache
SDKROOT=/Library/Developer/CommandLineTools/SDKs/MacOSX15.4.sdk \
SWIFT_MODULECACHE_PATH=tmp/.swift-module-cache \
swift scripts/render_terminal_frames.swift

ffmpeg -y -framerate 30 -i tmp/terminal_frames_4k/frame_%04d.png \
  -c:v libx264 -preset slow -crf 15 -pix_fmt yuv420p -movflags +faststart \
  "$OUT"

cp "$OUT" media/video.mp4
echo "Rendered: $OUT"
```

## Verify Output

```bash
LATEST="$(ls -t media/*-langchain4j-init-terminal-cinematic-15s.mp4 | head -n 1)"
ffprobe -v error -select_streams v:0 \
  -show_entries stream=width,height,r_frame_rate \
  -show_entries format=duration \
  -of default=noprint_wrappers=1 \
  "$LATEST"
```

Expected:

- `width=3840`
- `height=2160`
- `r_frame_rate=30/1`
- `duration=15.000000`

## Files

- `render_terminal_frames.swift`: current source of truth for the terminal content and timing
- `render_terminal_cinematic.swift`: alternative renderer
- `render_terminal_cinematic_ffmpeg.sh`: ffmpeg-only renderer path (auto timestamped output)

## ffmpeg-only Option

```bash
bash scripts/render_terminal_cinematic_ffmpeg.sh
```

This script writes directly to:

- `media/<timestamp>-langchain4j-init-terminal-cinematic.mp4`
