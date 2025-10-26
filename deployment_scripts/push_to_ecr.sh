#!/bin/bash
set -e

# ===== CONFIGURATION =====
AWS_REGION="us-east-1"             # Change to your AWS region
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
REPO_NAME="my-app"                 # Change to your ECR repository name
IMAGE_TAG="latest"                 # Or use a version like "v1.0.0"
DOCKERFILE_PATH="."                # Path to your Dockerfile

# ===== SCRIPT LOGIC =====
echo "Logging in to Amazon ECR..."
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin ${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com

echo "Building Docker image..."
docker build -t ${REPO_NAME}:${IMAGE_TAG} ${DOCKERFILE_PATH}

echo "Tagging image for ECR..."
docker tag ${REPO_NAME}:${IMAGE_TAG} ${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_NAME}:${IMAGE_TAG}

echo "Checking if ECR repo exists..."
if ! aws ecr describe-repositories --repository-names ${REPO_NAME} --region ${AWS_REGION} >/dev/null 2>&1; then
  echo "Repository not found. Creating ECR repo..."
  aws ecr create-repository --repository-name ${REPO_NAME} --region ${AWS_REGION}
fi

echo "Pushing image to ECR..."
docker push ${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_NAME}:${IMAGE_TAG}

echo "✅ Successfully pushed image to:"
echo "${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPO_NAME}:${IMAGE_TAG}"
