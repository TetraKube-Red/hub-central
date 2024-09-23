package red.tetracube.hubcentral.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import red.tetracube.hubcentral.database.entities.Hub;
import red.tetracube.hubcentral.database.repositories.HubRepository;
import red.tetracube.hubcentral.exceptions.HubCentralException;
import red.tetracube.hubcentral.services.dto.Result;

@RequestScoped
public class HubAuthServices {

    @Inject
    HubRepository hubRepository;

    @ConfigProperty(name = "mp.jwt.verify.audiences")
    Set<String> jwtAudiences;

    public Result<String> generateTokenForHub(String name, String accessCode) {
        Optional<Hub> optionalHub = null;
        try {
            optionalHub = hubRepository.getHubByName(name);
        } catch (Exception ex) {
            return Result.failed(
                    new HubCentralException.RepositoryException(ex));
        }
        if (optionalHub.isEmpty()) {
            return Result.failed(
                    new HubCentralException.EntityNotFoundException("Cannot find any Hub with given name"));
        }

        var theHub = optionalHub.get();
        if (!BcryptUtil.matches(accessCode, theHub.getAccessCode())) {
            return Result.failed(
                    new HubCentralException.UnauthorizedException("Access code is invalid"));
        }

        var tokenIssueTS = Instant.now();
        var tokenExpirationTS = Instant.now().plus(30, ChronoUnit.DAYS);
        var token = Jwt
                .claims()
                .issuer(theHub.getSlug())
                .upn(theHub.getName())
                .audience(jwtAudiences)
                .issuedAt(tokenExpirationTS)
                .expiresAt(tokenExpirationTS)
                .sign();
        return Result.success(token);
    }

}
