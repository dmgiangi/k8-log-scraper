# K8-Log-Scraper Development Guidelines

This document provides essential information for developers working on the K8-Log-Scraper project.

## Build/Configuration Instructions

### Prerequisites

- Java 21 (GraalVM recommended for native builds)
- Maven 3.8+
- Kubernetes cluster access for full functionality

### Building the Project

#### Standard JAR Build

```bash
./mvnw clean package
```

#### Native Executable Build

The project supports GraalVM native image compilation for faster startup and reduced memory footprint:

```bash
# Linux/macOS
./mvnw clean package -Pnative native:compile

# Windows
./mvnw clean package -Pnative native:compile
```

### Configuration

The application uses Spring Boot configuration with YAML files:

- `application.yaml` - Main configuration file
- `application-dev.yaml` - Development-specific configuration

To run with a specific profile:

```bash
java -jar target/k8-log-scraper-0.0.1.jar --spring.profiles.active=dev
```

## Testing Information

### Running Tests

To run all tests:

```bash
./mvnw test
```

To run a specific test class:

```bash
./mvnw test -Dtest=HelloCommandTest
```

### Adding New Tests

#### Shell Command Tests

For testing Spring Shell commands, use the `@ShellTest` annotation and `ShellTestClient`:

```java
@ShellTest
class YourCommandTest {

    @Autowired
    private ShellTestClient shellTestClient;

    @Test
    void testYourCommand() {
        // Create an interactive shell session
        ShellTestClient.InteractiveShellSession session = shellTestClient.interactive();
        
        // Execute your command
        session.write("your-command arg1 arg2");
        
        // Assert the output
        ShellAssertions.assertThat(session.screen()).containsText("Expected output");
    }
}
```

#### Example Test

The project includes a sample test for the `HelloCommand`:

```java
@ShellTest
class HelloCommandTest {

    @Autowired
    private ShellTestClient shellTestClient;

    @Test
    void shouldReturnDefaultGreeting() {
        ShellTestClient.InteractiveShellSession session = shellTestClient.interactive();
        session.write("hello");
        ShellAssertions.assertThat(session.screen()).containsText("Hello, World!");
    }

    @Test
    void shouldReturnCustomGreeting() {
        ShellTestClient.InteractiveShellSession session = shellTestClient.interactive();
        session.write("hello TestUser");
        ShellAssertions.assertThat(session.screen()).containsText("Hello, TestUser!");
    }
}
```

## Development Information

### Project Structure

- `src/main/java/dev/dmgiangi/k8/log/scraper/` - Main application code
- `src/main/java/dev/dmgiangi/k8/log/scraper/commands/` - Shell commands
- `src/main/resources/` - Configuration files
- `src/test/` - Test code

### Adding New Commands

To add a new command:

1. Create a new class in the `commands` package
2. Annotate it with `@ShellComponent`
3. Add methods annotated with `@ShellMethod`

Example:

```java
@ShellComponent
public class YourCommand {

    @ShellMethod(key = "command-name", value = "Command description")
    public String commandMethod(@ShellOption(defaultValue = "default") String arg) {
        // Command implementation
        return "Command result";
    }
}
```

### Kubernetes Client

The project uses the fabric8 Kubernetes client. To interact with Kubernetes:

```java
@Component
public class KubernetesService {

    private final KubernetesClient kubernetesClient;

    public KubernetesService(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    // Kubernetes operations
}
```

### Logging

The project uses SLF4J with Logback for logging. Use the Lombok `@Slf4j` annotation for easy logger access:

```java
@Slf4j
@Component
public class YourService {
    
    public void doSomething() {
        log.debug("Debug message");
        log.info("Info message");
        log.warn("Warning message");
        log.error("Error message");
    }
}
```

### Native Image Considerations

When developing for GraalVM native image compilation:

1. Avoid dynamic class loading or reflection without proper configuration
2. Add reflection hints using Spring's `@NativeHint` or GraalVM's `reflect-config.json`
3. Test native image builds regularly to catch issues early