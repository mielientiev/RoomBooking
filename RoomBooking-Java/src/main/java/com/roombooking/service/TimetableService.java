package com.roombooking.service;

import com.roombooking.dao.timetable.TimetableDao;
import com.roombooking.entity.Timetable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.List;

public class TimetableService {

    @Autowired
    private TimetableDao timetableDao;

    public Timetable getTimetable(int id) {
        return timetableDao.findById(id);
    }

    public List<Timetable> getTimetable() {
        return timetableDao.findAll();
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
}
