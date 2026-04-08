package hu.kalmancheysandor.applications.dominions.servers.admin.repository.breed;


import hu.kalmancheysandor.applications.dominions.servers.admin.entity.breed.Breed;
import hu.kalmancheysandor.applications.dominions.servers.admin.utils.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Integer>, UUIDRepository<Breed, Integer> {
    public Breed findById(int breedId);

    public Breed findByUuid(String uuid);

    public List<Breed> findAll();

    public List<Breed> findByEnabled(boolean enabled);

    public void deleteById(int breedId);

    public boolean existsById(int breedId);

    public Breed save(Breed breed);

    @Query("SELECT CASE WHEN count(b) > 0 THEN true ELSE false END FROM Breed b WHERE b.name = :paramName")
    public boolean isNameReserved(@Param("paramName") String name);

    @Query("SELECT CASE WHEN count(b) > 0 THEN true ELSE false END FROM Breed b WHERE b.name = :paramName and b.id<> :paramExcludedId")
    public boolean isNameReserved(@Param("paramName") String name, @Param("paramExcludedId") int excludedId);



    @Query("SELECT b.enabled FROM Breed b WHERE b.id=:paramId")
    boolean isEnabled(@Param("paramId") int id);

    @Query("SELECT b FROM Breed b WHERE b.enabled=true")
    List<Breed> listAllEnabled();

    @Query("SELECT b FROM Breed b WHERE b.id = :paramExcludedId OR b.enabled=true")
    List<Breed> listAllEnabledWithOneExcludedId(@Param("paramExcludedId") int excludedId);

    @Query("SELECT b FROM Breed b WHERE b.id IN :excludedIdList OR b.enabled=true")
    List<Breed> listAllEnabledWithMultipleExcludedId(@Param("excludedIdList") List<Integer> excludedIdList);

    @Query("SELECT b FROM Breed b WHERE b.uuid = :paramExcludedUuid OR b.enabled=true")
    List<Breed> listAllEnabledWithOneExcludedUuid(@Param("paramExcludedUuid") String excludedUuid);

    @Query("SELECT b FROM Breed b WHERE b.id IN :excludedUuidList OR b.enabled=true")
    List<Breed> listAllEnabledWithMultipleExcludedUuid(@Param("excludedUuidList") List<String> excludedUuidList);

    @Query("SELECT CASE WHEN " +
        "EXISTS (SELECT 1 FROM Dog d WHERE d.breed.id = :paramBreedId) " +
        "THEN TRUE ELSE FALSE END")
    boolean isReferencedElsewhere(@Param("paramBreedId") int breedId);
}
