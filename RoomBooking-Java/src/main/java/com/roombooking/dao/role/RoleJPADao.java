package com.roombooking.dao.role;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Role;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

public class RoleJPADao extends AbstractDao<Role> implements RoleDao {

    protected RoleJPADao() {
        super(Role.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Role findRoleByTitle(String title) {
        TypedQuery<Role> query = getEntityManager().createNamedQuery("Role.findRoleByTitle", entityClass);
        query.setParameter("title", title);
        List<Role> positions = query.getResultList();
        return positions.isEmpty() ? null : positions.get(0);
    }
}
