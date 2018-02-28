package dst.ass3.aop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
public class LoggingAspect {


    @Before("execution(* dst.ass3.aop.IPluginExecutable.execute(..)) && !@annotation(Invisible)")
    public void before(JoinPoint joinPoint) {
        String message = String.format("Plugin %s started to execute", joinPoint.getTarget().getClass());
        log(getLogger(joinPoint.getTarget()), message);
    }

    @After("execution(* dst.ass3.aop.IPluginExecutable.execute(..)) && !@annotation(Invisible)")
    public void after(JoinPoint joinPoint) {
        String message = String.format("Plugin %s is finished", joinPoint.getTarget().getClass());
        log(getLogger(joinPoint.getTarget()), message);

    }

    private void log(Logger logger, String message) {
        if (logger == null) {
            System.out.println(message);
        } else {
            logger.info(message);
        }
    }

    private Logger getLogger(Object object) {
        Class<?> someClass = object.getClass();

        for (Field field : someClass.getDeclaredFields()) {

            if (Logger.class.isAssignableFrom(field.getType())) {

                try {
                    field.setAccessible(true);
                    Logger log = (Logger) field.get(object);
                    log.setLevel(Level.INFO);
                    return log;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
