#!/bin/bash
set -e

#===============================================
# CONFIGURATION
#===============================================
AWS_REGION="ap-south-1"
CLUSTER_NAME="dev"
SERVICE_NAME="keycloak-service"
TASK_FAMILY="keycloak"
ECR_REPO="818957473199.dkr.ecr.ap-south-1.amazonaws.com/docq/keycloak"

#===============================================
# LOGIN TO AWS ECR
#===============================================
echo "🔐 Logging into AWS ECR..."
aws ecr get-login-password --region $AWS_REGION \
  | docker login --username AWS --password-stdin $ECR_REPO

#===============================================
# USE EXISTING TASK DEFINITION REVISION
#===============================================
echo "🔎 Using existing task definition..."

NEW_TASK_DEF=$(aws ecs describe-task-definition \
  --task-definition $TASK_FAMILY \
  --query "taskDefinition.taskDefinitionArn" \
  --output text)

echo "✅ Using Task Definition: $NEW_TASK_DEF"


#===============================================
# CHECK IF SERVICE EXISTS
#===============================================
echo "🔍 Checking if ECS service exists..."

SERVICE_EXISTS=$(aws ecs describe-services \
  --cluster $CLUSTER_NAME \
  --services $SERVICE_NAME \
  --query "services[0].status" \
  --output text 2>/dev/null || true)

#===============================================
# CREATE SERVICE (IF NOT EXISTS)
#===============================================
if [[ "$SERVICE_EXISTS" == "ACTIVE" ]]; then
  echo "🔄 Service exists → Updating service with new task definition..."

  aws ecs update-service \
    --cluster $CLUSTER_NAME \
    --service $SERVICE_NAME \
    --task-definition $NEW_TASK_DEF \
    --force-new-deployment \
    >/dev/null

  echo "✅ Service updated successfully."

else
  echo "🚀 Service not found → Creating new Keycloak service..."

  aws ecs create-service \
    --cluster $CLUSTER_NAME \
    --service-name $SERVICE_NAME \
    --task-definition $NEW_TASK_DEF \
    --desired-count 1 \
    --launch-type FARGATE \
    --platform-version "1.4.0" \
    --network-configuration "awsvpcConfiguration={
      subnets=[
        \"subnet-0d47fd58c849fa113\",
        \"subnet-06a1e1b0b30617fff\",
        \"subnet-0d84f9d67217aef6e\"
      ],
      securityGroups=[\"sg-0e2d81bdf39a775a6\"],
      assignPublicIp=\"ENABLED\"
    }" \
    --load-balancers "[
      {
        \"targetGroupArn\": \"arn:aws:elasticloadbalancing:ap-south-1:818957473199:targetgroup/keycloak-tg/87738b004a1b5c53\",
        \"containerName\": \"keycloak\",
        \"containerPort\": 8080
      }
    ]" >/dev/null

  echo "🎉 Keycloak service created successfully."
fi

echo "🎯 Deployment complete!"
