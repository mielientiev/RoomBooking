package com.roombooking.service;

import com.roombooking.dao.timetable.TimetableDao;
import com.roombooking.entity.Timetable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class TimetableService {

    @Autowired
    protected TimetableDao timetableDao;

    public Timetable getTimetable(int id) {
        return timetableDao.findById(id);
    }

    public List<Timetable> getTimetable() {
        return timetableDao.findAllOrderByTime();
    }

    public List<Timetable> getTimetableByRoomIdAndDate(int id, String date) {
        Date convertedDate;
        try {
            convertedDate = Date.valueOf(date);
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return timetableDao.getTimetableByRoomIdAndDate(id, convertedDate);
    }

    public Timetable addNewTimetable(Timetable timetable) {
        if (!isStartAndEndTimeSuitable(timetable.getStart(), timetable.getEnd())) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Timetable newTimetable = new Timetable();
        newTimetable.setFields(timetable);
        timetableDao.save(newTimetable);
        return newTimetable;
    }

    /**
     * Checks if time range between start and end is suitable with existed values in database
     * Example:
     * Timetable DB:
     * id |  start  | end
     * 1   7:45:00    9:30:00
     * 2   9:40:00    12:45:00
     * 3   13:00:00   13:50:00
     * <p/>
     * 1) Start: 7:50:00 End: 8:30:00
     * return false
     * 2) Start: 7:20:00 End: 8:00:00
     * return false
     * 3) Start: 9:31:00 End: 9:39:00
     * return true
     * 4) Start: 13:51:00 End: 15:00:00
     * return true
     *
     * @param start timetable start time
     * @param end   timetable end time
     * @return true if time range is correct
     */
    private boolean isStartAndEndTimeSuitable(Time start, Time end) {
        List<Timetable> searchedTimetables = timetableDao.findByTime(start, end);
        return searchedTimetables.isEmpty();
    }

    public void deleteTimetableById(int id) {
        Timetable searchedTimetable = timetableDao.findById(id);
        if (searchedTimetable == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        timetableDao.deleteById(id);
    }

    public Timetable editTimetable(int id, Timetable timetable) {
        Timetable searchedTimetable = timetableDao.findById(id);
        if (searchedTimetable == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        if (!isStartAndEndTimeSuitable(timetable.getStart(), timetable.getEnd(), searchedTimetable)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        searchedTimetable.setFields(timetable);
        return timetableDao.update(searchedTimetable);
    }

    /**
     * Checks if time range between start and end is suitable with existed values in database
     * Example:
     * Timetable DB:
     * id |  start  | end
     * 1   7:45:00    9:30:00
     * 2   9:40:00    12:45:00
     * 3   13:00:00   13:50:00
     * <p/>
     * 1) Start: 7:50:00 End: 8:30:00
     * return false
     * 2) Start: 7:20:00 End: 8:00:00
     * return false
     * 3) Start: 9:31:00 End: 9:39:00
     * return true
     * 4) Start: 13:51:00 End: 15:00:00
     * return true
     *
     * @param start             timetable start time
     * @param end               timetable end time
     * @param excludedTimetable
     * @return true if time range is correct
     */
    private boolean isStartAndEndTimeSuitable(Time start, Time end, Timetable excludedTimetable) {
        List<Timetable> searchedTimetables = timetableDao.findByTime(start, end);
        searchedTimetables.remove(excludedTimetable);
        return searchedTimetables.isEmpty();
    }

}
