#!/usr/bin/env sh

##############################################################################
# Custom minimal Gradle wrapper launcher
##############################################################################

set -euo pipefail

APP_HOME="$(cd "$(dirname "$0")"; pwd -P)"
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
WRAPPER_VERSION="8.2"
WRAPPER_URL="https://repo.gradle.org/gradle/libs-releases-local/org/gradle/gradle-wrapper/${WRAPPER_VERSION}/gradle-wrapper-${WRAPPER_VERSION}.jar"
MAIN_CLASS="org.gradle.wrapper.GradleWrapperMain"

if [ ! -f "$WRAPPER_JAR" ]; then
  mkdir -p "$(dirname "$WRAPPER_JAR")"
  echo "Gradle wrapper JAR not found, downloading ${WRAPPER_VERSION}..." >&2
  if command -v curl >/dev/null 2>&1; then
    curl --fail --location --output "${WRAPPER_JAR}.tmp" "$WRAPPER_URL"
  elif command -v wget >/dev/null 2>&1; then
    wget --output-document="${WRAPPER_JAR}.tmp" "$WRAPPER_URL"
  else
    echo "Neither curl nor wget is available to download the Gradle wrapper." >&2
    exit 1
  fi
  mv "${WRAPPER_JAR}.tmp" "$WRAPPER_JAR"
fi

if [ -n "${JAVA_HOME:-}" ]; then
  JAVA_CMD="$JAVA_HOME/bin/java"
else
  JAVA_CMD=$(command -v java 2>/dev/null || true)
fi

if [ -z "${JAVA_CMD:-}" ] || [ ! -x "$JAVA_CMD" ]; then
  echo "Java runtime could not be found. Please set JAVA_HOME." >&2
  exit 1
fi

exec "$JAVA_CMD" -cp "$WRAPPER_JAR" "$MAIN_CLASS" "$@"
