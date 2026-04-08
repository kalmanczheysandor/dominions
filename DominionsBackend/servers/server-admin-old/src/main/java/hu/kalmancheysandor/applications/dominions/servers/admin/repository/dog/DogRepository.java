package hu.kalmancheysandor.applications.dominions.servers.admin.repository.dog;


import hu.kalmancheysandor.applications.dominions.servers.admin.entity.dog.Dog;
import hu.kalmancheysandor.applications.dominions.servers.admin.utils.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogRepository extends JpaRepository<Dog, Integer>, UUIDRepository<Dog,Integer> {
    public Dog findById(int userId);
    public Dog findByUuid(String uuid);
    public List<Dog> findAll();
    public void deleteById(int userId);
    public boolean existsById(int userId);
    public Dog save(Dog user);

    @Query("SELECT CASE WHEN count(d) > 0 THEN true ELSE false END FROM Dog d WHERE d.prn = :paramPrn")
    public boolean isPrnReserved(@Param("paramPrn") String prn);

    @Query("SELECT CASE WHEN count(d) > 0 THEN true ELSE false END FROM Dog d WHERE d.prn = :paramPrn and d.id<> :paramExcludedId")
    public boolean isPrnReserved(@Param("paramPrn") String prn, @Param("paramExcludedId")  int excludedId);


    @Query("SELECT d.enabled FROM Dog d WHERE d.id=:paramId")
    boolean isEnabled(@Param("paramId") int id);



    @Query("SELECT FALSE")
    boolean isReferencedElsewhere(@Param("paramDogId") int dogId);


}
