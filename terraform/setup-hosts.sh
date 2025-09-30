#!/bin/bash

# Platform-independent hosts file setup for devops-project.test
# This script adds the required host entry for nginx domain routing

echo "🌐 Setting up hosts file for devops-project.test..."

HOST_ENTRIES=(
    "127.0.0.1 devops-project.test"
    "127.0.0.1 grafana.devops-project.test"
    "127.0.0.1 prometheus.devops-project.test"
    "127.0.0.1 kibana.devops-project.test"
)

# Determine hosts file location based on OS
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
    # Windows (Git Bash/WSL)
    HOSTS_FILE="/c/Windows/System32/drivers/etc/hosts"
    echo "📍 Detected Windows environment"
elif [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    HOSTS_FILE="/etc/hosts"
    echo "📍 Detected macOS environment"
else
    # Linux
    HOSTS_FILE="/etc/hosts"
    echo "📍 Detected Linux environment"
fi

# Check and add entries
ADDED_COUNT=0
for ENTRY in "${HOST_ENTRIES[@]}"; do
    DOMAIN=$(echo "$ENTRY" | cut -d' ' -f2)

    if grep -q "$DOMAIN" "$HOSTS_FILE" 2>/dev/null; then
        echo "✅ $DOMAIN already exists in hosts file"
    else
        echo "➕ Adding $ENTRY to $HOSTS_FILE"

        # Add the entry based on OS
        if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
            # Windows - requires running as Administrator
            echo "$ENTRY" >> "$HOSTS_FILE" 2>/dev/null && {
                echo "✅ Successfully added $DOMAIN!"
                ((ADDED_COUNT++))
            } || {
                echo "❌ Failed to add $DOMAIN. Please run as Administrator or manually add:"
                echo "   $ENTRY"
                echo "   to $HOSTS_FILE"
            }
        else
            # Unix-like (macOS/Linux) - use sudo
            echo "$ENTRY" | sudo tee -a "$HOSTS_FILE" >/dev/null && {
                echo "✅ Successfully added $DOMAIN!"
                ((ADDED_COUNT++))
            } || {
                echo "❌ Failed to add $DOMAIN. Please manually add:"
                echo "   $ENTRY"
                echo "   to $HOSTS_FILE"
            }
        fi
    fi
done

echo ""
if [ "$ADDED_COUNT" -gt 0 ]; then
    echo "🎉 Setup complete! Added $ADDED_COUNT new entries."
else
    echo "🎉 All entries already exist!"
fi
echo ""
echo "You can now access services at:"
echo "   🏠 Landing page: http://devops-project.test"
echo "   📊 Grafana: http://grafana.devops-project.test"
echo "   📈 Prometheus: http://prometheus.devops-project.test"
echo "   🔍 Kibana: http://kibana.devops-project.test"