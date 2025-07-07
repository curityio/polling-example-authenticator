#!/bin/bash
set -euo pipefail

# Build the project
mvn package

echo -e "\n|------------------------------------------------------------------|\n"

# Prompt for IDSVR_HOME if not set
if [[ -z "${IDSVR_HOME:-}" ]]; then
    read -rp "Enter Curity Identity Server home absolute path (IDSVR_HOME): " IDSVR_HOME
else
    echo "IDSVR_HOME is set to '$IDSVR_HOME'"
fi

# Define plugin directory
PLUGIN_DIR="$IDSVR_HOME/usr/share/plugins/authenticators.polling-example"
mkdir -p "$PLUGIN_DIR"

# Copy JAR and dependencies
cp target/polling-example-authenticator-1.0.0-SNAPSHOT.jar "$PLUGIN_DIR/"
cp target/lib/guava-33.4.8-jre.jar "$PLUGIN_DIR/"

# Define fragments overrides directory
TEMPLATE_OVERRIDES_FRAGMENTS_DIR="$IDSVR_HOME/usr/share/templates/overrides/fragments"
mkdir -p "$TEMPLATE_OVERRIDES_FRAGMENTS_DIR"

# Copy template overrides
cp src/main/resources/poller-example.vm "$TEMPLATE_OVERRIDES_FRAGMENTS_DIR/"

echo -e "\nAuthenticator deployment completed successfully."
echo "You can now restart the Curity Identity Server to load the authenticator."
