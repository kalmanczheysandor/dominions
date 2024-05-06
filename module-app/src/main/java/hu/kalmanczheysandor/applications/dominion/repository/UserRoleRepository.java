package hu.kalmanczheysandor.applications.dominion.repository;

import hu.kalmanczheysandor.applications.dominion.entity.User;
import hu.kalmanczheysandor.applications.dominion.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    public UserRole findById(long id);
    public List<UserRole> findAll();
    public void deleteById(long id);
    public boolean existsById(long id);
    public UserRole save(UserRole userRole);

    public void deleteAllByUserId(long userId);
    public List<UserRole> findAllByUserId(long userId);
}
