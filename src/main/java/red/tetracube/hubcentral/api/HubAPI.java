package red.tetracube.hubcentral.api;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.security.Authenticated;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import red.tetracube.hubcentral.api.payloads.HubPayload;
import red.tetracube.hubcentral.exceptions.HubCentralException;
import red.tetracube.hubcentral.services.HubServices;

@Path("/")
@Authenticated
public class HubAPI {

    @Inject
    JsonWebToken jwt;

    @Inject
    HubServices hubServices;

    private final static Logger LOG = LoggerFactory.getLogger(HubAPI.class);

    @Path("/info")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public HubPayload.Reply getInfo() {
        var slug = jwt.getIssuer();
        var result = hubServices.getHubBySlug(slug);
        if (result.isSuccess()) {
            var hubEntity = result.getContent();
            try {
                var hubReply = new HubPayload.Reply(
                        hubEntity.getSlug(),
                        hubEntity.getName(),
                        hubEntity.getRooms().stream()
                                .map(r -> new HubPayload.RoomPayload(r.getSlug(), r.getName()))
                                .toList());
                return hubReply;
            } catch (Exception ex) {
                LOG.error("There was an error during hub entity mapping into reply", ex);
                ;
                throw new InternalServerErrorException("Internal hub error");
            }
        }
        var exception = result.getException();
        if (exception instanceof HubCentralException.RepositoryException) {
            throw new InternalServerErrorException("Internal hub error");
        } else if (exception instanceof HubCentralException.EntityNotFoundException) {
            throw new NotFoundException();
        } else {
            throw new InternalServerErrorException(exception);
        }
    }
}
