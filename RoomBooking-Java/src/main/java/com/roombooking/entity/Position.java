package com.roombooking.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonInclude(Include.NON_EMPTY)
@NamedQueries({
        @NamedQuery(name = "Position.findPositionByTitle", query =
                "SELECT pos FROM Position pos WHERE pos.title=:title")
})
public class Position {

    private int id;

    private String title;

    private List<Rights> rights;

    private List<User> users;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = false, insertable = true, updatable = true, length = 45)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (id != position.id) return false;
        if (!title.equals(position.title)) return false;

        return true;
    }

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    public List<Rights> getRights() {
        return rights;
    }

    public void setRights(List<Rights> rightsById) {
        this.rights = rightsById;
    }

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> usersById) {
        this.users = usersById;
    }

    public void setFields(Position position) {
        this.title = position.title;
    }
}
