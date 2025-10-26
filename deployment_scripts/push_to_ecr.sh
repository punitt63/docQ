#!/bin/bash
set -e

# ===== CONFIGURATION =====
AWS_REGION="ap-south-1"
ACCOUNT_ID="818957473199"
ECR_BASE_URL="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if version is provided
if [[ -z "$1" ]]; then
    print_error "Version is required!"
    echo "Usage: $0 <version>"
    echo "Example: $0 1.0"
    exit 1
fi

VERSION=$1
print_status "Pushing version: $VERSION"

# Check if JARs exist
HEALTH_FACILITY_JAR="health-facility/target/health-facility-${VERSION}.jar"
PATIENT_JAR="patient/target/patient-${VERSION}.jar"

# Also check for SNAPSHOT versions during development
HEALTH_FACILITY_SNAPSHOT="health-facility/target/health-facility-${VERSION}-SNAPSHOT.jar"
PATIENT_SNAPSHOT="patient/target/patient-${VERSION}-SNAPSHOT.jar"

# Use versioned JAR if it exists, otherwise use SNAPSHOT
if [[ -f "$HEALTH_FACILITY_SNAPSHOT" && ! -f "$HEALTH_FACILITY_JAR" ]]; then
    HEALTH_FACILITY_JAR="$HEALTH_FACILITY_SNAPSHOT"
    JAR_NAME="health-facility-${VERSION}-SNAPSHOT"
elif [[ -f "$HEALTH_FACILITY_JAR" ]]; then
    JAR_NAME="health-facility-${VERSION}"
fi

if [[ -f "$PATIENT_SNAPSHOT" && ! -f "$PATIENT_JAR" ]]; then
    PATIENT_JAR="$PATIENT_SNAPSHOT"
    PATIENT_JAR_NAME="patient-${VERSION}-SNAPSHOT"
elif [[ -f "$PATIENT_JAR" ]]; then
    PATIENT_JAR_NAME="patient-${VERSION}"
fi

print_status "Checking for versioned JARs..."

if [[ ! -f "$HEALTH_FACILITY_JAR" ]]; then
    print_error "JAR not found: $HEALTH_FACILITY_JAR"
    print_error "Please build the project with version $VERSION first"
    exit 1
fi

if [[ ! -f "$PATIENT_JAR" ]]; then
    print_error "JAR not found: $PATIENT_JAR"
    print_error "Please build the project with version $VERSION first"
    exit 1
fi

print_success "Found JARs:"
echo "  ✓ $HEALTH_FACILITY_JAR"
echo "  ✓ $PATIENT_JAR"
echo ""

# Login to ECR
print_status "Logging in to Amazon ECR..."
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_BASE_URL

# Create Dockerfiles temporarily
print_status "Creating Dockerfiles..."

cat > Dockerfile.health-facility << EOF
FROM openjdk:17-jdk-slim
COPY ${JAR_NAME}.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

cat > Dockerfile.patient << EOF
FROM openjdk:17-jdk-slim
COPY ${PATIENT_JAR_NAME}.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

# Build and push health-facility
print_status "Building Docker image for health-facility..."

# Copy JAR into container build context
mkdir -p temp-build/health-facility
cp $HEALTH_FACILITY_JAR temp-build/health-facility/${JAR_NAME}.jar
cp Dockerfile.health-facility temp-build/health-facility/Dockerfile

cd temp-build/health-facility
docker build -f Dockerfile -t health-facility:$VERSION -t health-facility:latest .
cd ../..

print_status "Tagging health-facility image for ECR..."
docker tag health-facility:$VERSION ${ECR_BASE_URL}/docq/health-facility:$VERSION
docker tag health-facility:latest ${ECR_BASE_URL}/docq/health-facility:latest

print_status "Checking if ECR repo exists for health-facility..."
if ! aws ecr describe-repositories --repository-names docq/health-facility --region $AWS_REGION >/dev/null 2>&1; then
    print_warning "Repository not found. Creating ECR repo..."
    aws ecr create-repository --repository-name docq/health-facility --region $AWS_REGION
fi

print_status "Pushing health-facility image to ECR..."
docker push ${ECR_BASE_URL}/docq/health-facility:$VERSION
docker push ${ECR_BASE_URL}/docq/health-facility:latest

print_success "Pushed health-facility image:"
echo "  ${ECR_BASE_URL}/docq/health-facility:$VERSION"
echo "  ${ECR_BASE_URL}/docq/health-facility:latest"

# Build and push patient
print_status "Building Docker image for patient..."
mkdir -p temp-build/patient
cp $PATIENT_JAR temp-build/patient/${PATIENT_JAR_NAME}.jar
cp Dockerfile.patient temp-build/patient/Dockerfile

cd temp-build/patient
docker build -f Dockerfile -t patient:$VERSION -t patient:latest .
cd ../..

print_status "Tagging patient image for ECR..."
docker tag patient:$VERSION ${ECR_BASE_URL}/docq/patient:$VERSION
docker tag patient:latest ${ECR_BASE_URL}/docq/patient:latest

print_status "Checking if ECR repo exists for patient..."
if ! aws ecr describe-repositories --repository-names docq/patient --region $AWS_REGION >/dev/null 2>&1; then
    print_warning "Repository not found. Creating ECR repo..."
    aws ecr create-repository --repository-name docq/patient --region $AWS_REGION
fi

print_status "Pushing patient image to ECR..."
docker push ${ECR_BASE_URL}/docq/patient:$VERSION
docker push ${ECR_BASE_URL}/docq/patient:latest

print_success "Pushed patient image:"
echo "  ${ECR_BASE_URL}/docq/patient:$VERSION"
echo "  ${ECR_BASE_URL}/docq/patient:latest"

# Cleanup
print_status "Cleaning up temporary files..."
rm -f Dockerfile.health-facility Dockerfile.patient
rm -rf temp-build

echo ""
print_success "🎉 Successfully pushed all images to ECR!"
echo ""
print_status "Summary:"
echo "- Health Facility: ${ECR_BASE_URL}/docq/health-facility:$VERSION"
echo "- Patient: ${ECR_BASE_URL}/docq/patient:$VERSION"
echo ""
