package com.roombooking.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoomType {

    private int id;

    private String roomType;

    private Set<Rights> rights;

    private List<Room> rooms;

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
    @Column(name = "room_type", nullable = false, insertable = true, updatable = true, length = 50)
    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (roomType != null ? roomType.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomType that = (RoomType) o;

        if (id != that.id) return false;
        if (roomType != null ? !roomType.equals(that.roomType) : that.roomType != null) return false;

        return true;
    }

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
    public Set<Rights> getRights() {
        return rights;
    }

    public void setRights(Set<Rights> rightsById) {
        this.rights = rightsById;
    }

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> roomsById) {
        this.rooms = roomsById;
    }

}
