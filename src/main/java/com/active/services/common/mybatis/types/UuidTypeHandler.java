package com.active.services.common.mybatis.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class UuidTypeHandler extends BaseTypeHandler<UUID> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, Optional.ofNullable(parameter)
          .map(UUID::toString)
          .orElse(null));
    }

    @Override
    public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Optional.ofNullable(rs.getString(columnName))
          .map(UUID::fromString)
          .orElse(null);
    }

    @Override
    public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Optional.ofNullable(rs.getString(columnIndex))
          .map(UUID::fromString)
          .orElse(null);
    }

    @Override
    public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Optional.ofNullable(cs.getString(columnIndex))
          .map(UUID::fromString)
          .orElse(null);
    }
}
