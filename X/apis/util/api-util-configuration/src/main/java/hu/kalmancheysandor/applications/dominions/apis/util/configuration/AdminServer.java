package hu.kalmancheysandor.applications.dominions.apis.util.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminServer {
    private Account account;
    private GameScenario gameScenario;

    @Data
    @NoArgsConstructor
    public static class Account {
        private Site site;
        private Admin admin;

        @Data
        @NoArgsConstructor
        public static class Site {
            private User user;

            @Data
            @NoArgsConstructor
            public static class User {
                private String photoPath;
            }
        }

        @Data
        @NoArgsConstructor
        public static class Admin {
            private User user;
            private Settings settings;

            @Data
            @NoArgsConstructor
            public static class User {
                private String photoPath;
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
    }


    @Data
    @NoArgsConstructor
    public static class GameScenario {
        private String mainImagePath;
    }
}
