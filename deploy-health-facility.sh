#!/bin/bash
set -e

#===============================================
# DEPLOY health-facility (no build/push here)
#===============================================

# Basic configuration
AWS_REGION="ap-south-1"
CLUSTER_NAME="dev"
SERVICE_NAME="health-facility-service"
TASK_FAMILY="health-facility"

#===============================================
# 1️⃣ FETCH EXISTING TASK DEFINITION
#===============================================
echo "🔎 Fetching existing task definition..."

EXISTING_TASK_DEF=$(aws ecs describe-task-definition \
  --task-definition "$TASK_FAMILY" \
  --query "taskDefinition.taskDefinitionArn" \
  --output text)

echo "📌 Using Task Definition: $EXISTING_TASK_DEF"


#===============================================
# 2️⃣ CHECK IF SERVICE EXISTS
#===============================================
echo "🔍 Checking if ECS service exists..."

SERVICE_EXISTS=$(aws ecs describe-services \
  --cluster "$CLUSTER_NAME" \
  --services "$SERVICE_NAME" \
  --query "services[0].status" \
  --output text 2>/dev/null || true)


#===============================================
# 3️⃣ CREATE OR UPDATE SERVICE
#===============================================
if [[ "$SERVICE_EXISTS" == "ACTIVE" ]]; then
  echo "🔄 Service exists → Updating with new task definition..."

  aws ecs update-service \
    --cluster "$CLUSTER_NAME" \
    --service "$SERVICE_NAME" \
    --task-definition "$EXISTING_TASK_DEF" \
    --force-new-deployment \
    >/dev/null

  echo "✅ ECS service updated."

else
  echo "🚀 Service does not exist → Creating new service..."

  aws ecs create-service \
    --cluster "$CLUSTER_NAME" \
    --service-name "$SERVICE_NAME" \
    --task-definition "$EXISTING_TASK_DEF" \
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
        \"targetGroupArn\": \"arn:aws:elasticloadbalancing:ap-south-1:818957473199:targetgroup/health-facility-tg/6ecb3f754e10b7ba\",
        \"containerName\": \"health-facility\",
        \"containerPort\": 9097
      }
    ]" >/dev/null

  echo "🎉 New health-facility ECS service created."
fi

echo "🎯 Deployment complete!"

