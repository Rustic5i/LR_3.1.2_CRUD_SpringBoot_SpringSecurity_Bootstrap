package com.baratov.spring.springboot.model;

import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId(mutable = true)
    @Column
    private String authority;


    @ManyToMany(mappedBy = "roles") //Если ассоциация двунаправленная, одна сторона должна
    // быть владельцем, а другая - обратным концом (т. Е. Она будет
    // проигнорирована при обновлении значений взаимосвязи в таблице ассоциаций):
    // Итак, сторона, имеющая mappedByатрибут, является обратной стороной. Сторона,
    // у которой нет mappedByатрибута, является владельцем.
    private Set<User> user = new HashSet<>();

    public Role() {
    }


    public Role(String authority) {
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public Set<User> getUser() {
        return user;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() { //получить Полномочия
        return authority;
    }

    @Override
    public String toString() {
        return authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(authority, role.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority);
    }
}

