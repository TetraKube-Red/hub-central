package red.tetracube.hubcentral.database.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import red.tetracube.hubcentral.database.entities.HubEntity;
import red.tetracube.hubcentral.database.entities.RoomEntity;

@ApplicationScoped
public class RoomRepository {

    @Inject
    EntityManager em;

    public List<RoomEntity> getByHubId(UUID hubId) {
        var rooms = em.<RoomEntity>createQuery("""
                from RoomEntity r
                where r.hub.id = :id
                """,
                RoomEntity.class)
                .setParameter("id", hubId)
                .getResultList();
        return rooms;
    }

}
