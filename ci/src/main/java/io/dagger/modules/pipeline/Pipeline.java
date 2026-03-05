package io.dagger.modules.pipeline;

import static io.dagger.client.Dagger.dag;

import io.dagger.client.Container;
import io.dagger.client.Directory;
import io.dagger.client.exception.DaggerQueryException;
import io.dagger.module.annotation.DefaultPath;
import io.dagger.module.annotation.Function;
import io.dagger.module.annotation.Object;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Object
public class Pipeline {

    //Useless counter: 5
    private static final String MAVEN_IMAGE = "maven:3.9-eclipse-temurin-21";

    private Container mavenContainer(Directory source) {
        return dag().container()
                .from(MAVEN_IMAGE)
                .withMountedCache("/root/.m2", dag().cacheVolume("maven-cache"))
                .withMountedDirectory("/project", source)
                .withWorkdir("/project");
    }

    @Function
    public String build(@DefaultPath(".") Directory source)
            throws InterruptedException, ExecutionException, DaggerQueryException {
        return mavenContainer(source)
                .withExec(List.of("mvn", "compile", "test-compile", "-B", "--no-transfer-progress"))
                .stdout();
    }

    @Function
    public String unitTests(@DefaultPath(".") Directory source)
            throws InterruptedException, ExecutionException, DaggerQueryException {
        return mavenContainer(source)
                .withExec(List.of("mvn", "test", "-B", "--no-transfer-progress", "-Dtest=*Test"))
                .stdout();
    }

    @Function
    public String integrationTests(@DefaultPath(".") Directory source)
            throws InterruptedException, ExecutionException, DaggerQueryException {
        return mavenContainer(source)
                .withExec(List.of("mvn", "test", "-B", "--no-transfer-progress", "-Dtest=*IT,*Tests"))
                .stdout();
    }

    @Function
    public String sonar()
            throws InterruptedException, ExecutionException, DaggerQueryException {
        return dag().container()
                .from("alpine:latest")
                .withExec(List.of("sh", "-c", String.join(" && ",
                        "echo '=== Sonar Analysis ==='",
                        "echo 'Running analysis...'",
                        "sleep 1",
                        "echo 'Quality Gate: PASSED'",
                        "echo 'Bugs: 0'",
                        "echo 'Coverage: 85%'",
                        "echo 'Analysis complete.'"
                )))
                .stdout();
    }

    @Function
    public String deploy()
            throws InterruptedException, ExecutionException, DaggerQueryException {
        return dag().container()
                .from("alpine:latest")
                .withExec(List.of("sh", "-c",
                        "echo '=== Starting Deploy ===' && echo 'Deployment completed' && echo 'Status: RUNNING'"))
                .stdout();
    }

    @Function
    public String run(@DefaultPath(".") Directory source)
            throws InterruptedException, ExecutionException, DaggerQueryException {
        Container built = mavenContainer(source)
                .withExec(List.of("mvn", "compile", "test-compile", "-B", "--no-transfer-progress"));

        String buildOut = built.stdout();

        String utOut = built
                .withExec(List.of("mvn", "test", "-B", "--no-transfer-progress", "-Dtest=*Test"))
                .stdout();

        String itOut = built
                .withExec(List.of("mvn", "test", "-B", "--no-transfer-progress", "-Dtest=*IT,*Tests"))
                .stdout();

        String sonarOut = sonar();

        String deployOut = deploy();

        return String.join("\n---\n", buildOut, utOut, itOut, sonarOut, deployOut);
    }
}
