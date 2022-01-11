package com.pifrans.auth.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pifrans.auth.exceptions.errors.PropertyValueException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
@AuditTable(value = "aud_users", catalog = "audit", schema = "audit")
@Entity
@Table(name = "users", catalog = "auth", schema = "auth")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "current_access")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date currentAccess;

    @Column(name = "last_access")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date lastAccess;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    private String token;

    @NotAudited
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_profile", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_profile"), catalog = "auth", schema = "auth")
    private Set<Profile> profiles;

    public void setEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if (matcher.find()) {
            this.email = email;
        } else {
            String message = String.format("E-mail inválido (%s)!", email);
            throw new PropertyValueException(message);
        }
    }

    public void setPassword(String password) {
        String regex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()--+={}\\[\\]|\\\\:;\"'<>,.?/_]).{8,255}";
        if (password.matches(regex)) {
            this.password = new BCryptPasswordEncoder().encode(password);
        } else {
            String message = String.format("Senha inválida (%s), a senha deve ter de 8 a 255 caracteres com letras maiúsculas, minusculas, números e caracteres especiais!", password);
            throw new PropertyValueException(message);
        }
    }
}
