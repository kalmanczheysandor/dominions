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
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

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

//    @Bean
//    fun messageAuthorizationManager(messages: MessageMatcherDelegatingAuthorizationManager.Builder): AuthorizationManager<Message<*>> {
//        messages.nullDestMatcher().authenticated()
//            .simpSubscribeDestMatchers("/app/notifications").permitAll()
//            .simpSubscribeDestMatchers("/queue/tasks/**").hasAuthority("SCOPE_tasks.read")
//            .simpDestMatchers("/app/**").hasRole("ADMIN")
//        return messages.build()
//    }



    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//            .anyRequest().permitAll() // Allow all requests without authentication
//            .and()
//            .csrf().disable(); // Disable CSRF protection
//


//        http.cors(cors -> cors.configurationSource(request -> {
//            CorsConfiguration configuration = new CorsConfiguration();
//            configuration.setAllowedOrigins(Arrays.asList("*"));
//            configuration.setAllowedMethods(Arrays.asList("*"));
//            configuration.setAllowedHeaders(Arrays.asList("*"));
//            return configuration;
//        }));


        http.userDetailsService(userDetailsService)
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("*"));
                configuration.setAllowedMethods(Arrays.asList("*"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                return configuration;
            }))
            .authorizeHttpRequests(request -> request
                .requestMatchers("/").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/font/**").permitAll()
                .requestMatchers("/dashboard").hasRole("PLAYER")
                .requestMatchers("/register").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                //.anyRequest().hasRole("PLAYER")
                .anyRequest().permitAll()
            )
            .formLogin(formlogin -> formlogin.loginPage("/login").defaultSuccessUrl("/dashboard").permitAll()
            )
            .logout(logout -> logout.logoutUrl("/logout").permitAll()).httpBasic(Customizer.withDefaults())
            .csrf(httpSecurityCsrfConfigurer ->httpSecurityCsrfConfigurer.disable())
        ;
        return http.build();
    }


//    @Bean
//    CustomUserDetailsService customUserDetailsService() {
//        return new CustomUserDetailsService();
//    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers("/js/**", "/css/**", "/font/**", "/image/**", "/ws/**");
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}