package com.roombooking.dao.timetable;

import com.roombooking.dao.Dao;
import com.roombooking.entity.Timetable;

import java.sql.Date;
import java.util.List;

public interface TimetableDao extends Dao<Timetable> {

    List<Timetable> getTimetableByRoomIdAndDate(int id, Date date);

}
