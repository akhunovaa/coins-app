package ru.leon4uk.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.leon4uk.app.dao.StatisticsDAO;
import ru.leon4uk.app.domain.Statistics;

import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService{

    @Autowired
    private StatisticsDAO statisticsDAO;

    @Override
    public void addStatistics(Statistics statistics) {
        statisticsDAO.addStatistics(statistics);
    }

    @Override
    public List<Statistics> listStatistics() {
        return statisticsDAO.listStatistics();
    }

    @Override
    public void removeStatistics(Integer id) {
        statisticsDAO.removeStatistics(id);
    }

}
