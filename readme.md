## Build the project
### Build Locally:
To build the project locally, run the following command:
> mvn clean compile

### Build using Docker-compose
TODO :: To build a containerized environment run:
> docker-compose -f docker-compose.yml --build

TODO :: To push the image to a Docker registry run (NB: Make sure Docker CLI is already logged in to Docker registry first):
> docker-compose -f docker-compose.yml push

### Build using Github actions
From the remote repository, a Github workflow is available that wil be triggered when pushing to master branch.


### Build with GH actions:
A Github action automation is in place for this repo to build a Docker image and publish it to DockerHub registry.

## Set up the application
1. Before running the application, we need to either set up a system env variable (OPENAI_API_TOKEN) or create a file secret.config file under src/main/resources (openai.api.token = <OPENAI_API_TOKEN>)

## Run the application

### Run locally
Two types of runners have been created for convenience:
* One is a simple standalone Main app
* The second is a SpringBoot application listening on port server.port=8082

To run from an IDE, simply run one of the main classes, i.e. Main.java or OpenaiConnectorDemoApplication.java as java.

NB: 
- The application is set to relay network traffic through a forward proxy, i.e. squid, listening locally on port 3128 as per squid-proxy.yml deployed to -n misc. For this run below commands before starting the application locally:
  >* az vm start --resource-group STRMGATEWAY-NA2 --name vlmazsgw-lx2 --subscription 89e05d73-8862-4007-a700-0f895fc0f7ea --> This will make sure the the VM is started
  >* kubectl apply -f squid-proxy.yml -n misc --> Deploy the proxy pod and a NodePort service 
  >* kubectl port-forward service/squid-service -n misc 3128:3128 --> Forward traffic from local port 3128 to the remote squid proxy pod listening also on port 3128 
 
### Deploy to Kubernetes
To Deploy the containerized version of the app on Kubernetes cluster, two ways either manually through kubectl commands or automatically as explained below:

#### Deploy using Kubectl
Run below commands:
```sh
# Deploy Squid as a forward proxy 
kubectl apply -f k8s.squid-proxy.yml -n misc --> Deploy the proxy pod and a NodePort service
# Deploy the app
kubectl apply -f k8s.deployment.yml -n misc
# Use Postman to connect on e.g. localhost:8082/prompt and  send messages
kubectl port-forward service/apenai-connector-service 8082:80 -n misc
```

#### Deploy through Argo CD GitOps pipeline