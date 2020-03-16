package com.active.services.cart.repository.mybatis;

import com.active.services.cart.BaseTestCase;
import com.active.services.cart.domain.BaseDomainObject;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class AuditingInterceptorTestCase extends BaseTestCase {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void interceptWhenInsertSuccess() throws Throwable {
        MappedStatement mappedStatement = buildMappedStatement(SqlCommandType.INSERT);
        BaseDomainObject baseDomainObject = new BaseDomainObject();
        Object[] objects = new Object[2];
        objects[0] = mappedStatement;
        objects[1] = baseDomainObject;
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArgs()).thenReturn(objects);
        when(invocation.proceed()).thenReturn(new Object());
        assertNotNull(new AuditingInterceptor().intercept(invocation));
    }

    @Test
    public void interceptWhenUpdateSuccess() throws Throwable {
        MappedStatement mappedStatement = buildMappedStatement(SqlCommandType.UPDATE);
        BaseDomainObject baseDomainObject = new BaseDomainObject();
        Object[] objects = new Object[2];
        objects[0] = mappedStatement;
        objects[1] = baseDomainObject;
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArgs()).thenReturn(objects);
        when(invocation.proceed()).thenReturn(new Object());
        assertNotNull(new AuditingInterceptor().intercept(invocation));
    }

    @Test
    public void interceptWithParamMapSuccess() throws Throwable {
        MappedStatement mappedStatement = buildMappedStatement(SqlCommandType.INSERT);
        MapperMethod.ParamMap paramMap = new MapperMethod.ParamMap();
        paramMap.put("key", new BaseDomainObject());
        Object[] objects = new Object[2];
        objects[0] = mappedStatement;
        objects[1] = paramMap;
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArgs()).thenReturn(objects);
        when(invocation.proceed()).thenReturn(new Object());
        assertNotNull(new AuditingInterceptor().intercept(invocation));
    }

    @Test
    public void interceptWhenUnMatchParamSuccess() throws Throwable {
        MappedStatement mappedStatement = buildMappedStatement(SqlCommandType.INSERT);
        Object[] objects = new Object[2];
        objects[0] = mappedStatement;
        objects[1] = new Object();
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArgs()).thenReturn(objects);
        when(invocation.proceed()).thenReturn(new Object());
        assertNotNull(new AuditingInterceptor().intercept(invocation));
    }

    private MappedStatement buildMappedStatement(SqlCommandType sqlCommandType) {
        Configuration configuration = new Configuration();
        return new MappedStatement.Builder(configuration, "id", new StaticSqlSource(configuration, "sql"),
                sqlCommandType).build();
    }

    @Test
    public void pluginSuccess() {
        assertNotNull(new AuditingInterceptor().plugin(new Object()));
    }

    @Test
    public void pluginWithExecutorSuccess() {
        Executor executor = new SimpleExecutor(new Configuration(), new JdbcTransaction(null));
        assertNotNull(new AuditingInterceptor().plugin(executor));
    }

    @Test
    public void setPropertiesSuccess() {
        new AuditingInterceptor().setProperties(new Properties());
    }
}
