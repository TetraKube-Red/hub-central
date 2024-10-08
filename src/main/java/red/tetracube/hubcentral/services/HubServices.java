package red.tetracube.hubcentral.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import red.tetracube.hubcentral.database.entities.HubEntity;
import red.tetracube.hubcentral.database.repositories.HubRepository;
import red.tetracube.hubcentral.database.repositories.RoomRepository;
import red.tetracube.hubcentral.exceptions.HubCentralException;
import red.tetracube.hubcentral.services.dto.Result;

@RequestScoped
public class HubServices {

    @Inject
    HubRepository hubRepository;

    @Inject
    RoomRepository roomRepository;

    @ConfigProperty(name = "mp.jwt.verify.audiences")
    Set<String> jwtAudiences;

    private final static Logger LOG = LoggerFactory.getLogger(HubServices.class);

    public Result<String> generateTokenForHub(String name, String accessCode) {
        Optional<HubEntity> optionalHub = null;
        try {
            optionalHub = hubRepository.getHubByName(name);
        } catch (Exception ex) {
            LOG.error("Error on repository query:", ex);
            return Result.failed(
                    new HubCentralException.RepositoryException(ex));
        }
        if (optionalHub.isEmpty()) {
            LOG.warn("No hub named {} found", name);
            return Result.failed(
                    new HubCentralException.EntityNotFoundException("Cannot find any Hub with given name"));
        }

        var theHub = optionalHub.get();
        if (!BcryptUtil.matches(accessCode, theHub.getAccessCode())) {
            LOG.warn("The access code does not match");
            return Result.failed(
                    new HubCentralException.UnauthorizedException("Access code is invalid"));
        }

        var tokenIssueTS = Instant.now();
        var tokenExpirationTS = Instant.now().plus(30, ChronoUnit.DAYS);
        var token = Jwt
                .claims()
                .issuer(theHub.getSlug())
                .issuedAt(tokenIssueTS)
                .upn(theHub.getName())
                .audience(jwtAudiences)
                .expiresAt(tokenExpirationTS)
                .sign();
        LOG.info("Hub access granted, created the JWT");
        return Result.success(token);
    }

    @Transactional
    public Result<HubEntity> getHubBySlug(String slug) {
        Optional<HubEntity> optionalHub = null;
        try {
            optionalHub = hubRepository.getHubBySlug(slug);
        } catch (Exception ex) {
            LOG.error("Error on repository query:", ex);
            return Result.failed(
                    new HubCentralException.RepositoryException(ex));
        }
        if (optionalHub.isEmpty()) {
            return Result.failed(
                    new HubCentralException.EntityNotFoundException("Cannot find any Hub with given name"));
        }

        var hub = optionalHub.get();
        hub.getRooms().size();
        return Result.success(hub);
    }

}
