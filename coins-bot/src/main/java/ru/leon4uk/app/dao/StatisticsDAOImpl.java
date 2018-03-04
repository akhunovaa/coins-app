package ru.leon4uk.app.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.leon4uk.app.domain.Statistics;

import java.util.List;

@Repository
public class StatisticsDAOImpl implements StatisticsDAO{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addStatistics(Statistics statistics) {
        sessionFactory.openSession().save(statistics);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Statistics> listStatistics() {
        return sessionFactory.openSession().createQuery("from Statistics").list();
    }

    @Override
    public void removeStatistics(Integer id) {
        Statistics statistics = (Statistics) sessionFactory.getCurrentSession().load(Statistics.class, id);
        if (null != statistics) {
            sessionFactory.getCurrentSession().delete(statistics);
        }
    }

}
