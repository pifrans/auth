package com.pifrans.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

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

    @NotEmpty(message = "Campo obrigatório!")
    @Email(message = "E-mail inválido!")
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @NotAudited
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_profile", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_profile"), catalog = "auth", schema = "auth")
    private List<Profile> profiles;
}
