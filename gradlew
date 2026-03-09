#!/usr/bin/env bash

set -euo pipefail

APP_HOME="$(cd "$(dirname "$0")" && pwd -P)"
WRAPPER_PROPS="$APP_HOME/gradle/wrapper/gradle-wrapper.properties"

if [[ ! -f "$WRAPPER_PROPS" ]]; then
  echo "Gradle wrapper properties not found: $WRAPPER_PROPS" >&2
  exit 1
fi

DISTRIBUTION_URL=$(grep '^distributionUrl=' "$WRAPPER_PROPS" | cut -d= -f2- | sed 's#\\:#:#g')
if [[ -z "${DISTRIBUTION_URL:-}" ]]; then
  echo "distributionUrl is missing in $WRAPPER_PROPS" >&2
  exit 1
fi

DIST_NAME="$(basename "$DISTRIBUTION_URL")"
DIST_BASENAME="${DIST_NAME%.zip}"
DIST_DIR="$HOME/.gradle-wrapper-cache/$DIST_BASENAME"
ZIP_PATH="$HOME/.gradle-wrapper-cache/$DIST_NAME"
GRADLE_BIN=$(find "$DIST_DIR" -path '*/bin/gradle' -type f 2>/dev/null | head -n 1 || true)

mkdir -p "$HOME/.gradle-wrapper-cache"

if [[ -z "$GRADLE_BIN" || ! -x "$GRADLE_BIN" ]]; then
  if [[ ! -f "$ZIP_PATH" ]]; then
    echo "Downloading Gradle distribution: $DISTRIBUTION_URL" >&2
    curl --fail --location --output "$ZIP_PATH.tmp" "$DISTRIBUTION_URL"
    mv "$ZIP_PATH.tmp" "$ZIP_PATH"
  fi

  rm -rf "$DIST_DIR"
  mkdir -p "$DIST_DIR"
  unzip -q "$ZIP_PATH" -d "$DIST_DIR"
  GRADLE_BIN=$(find "$DIST_DIR" -path '*/bin/gradle' -type f | head -n 1 || true)
fi

if [[ -z "$GRADLE_BIN" || ! -x "$GRADLE_BIN" ]]; then
  echo "Unable to bootstrap Gradle from $DISTRIBUTION_URL" >&2
  exit 1
fi

exec "$GRADLE_BIN" "$@"
