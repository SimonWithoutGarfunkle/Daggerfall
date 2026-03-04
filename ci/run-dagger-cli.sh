#!/bin/bash
set -e

IMAGE_MAVEN="maven:3.9.6-eclipse-temurin-21"
IMAGE_ALPINE="alpine:latest"

echo "Démarrage de la pipeline Dagger"

echo "--- [Step 1 & 2: Build & Unit Tests] ---"
dagger -c "
  container |
    from ${IMAGE_MAVEN} |
    with-mounted-cache /root/.m2/repository \$(cache-volume m2-cache) |
    with-directory /src \$(host | directory . --exclude target) |
    with-workdir /src |
    with-exec -- mvn compile test-compile -B --no-transfer-progress |
    with-exec -- mvn test -B --no-transfer-progress -Dtest=*Test |
    stdout
"

echo "--- [Step 3: Integration Tests] ---"
dagger -c "
  container |
    from ${IMAGE_MAVEN} |
    with-mounted-cache /root/.m2/repository \$(cache-volume m2-cache) |
    with-directory /src \$(host | directory . --exclude target) |
    with-workdir /src |
    with-exec -- mvn test -B --no-transfer-progress -Dtest=*IT,*Tests |
    stdout
"

echo "--- [Step 4: Sonar (Simulation)] ---"
dagger -c "
  container |
    from ${IMAGE_ALPINE} |
    with-exec -- sh -c \"
      echo 'Quality Gate: PASSED' && \
      echo 'Coverage: 85.2%'
    \" |
    stdout
"

echo "--- [Step 5: Deploy (Simulation)] ---"
dagger -c "
  container |
    from ${IMAGE_ALPINE} |
    with-exec -- sh -c \"
      echo 'Deployment completed successfully' && \
      echo 'Version: 0.0.1-SNAPSHOT'
    \" |
    stdout
"

echo "Pipeline terminée avec succès !"