# Deployment Scripts

This directory contains scripts for Maven release and deployment.

## Maven Release

### maven-release-complete.sh

Performs both release:prepare and release:perform with your specified parameters.

**Usage:**
```bash
# Use default Maven settings
./maven-release-complete.sh

# Or specify custom Maven settings path as argument
./maven-release-complete.sh /path/to/settings.xml
```

**What it does:**
- Runs `mvn release:prepare`
- Runs `mvn release:perform`
- Skips tests, Flyway, and Swagger generation
- Creates release commit, tag, and artifacts
- Ready to push to GitHub

## Parameters

The script uses these Maven parameters:
- `-DskipTests` - Skip unit tests
- `-Dflyway.skip=true` - Skip Flyway migrations
- `-Dmaven.test.skip` - Skip tests
- `-Dskip.it=true` - Skip integration tests
- `-Dswagger.generate.skip=true` - Skip Swagger generation

## After Release

Once the release is complete:
```bash
# Push commits
git push origin main

# Push tags
git push origin --tags
```

## Example

```bash
# Run the release
./deployment_scripts/maven-release-complete.sh

# When prompted, enter:
# - Release version (e.g., 1.0.0)
# - Next dev version (e.g., 1.0.1-SNAPSHOT)
```