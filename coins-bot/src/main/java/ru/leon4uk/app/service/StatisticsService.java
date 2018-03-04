package ru.leon4uk.app.service;

import ru.leon4uk.app.domain.Statistics;

import java.util.List;

public interface StatisticsService {

    void addStatistics(Statistics statistics);

    List<Statistics> listStatistics();

    void removeStatistics(Integer id);
}
