package org.acme;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.yaml.snakeyaml.Yaml;
import java.util.Map;

@Path("/deploy")
public class DeploymentResource {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createDeploymentConfig(String dcYaml) throws Exception {
        // Convert DeploymentConfig YAML to standard Deployment YAML (dummy transformation for simplicity)
        Yaml yaml = new Yaml();
        Map<String, Object> dcMap = yaml.load(dcYaml);

        // Perform a basic transformation to mimic converting a DC to Deployment (actual conversion requires more work)
        dcMap.put("kind", "Deployment");
        dcMap.remove("strategy");

        // Output the transformed Deployment YAML
        String deploymentYaml = yaml.dump(dcMap);
        return Response.ok(deploymentYaml).build();
    }
}
