package ru.leon4uk.coins.app.web.database.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.leon4uk.coins.app.web.database.entity.TempResult;


import java.sql.ResultSet;
import java.sql.SQLException;

public class TempResultMapper implements RowMapper {

    @Override
    public TempResult mapRow(ResultSet resultSet, int i) throws SQLException {
        TempResult tempResult = new TempResult();
        tempResult.setId(resultSet.getInt("id"));
        tempResult.setFirstRialtoId(resultSet.getInt("first_rialto_id"));
        tempResult.setSecondRialtoId(resultSet.getInt("second_rialto_id"));
        tempResult.setTime(resultSet.getTimestamp("when_create_time"));
        tempResult.setCurrencyPairId(resultSet.getInt("currency_pair_id_one"));
        tempResult.setFirstBidPrice(resultSet.getDouble("first_bid_price"));
        tempResult.setSecondBidPrice(resultSet.getDouble("second_bid_price"));
        tempResult.setFirstBidAmount(resultSet.getDouble("first_bid_amount"));
        tempResult.setSecondBidAmount(resultSet.getDouble("second_bid_amount"));
        tempResult.setCurrencyPairIdTwo(resultSet.getInt("currency_pair_id_two"));
        tempResult.setFirstAskPrice(resultSet.getDouble("first_ask_price"));
        tempResult.setSecondAskPrice(resultSet.getDouble("second_ask_price"));
        tempResult.setFirstAskAmount(resultSet.getDouble("first_ask_amount"));
        tempResult.setSecondAskAmount(resultSet.getDouble("second_ask_amount"));
        tempResult.setPriceDifference(resultSet.getDouble("price_difference"));
        tempResult.setRialtoNameOne(resultSet.getString("name1"));
        tempResult.setRialtoNameTwo(resultSet.getString("name2"));
        return tempResult;
    }
}