package hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.repository.training;


import hu.kalmancheysandor.applications.dominions.apis.server.ai.liz.shared.entity.training.LizCharacter;
import hu.kalmancheysandor.applications.dominions.apis.util.uuid.UUIDRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LizCharacterRepository extends JpaRepository<LizCharacter, String>, UUIDRepository<LizCharacter, String> {

    public LizCharacter findById(int lizCharacterId);

    public LizCharacter findByUuid(String lizCharacterUuid);

    public List<LizCharacter> findAll();

    public void deleteById(int lizCharacterId);

    public void deleteByUuid(String lizCharacterUuid);

    public boolean existsById(int lizCharacterId);

    public boolean existsByUuid(String lizCharacterUuid);

    public LizCharacter save(LizCharacter lizCharacter);

    @Query("SELECT t FROM LizCharacter t WHERE t.enabled=true")
    List<LizCharacter> listAllEnabled();

    @Query("SELECT t FROM LizCharacter t")
    List<LizCharacter> listAll();


    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM LizCharacter t WHERE t.name = :name")
    public boolean isNameReserved(@Param("name") String title);

    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM LizCharacter t WHERE t.name = :name and t.id<> :excludedId")
    public boolean isNameReserved(@Param("name") String title, @Param("excludedId") int excludedId);


    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM LizCharacter t WHERE t.code = :code")
    public boolean isCodeReserved(@Param("code") String title);

    @Query("SELECT CASE WHEN count(t) > 0 THEN true ELSE false END FROM LizCharacter t WHERE t.code = :code and t.id<> :excludedId")
    public boolean isCodeReserved(@Param("code") String title, @Param("excludedId") int excludedId);


    @Query("SELECT t.enabled FROM LizCharacter t WHERE t.id=:lizCharacterId")
    boolean isEnabled(@Param("lizCharacterId") int lizCharacterId);

    @Query("SELECT FALSE")
    boolean isReferencedElsewhere(@Param("lizCharacterId") int lizCharacterId);
}


