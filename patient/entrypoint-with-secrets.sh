#!/bin/bash

# Path to the secrets file
SECRETS_FILE="/secrets/keycloak-secrets.properties"

# Check if secrets file exists
if [ -f "$SECRETS_FILE" ]; then
    echo "Loading Keycloak secrets from $SECRETS_FILE..."
    
    # Read properties and export as environment variables
    while IFS='=' read -r key value; do
        # Skip comments and empty lines
        [[ "$key" =~ ^#.*$ ]] && continue
        [[ -z "$key" ]] && continue
        
        # Remove leading/trailing whitespace
        key=$(echo "$key" | xargs)
        value=$(echo "$value" | xargs)
        
        # Convert property names to environment variable format
        case "$key" in
            "keycloak.patient.backend.client.id")
                export KEYCLOAK_BACKEND_CLIENT_ID="$value"
                echo "Loaded KEYCLOAK_BACKEND_CLIENT_ID"
                ;;
            "keycloak.patient.backend.client.secret")
                export KEYCLOAK_BACKEND_CLIENT_SECRET="$value"
                echo "Loaded KEYCLOAK_BACKEND_CLIENT_SECRET"
                ;;
            "keycloak.realm")
                export KEYCLOAK_REALM="$value"
                echo "Loaded KEYCLOAK_REALM"
                ;;
            "keycloak.base.url")
                # Note: KEYCLOAK_BASE_URL is set in docker-compose for container networking
                # Only use file value if not set in docker-compose
                if [ -z "$KEYCLOAK_BASE_URL" ]; then
                    export KEYCLOAK_BASE_URL="$value"
                    echo "Loaded KEYCLOAK_BASE_URL from secrets"
                fi
                ;;
        esac
    done < "$SECRETS_FILE"
    
    echo "Secrets loaded successfully!"
else
    echo "Warning: Secrets file not found at $SECRETS_FILE"
    echo "Using default environment variables or application.yml values"
fi

# Start the Spring Boot application
echo "Starting Patient Service..."
exec java -jar /app/patient-service.jar

