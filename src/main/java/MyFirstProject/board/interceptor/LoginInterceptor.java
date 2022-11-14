package MyFirstProject.board.interceptor;

import MyFirstProject.constant.SessionConstants;
import MyFirstProject.member.dto.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    public final static String TARGET_PATTERN = "/board/**";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("-----------------------------");
        log.debug("start - request uri : "+request.getRequestURI());
        MemberDto memberDto = (MemberDto) request.getSession(true).getAttribute(SessionConstants.LOGIN_MEMBER);
        if (memberDto!=null){return true;} //로그인 되어있음
        //로그인이 안된경우
        response.sendRedirect("/");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //model에 세션 멤버 명 넣어주기
        MemberDto memberDto = (MemberDto) request.getSession().getAttribute(SessionConstants.LOGIN_MEMBER);
        modelAndView.addObject("member",memberDto);
        log.debug("end - request uri : "+request.getRequestURI());
        log.debug("-----------------------------");
    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//    }
}
