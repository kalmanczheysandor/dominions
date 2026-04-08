package hu.kalmancheysandor.applications.dominions.apis.util.configuration;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SiteServer {
    private Account account;
    private User user;
    private GameScenario gameScenario;

    @Data
    @NoArgsConstructor
    public static class Account {

        private SignUp signUp;
        private Recovery recovery;
        private Settings settings;

        @Data
        @NoArgsConstructor
        public static class SignUp {
            @Min(0)
            private int verificationExpirationMinutes;
        }

        @Data
        @NoArgsConstructor
        public static class Recovery {
            @Min(0)
            private int verificationExpirationMinutes;
        }

        @Data
        @NoArgsConstructor
        public static class Settings {
            private Profile profile;
            @Data
            @NoArgsConstructor
            public static class Profile {
                private String photoPath;
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class User {
        private String profilePhotoPath;
    }


    @Data
    @NoArgsConstructor
    public static class GameScenario {
        private String mainImagePath;
    }


    public User getUser() {
        return user;
    }
}
