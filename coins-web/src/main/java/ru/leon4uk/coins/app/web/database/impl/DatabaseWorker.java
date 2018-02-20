package ru.leon4uk.coins.app.web.database.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.leon4uk.coins.app.web.database.DataBaseImpl;
import ru.leon4uk.coins.app.web.database.entity.TempResult;
import ru.leon4uk.coins.app.web.database.entity.TempTime;
import ru.leon4uk.coins.app.web.database.rowmapper.TempResultMapper;
import ru.leon4uk.coins.app.web.database.rowmapper.TempTimeMapper;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DatabaseWorker implements DataBaseImpl {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<TempResult> getTempresult(String from, String to) {
        String sql = "select * from temp_results where first_rialto_id in (4, 6) and second_rialto_id in (4, 6) and when_create_time BETWEEN ? and ?";

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.YEAR, -2);
        Date start = cal.getTime();
        cal.add(Calendar.YEAR, +3);
        Date end = cal.getTime();
        if (from != null && from.trim().length() != 0) {
            try {
                start = format.parse(from);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (to != null && to.trim().length() != 0) {
            try {
                end = format.parse(to);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        java.sql.Date startSql = new java.sql.Date(start.getTime());
        java.sql.Date endSql = new java.sql.Date(end.getTime());
        return jdbcTemplate.query(sql, new Object[]{startSql, endSql}, new TempResultMapper());
    }

    @Override
    public List<TempTime> getDataForHour() {
        String sql = "select to_char(DATE_TRUNC('minute',when_create_time), 'HH24:MI:SS') as time, trunc(avg(price_difference)::NUMERIC, 4) as difference\n" +
                "from temp_results a where a.first_rialto_id in (4) and a.second_rialto_id in (6) and a.when_create_time\n" +
                "BETWEEN current_timestamp - interval '1 hour' and current_timestamp GROUP BY 1 ORDER BY 1";
        return jdbcTemplate.query(sql, new TempTimeMapper());
    }

    @Override
    public List<TempTime> getAllDataForHour() {
        String sql = "select to_char(DATE_TRUNC('minute', when_create_time), 'HH24:MI:SS') as time,\n" +
                "  trunc(price_difference::NUMERIC, 4) as difference from temp_results\n" +
                "where first_rialto_id in (4, 6) and second_rialto_id in (4, 6)\n" +
                "GROUP BY 1, 2\n" +
                "ORDER BY 1";
        return jdbcTemplate.query(sql, new TempTimeMapper());
    }
}
