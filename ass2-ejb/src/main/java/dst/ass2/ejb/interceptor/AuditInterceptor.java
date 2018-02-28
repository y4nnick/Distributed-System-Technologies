package dst.ass2.ejb.interceptor;

import dst.ass2.ejb.model.IAuditLog;
import dst.ass2.ejb.model.IAuditParameter;
import dst.ass2.ejb.model.impl.AuditLog;
import dst.ass2.ejb.model.impl.AuditParameter;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;

public class AuditInterceptor {

    @PersistenceContext
    private EntityManager entityManager;


    //will be invoked for intercept
    //sa ai oznaceno koja ce mehtoda biti pozvana kroz interceptor i zatim audit object napraviti
    @AroundInvoke
    public Object intercept(InvocationContext icontext) throws Exception {

        IAuditLog auditlog = new AuditLog();

        ArrayList<IAuditParameter> list = new ArrayList<>();

        //loop over method parameters
        for(int index=0; index < icontext.getParameters().length; index++){
            Object parameter = icontext.getParameters()[index];

            AuditParameter auditParameter = new AuditParameter();
            auditParameter.setAuditLog(auditlog);
            auditParameter.setValue(asString(parameter));
            auditParameter.setType(asString(parameter.getClass()));
            auditParameter.setParameterIndex(index);

            list.add(auditParameter);
        }

        auditlog.setMethod(icontext.getMethod().getName());
        auditlog.setInvocationTime(new Date());
        auditlog.setParameters(list);
        entityManager.persist(auditlog);

        //nek bean dalje se izvrsava, moze rollback triggern
        try {
            //invoke method
            Object result = icontext.proceed();
            auditlog.setResult(asString(result));
            return result;

        } catch (Exception exception) {
            //if method fails
            auditlog.setResult(exception.toString());
            throw exception;

        } finally {
            //persist the audit log and parameters
            for (IAuditParameter parameter : auditlog.getParameters()) {
                entityManager.persist(parameter);
            }
            //make sure to flush data in db even if rollback occurs
            entityManager.flush();
        }
    }

    private String asString(Object object) {
        return (object == null) ? "null" : object.toString();
    }
}
