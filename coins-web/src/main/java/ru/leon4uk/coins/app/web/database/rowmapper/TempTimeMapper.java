package ru.leon4uk.coins.app.web.database.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.leon4uk.coins.app.web.database.entity.TempTime;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TempTimeMapper  implements RowMapper {

    @Override
    public TempTime mapRow(ResultSet resultSet, int i) throws SQLException {
        TempTime tempTime = new TempTime();
        tempTime.setTime(resultSet.getString("time"));
        tempTime.setDifference(resultSet.getDouble("difference"));
        return tempTime;
    }
}