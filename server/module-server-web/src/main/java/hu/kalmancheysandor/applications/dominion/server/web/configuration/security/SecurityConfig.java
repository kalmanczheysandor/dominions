package hu.kalmancheysandor.applications.dominion.server.web.configuration.security;


import hu.kalmancheysandor.applications.dominion.server.web.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService userDetailsService;
//    private final UserDetailsService userDetailsService;
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.userDetailsService(userDetailsService)
//            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
//                .anyRequest().authenticated())
//            .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
//                .loginPage("/login").permitAll()
//                .defaultSuccessUrl("/index"))
//            .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.permitAll()
//                                                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                                                                                .logoutSuccessUrl("/login"));
//        return http.build();
//    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(request -> request
                .requestMatchers("/").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/font/**").permitAll()
                .requestMatchers("/dashboard").hasRole("PLAYER")
                .requestMatchers("/register").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                //.anyRequest().hasRole("PLAYER")
                .anyRequest().permitAll()
            )
            .formLogin(formlogin ->
                formlogin
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard")
                    .permitAll()

            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }


//    @Bean
//    CustomUserDetailsService customUserDetailsService() {
//        return new CustomUserDetailsService();
//    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/js/**", "/css/**", "/font/**","/image/**");
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}