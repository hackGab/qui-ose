package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import com.lacouf.rsbjwt.repository.*;
import com.lacouf.rsbjwt.security.JwtTokenProvider;
import com.lacouf.rsbjwt.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserAppServiceTest {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserAppService userAppService;
    private UserAppRepository userAppRepository;
    private EtudiantRepository etudiantRepository;
    private ProfesseurRepository professeurRepository;
    private EmployeurRepository employeurRepository;
    private GestionnaireRepository gestionnaireRepository;
    private LoginDTO loginDto;
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        userAppRepository = Mockito.mock(UserAppRepository.class);
        etudiantRepository = Mockito.mock(EtudiantRepository.class);
        professeurRepository = Mockito.mock(ProfesseurRepository.class);
        employeurRepository = Mockito.mock(EmployeurRepository.class);
        gestionnaireRepository = Mockito.mock(GestionnaireRepository.class);
        notificationRepository = Mockito.mock(NotificationRepository.class);
        userAppService = new UserAppService(authenticationManager, jwtTokenProvider, userAppRepository, etudiantRepository, professeurRepository, employeurRepository, gestionnaireRepository, notificationRepository);

        loginDto = new LoginDTO("user@example.com", "password");
    }

    @Test
    void authenticateUser_ShouldReturnToken_WhenValidCredentials() {
        // Arrange
        String expectedToken = "mockJwtToken";
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(jwtTokenProvider.generateToken(mockAuth)).thenReturn(expectedToken);

        // Act
        String actualToken = userAppService.authenticateUser(loginDto);

        // Assert
        assertEquals(expectedToken, actualToken);
    }

    @Test
    void authenticateUser_ShouldThrowException_WhenInvalidCredentials() {
        // Arrange
        loginDto = new LoginDTO("user@example.com", "wrongPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userAppService.authenticateUser(loginDto);
        });

        // Assert
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void getEtudiantDTO_ShouldReturnEtudiantDTO_WhenEtudiantExists() {
        // Arrange
        Long id = 1L;
        Etudiant etudiant = new Etudiant("John", "Doe", "john.doe@example.com", "password", "1234567890", Departement.TECHNIQUES_INFORMATIQUE);
        when(etudiantRepository.findById(id)).thenReturn(Optional.of(etudiant));

        // Act
        EtudiantDTO result = userAppService.getEtudiantDTO(id);

        // Assert
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals( Departement.TECHNIQUES_INFORMATIQUE , result.getDepartement());
        assertEquals("john.doe@example.com", result.getCredentials().getEmail());
    }

    @Test
    void getProfesseurDTO_ShouldReturnProfesseurDTO_WhenProfesseurExists() {
        // Arrange
        Long id = 1L;
        Professeur professeur = new Professeur("Jane", "Smith", "jane.smith@example.com", "password", "0987654321", Departement.TECHNIQUES_INFORMATIQUE);
        when(professeurRepository.findById(id)).thenReturn(Optional.of(professeur));

        // Act
        ProfesseurDTO result = userAppService.getProfesseurDTO(id);

        // Assert
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals(Departement.TECHNIQUES_INFORMATIQUE, result.getDepartement());
        assertEquals("jane.smith@example.com", result.getCredentials().getEmail());
    }

    @Test
    void getEmployeurDTO_ShouldReturnEmployeurDTO_WhenEmployeurExists() {
        // Arrange
        Long id = 1L;
        Employeur employeur = new Employeur("Company", "Bob", "contact@company.com", "0123456789", "Company Inc.", "Company Inc.");
        when(employeurRepository.findById(id)).thenReturn(Optional.of(employeur));

        // Act
        EmployeurDTO result = userAppService.getEmployeurDTO(id);

        // Assert
        assertEquals("Company", result.getFirstName());
        assertEquals("contact@company.com", result.getCredentials().getEmail());
        assertEquals("Company Inc.", result.getEntreprise());
    }

    @Test
    void getGestionnaireDTO_ShouldReturnGestionnaireDTO_WhenGestionnaireExists() {
        // Arrange
        Long id = 1L;
        Gestionnaire gestionnaire = new Gestionnaire("Admin", "bob", "admin@example.com", "0987654321", "1234567");
        when(gestionnaireRepository.findById(id)).thenReturn(Optional.of(gestionnaire));

        // Act
        GestionnaireDTO result = userAppService.getGestionnaireDTO(id);

        // Assert
        assertEquals("Admin", result.getFirstName());
        assertEquals("admin@example.com", result.getCredentials().getEmail());
    }

    @Test
    void getAllDepartementDisplayNames_ShouldReturnAllDisplayNames() {
        // Act
        List<String> result = userAppService.getAllDepartementDisplayNames();

        // Assert
        assertEquals(Arrays.asList(
                "Baccalauréat international en Sciences de la nature Option Sciences de la santé",
                "Cinéma",
                "Gestion de commerces",
                "Gestion des opérations et de la chaîne logistique",
                "Journalisme multimédia",
                "Langues – profil Trilinguisme et cultures",
                "Photographie et design graphique",
                "Sciences de la nature",
                "Sciences humaines – profil Administration et économie",
                "Sciences humaines – profil Individu et relations humaines",
                "Sciences humaines – profil Monde en action",
                "Sciences humaines – profil Sciences humaines avec mathématiques",
                "Soins infirmiers",
                "Soins infirmiers pour auxiliaires",
                "Techniques d'éducation à l'enfance",
                "Techniques de bureautique",
                "Techniques de comptabilité et de gestion",
                "Techniques de l'informatique",
                "Techniques de travail social",
                "Technologie de l'architecture",
                "Technologie de l'estimation et de l'évaluation en bâtiment",
                "Technologie du génie civil",
                "Technologie du génie électrique: automatisation et contrôle",
                "Technologie du génie physique",
                "Tremplin DEC"
        ), result);
    }

    @Test
    void getDepartementByEmail_ShouldReturnMessage_WhenUserRoleHasNoDepartment() {
        // Arrange
        String email = "user@example.com";
        Gestionnaire user = new Gestionnaire();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setCredentials(new Credentials("john@gmail.com", "password", Role.GESTIONNAIRE));
        when(userAppRepository.findUserAppByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<String> result = userAppService.getDepartementByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("This user role does not have a department.", result.get());
    }


    @Test
    void getDepartementByEmail_ShouldReturnDepartement_WhenUserIsProfesseur() {
        // Arrange
        String email = "prof@example.com";
        Professeur professeur = new Professeur();
        professeur.setFirstName("John");
        professeur.setLastName("Doe");
        professeur.setCredentials(new Credentials("john@gmail.com", "password", Role.PROFESSEUR));
        professeur.setDepartement(Departement.TECHNIQUES_INFORMATIQUE);
        when(userAppRepository.findUserAppByEmail(email)).thenReturn(Optional.of(professeur));
        when(professeurRepository.findByEmail(email)).thenReturn(Optional.of(professeur));

        // Act
        Optional<String> result = userAppService.getDepartementByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Techniques de l'informatique", result.get());
    }


    @Test
    void getDepartementByEmail_ShouldReturnDepartement_WhenUserIsEtudiant() {
        // Arrange
        String email = "student@example.com";
        Etudiant user = new Etudiant();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setCredentials(new Credentials("john@gmail.com","password", Role.ETUDIANT));

        user.setDepartement(Departement.TECHNIQUES_INFORMATIQUE);
        when(userAppRepository.findUserAppByEmail(email)).thenReturn(Optional.of(user));
        when(etudiantRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<String> result = userAppService.getDepartementByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Techniques de l'informatique", result.get());
    }
}
