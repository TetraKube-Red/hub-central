package red.tetracube.hubcentral.database.repositories;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.Transactional;
import red.tetracube.hubcentral.database.entities.Hub;

@ApplicationScoped
public class HubRepository {

    @Inject
    @PersistenceUnit(unitName = "hubs")
    EntityManager em;

    @Transactional
    public Optional<Hub> getHubByName(String name) {
        try {
            var hub = em.<Hub>createQuery("name = :name", Hub.class)
                    .setParameter("name", name)
                    .setMaxResults(1)
                    .getSingleResult();
            return Optional.ofNullable(hub);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

}
