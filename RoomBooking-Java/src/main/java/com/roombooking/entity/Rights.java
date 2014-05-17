package com.roombooking.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonInclude(Include.NON_EMPTY)
public class Rights {

    private int id;

    private Boolean canBookRoom;

    @JsonView({Rights.class})
    private Position position;

    @JsonView({Rights.class})
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
    @Column(name = "can_book_room", nullable = true, insertable = true, updatable = true)
    public Boolean getCanBookRoom() {
        return canBookRoom;
    }

    public void setCanBookRoom(Boolean canBookRoom) {
        this.canBookRoom = canBookRoom;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + canBookRoom.hashCode();
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (roomType != null ? roomType.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rights rights = (Rights) o;

        if (id != rights.id) return false;
        if (!canBookRoom.equals(rights.canBookRoom)) return false;
        if (position != null ? !position.equals(rights.position) : rights.position != null) return false;
        if (roomType != null ? !roomType.equals(rights.roomType) : rights.roomType != null) return false;

        return true;
    }

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position positionByPositionId) {
        this.position = positionByPositionId;
    }

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomTypeByRoomId) {
        this.roomType = roomTypeByRoomId;
    }

}

