package ru.leon4uk.app.dao;

import ru.leon4uk.app.domain.Statistics;

import java.util.List;

public interface StatisticsDAO {

    void addStatistics(Statistics statistics);

    List<Statistics> listStatistics();

    void removeStatistics(Integer id);

    List<Statistics> listHourStatistics();
}
