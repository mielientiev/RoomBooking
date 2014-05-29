package com.roombooking.dao.position;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Position;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

public class PositionJPADao extends AbstractDao<Position> implements PositionDao {

    protected PositionJPADao() {
        super(Position.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Position findByPositionTitle(String title) {
        TypedQuery<Position> query = getEntityManager().createNamedQuery("Position.findPositionByTitle", entityClass);
        query.setParameter("title", title);
        List<Position> positions = query.getResultList();
        return positions.isEmpty() ? null : positions.get(0);
    }
}
