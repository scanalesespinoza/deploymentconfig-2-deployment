
package org.acme;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Path("/deploy")
public class DeploymentResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDeploymentConfig(FormDataMultiPart formData) {
        String dcYaml = formData.getField("dcText").getValue();
        
        if (dcYaml.isEmpty()) {
            InputStream fileInputStream = formData.getField("dcFile").getEntityAs(InputStream.class);
            dcYaml = new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        // Convert DeploymentConfig YAML to standard Deployment YAML (dummy transformation for simplicity)
        Yaml yaml = new Yaml();
        Map<String, Object> dcMap = yaml.load(dcYaml);

        // Perform a basic transformation to mimic converting a DC to Deployment (actual conversion requires more work)
        dcMap.put("kind", "Deployment");
        dcMap.remove("strategy");

        String deploymentYaml = yaml.dump(dcMap);
        return Response.ok(deploymentYaml).build();
    }
}
