package com.roombooking.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.List;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class RoomType {

    private int id;
    private String roomType;
    private List<Rights> rights;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomType that = (RoomType) o;

        if (id != that.id) return false;
        if (roomType != null ? !roomType.equals(that.roomType) : that.roomType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (roomType != null ? roomType.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "roomType")
    public List<Rights> getRights() {
        return rights;
    }

    public void setRights(List<Rights> rightsById) {
        this.rights = rightsById;
    }

    @OneToMany(mappedBy = "roomType")
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> roomsById) {
        this.rooms = roomsById;
    }

}
