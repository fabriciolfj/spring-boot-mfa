#!/usr/bin/env bash
set -euo pipefail

BASE=http://localhost:8080
JAR=/tmp/ott-cookies.txt

csrf_from() {
  grep 'name="_csrf"' | grep -oP 'value="\K[^"]+'
}

echo "=== 1. Login admin/admin123 ==="
CSRF=$(curl -s -c "$JAR" "$BASE/login" | csrf_from)
curl -s -b "$JAR" -c "$JAR" -X POST "$BASE/login" \
  --data-urlencode "username=admin" \
  --data-urlencode "password=admin123" \
  --data-urlencode "_csrf=$CSRF" \
  -D - -o /dev/null | grep -E "^HTTP/|^[Ll]ocation:"

echo ""
echo "=== 2. Gerar OTT para admin ==="
CSRF=$(curl -s -b "$JAR" -c "$JAR" "$BASE/login/ott" | csrf_from)
curl -s -b "$JAR" -c "$JAR" -X POST "$BASE/ott/generate" \
  --data-urlencode "username=admin" \
  --data-urlencode "_csrf=$CSRF" \
  -D - -o /dev/null | grep -E "^HTTP/|^[Ll]ocation:"

echo ""
echo "=== 3. Verifique o token no console da aplicação ==="
read -rp "Cole o token OTT: " OTT_TOKEN

echo ""
echo "=== 4. Submeter OTT ==="
CSRF=$(curl -s -b "$JAR" -c "$JAR" "$BASE/login/ott" | csrf_from)
curl -s -b "$JAR" -c "$JAR" -X POST "$BASE/login/ott" \
  --data-urlencode "token=$OTT_TOKEN" \
  --data-urlencode "_csrf=$CSRF" \
  -D - -o /dev/null | grep -E "^HTTP/|^[Ll]ocation:"

echo ""
echo "=== 5. Acessar endpoints protegidos ==="
echo -n "/public:           "
curl -s -b "$JAR" -c "$JAR" "$BASE/public"

echo ""
echo -n "/profile:          "
curl -s -b "$JAR" -c "$JAR" "$BASE/profile"

echo ""
echo -n "/admin/dashboard:  "
curl -s -b "$JAR" -c "$JAR" "$BASE/admin/dashboard"

echo ""