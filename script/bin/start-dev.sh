#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/../.." && pwd)"
export PATH="${ROOT_DIR}/tools/bin:${PATH}"
export DOCKER_HOST="${DOCKER_HOST:-tcp://127.0.0.1:2375}"

ensure_docker_cli() {
  if command -v docker >/dev/null 2>&1; then
    return
  fi
  mkdir -p "${ROOT_DIR}/tools/bin"
  if [[ ! -x "${ROOT_DIR}/tools/bin/docker" ]]; then
    curl -fsSL -o /tmp/docker.tgz https://download.docker.com/linux/static/stable/x86_64/docker-29.1.4.tgz
    tar -xzf /tmp/docker.tgz -C /tmp
    cp /tmp/docker/docker "${ROOT_DIR}/tools/bin/docker"
    chmod +x "${ROOT_DIR}/tools/bin/docker"
  fi
}

mysql_exec() {
  if command -v mysql >/dev/null 2>&1; then
    mysql -h127.0.0.1 -uroot -proot --default-character-set=utf8mb4 "$@"
  else
    ensure_docker_cli
    if [[ -t 0 ]]; then
      docker exec junjun-mysql mysql -uroot -proot --default-character-set=utf8mb4 "$@"
    else
      docker exec -i junjun-mysql mysql -uroot -proot --default-character-set=utf8mb4 "$@"
    fi
  fi
}

mysql_ping() {
  if command -v mysql >/dev/null 2>&1; then
    mysql -h127.0.0.1 -uroot -proot -e "SELECT 1" >/dev/null 2>&1
  else
    ensure_docker_cli
    docker exec junjun-mysql mysqladmin -uroot -proot ping >/dev/null 2>&1
  fi
}

ensure_mysql_redis() {
  ensure_docker_cli
  if ! docker ps --format '{{.Names}}' | grep -qx junjun-redis; then
    docker start junjun-redis >/dev/null 2>&1 || docker run -d --name junjun-redis \
      -p 6379:6379 redis:7.2.8 redis-server --requirepass ruoyi123
  fi
  if ! docker ps --format '{{.Names}}' | grep -qx junjun-mysql; then
    docker start junjun-mysql >/dev/null 2>&1 || docker run -d --name junjun-mysql \
      -e TZ=Asia/Shanghai \
      -e MYSQL_ROOT_PASSWORD=root \
      -e MYSQL_DATABASE=ry-vue \
      -p 3306:3306 \
      -v workspace_mysql-data:/var/lib/mysql \
      mysql:8.0.42 \
      --character-set-server=utf8mb4 \
      --collation-server=utf8mb4_general_ci \
      --explicit_defaults_for_timestamp=true \
      --lower_case_table_names=1
  fi
  for i in $(seq 1 60); do
    if mysql_ping; then
      break
    fi
    sleep 2
  done
  local tables
  tables="$(mysql_exec -N -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='ry-vue';" 2>/dev/null | tr -d '\r' || echo 0)"
  if [[ "${tables}" -lt 10 ]]; then
    echo "Importing SQL..."
    mysql_exec ry-vue < "${ROOT_DIR}/script/sql/ry_vue_5.X.sql"
    mysql_exec ry-vue < "${ROOT_DIR}/script/sql/ry_workflow.sql"
    mysql_exec ry-vue < "${ROOT_DIR}/script/sql/nail_business.sql"
    mysql_exec ry-vue < "${ROOT_DIR}/script/sql/fix_oss_minio.sql"
  fi
  mysql_exec ry-vue < "${ROOT_DIR}/script/sql/add_employee_performance.sql"
  mysql_exec ry-vue < "${ROOT_DIR}/script/sql/add_performance_praise_images.sql" || true
  mysql_exec ry-vue < "${ROOT_DIR}/script/sql/add_performance_pay_type.sql" || true
}

ensure_minio() {
  bash "${ROOT_DIR}/script/bin/start-minio.sh"
}

ensure_backend() {
  if ! curl -fsS http://127.0.0.1:18080/ >/dev/null 2>&1; then
    if [[ ! -f "${ROOT_DIR}/ruoyi-admin/target/ruoyi-admin.jar" ]]; then
      (cd "${ROOT_DIR}" && mvn -pl ruoyi-admin -am -DskipTests package -q)
    fi
    nohup java -jar "${ROOT_DIR}/ruoyi-admin/target/ruoyi-admin.jar" --spring.profiles.active=dev \
      > /tmp/nail-backend.log 2>&1 &
    for i in $(seq 1 60); do
      if curl -fsS http://127.0.0.1:18080/ >/dev/null 2>&1; then
        break
      fi
      sleep 2
    done
  fi
}

ensure_frontend() {
  if ! curl -fsS http://127.0.0.1:18418/ >/dev/null 2>&1; then
    if [[ ! -d "${ROOT_DIR}/plus-ui/node_modules" ]]; then
      (cd "${ROOT_DIR}/plus-ui" && npm install)
    fi
    nohup npm --prefix "${ROOT_DIR}/plus-ui" run dev > /tmp/nail-frontend.log 2>&1 &
    for i in $(seq 1 30); do
      if curl -fsS http://127.0.0.1:18418/ >/dev/null 2>&1; then
        break
      fi
      sleep 2
    done
  fi
}

ensure_mysql_redis
ensure_minio
ensure_backend
ensure_frontend

echo "Backend : http://127.0.0.1:18080"
echo "Frontend: http://127.0.0.1:18418"
echo "MinIO   : http://127.0.0.1:9000"
