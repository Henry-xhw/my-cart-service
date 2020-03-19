package com.active.services.cart.common.mybatis.types;

import com.active.platform.utils.JSONUtil;
import com.google.common.collect.Sets;
import com.zaxxer.hikari.pool.HikariProxyCallableStatement;
import com.zaxxer.hikari.pool.HikariProxyResultSet;

import org.apache.ibatis.type.JdbcType;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StringSetTypeHandlerTestCase {

    @Test
    public void setNonNullParameterSuccess() throws SQLException {
        HikariProxyCallableStatement preparedStatement = Mockito.mock(HikariProxyCallableStatement.class);
        doNothing().when(preparedStatement).setString(anyInt(), any());
        new StringSetTypeHandler().setNonNullParameter(preparedStatement, 0, Sets.newHashSet("1"),
                JdbcType.NCHAR);
        verify(preparedStatement, times(1)).setString(anyInt(), any());
    }

    @Test
    public void getNullableResultByColumnNameSuccess() throws SQLException {
        ResultSet resultSet = Mockito.mock(HikariProxyResultSet.class);
        Set<String> set = Sets.newHashSet("1");
        when(resultSet.getString(anyString())).thenReturn(JSONUtil.encode(set));
        assertEquals(set, new StringSetTypeHandler().getNullableResult(resultSet, "columnName"));
        verify(resultSet, times(1)).getString(anyString());
    }

    @Test
    public void getNullableResultByColumnIndexSuccess() throws SQLException {
        ResultSet resultSet = Mockito.mock(HikariProxyResultSet.class);
        Set<String> set = Sets.newHashSet("1");
        when(resultSet.getString(anyInt())).thenReturn(JSONUtil.encode(set));
        assertEquals(set, new StringSetTypeHandler().getNullableResult(resultSet, 0));
        verify(resultSet, times(1)).getString(anyInt());
    }

    @Test
    public void getNullableResultByCallableStatementSuccess() throws SQLException {
        CallableStatement callableStatement = Mockito.mock(HikariProxyCallableStatement.class);
        Set<String> set = Sets.newHashSet("1");
        when(callableStatement.getString(anyInt())).thenReturn(JSONUtil.encode(set));
        assertEquals(set, new StringSetTypeHandler().getNullableResult(callableStatement, 0));
        verify(callableStatement, times(1)).getString(anyInt());
    }
}
