package rekru.sii.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rekru.sii.gym.model.Gym;

import java.util.UUID;

public interface GymRepository extends JpaRepository<Gym, UUID> {
}
