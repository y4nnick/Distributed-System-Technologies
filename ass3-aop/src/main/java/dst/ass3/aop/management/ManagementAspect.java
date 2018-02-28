package dst.ass3.aop.management;

import dst.ass3.aop.IPluginExecutable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import java.util.Timer;
import java.util.TimerTask;

@Aspect
public class ManagementAspect {

    private Timer timer = new Timer();

    //Runs around (before & after) plugin execution. Plugin is executed with joinPoint.proceed()
    @Around("execution(* dst.ass3.aop.IPluginExecutable.execute(..)) && @annotation(timeout)")
    public void beforeTimeout(ProceedingJoinPoint joinPoint, Timeout timeout) {
        IPluginExecutable plugin = (IPluginExecutable) joinPoint.getTarget();

        TimeoutTask timeoutTask = new TimeoutTask();
        timeoutTask.setExecutable(plugin);

        timer.schedule(timeoutTask, timeout.value());

        try {
            joinPoint.proceed();
            timeoutTask.setExecutable(null);
        } catch (Throwable t) {
            t.printStackTrace();
            timeoutTask.setExecutable(null);
        }
    }

    class TimeoutTask extends TimerTask{
        private IPluginExecutable pluginExecutable;

        public void setExecutable(IPluginExecutable pluginExecutable) {
            this.pluginExecutable=pluginExecutable;
        }

        @Override
        public void run() {
            if (pluginExecutable != null) this.pluginExecutable.interrupted();
        }
    }
}
