package com.dh.sp.reactive.mongo;

import java.util.Map;
import java.util.stream.Stream;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(
        initializers = IntegrationTest.Initializer.class)
public abstract class IntegrationTest {

    static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {

        static GenericContainer mongoDb = new GenericContainer("mongo")
                .withExposedPorts(27017);

        private static void startContainers() {
            Startables.deepStart(Stream.of(mongoDb)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.data.mongodb.ur", String.format("mongodb://%s:%s/test",
                    mongoDb.getContainerIpAddress(),
                    mongoDb.getMappedPort(27017)),
                    "spring.data.mongodb.host", mongoDb.getContainerIpAddress(),
                    "spring.data.mongodb.port", mongoDb.getMappedPort(27017).toString()
            );
        }

        @Override
        public void initialize(
                ConfigurableApplicationContext applicationContext) {

            startContainers();

            ConfigurableEnvironment environment =
                    applicationContext.getEnvironment();

            MapPropertySource testcontainers = new MapPropertySource(
                    "testcontainers",
                    (Map) createConnectionConfiguration()
            );

            environment.getPropertySources().addFirst(testcontainers);
        }
    }
}
