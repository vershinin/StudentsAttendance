package ee.ttu.vk.sa.repository;

import ee.ttu.vk.sa.domain.Group;
import ee.ttu.vk.sa.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by fjodor on 6.02.16.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByName(String name);
}
