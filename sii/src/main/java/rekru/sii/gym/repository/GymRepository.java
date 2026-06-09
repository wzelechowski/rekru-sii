package rekru.sii.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rekru.sii.gym.model.Gym;

import java.util.UUID;

@Repository
public interface GymRepository extends JpaRepository<Gym, UUID> {
}
