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
        @NamedQuery(name = "Room.findAllRoomsWithUserPositionRights", query =
                "SELECT room FROM  Room as room, User u \n" +
                        "INNER JOIN fetch room.roomType as rtype \n" +
                        "INNER JOIN fetch rtype.rights as rights \n" +
                        "WHERE rights.position.id = u.position.id AND u.id =:usid"),

        @NamedQuery(name = "Room.findRoomByIdWithUserPositionRights", query =
                "SELECT room FROM  Room as room, User u \n" +
                        "INNER JOIN fetch room.roomType as rtype \n" +
                        "INNER JOIN fetch rtype.rights as rights \n" +
                        "WHERE room.id=:roomId AND rights.position.id = u.position.id AND u.id =:usid"),

        @NamedQuery(name = "Room.filterRooms", query =
                "SELECT r FROM Room r " +
                        "WHERE r.places>=:places AND r.computers>=:comp AND r.projector>=:proj AND r.board>=:board " +
                        "AND r.roomType.id =:typeid AND r.roomName LIKE :roomName")
})

public class Room {

    private int id;

    private String roomName;

    private int floor;

    private int places;

    private Integer computers;

    private Boolean board;

    private Boolean projector;

    private List<Booking> bookings;

    private RoomType roomType;

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
    public int hashCode() {
        int result = id;
        result = 31 * result + roomName.hashCode();
        result = 31 * result + floor;
        result = 31 * result + places;
        result = 31 * result + computers.hashCode();
        result = 31 * result + board.hashCode();
        result = 31 * result + projector.hashCode();
        result = 31 * result + (roomType != null ? roomType.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (floor != room.floor) return false;
        if (id != room.id) return false;
        if (places != room.places) return false;
        if (!board.equals(room.board)) return false;
        if (!computers.equals(room.computers)) return false;
        if (!projector.equals(room.projector)) return false;
        if (!roomName.equals(room.roomName)) return false;
        if (roomType != null ? !roomType.equals(room.roomType) : room.roomType != null) return false;

        return true;
    }

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookingsById) {
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
