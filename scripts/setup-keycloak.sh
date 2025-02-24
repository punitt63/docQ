#!/bin/bash

# Function to extract value using grep and sed
extract_field() {
    local json=$1
    local field=$2

    echo "$json" | grep -o "\"$field\":\"[^\"]*\"" | sed "s/\"$field\":\"//;s/\"$//"
}

admin_user_access_token_response=$(curl -s --location 'http://localhost:8080/realms/master/protocol/openid-connect/token' \
                                   --header 'Content-Type: application/x-www-form-urlencoded' \
                                   --data-urlencode 'grant_type=password' \
                                   --data-urlencode 'username=admin' \
                                   --data-urlencode 'password=admin' \
                                   --data-urlencode 'client_id=admin-cli')
admin_user_access_token=$(extract_field "$admin_user_access_token_response" "access_token")
echo "Admin User Access Token Created"
echo $admin_user_access_token

# Create Realm For Health Facility
curl -s --location 'http://localhost:8080/admin/realms' \
--header 'Content-Type: application/json' \
--header "Authorization: Bearer $admin_user_access_token" \
--data '{
    "realm": "health-facility",
    "enabled": true
}'

echo 'Realm for Health Facility Created'

# Create Client for Health Facility Backend App
curl -s --location 'http://localhost:8080/admin/realms/health-facility/clients' \
--header 'Content-Type: application/json' \
--header "Authorization: Bearer $admin_user_access_token" \
--data '{ "clientId" : "health-facility-backend-app", "id": "de51cfc8-70d2-41c8-b251-2e93861fc311", "serviceAccountsEnabled" : true}'

health_facility_backend_app_get_secret_response=$(curl --location 'http://localhost:8080/admin/realms/health-facility/clients/de51cfc8-70d2-41c8-b251-2e93861fc311/client-secret' --header "Authorization: Bearer $admin_user_access_token")
health_facility_backend_app_secret=$(extract_field "$health_facility_backend_app_get_secret_response" "value")

echo 'Health Facility Backend App Client ID : health-facility-backend-app'
echo "Health Facility Backend App Client Secret : $health_facility_backend_app_secret"

health_facility_backend_app_get_service_account_response=$(curl --location 'http://localhost:8080/admin/realms/health-facility/clients/de51cfc8-70d2-41c8-b251-2e93861fc311/service-account-user' --header "Authorization: Bearer $admin_user_access_token")
health_facility_backend_app_get_service_account_user=$(extract_field "$health_facility_backend_app_get_service_account_response" "id")

echo "Health Facility Backend App Service Account User : $health_facility_backend_app_get_service_account_user"

get_realm_management_client_id_response=$(curl --location 'http://localhost:8080/admin/realms/health-facility/clients?clientId=realm-management' --header "Authorization: Bearer $admin_user_access_token")
get_realm_management_client_id=$(extract_field "$get_realm_management_client_id_response" "id")

echo "Realm Management Client ID : $get_realm_management_client_id"

get_manage_users_role_response=$(curl --location "http://localhost:8080/admin/realms/health-facility/clients/$get_realm_management_client_id/roles/manage-users" --header "Authorization: Bearer $admin_user_access_token")
get_manage_users_role_id=$(extract_field "$get_manage_users_role_response" "id")

echo "Manage Users Role ID : $get_manage_users_role_id"

curl --location --request POST "http://localhost:8080/admin/realms/health-facility/users/$health_facility_backend_app_get_service_account_user/role-mappings/clients/$get_realm_management_client_id" \
--header 'Content-Type: application/json' \
--header "Authorization: Bearer $admin_user_access_token" \
--data "[{\"id\":\"$get_manage_users_role_id\",\"name\":\"manage-users\",\"description\":\"${role_manage-users}\"}]"

echo "Added manage-users role to health-facility-backend-app service account user"

# Create Client for Health Facility Desktop App
curl -s --location 'http://localhost:8080/admin/realms/health-facility/clients' \
--header 'Content-Type: application/json' \
--header "Authorization: Bearer $admin_user_access_token" \
--data '{ "clientId" : "health-facility-desktop-app", "id": "fe51cfc8-80d2-41c8-b251-2e93861fc312"}'

health_facility_desktop_app_get_secret_response=$(curl --location 'http://localhost:8080/admin/realms/health-facility/clients/fe51cfc8-80d2-41c8-b251-2e93861fc312/client-secret' --header "Authorization: Bearer $admin_user_access_token")
health_facility_desktop_app_secret=$(extract_field "$health_facility_desktop_app_get_secret_response" "value")

echo 'Health Facility Desktop App Client ID : health-facility-desktop-app'
echo "Health Facility Desktop App Client Secret : $health_facility_desktop_app_secret"

curl -s --location 'http://localhost:8080/admin/realms/health-facility/roles' \
--header 'Content-Type: application/json' \
--header "Authorization: Bearer $admin_user_access_token" \
--data '{
    "name" : "facility-manager"
}'
echo 'Facility Manager Role Created'

curl -s --location 'http://localhost:8080/admin/realms/health-facility/roles' \
--header 'Content-Type: application/json' \
--header "Authorization: Bearer $admin_user_access_token" \
--data '{
    "name" : "doctor"
}'
echo 'Doctor Role Created'