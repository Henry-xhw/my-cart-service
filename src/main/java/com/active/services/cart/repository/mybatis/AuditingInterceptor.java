package com.active.services.cart.repository.mybatis;

import com.active.services.ContextWrapper;
import com.active.services.cart.domain.BaseDomainObject;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Properties;

@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AuditingInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        if (parameter instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameter;
            paramMap.forEach((key, value) -> setAuditingInfo(value, sqlCommandType));
        } else {
            setAuditingInfo(parameter, sqlCommandType);
        }
        return invocation.proceed();
    }

    private void setAuditingInfo(Object parameter, SqlCommandType sqlCommandType) {
        if (parameter instanceof BaseDomainObject) {
            BaseDomainObject baseDomain = (BaseDomainObject) parameter;
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                baseDomain.setCreatedBy(ContextWrapper.get().getActorId());
                baseDomain.setCreatedDt(Instant.now());
            }
            if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
                baseDomain.setModifiedBy(ContextWrapper.get().getActorId());
                baseDomain.setModifiedDt(Instant.now());
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
