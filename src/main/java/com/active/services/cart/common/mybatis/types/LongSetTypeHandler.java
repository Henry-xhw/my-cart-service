package com.active.services.cart.common.mybatis.types;

import com.active.platform.utils.JSONUtil;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LongSetTypeHandler extends BaseTypeHandler<Set<Long>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<Long> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, Optional.ofNullable(parameter)
                .map(set -> JSONUtil.encode(parameter))
                .orElse(null));
    }

    @Override
    public Set<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Optional.ofNullable(rs.getString(columnName))
                .map(str -> JSONUtil.decode(str, HashSet.class))
                .orElse(null);
    }

    @Override
    public Set<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Optional.ofNullable(rs.getString(columnIndex))
                .map(str -> JSONUtil.decode(str, HashSet.class))
                .orElse(null);
    }

    @Override
    public Set<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Optional.ofNullable(cs.getString(columnIndex))
                .map(str -> JSONUtil.decode(str, HashSet.class))
                .orElse(null);
    }
}
