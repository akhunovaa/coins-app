package ru.leon4uk.coins.app.web.database;

import ru.leon4uk.coins.app.web.database.entity.TempResult;
import ru.leon4uk.coins.app.web.database.entity.TempTime;

import java.util.List;

public interface DataBaseImpl {

    List<TempResult> getTempresult(String from, String to);

    List<TempTime> getDataForHour();

    List<TempTime> getAllDataForHour();
}
