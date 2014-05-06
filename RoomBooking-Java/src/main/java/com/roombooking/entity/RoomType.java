package com.roombooking.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Collection;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RoomType {

    private int id;
    private String roomType;
    private Collection<Right> rights;
    private Collection<Room> rooms;

    @Id
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
    public Collection<Right> getRights() {
        return rights;
    }

    public void setRights(Collection<Right> rightsById) {
        this.rights = rightsById;
    }

    @OneToMany(mappedBy = "roomType")
    public Collection<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Collection<Room> roomsById) {
        this.rooms = roomsById;
    }

}
