package hu.kalmancheysandor.applications.dominions.utils.uuid;

import hu.kalmancheysandor.applications.dominions.servers.admin.exception.UUIDGenerationException;

import java.util.UUID;


public class UUIDGenerator {


    public <T extends UUIDIdentifiable, ID> T saveWithRetry(UUIDRepository<T, ID> repository, T entity) {
        int maxRetryCount = 3;
        int attemptCount = 1;

        String uuid=null;
        do {

            if(attemptCount>maxRetryCount) {
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
        return repository.save(entity);
    }


//    public <T extends UUIDIdentifiable, ID> T saveWithRetry(JpaRepository<T, ID> repository, T entity) {
//        int maxRetryCount = 3;
//        int retryCount = 0;
//
//        while (true) {
//            try {
//
//                // Generate UUID if not done before
//                if (retryCount == 0 && entity.getUuid() == null) {
//                    UUID uuid = UUID.randomUUID();
//                    entity.setUuid(uuid.toString());
//                }
//                entity.setUuid("429395f4-09b0-4a55-80de-2854d2160221");
//                return repository.save(entity); // Attempt to save
//            } catch (DataIntegrityViolationException e) { // Check if it's a UUID collision
//                Throwable cause = e.getCause();
//                System.out.println("Cause:"+cause);
//                if (!(cause instanceof ConstraintViolationException)) {
//                    throw e;
//                }
//
//                ConstraintViolationException cve = (ConstraintViolationException) cause;
//                String constraintName = cve.getConstraintName();
//                System.out.println("constraintName:"+constraintName);
//                if (!"uuid".equalsIgnoreCase(constraintName)) {
//                    throw e;
//                }
//
//                if (retryCount > maxRetryCount) {
//                    throw new UUIDGenerationException(entity.getClass().getSimpleName(), retryCount);
//                }
//
//                // Generate a new UUID
//                UUID uuid = UUID.randomUUID();
//                entity.setUuid(uuid.toString());
//                retryCount++;
//            }
//        }
//    }

//    public UUID geberateUUID(String uuidValue) {
//        try {
//            System.out.println("UUID convert[before]:"+uuidValue);
//            uuidValue = URLDecoder.decode(uuidValue, StandardCharsets.UTF_8.name());
//            uuidValue = uuidValue.trim();
//
//            System.out.println("UUID convert[after]:"+uuidValue);
//            return UUID.fromString(uuidValue.toUpperCase());
//        } catch (IllegalArgumentException e) {
//            throw new InvalidUuidStringException(uuidValue);
//        } catch (UnsupportedEncodingException e) {
//            throw new InvalidUuidStringException(uuidValue);
//        }
//    }
}