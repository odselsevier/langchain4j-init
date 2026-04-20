#!/usr/bin/env bash
set -euo pipefail

mkdir -p media /tmp/langchain4j-video-text
TS="$(date +%Y%m%d-%H%M%S)"
OUT="media/${TS}-langchain4j-init-terminal-cinematic.mp4"
FONT="/System/Library/Fonts/Menlo.ttc"
TXT_DIR="/tmp/langchain4j-video-text"

cat > "$TXT_DIR/prompt.txt" <<'EOF'
dykyio@dev ~/Documents/GitHub/langchain4j-init % 
EOF
cat > "$TXT_DIR/cmd.txt" <<'EOF'
java -jar target/langchain4j-init-1.0.0-SNAPSHOT.jar faq
EOF
cat > "$TXT_DIR/l1.txt" <<'EOF'
14:22:10.104 [main] INFO  com.showcase.Main - Launching demo: faq
EOF
cat > "$TXT_DIR/l2.txt" <<'EOF'
14:22:10.119 [main] INFO  com.showcase.features.ToolCallingDemo - === Website FAQ Demo (RAG-style) ===
EOF
cat > "$TXT_DIR/l3.txt" <<'EOF'
14:22:10.361 [main] DEBUG dev.langchain4j.service.AiServices - Building AI service: AssistantWithTools
EOF
cat > "$TXT_DIR/l4.txt" <<'EOF'
14:22:10.624 [main] DEBUG dev.langchain4j.service.AiServices - Registered tools: WebsiteFaqTools.searchFaq, InventoryTools.checkInventory
EOF
cat > "$TXT_DIR/l5.txt" <<'EOF'
14:22:11.052 [main] INFO  com.showcase.features.ToolCallingDemo - User -> What events are next and stock for PRODUCT-99?
EOF
cat > "$TXT_DIR/l6.txt" <<'EOF'
14:22:11.468 [main] INFO  dev.langchain4j.model.ollama.OllamaChatModel - Tool execution requested: searchFaq(userQuestion)
EOF
cat > "$TXT_DIR/l7.txt" <<'EOF'
14:22:12.053 [main] INFO  dev.langchain4j.model.ollama.OllamaChatModel - Tool execution requested: checkInventory(sku=PRODUCT-99)
EOF
cat > "$TXT_DIR/l8.txt" <<'EOF'
14:22:12.757 [main] INFO  com.showcase.ai.InventoryTools - checkInventory(PRODUCT-99) -> SKU-PRODUCT-99: 42 products in stock
EOF
cat > "$TXT_DIR/l9.txt" <<'EOF'
14:22:13.201 [main] INFO  com.showcase.features.ToolCallingDemo - Assistant -> Upcoming events are listed in FAQ. SKU-PRODUCT-99 has 42 products in stock.
EOF

cat > "$TXT_DIR/graph.ff" <<EOF
format=rgba,
drawbox=x=0:y=0:w=3840:h=2160:color=#020303@1:t=fill,
drawbox=x=0:y=1570:w=3840:h=590:color=#0b0d0e@1:t=fill,
drawbox=x=350:y=210:w=3140:h=1740:color=#0f1112@1:t=fill,
drawbox=x=355:y=215:w=3130:h=1730:color=#1bf794@0.06:t=2,
drawbox=x=356:y=215:w=3130:h=1730:color=#4aa5ff@0.04:t=1,
drawbox=x=500:y=350:w=2840:h=1370:color=#020905@1:t=fill,
drawbox=x=510:y=360:w=2820:h=1350:color=#02110a@0.08:t=fill,
drawbox=x=580:y=400:w=600:h=8:color=#7cf8ff@0.06:t=fill,
drawtext=fontfile=${FONT}:textfile=${TXT_DIR}/prompt.txt:x=565:y=435:fontsize=40:fontcolor=#76ff9d:shadowcolor=#2cff8a@0.35:shadowx=0:shadowy=0,
drawtext=fontfile=${FONT}:textfile=${TXT_DIR}/cmd.txt:x=1600:y=435:fontsize=40:fontcolor=#76ff9d:shadowcolor=#2cff8a@0.42:shadowx=0:shadowy=0,
drawbox=x='1600+1700*min(max(t,0),1)':y=420:w='1700*(1-min(max(t,0),1))':h=56:color=#020905@1:t=fill,
drawbox=x='1600+1700*min(max(t,0),1)':y=426:w=12:h=42:color=#76ff9d@0.98:t=fill:enable='lt(t,1)*between(mod(t,0.4),0,0.2)',
drawtext=fontfile=${FONT}:textfile=${TXT_DIR}/l1.txt:x=565:y=492:fontsize=30:fontcolor=#61e58d:shadowcolor=#21ff8d@0.20:shadowx=0:shadowy=0:enable='gte(t,3.10)',
drawtext=fontfile=${FONT}:textfile=${TXT_DIR}/l2.txt:x=565:y=542:fontsize=30:fontcolor=#5bb8ff:shadowcolor=#58a9ff@0.25:shadowx=0:shadowy=0:enable='gte(t,3.45)',
drawtext=fontfile=${FONT}:textfile=${TXT_DIR}/l3.txt:x=565:y=592:fontsize=30:fontcolor=#61e58d:shadowcolor=#21ff8d@0.20:shadowx=0:shadowy=0:enable='gte(t,4.05)',
drawtext=fontfile=${FONT}:textfile=${TXT_DIR}/l4.txt:x=565:y=642:fontsize=30:fontcolor=#61e58d:shadowcolor=#21ff8d@0.20:shadowx=0:shadowy=0:enable='gte(t,4.70)',
drawtext=fontfile=${FONT}:textfile=${TXT_DIR}/l5.txt:x=565:y=692:fontsize=30:fontcolor=#61e58d:shadowcolor=#21ff8d@0.20:shadowx=0:shadowy=0:enable='gte(t,5.55)',
drawtext=fontfile=${FONT}:textfile=${TXT_DIR}/l6.txt:x=565:y=742:fontsize=30:fontcolor=#61e58d:shadowcolor=#21ff8d@0.20:shadowx=0:shadowy=0:enable='gte(t,6.40)',
drawtext=fontfile=${FONT}:textfile=${TXT_DIR}/l7.txt:x=565:y=792:fontsize=30:fontcolor=#61e58d:shadowcolor=#21ff8d@0.20:shadowx=0:shadowy=0:enable='gte(t,7.30)',
drawtext=fontfile=${FONT}:textfile=${TXT_DIR}/l8.txt:x=565:y=842:fontsize=30:fontcolor=#61e58d:shadowcolor=#21ff8d@0.20:shadowx=0:shadowy=0:enable='gte(t,8.25)',
drawtext=fontfile=${FONT}:textfile=${TXT_DIR}/l9.txt:x=565:y=892:fontsize=30:fontcolor=#e7fff3:shadowcolor=#9ef5ff@0.30:shadowx=0:shadowy=0:enable='gte(t,9.60)',
drawgrid=w=2:h=4:t=1:c=black@0.12,
drawbox=x=620:y=1460:w=2600:h=620:color=#3dffd2@0.04:t=fill,
drawbox=x=720:y=1500:w=2400:h=560:color=#4a9bff@0.035:t=fill,
gblur=sigma=0.35
EOF

ffmpeg -y -f lavfi -i "color=c=#040607:s=3840x2160:d=15:r=30" \
  -filter_complex_script "$TXT_DIR/graph.ff" \
  -c:v libx264 -pix_fmt yuv420p -preset slow -crf 14 -movflags +faststart -r 30 "$OUT"

echo "Wrote $OUT"
