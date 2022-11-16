package MyFirstProject.board.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LoggerAspect {
    @Around("execution(* MyFirstProject..controller.*Controller.*(..)) or" +
            "execution(* MyFirstProject..service.*Service.*(..)) or" +
            "execution(* MyFirstProject..mapper.*Mapper.*(..))")
    public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable{
        String type = "";
        String name = joinPoint.getSignature().getDeclaringTypeName(); //패키지 명을 포함한 클래스 이름 ex: MyFirstProject.member.controller.MemberController
        log.debug(name);
        if (name.indexOf("Controller") > -1){
            type = "Controller \t: ";
        } else if (name.indexOf("Service") > -1) {
            type = "Service \t: ";
        } else if (name.indexOf("Mapper") > -1) {
            type = "Mapper \t: ";
        }

        log.debug(type + name + "." + joinPoint.getSignature().getName() + "()"); //getName : 메서드명
        return joinPoint.proceed();
    }
}

