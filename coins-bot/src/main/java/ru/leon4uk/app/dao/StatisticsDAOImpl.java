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
        return sessionFactory.openSession().createQuery("from Statistics order by id desc").setMaxResults(20).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Statistics> listHourStatistics() {
        return sessionFactory.openSession().createQuery("from Statistics where create_time BETWEEN current_timestamp - HOUR('1') and current_timestamp and first_rialto_id = 4 and second_rialto_id = 6 GROUP BY 1 order by 1").setMaxResults(500).list();
    }

    @Override
    public void removeStatistics(Integer id) {
        Statistics statistics = (Statistics) sessionFactory.getCurrentSession().load(Statistics.class, id);
        if (null != statistics) {
            sessionFactory.getCurrentSession().delete(statistics);
        }
    }

}
