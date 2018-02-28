package ru.leon4uk.app.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.leon4uk.app.domain.Rialto;

import java.util.List;

@Repository
public class RialtoDAOImpl implements RialtoDAO{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Rialto> listRialto() {
        return sessionFactory.openSession().createQuery("from Rialto").list();
    }
}
