package red.tetracube.hubcentral.database.repositories;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import red.tetracube.hubcentral.database.entities.HubEntity;

@ApplicationScoped
public class HubRepository {

    @Inject
    EntityManager em;

    @Transactional
    public Optional<HubEntity> getHubByName(String name) {
        try {
            var hub = em.<HubEntity>createQuery("""
                    from HubEntity h
                    where h.name = :name
                    """,
                    HubEntity.class)
                    .setParameter("name", name)
                    .setMaxResults(1)
                    .getSingleResult();
            return Optional.ofNullable(hub);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<HubEntity> getHubBySlug(String slug) {
        try {
            var hub = em.<HubEntity>createQuery("""
                    from HubEntity h
                    where h.slug = :slug
                    """,
                    HubEntity.class)
                    .setParameter("slug", slug)
                    .setMaxResults(1)
                    .getSingleResult();
            return Optional.ofNullable(hub);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

}
