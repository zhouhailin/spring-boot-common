package link.thingscloud.spring.boot.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p>LoggingAspect class.</p>
 *
 * @author : zhouhailin
 * @version 1.0.0
 */
@Aspect
@Component
public class LoggingAspect {

    private final boolean isTraceEnabled = log.isTraceEnabled();

    private static final String LOGGING_PREFIX = "[logging] [{}ms] {}, {}";

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * logging pointcut
     */
    @Pointcut("@annotation(link.thingscloud.spring.boot.common.aop.Logging)")
    public void logging() {
    }

    /**
     * logging doAround
     *
     * @param joinPoint 切面对象
     * @return 返回值
     * @throws java.lang.Throwable if any.
     */
    @Around("logging()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();
        try {
            Object proceed = joinPoint.proceed();
            doLogging(joinPoint, proceed, null, System.currentTimeMillis() - start);
            return proceed;
        } catch (Throwable e) {
            doLogging(joinPoint, null, e, System.currentTimeMillis() - start);
            throw e;
        }
    }

    /**
     * 前置通知，执行目标方法之前，执行的操作
     *
     * @param joinPoint 切面对象
     */
    @Before("logging()")
    public void doBefore(JoinPoint joinPoint) {
        if (isTraceEnabled) {
            log.trace("logging aspect doBefore joinPoint : {}", joinPoint);
        }
    }

    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     *
     * @param joinPoint 切面对象
     */
    @After("logging()")
    public void doAfter(JoinPoint joinPoint) {
        if (isTraceEnabled) {
            log.trace("logging aspect doAfterReturning joinPoint : {}", joinPoint);
        }
    }

    /**
     * 后置异常通知
     *
     * @param joinPoint 切面对象
     * @param cause     异常信息
     */
    @AfterThrowing(pointcut = "logging()", throwing = "cause")
    public void doAfterThrowing(JoinPoint joinPoint, Exception cause) {
        if (isTraceEnabled) {
            log.trace("logging aspect doAfterReturning joinPoint : {}, result : {}", joinPoint, cause);
        }
    }

    /**
     * 处理完请求，返回内容
     *
     * @param joinPoint 切面对象
     * @param result    响应结果
     */
    @AfterReturning(pointcut = "logging()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        if (isTraceEnabled) {
            log.trace("logging aspect doAfterReturning joinPoint : {}, result : {}", joinPoint, result);
        }
    }

    /**
     * 日志输出
     *
     * @param joinPoint 切面对象
     * @param proceed   执行目标方法返回结果
     * @param cause     执行目标方法抛出的异常
     */
    private void doLogging(JoinPoint joinPoint, Object proceed, Throwable cause, long costTimeMillis) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        StringBuilder sb = new StringBuilder();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Class<?> returnType = methodSignature.getReturnType();
        for (int i = 0; i < parameterNames.length; i++) {
            sb.append(parameterNames[i]).append(" : ").append(args[i]).append(", ");
        }
        Logging logging = methodSignature.getMethod().getAnnotation(Logging.class);
        if (logging.result() && returnType != void.class) {
            sb.append("return").append(" : ").append(proceed);
        } else {
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 2);
            }
        }
        if (cause == null) {
            switch (logging.level()) {
                case TRACE:
                    getLogger(joinPoint).trace(LOGGING_PREFIX, costTimeMillis, joinPoint.getSignature().getName(), sb);
                    break;
                case DEBUG:
                    getLogger(joinPoint).debug(LOGGING_PREFIX, costTimeMillis, joinPoint.getSignature().getName(), sb);
                    break;
                case WARN:
                    getLogger(joinPoint).warn(LOGGING_PREFIX, costTimeMillis, joinPoint.getSignature().getName(), sb);
                    break;
                case ERROR:
                    getLogger(joinPoint).error(LOGGING_PREFIX, costTimeMillis, joinPoint.getSignature().getName(), sb);
                    break;
                default:
                    getLogger(joinPoint).info(LOGGING_PREFIX, costTimeMillis, joinPoint.getSignature().getName(), sb);
                    break;
            }
        } else {
            getLogger(joinPoint).error("[logging] [{}ms] {}, {}, cause : ", costTimeMillis, joinPoint.getSignature().getName(), sb, cause);
        }
    }

    /**
     * 根据切面的方法获取Logger
     *
     * @param joinPoint 切面对象
     * @return logger logger
     */
    private Logger getLogger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    }

}
