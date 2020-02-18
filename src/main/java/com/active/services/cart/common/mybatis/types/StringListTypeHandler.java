package com.active.services.cart.common.mybatis.types;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringListTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, Optional.ofNullable(parameter)
                .map(set -> String.join(",", set))
                .orElse(null));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Optional.ofNullable(rs.getString(columnName))
                .map(str -> Stream.of(str.split(",")).filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Optional.ofNullable(rs.getString(columnIndex))
                .map(str -> Stream.of(str.split(",")).filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Optional.ofNullable(cs.getString(columnIndex))
                .map(str -> Stream.of(str.split(",")).filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList()))
                .orElse(null);
    }
}
