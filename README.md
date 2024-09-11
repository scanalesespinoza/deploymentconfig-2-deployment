
# Quarkus DeploymentConfig to Deployment Converter

This tool is a Quarkus-based application that converts OpenShift `DeploymentConfig` YAML files into Kubernetes `Deployment` YAML files. The application can be accessed through a web interface or via REST API calls.

## Features
- Converts OpenShift `DeploymentConfig` YAML to Kubernetes `Deployment` YAML.
- Handles transformations such as changing `apiVersion`, `kind`, `spec`, and removing unwanted fields from metadata.
- Provides both a user-friendly web interface and a REST API.

## Available images
- `podman pull quay.io/sergio_canales_e/quarkus/dc2d:1.0.0-SNAPSHOT`

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
  - [Web Interface](#web-interface)
  - [REST API](#rest-api)
- [Running on OpenShift](#running-on-openshift)
- [Building a Native Image](#building-a-native-image)

## Installation

### Prerequisites
- Java 21+
- Maven 3.6.3+
- Docker (optional, for containerized builds)

### K8s Install

   ```bash
   # K8s
   kubectl apply -f k8s/deployment.yaml
   kubectl apply -f k8s/service.yaml
   # Openshift
   oc apply -f k8s/deployment.yaml
   oc apply -f k8s/service.yaml
   oc apply -f k8s/route.yaml
   ```

### Podman

   ```bash
   podman pull quay.io/sergio_canales_e/quarkus/dc2d:1.0.0-SNAPSHOT
   podman run -p 8080:8080 dc2d:1.0.0-SNAPSHOT
   ```

### Build the Application
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. Build the application:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   java -jar target/quarkus-app-runner.jar
   ```

4. Access the application at: `http://localhost:8080`

## Usage

### Web Interface

The web interface provides a simple form where you can input your OpenShift `DeploymentConfig` YAML, and it will convert it to a Kubernetes `Deployment`.

1. Open your browser and go to `http://localhost:8080`.

2. You will see a form with a text area where you can paste your `DeploymentConfig` YAML.

3. Click the **Submit** button, and the converted `Deployment` YAML will be displayed.

#### Example:
```yaml
apiVersion: apps.openshift.io/v1
kind: DeploymentConfig
metadata:
  name: example
spec:
  replicas: 1
  template:
    spec:
      containers:
        - name: example
          image: example-image
```

After clicking **Submit**, you will get the corresponding Kubernetes `Deployment` YAML.

### REST API

You can also use the tool via the REST API. The REST API expects a POST request with the `DeploymentConfig` YAML as the payload. The response will be the converted Kubernetes `Deployment` YAML.

#### REST API Endpoint
```
POST /deploy
```

#### Request Format

- Content-Type: `text/plain`
- Method: `POST`
- Body: Plain text YAML of the OpenShift `DeploymentConfig`.

#### Example Request (via `curl`):
```bash
curl -X POST http://localhost:8080/deploy      -H "Content-Type: text/plain"      --data-binary @deploymentconfig.yaml
```

Where `deploymentconfig.yaml` is your OpenShift `DeploymentConfig` YAML file.

#### Example Request Body:
```yaml
apiVersion: apps.openshift.io/v1
kind: DeploymentConfig
metadata:
  name: example
spec:
  replicas: 1
  template:
    spec:
      containers:
        - name: example
          image: example-image
```

#### Example Response:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: example
spec:
  replicas: 1
  selector:
    matchLabels:
      name: example
  template:
    metadata:
      labels:
        name: example
    spec:
      containers:
        - name: example
          image: example-image
```

## Running on OpenShift

You can deploy this application as a Knative service on OpenShift. Follow these steps:

1. Build the application using a native image:

   ```bash
   mvn clean package -Pnative -Dquarkus.native.container-build=true
   ```

2. Create a Docker container image for the application:

   ```bash
   docker build -t quay.io/<username>/deployment-converter:latest .
   docker push quay.io/<username>/deployment-converter:latest
   ```

3. Create a `knative-service.yaml` file:

   ```yaml
   apiVersion: serving.knative.dev/v1
   kind: Service
   metadata:
     name: deployment-converter
   spec:
     template:
       spec:
         containers:
           - image: quay.io/<username>/deployment-converter:latest
             ports:
               - containerPort: 8080
   ```

4. Deploy the Knative service to OpenShift:

   ```bash
   oc apply -f knative-service.yaml
   ```

5. Once deployed, access the application via the Knative service URL provided by OpenShift.

## Building a Native Image

To build a native image for faster startup times:

1. Run the following Maven command:

   ```bash
   mvn clean package -Pnative -Dquarkus.native.container-build=true
   ```

2. The native binary will be available in the `target` directory.

---

This tool helps automate the transformation of OpenShift `DeploymentConfig` resources to Kubernetes `Deployment` resources, making it easy to migrate and deploy on Kubernetes clusters.

