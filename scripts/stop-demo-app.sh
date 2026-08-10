#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PID_FILE="$ROOT_DIR/target/tmobile-demo-app.pid"

if [[ ! -f "$PID_FILE" ]]; then
  echo "No T-Mobile demo app PID file found."
  exit 0
fi

PID="$(cat "$PID_FILE")"

if kill -0 "$PID" 2>/dev/null; then
  kill "$PID"
  echo "Stopped T-Mobile demo app process $PID"
fi

rm -f "$PID_FILE"
