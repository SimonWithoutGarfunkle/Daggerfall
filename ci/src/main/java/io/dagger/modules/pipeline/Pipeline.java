package io.dagger.modules.pipeline;

import static io.dagger.client.Dagger.dag;

import io.dagger.client.Container;
import io.dagger.client.Directory;
import io.dagger.client.exception.DaggerQueryException;
import io.dagger.module.annotation.Function;
import io.dagger.module.annotation.Object;

import java.util.List;
import java.util.concurrent.ExecutionException;

/** Pipeline CI/CD du forgeron */
@Object
public class Pipeline {

    private static final String MAVEN_IMAGE = "maven:3.9-eclipse-temurin-21";

    private Container mavenContainer(Directory source) {
        return dag().container()
                .from(MAVEN_IMAGE)
                .withMountedCache("/root/.m2", dag().cacheVolume("maven-cache"))
                .withMountedDirectory("/project", source)
                .withWorkdir("/project");
    }

    /** Compile le projet et les sources de test */
    @Function
    public String build(Directory source)
            throws InterruptedException, ExecutionException, DaggerQueryException {
        return mavenContainer(source)
                .withExec(List.of("mvn", "compile", "test-compile", "-B", "--no-transfer-progress"))
                .stdout();
    }

    /** Lance les tests unitaires (package service, pattern *Test) */
    @Function
    public String unitTests(Directory source)
            throws InterruptedException, ExecutionException, DaggerQueryException {
        return mavenContainer(source)
                .withExec(List.of("mvn", "test", "-B", "--no-transfer-progress", "-Dtest=*Test"))
                .stdout();
    }

    /** Lance les tests d'intégration (package controller, pattern *IT et *Tests) */
    @Function
    public String integrationTests(Directory source)
            throws InterruptedException, ExecutionException, DaggerQueryException {
        return mavenContainer(source)
                .withExec(List.of("mvn", "test", "-B", "--no-transfer-progress", "-Dtest=*IT,*Tests"))
                .stdout();
    }

    /** Analyse Sonar (simulée) */
    @Function
    public String sonar(Directory source)
            throws InterruptedException, ExecutionException, DaggerQueryException {
        return dag().container()
                .from("alpine:latest")
                .withExec(List.of("sh", "-c",
                        "echo 'Quality Gate: PASSED' && echo 'Bugs: 0' && echo 'Coverage: 85%'"))
                .stdout();
    }

    /** Déploiement (simulé) */
    @Function
    public String deploy(Directory source)
            throws InterruptedException, ExecutionException, DaggerQueryException {
        return dag().container()
                .from("alpine:latest")
                .withExec(List.of("sh", "-c",
                        "echo 'Deployment completed' && echo 'Status: RUNNING'"))
                .stdout();
    }
}
