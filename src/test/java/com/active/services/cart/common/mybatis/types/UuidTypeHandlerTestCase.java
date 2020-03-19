package com.active.services.cart.common.mybatis.types;

import com.zaxxer.hikari.pool.HikariProxyCallableStatement;
import com.zaxxer.hikari.pool.HikariProxyResultSet;

import org.apache.ibatis.type.JdbcType;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UuidTypeHandlerTestCase {

    @Test
    public void setNonNullParameterSuccess() throws SQLException {
        HikariProxyCallableStatement preparedStatement = Mockito.mock(HikariProxyCallableStatement.class);
        doNothing().when(preparedStatement).setString(anyInt(), any());
        new UuidTypeHandler().setNonNullParameter(preparedStatement, 0, UUID.randomUUID(), JdbcType.NCHAR);
        verify(preparedStatement, times(1)).setString(anyInt(), any());
    }

    @Test
    public void getNullableResultByColumnNameSuccess() throws SQLException {
        ResultSet resultSet = Mockito.mock(HikariProxyResultSet.class);
        UUID uuid = UUID.randomUUID();
        when(resultSet.getString(anyString())).thenReturn(uuid.toString());
        assertEquals(uuid, new UuidTypeHandler().getNullableResult(resultSet, "columnName"));
        verify(resultSet, times(1)).getString(anyString());
    }

    @Test
    public void getNullableResultByColumnIndexSuccess() throws SQLException {
        ResultSet resultSet = Mockito.mock(HikariProxyResultSet.class);
        UUID uuid = UUID.randomUUID();
        when(resultSet.getString(anyInt())).thenReturn(uuid.toString());
        assertEquals(uuid, new UuidTypeHandler().getNullableResult(resultSet, 0));
        verify(resultSet, times(1)).getString(anyInt());
    }

    @Test
    public void getNullableResultByCallableStatementSuccess() throws SQLException {
        CallableStatement callableStatement = Mockito.mock(HikariProxyCallableStatement.class);
        UUID uuid = UUID.randomUUID();
        when(callableStatement.getString(anyInt())).thenReturn(uuid.toString());
        assertEquals(uuid, new UuidTypeHandler().getNullableResult(callableStatement, 0));
        verify(callableStatement, times(1)).getString(anyInt());
    }
}
