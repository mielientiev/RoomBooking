package com.roombooking.dao.rights;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Rights;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

public class RightsJPADao extends AbstractDao<Rights> implements RightsDao {

    protected RightsJPADao() {
        super(Rights.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Rights getByPositionIdAndRoomTypeId(int posId, int roomTypeId) {
        TypedQuery<Rights> query = getEntityManager().createNamedQuery("Rights.findByPositionAndRoomType", entityClass);
        query.setParameter("posId", posId);
        query.setParameter("roomTypeId", roomTypeId);
        List<Rights> rights = query.getResultList();
        return rights.isEmpty() ? null : rights.get(0);
    }
}
