package com.lacouf.rsbjwt.security;

import com.lacouf.rsbjwt.repository.UserAppRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor

public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAppRepository userRepository;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                        .disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/departements/**").permitAll()
                        .requestMatchers(POST, "/user/login").permitAll()
                        .requestMatchers(GET, "/user/me").permitAll()
                        .requestMatchers(GET, "/user/departements").permitAll()
                        .requestMatchers(POST, "/etudiant/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/cv/creerCV/**").permitAll()
                        .requestMatchers(POST, "/entrevues/creerEntrevue/**").permitAll()
                        .requestMatchers(GET, "/entrevues/etudiantsAvecEntrevue").permitAll()
                        .requestMatchers(GET, "/etudiant/{email}/entrevues").permitAll()
                        .requestMatchers(GET, "/entrevues/**").permitAll()
                        .requestMatchers(POST, "/generatePDF/contrat").permitAll()
                        .requestMatchers(POST, "/contrat/creerContrat").permitAll()
                        .requestMatchers(GET, "/contrat/all").permitAll()
                        .requestMatchers(GET, "/contrat/getContrats-employeur/{employeurEmail}/session/{session}").permitAll()
                        .requestMatchers(GET, "/contrat/getContrats-etudiant/{etudiantEmail}/session/{session}").permitAll()
                        .requestMatchers(PUT, "/contrat/signer-employeur/{uuid}").permitAll()
                        .requestMatchers(PUT, "/contrat/signer-etudiant/{uuid}").permitAll()
                        .requestMatchers(PUT, "/contrat/signer-gestionnaire/{uuid}/{email}").permitAll()
                        .requestMatchers(PUT, "/entrevues/changerStatus/{emailEtudiant}/{idOffreDeStage}").permitAll()
                        .requestMatchers(DELETE, "/cv/supprimerCV/**").permitAll()
                        .requestMatchers(GET, "/cv/attentes").permitAll()
                        .requestMatchers(DELETE, "/entrevues/retirerOffre/{offreId}").permitAll()
                        .requestMatchers("/professeur/**").permitAll()
                        .requestMatchers(GET, "/professeur/all").permitAll()
                        .requestMatchers(GET, "/professeur/{professeurId}").permitAll()
                        .requestMatchers(POST, "/professeur/assignerEtudiants/{professeurEmail}").permitAll()
                        .requestMatchers(PUT, "/deassignerEtudiants/{professeurEmail}").permitAll()
                        .requestMatchers(POST, "/etudiant/creerEtudiant").permitAll()
                        .requestMatchers(GET, "/etudiant/all").permitAll()
                        .requestMatchers(POST, "/etudiant/{etudiantId}/offre/{offreId}").permitAll()
                        .requestMatchers(GET, "/etudiant/{etudiantEmail}/offres").permitAll()
                        .requestMatchers(GET, "/etudiant/departement/{departement}").permitAll()
                        .requestMatchers(GET, "/etudiant/credentials/{email}").permitAll()
                        .requestMatchers(GET, "/etudiant/{etudiantEmail}/notifications").permitAll()
                        .requestMatchers(PUT, "/etudiant/{email}/retirerOffre/{offreId}").permitAll()
                        .requestMatchers(POST, "/professeur/creerProfesseur").permitAll()
                        .requestMatchers(GET, "/professeur/etudiants/{professeurEmail}").permitAll()
                        .requestMatchers(POST, "/employeur/creerEmployeur").permitAll()
                        .requestMatchers(POST, "/employeur/creerEvaluationEtudiant/{emailEmployeur}/{emailEtudiant}").permitAll()
                        .requestMatchers(GET, "/employeur/evaluationEmployeur/all").permitAll()
                        .requestMatchers(GET, "/employeur/evaluationEmployeur/{emailEmployeur}/{emailEtudiant}").permitAll()
                        .requestMatchers(POST, "/offreDeStage/creerOffreDeStage/**").permitAll()
                        .requestMatchers(GET, "/offreDeStage/offresEmployeur/**").permitAll()
                        .requestMatchers(GET, "/offreDeStage/offresEmployeur/{email}/session/{session}").permitAll()
                        .requestMatchers(GET, "/offreDeStage/all").permitAll()
                        .requestMatchers(GET, "/offreDeStage/{offreId}").permitAll()
                        .requestMatchers(DELETE, "/offreDeStage/**").permitAll()
                        .requestMatchers(PUT, "/offreDeStage/**").permitAll()
                        .requestMatchers(GET, "/offreDeStage/{offreId}").permitAll()
                        .requestMatchers(GET, "/offreDeStage/offresValidees/session/{session}").permitAll()
                        .requestMatchers(PUT, "/gestionnaire/validerOuRejeterCV/**").permitAll()
                        .requestMatchers(PUT, "/gestionnaire/validerOuRejeterOffre/**").permitAll()
                        .requestMatchers(GET, "/offreDeStage/{offreId}/etudiants").permitAll()
                        .requestMatchers(GET, "/entrevues/offre/{offreId}").permitAll()
                        .requestMatchers(GET, "/entrevues/etudiant/{email}/session/{session}").permitAll()
                        .requestMatchers(GET, "/entrevues/enAttente/etudiant/{email}").permitAll()
                        .requestMatchers(GET, "/entrevues/entrevueAcceptee/offre/{offreId}").permitAll()
                        .requestMatchers(PUT, "/candidatures/accepter/{entrevueId}").permitAll()
                        .requestMatchers(PUT, "/candidatures/refuser/{entrevueId}").permitAll()
                        .requestMatchers(GET, "/candidatures/{entrevueId}").permitAll()
                        .requestMatchers(GET, "/candidatures/all").permitAll()
                        .requestMatchers(GET, "/user/etudiants/departement/{departement}").permitAll()
                        .requestMatchers(PUT, "/gestionnaire/etudiants/deassignerProfesseur/{email}").permitAll()
                        .requestMatchers(GET, "/professeur/etudiants/departement/{departement}").permitAll()
                        .requestMatchers(GET, "/professeur/evaluations/{professeurEmail}").permitAll()
                        .requestMatchers(PUT, "/professeur/evaluerStage").permitAll()
                        .requestMatchers(GET, "/notification/allUnread/{email}").permitAll()
                        .requestMatchers(PUT, "/notification/markAsRead/{id}").permitAll()
                        .requestMatchers(POST, "/generatePDF/evaluationProf").permitAll()
                        .requestMatchers(POST, "/generatePDF/evaluationEmployeur").permitAll()
                        .requestMatchers(GET, "/offreDeStage/session/{session}").permitAll()
                        .requestMatchers(GET, "/offreDeStage/annee/{annee}").permitAll()
                        .requestMatchers(GET, "/candidatures/session/{session}").permitAll()
                        .requestMatchers(GET, "/contrat/session/{session}").permitAll()
                        .requestMatchers(GET, "entrevues/acceptees/employeur/{email}/session/{session}").permitAll()
                        .requestMatchers("/gestionnaire/**").hasAuthority("GESTIONNAIRE")
                        .requestMatchers("/employeur/**").hasAuthority("EMPLOYEUR")
                        .anyRequest().denyAll()
                )
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                .sessionManagement(secuManagement -> {
                    secuManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(jwtTokenProvider, userRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
