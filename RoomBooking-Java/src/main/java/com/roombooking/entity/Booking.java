package com.roombooking.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.roombooking.utils.CustomJsonDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NamedQueries({
        @NamedQuery(name = "Booking.findAllBookingByUserId",query =
                "SELECT booking FROM Booking booking WHERE booking.user.id=:userId"),

        @NamedQuery(name = "Booking.findAllAvailableBookingByUserId", query =
                "SELECT booking FROM Booking booking WHERE booking.user.id=:userId AND booking.date>=:date"),

        @NamedQuery(name = "Booking.findBookingByRoomIdAndDate", query =
                "SELECT b FROM Timetable timetable, Booking b, Room r " +
                "WHERE b.timetable.id = timetable.id AND b.room.id = r.id AND b.date =:date " +
                "AND r.id =:roomId order by timetable.id"),

        @NamedQuery(name = "Booking.findBookingByRoomIdDateAndTimetableId", query =
                "SELECT b FROM Timetable timetable, Booking b, Room r " +
                        "WHERE b.timetable.id = timetable.id AND timetable.id=:timeId " +
                        "AND b.room.id = r.id AND b.date =:date " +
                        "AND r.id =:roomId order by timetable.id")




})
public class Booking {

    private int id;
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date date;
    private User user;
    private Timetable timetable;
    private Room room;

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
    @Column(name = "date", nullable = false, insertable = true, updatable = true)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking that = (Booking) o;

        if (id != that.id) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + user.hashCode();
        result = 31 * result + room.hashCode();
        result = 31 * result + timetable.hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "timetable_id", referencedColumnName = "id")
    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetableByTimetableId) {
        this.timetable = timetableByTimetableId;
    }

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room roomByRoomId) {
        this.room = roomByRoomId;
    }

}
