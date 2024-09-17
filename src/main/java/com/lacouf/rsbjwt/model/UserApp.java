package com.lacouf.rsbjwt.model;

import com.lacouf.rsbjwt.model.auth.Credentials;
import com.lacouf.rsbjwt.model.auth.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public abstract class UserApp  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;

    private String lastName;

    private String password;

    private String passwordConfirm;

    private String email;

    private String phoneNumber;

    private String role;

    @Embedded
    private Credentials credentials;

    public UserApp(String firstName, String lastName, String email, String password, String phoneNumber, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public String getEmail(){
        return credentials.getEmail();
    }

    public String getPassword(){
        return credentials.getPassword();
    }

    public Role getRole(){
        return credentials.getRole();
    }


    public Collection<? extends GrantedAuthority> getAuthorities(){
        return credentials.getAuthorities();
    }
}
