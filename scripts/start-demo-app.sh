#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
APP_DIR="$ROOT_DIR/tmobile-demo-app"
PID_FILE="$ROOT_DIR/target/tmobile-demo-app.pid"
LOG_FILE="$ROOT_DIR/target/tmobile-demo-app.log"
PORT="${PORT:-3000}"

mkdir -p "$ROOT_DIR/target"

if [[ -f "$PID_FILE" ]] && kill -0 "$(cat "$PID_FILE")" 2>/dev/null; then
  echo "T-Mobile demo app already running on port $PORT"
  exit 0
fi

cd "$APP_DIR"
npm install

nohup env PORT="$PORT" npm start > "$LOG_FILE" 2>&1 &
echo "$!" > "$PID_FILE"

for _ in {1..30}; do
  if curl -fsS "http://localhost:$PORT/api/health" >/dev/null; then
    echo "T-Mobile demo app started at http://localhost:$PORT"
    exit 0
  fi
  sleep 1
done

echo "T-Mobile demo app did not become healthy. See $LOG_FILE"
exit 1
