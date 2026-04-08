package hu.kalmancheysandor.applications.dominions.apis.util.uuid;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UUIDRepository<T, ID> {
    public <S extends T> S findByUuid(String uuid);
    public <S extends T> S save(S user);

    @Query("SELECT CASE WHEN count(u) > 0 THEN true ELSE false END FROM #{#entityName} u WHERE u.uuid = :paramUuid")
    public boolean isUuidReserved(@Param("paramUuid") String uuid);
}
