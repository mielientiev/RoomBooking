package com.roombooking.dao.timetable;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Timetable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.List;

public class TimetableJPADao extends AbstractDao<Timetable> implements TimetableDao {

    protected TimetableJPADao() {
        super(Timetable.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Timetable> getTimetableByRoomIdAndDate(int id, Date date) {
        TypedQuery<Timetable> query = getEntityManager()
                .createNamedQuery("Timetable.findTimetableByRoomIdAndDate", entityClass);
        query.setParameter("roomId", id);
        query.setParameter("date", date);
        return query.getResultList();
    }
}
