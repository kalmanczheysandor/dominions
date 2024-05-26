package hu.kalmancheysandor.applications.dominion.server.web.repository;

import hu.kalmancheysandor.applications.dominion.server.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByIdentifier(String username);
    public User findById(long userId);
    public List<User> findAll();
    public void deleteById(long userId);
    public boolean existsById(long userId);
    public User save(User user);
}
