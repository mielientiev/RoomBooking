package com.roombooking.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.roombooking.utils.JsonTimeDeserializer;
import com.roombooking.utils.JsonTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonInclude(Include.NON_EMPTY)
@NamedQueries({
        @NamedQuery(name = "Timetable.findTimetableByRoomIdAndDate", query =
                "SELECT timetable FROM Timetable timetable, Booking b " +
                        "WHERE b.timetable.id = timetable.id AND b.date =:date AND b.room.id=:roomId"),

        @NamedQuery(name = "Timetable.findTimetableByTime", query =
                "SELECT timetable FROM Timetable timetable " +
                        "WHERE (:start BETWEEN timetable.start AND timetable.end) " +
                        "OR (:end BETWEEN timetable.start AND timetable.end)")
})
public class Timetable {

    private int id;

    private Time start;

    private Time end;

    private List<Booking> bookings;

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
    @JsonSerialize(using = JsonTimeSerializer.class)
    @JsonDeserialize(using = JsonTimeDeserializer.class)
    @Column(name = "start", nullable = false, insertable = true, updatable = true)
    public Time getStart() {
        return start;
    }

    public void setStart(Time start) {
        this.start = start;
    }

    @Basic
    @JsonSerialize(using = JsonTimeSerializer.class)
    @JsonDeserialize(using = JsonTimeDeserializer.class)
    @Column(name = "end", nullable = false, insertable = true, updatable = true)
    public Time getEnd() {
        return end;
    }

    public void setEnd(Time end) {
        this.end = end;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timetable that = (Timetable) o;

        if (id != that.id) return false;
        if (end != null ? !end.equals(that.end) : that.end != null) return false;
        if (start != null ? !start.equals(that.start) : that.start != null) return false;

        return true;
    }

    @OneToMany(mappedBy = "timetable", cascade = CascadeType.ALL)
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookingsById) {
        this.bookings = bookingsById;
    }

    public void setFields(Timetable timetable) {
        this.start = timetable.getStart();
        this.end = timetable.getEnd();
    }

}
