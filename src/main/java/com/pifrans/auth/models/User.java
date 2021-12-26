package com.pifrans.auth.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
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

    @Email(message = "E-mail inválido!")
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

    @NotAudited
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_profile", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_profile"), catalog = "auth", schema = "auth")
    private Set<Profile> profiles;
}
