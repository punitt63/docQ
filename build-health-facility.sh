#!/bin/bash
set -e

#===============================================
# BUILD & PUSH health-facility IMAGE
#===============================================

# AWS / ECR configuration
AWS_REGION="ap-south-1"
ECR_REPO="818957473199.dkr.ecr.ap-south-1.amazonaws.com/docq/health-facility"

echo "🐳 Building Docker image for health-facility..."
docker build -t health-facility:latest -f health-facility/Dockerfile .

echo "🔐 Logging into AWS ECR..."
aws ecr get-login-password --region "$AWS_REGION" \
  | docker login --username AWS --password-stdin "$ECR_REPO"

echo "🏷 Tagging image..."
docker tag health-facility:latest "$ECR_REPO:latest"

echo "📤 Pushing image to ECR..."
docker push "$ECR_REPO:latest"

echo "✅ Image built and pushed to ECR (tag: latest)."


