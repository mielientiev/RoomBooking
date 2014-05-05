package com.roombooking.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@id2")
public class Booking {

    private int id;
    private Date date;
    private User user;
    private Timetable timetable;
    private Room room;

    @Id
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
