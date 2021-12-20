package com.epam.esm.mapper;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.DataUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

    private final static String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setId(rs.getInt("id"));
        giftCertificate.setName(rs.getString("name"));
        giftCertificate.setDescription(rs.getString("description"));
        giftCertificate.setPrice(rs.getBigDecimal("price"));
        giftCertificate.setDuration(rs.getLong("duration"));

        String createDataString = rs.getString("create_date");
        LocalDateTime createData = DataUtils.parseLocalDateType(createDataString, DATE_TIME_PATTERN);
        giftCertificate.setCreateData(createData);

        String lastUpdateDateString = rs.getString("last_update_date");
        LocalDateTime lastUpdateDate = DataUtils.parseLocalDateType(lastUpdateDateString, DATE_TIME_PATTERN);
        giftCertificate.setLastUpdateDate(lastUpdateDate);

        return giftCertificate;
    }
}

