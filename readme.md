## Build the project
To build the project locally, run the following command:
> mvn clean compile

TODO :: To build a containerized environment run:
> docker-compose -f docker-compose.yml --build

TODO :: To push the image to a Docker registry run (NB: Make sure Docker CLI is already logged in to Docker registry first):
> docker-compose -f docker-compose.yml push

## Set up the application
1. Before running the application, we need to either set up a system env variable (OPENAI_API_TOKEN) or create a file secret.config file under src/main/resources (openai.api.token = <OPENAI_API_TOKEN>)

## Run the application
Two types of runners have been created for convenience:
* One is a simple standalone Main app
* The second is a SpringBoot application listening on port server.port=8082

To run from an IDE, simply run one of the main classes, i.e. Main.java or OpenaiConnectorDemoApplication.java as java.
