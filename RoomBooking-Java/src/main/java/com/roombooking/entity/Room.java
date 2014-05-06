package com.roombooking.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Collection;


@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Room {

    private int id;
    private String roomName;
    private int floor;
    private int places;
    private Integer computers;
    private Boolean board;
    private Boolean projector;
    private Collection<Booking> bookings;
    private RoomType roomType;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "room_name", nullable = false, insertable = true, updatable = true, length = 15)
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Basic
    @Column(name = "floor", nullable = false, insertable = true, updatable = true)
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Basic
    @Column(name = "places", nullable = false, insertable = true, updatable = true)
    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    @Basic
    @Column(name = "computers", nullable = true, insertable = true, updatable = true)
    public Integer getComputers() {
        return computers;
    }

    public void setComputers(Integer computers) {
        this.computers = computers;
    }

    @Basic
    @Column(name = "board", nullable = true, insertable = true, updatable = true)
    public Boolean getBoard() {
        return board;
    }

    public void setBoard(Boolean board) {
        this.board = board;
    }

    @Basic
    @Column(name = "projector", nullable = true, insertable = true, updatable = true)
    public Boolean getProjector() {
        return projector;
    }

    public void setProjector(Boolean projector) {
        this.projector = projector;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room that = (Room) o;

        if (floor != that.floor) return false;
        if (id != that.id) return false;
        if (places != that.places) return false;
        if (board != null ? !board.equals(that.board) : that.board != null) return false;
        if (computers != null ? !computers.equals(that.computers) : that.computers != null) return false;
        if (projector != null ? !projector.equals(that.projector) : that.projector != null) return false;
        if (roomName != null ? !roomName.equals(that.roomName) : that.roomName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (roomName != null ? roomName.hashCode() : 0);
        result = 31 * result + floor;
        result = 31 * result + places;
        result = 31 * result + (computers != null ? computers.hashCode() : 0);
        result = 31 * result + (board != null ? board.hashCode() : 0);
        result = 31 * result + (projector != null ? projector.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "room")
    public Collection<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Collection<Booking> bookingsById) {
        this.bookings = bookingsById;
    }

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "id", nullable = false)
    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomTypeByType) {
        this.roomType = roomTypeByType;
    }

}
