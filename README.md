# cel-lab
## Run using Docker

```shell
docker run --rm -p 8081:8081 michalklempa/cel-lab:latest
```

Browse to [localhost:8081](http://localhost:8081)

## Obtaining a `.desc` File for the UI

The UI evaluates CEL expressions against a message type loaded from a Protobuf
descriptor set (`FileDescriptorSet`, conventionally a `.desc` file). Upload one
in the UI, then pick the message type to evaluate against.

Generate the descriptor with `protoc`:

```bash
protoc --descriptor_set_out=out.desc --include_imports *.proto
```

- `--descriptor_set_out` is the output `.desc` path.
- `--include_imports` is **required** — it bundles every transitive import into a
  single self-contained file. Without it, CEL cannot resolve types referenced
  across `.proto` files and the upload will fail.

Any tool that emits a `FileDescriptorSet` works as well, for example
[Buf](https://buf.build):

```bash
buf build -o out.desc
```

## Project Structure

Source code for the UI part is located at:
```
src/main/java/com/michalklempa
```

The main entry point into the application is `Application.java`. This class contains the `main()` method that starts up 
the Spring Boot application.

In the `src/main/java/com/michalklempa/base/ui` is the `MainLayout.java` of the Application.

The only one screen of the application is actually a Playground to edit CEL expressions located in `src/main/java/com/michalklempa/playground`
 
Directory `src/main/java/main/frontend` can be ignored, it is generated during compilation.

## Starting in Development Mode

To start the application in development mode:

```bash
mvn spring-boot:run
``` 

## Building for Production

To build the application in production mode, run:

```bash
mvn package
```

To build a Docker image, run:

```bash
docker build -t my-application:latest .
```

## Native Application Image (jpackage)

Bundles the app with a Java runtime into a self-contained launcher. Requires
**JDK 25** active; build on the OS/arch you target (no cross-compilation).

Linux — launcher at `target/dist/cel-lab/bin/cel-lab`:

```bash
mvn -Plinux-jpackage -Dvaadin.force.production.build=true -DskipTests clean package
```

Windows — adds `--win-console`; launcher at `target\dist\cel-lab\cel-lab.exe`:

```bash
mvn -Pwindows-jpackage -Dvaadin.force.production.build=true -DskipTests clean package
```
