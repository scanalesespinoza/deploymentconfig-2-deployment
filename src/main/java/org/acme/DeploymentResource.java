
package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/deploy")
public class DeploymentResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDeploymentConfig(String deploymentConfig) {
        // For simplicity, returning a fake deployment response
        String deployment = "{\"status\": \"success\", \"message\": \"Deployment created for: \" + deploymentConfig}";
        return Response.ok(deployment).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Send a POST request with DeploymentConfig JSON!";
    }
}
