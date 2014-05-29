package com.roombooking.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.roombooking.utils.JsonDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NamedQueries({
        @NamedQuery(name = "Booking.findAllBookingsByUserId", query =
                "SELECT booking FROM Booking booking WHERE booking.user.id=:userId ORDER BY booking.date DESC, booking.timetable.id ASC"),

        @NamedQuery(name = "Booking.findAllAvailableBookingsByUserId", query =
                "SELECT booking FROM Booking booking " +
                        "WHERE booking.user.id=:userId AND (booking.date>:date OR (booking.date=:date AND booking.timetable.start>=:time)) " +
                        "ORDER BY booking.date DESC, booking.timetable.id ASC"),

        @NamedQuery(name = "Booking.findBookingsByRoomIdAndDate", query =
                "SELECT b FROM Timetable timetable, Booking b " +
                        "WHERE b.timetable.id = timetable.id AND b.room.id =:roomId AND b.date =:date " +
                        "ORDER BY b.date DESC, b.timetable.id ASC"),

        @NamedQuery(name = "Booking.findBookingByRoomIdDateAndTimetableId", query =
                "SELECT b FROM Booking b " +
                        "WHERE b.timetable.id =:timeId AND b.room.id =:roomId AND b.date =:date " +
                        "ORDER BY b.date DESC, b.timetable.id ASC"),

        @NamedQuery(name = "Booking.filterBookingByDate", query =
                "SELECT b FROM Booking b " +
                        "WHERE b.user.id=:userId AND b.date between :dateFrom AND :dateTo ORDER BY b.date"),

})
public class Booking {

    private int id;

    private Date date;

    @JsonView(User.class)
    private User user;

    private Timetable timetable;

    @JsonView(Room.class)
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
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @Column(name = "date", nullable = false, insertable = true, updatable = true)
    public Date getDate() {
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + date.hashCode();
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (timetable != null ? timetable.hashCode() : 0);
        result = 31 * result + (room != null ? room.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (id != booking.id) return false;
        if (!date.equals(booking.date)) return false;
        if (room != null ? !room.equals(booking.room) : booking.room != null) return false;
        if (timetable != null ? !timetable.equals(booking.timetable) : booking.timetable != null) return false;
        if (user != null ? !user.equals(booking.user) : booking.user != null) return false;

        return true;
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
