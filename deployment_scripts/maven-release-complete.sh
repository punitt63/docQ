#!/bin/bash

# Complete Maven Release Script
# This script performs both release:prepare and release:perform

set -e  # Exit on any error

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

echo "🚀 Complete Maven Release Script"
echo "=================================="
echo ""

# Maven settings file path (default or from argument)
MAVEN_SETTINGS="${1:-/Users/ppichhol/IdeaProjects/.m2/settings.xml}"

if [[ ! -f "$MAVEN_SETTINGS" ]]; then
    echo "⚠️  Warning: Maven settings file not found at: $MAVEN_SETTINGS"
    echo "Using default Maven settings"
    MAVEN_SETTINGS=""
else
    echo "Using Maven settings: $MAVEN_SETTINGS"
fi

echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed. Please install it first:"
    echo "brew install maven"
    exit 1
fi

# Check if we're in a git repository
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    print_error "Not a git repository"
    exit 1
fi

# Check for uncommitted changes
if [[ -n $(git status -s) ]]; then
    print_warning "You have uncommitted changes"
    git status -s
    read -p "Do you want to continue anyway? (y/N): " -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_error "Release cancelled. Please commit or stash your changes."
        exit 1
    fi
fi

# Step 1: Release:prepare
print_status "Step 1: Running release:prepare..."

if [[ -n "$MAVEN_SETTINGS" ]]; then
    MAVEN_ARGS="-s $MAVEN_SETTINGS"
else
    MAVEN_ARGS=""
fi

mvn $MAVEN_ARGS clean \
  -Darguments="-DskipTests -Dflyway.skip=true -Dmaven.test.skip -Dskip.it=true -Dswagger.generate.skip=true" \
  -DskipTests=true \
  -Dmaven.test.skip \
  -Dskip.it=true \
  -f pom.xml \
  release:prepare

if [[ $? -ne 0 ]]; then
    print_error "Release:prepare failed!"
    exit 1
fi

print_success "Release:prepare completed!"

# Step 2: Release:perform (with install only, no deploy, skip javadoc)
print_status "Step 2: Running release:perform..."
mvn $MAVEN_ARGS release:perform -Dgoals=install -Darguments="-DskipTests -Dflyway.skip=true -Dmaven.test.skip -Dskip.it=true -Dswagger.generate.skip=true -Dmaven.javadoc.skip=true"

if [[ $? -ne 0 ]]; then
    print_error "Release:perform failed!"
    exit 1
fi

print_success "Release:perform completed!"

# Step 3: Push to GitHub
print_status "Step 3: Pushing to GitHub..."
git push origin main
if [[ $? -ne 0 ]]; then
    print_error "Failed to push to origin main!"
    exit 1
fi

git push origin --tags
if [[ $? -ne 0 ]]; then
    print_error "Failed to push tags!"
    exit 1
fi

print_success "Pushed to GitHub successfully!"

echo ""
print_success "🎉 Maven release completed successfully!"
echo ""
print_status "Summary:"
echo "- Release tag created and pushed"
echo "- Version bumped for next development"
echo "- Release artifacts created and installed"
echo "- Changes pushed to GitHub"
echo ""
