package hu.kalmancheysandor.applications.dominions.apis.util.uuid;

import java.util.UUID;


public class UUIDGenerator {


    public <T extends UUIDIdentifiable, ID> T saveWithRetry(UUIDRepository<T, ID> repository, T entity) {
        int maxRetryCount = 3;
        int attemptCount = 1;

        String uuid=null;
        if(entity.getUuid()==null) {
            do {

                if (attemptCount > maxRetryCount) {
                    throw new UUIDGenerationException(entity.getClass().getSimpleName(), attemptCount);
                }

                // Attempt
                uuid = (UUID.randomUUID()).toString();
                if (!repository.isUuidReserved(uuid)) {
                    break;
                }

                attemptCount++;
            }
            while (true);
            entity.setUuid(uuid);
        }
        return repository.save(entity);
    }

}