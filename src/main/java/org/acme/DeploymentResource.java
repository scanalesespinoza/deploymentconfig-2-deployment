package org.acme;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;

import java.util.Map;

@Path("/deploy")
public class DeploymentResource {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)  // Now returning plain text
    public Response createDeploymentConfig(String dcYaml) throws Exception {

        // Set dummper options
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
 
        Yaml yaml = new Yaml(options);
        Map<String, Object> dcMap = yaml.load(dcYaml);

        // 1. Change apiVersion
        String apiVersion = (String) dcMap.get("apiVersion");
        if ("v1".equals(apiVersion) || "apps.openshift.io/v1".equals(apiVersion)) {
            dcMap.put("apiVersion", "apps/v1");
        }

        // 2. Change kind from DeploymentConfig to Deployment
        String kind = (String) dcMap.get("kind");
        if ("DeploymentConfig".equals(kind)) {
            dcMap.put("kind", "Deployment");
        }

        // 3. Remove unwanted metadata fields
        Map<String, Object> metadata = (Map<String, Object>) dcMap.get("metadata");
        if (metadata != null) {
            metadata.remove("uid");
            metadata.remove("resourceVersion");
            metadata.remove("generation");
            metadata.remove("creationTimestamp");
            metadata.remove("managedFields");
        }

        // 4. Ensure spec.selector.matchLabels is not empty and matches spec.template.metadata.labels
        Map<String, Object> spec = (Map<String, Object>) dcMap.get("spec");
        if (spec != null) {

            // Store selector values because select will be cleared
            Map<String, Object> selectorBackup = (Map<String, Object>) spec.get("selector");

            Map<String, Object> selector = (Map<String, Object>) spec.get("selector");
            if (selector == null) {
                selector = new java.util.HashMap<>();
                spec.put("selector", selector);
            } else {
                // Remove old labels from selector
                selector.clear();
            }

            Map<String, Object> matchLabels = (Map<String, Object>) selectorBackup.get("matchLabels");
            if (matchLabels == null) {
                matchLabels = new java.util.HashMap<>();
                selector.put("matchLabels", matchLabels);
            }

            // Ensure spec.template.metadata.labels exists and sync it with spec.selector.matchLabels
            Map<String, Object> template = (Map<String, Object>) spec.get("template");
            if (template != null) {
                Map<String, Object> templateMetadata = (Map<String, Object>) template.get("metadata");
                if (templateMetadata != null) {
                    Map<String, Object> templateLabels = (Map<String, Object>) templateMetadata.get("labels");
                    if (templateLabels != null) {
                        // Copy all labels from template.metadata.labels to spec.selector.matchLabels
                        matchLabels.putAll(templateLabels);
                    } else {
                        // Create empty labels if missing and ensure they match with the selector
                        templateLabels = new java.util.HashMap<>();
                        templateMetadata.put("labels", templateLabels);
                        matchLabels.putAll(templateLabels);
                    }
                }
            }

            // 5. Ensure spec.template.spec.containers.image is set, but do not override existing image
            Map<String, Object> templateSpec = (Map<String, Object>) template.get("spec");
            if (templateSpec != null) {
                java.util.List<Map<String, Object>> containers = (java.util.List<Map<String, Object>>) templateSpec.get("containers");
                if (containers != null && !containers.isEmpty()) {
                    Map<String, Object> container = containers.get(0);  // Assuming the first container
                    // Only set image if it is not already set
                    if (!container.containsKey("image")) {
                        container.put("image", "registry.access.redhat.com/rhscl/postgresql-${POSTGRESQL_VERSION}-rhel7");
                    }
                }
            }

            // 6. Modify spec.strategy
            Map<String, Object> strategy = (Map<String, Object>) spec.get("strategy");
            if (strategy != null) {
                // strategy: replace Rolling with RollingUpdate
                String type = (String) strategy.get("type");
                if ("Rolling".equals(type)) {
                strategy.put("type", "RollingUpdate");
                }

                // strategy: modify rollingParams
                Map<String, Object> rollingParams = (Map<String, Object>) strategy.get("rollingParams");
                if (rollingParams != null) {
                    // Remove spec.strategy.rollingParams.updatePeriodSeconds
                    rollingParams.remove("updatePeriodSeconds");

                    // Remove spec.strategy.rollingParams.intervalSeconds
                    rollingParams.remove("intervalSeconds");

                    // Remove spec.strategy.rollingParams.timeoutSeconds
                    rollingParams.remove("timeoutSeconds");
                }

                //replace rollingParams with rollingUpdate
                Map<String, Object> rollingUpdate = rollingParams;
                if (rollingUpdate != null) {
                    strategy.put("rollingUpdate", rollingUpdate);
                }
                strategy.remove("rollingParams");

                // strategy: remove spec.strategy.resources
                strategy.remove("resources");
 
                // strategy: remove spec.strategy.activeDeadlineSeconds
                strategy.remove("activeDeadlineSeconds");
            }
            

            // 7. Remove spec.triggers
            spec.remove("triggers");

            // 8. Remove spec.test
            spec.remove("test");


            // 9. Remove status block if it exists
            dcMap.remove("status");
            
        }

        // Convert back to YAML
        String deploymentYaml = yaml.dump(dcMap);

        // Return the transformed YAML as plain text
        return Response.ok(deploymentYaml).build();
    }
}
