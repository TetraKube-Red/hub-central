package red.tetracube.hubcentral.api;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import red.tetracube.hubcentral.api.payloads.Login;

@Path("/auth")
public class HubAuthAPI {
    
    @PermitAll
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public Login.Reply login(@Valid @RequestBody Login.Request loginRequest) {
        return hubSecurityServices.tryToAuthenticateUser(loginRequest);
    }

}
