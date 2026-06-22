# cel-lab
## Run using Docker

```shell
docker run --rm -p 8081:8081 michalklempa/cel-lab:latest
```

Browse to [localhost:8081](http://localhost:8081)

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
