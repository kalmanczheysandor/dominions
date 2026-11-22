package hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.repository.training;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.otto.shared.entity.training.OttoCharacter;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OttoCharacterRepository extends JpaRepository<OttoCharacter, String>, UUIDRepository<OttoCharacter, String> {

    public OttoCharacter findById(int characterId);

    public OttoCharacter findByUuid(String characterUuid);

    public OttoCharacter findByCode(String code);

    public List<OttoCharacter> findAll();

    public void deleteById(int characterId);

    public void deleteByUuid(String characterUuid);

    public boolean existsById(int characterId);

    public boolean existsByUuid(String characterUuid);

    public OttoCharacter save(OttoCharacter character);

    @Query("SELECT t FROM OttoCharacter t WHERE t.enabled=true")
    List<OttoCharacter> listAllEnabled();

    @Query("SELECT t FROM OttoCharacter t")
    List<OttoCharacter> listAll();


    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM OttoCharacter t WHERE t.name = :name")
    public boolean isNameReserved(@Param("name") String title);

    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM OttoCharacter t WHERE t.name = :name and t.id<> :excludedId")
    public boolean isNameReserved(@Param("name") String title, @Param("excludedId") int excludedId);


    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM OttoCharacter t WHERE t.code = :code")
    public boolean isCodeReserved(@Param("code") String title);

    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM OttoCharacter t WHERE t.code = :code and t.id<> :excludedId")
    public boolean isCodeReserved(@Param("code") String title, @Param("excludedId") int excludedId);


    @Query("SELECT t.enabled FROM OttoCharacter t WHERE t.id=:characterId")
    boolean isEnabled(@Param("characterId") int characterId);

    @Query("SELECT FALSE")
    boolean isReferencedElsewhere(@Param("characterId") int characterId);
}


