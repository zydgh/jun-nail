#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
MINIO_DIR="${ROOT_DIR}/tools/minio"
DATA_DIR="${MINIO_DIR}/data"
MINIO_BIN="${MINIO_DIR}/minio"
MC_BIN="${MINIO_DIR}/mc"
MINIO_URL="https://github.com/minio/minio/releases/download/RELEASE.2025-09-07T16-13-09Z/minio.linux-amd64.RELEASE.2025-09-07T16-13-09Z"
MC_URL="https://github.com/minio/mc/releases/download/RELEASE.2025-08-13T08-35-41Z/mc.linux-amd64.RELEASE.2025-08-13T08-35-41Z"

mkdir -p "${DATA_DIR}"

if [[ ! -x "${MINIO_BIN}" ]]; then
  echo "Downloading MinIO..."
  curl -fsSL -o "${MINIO_BIN}" "${MINIO_URL}"
  chmod +x "${MINIO_BIN}"
fi

if [[ ! -x "${MC_BIN}" ]]; then
  echo "Downloading mc..."
  curl -fsSL -o "${MC_BIN}" "${MC_URL}"
  chmod +x "${MC_BIN}"
fi

export MINIO_ROOT_USER="${MINIO_ROOT_USER:-ruoyi}"
export MINIO_ROOT_PASSWORD="${MINIO_ROOT_PASSWORD:-ruoyi123}"

if pgrep -f "${MINIO_BIN} server" >/dev/null 2>&1; then
  echo "MinIO already running"
else
  nohup "${MINIO_BIN}" server "${DATA_DIR}" --address :9000 --console-address :9001 > /tmp/minio.log 2>&1 &
  echo "MinIO started, pid=$!"
  sleep 2
fi

"${MC_BIN}" alias set local http://127.0.0.1:9000 "${MINIO_ROOT_USER}" "${MINIO_ROOT_PASSWORD}" >/dev/null
"${MC_BIN}" mb -p local/ruoyi >/dev/null 2>&1 || true
"${MC_BIN}" anonymous set download local/ruoyi >/dev/null
echo "MinIO API: http://127.0.0.1:9000  Console: http://127.0.0.1:9001"
echo "Bucket: ruoyi  User/Pass: ${MINIO_ROOT_USER}/${MINIO_ROOT_PASSWORD}"
