package lk.kdu.growed.backend.repository;

import lk.kdu.growed.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByIndexNumber(String indexNumber);
    Optional<User> findByIndexNumber(String indexNumber); // Keep only this line
}
