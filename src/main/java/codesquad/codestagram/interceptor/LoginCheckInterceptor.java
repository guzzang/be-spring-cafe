package codesquad.codestagram.interceptor;

import codesquad.codestagram.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // 조회 기능만 인터셉터 제외
        if (requestURI.matches("^/articles/\\d+$") && "GET".equals(method)) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute(SessionConst.LOGIN_USER) == null){
            response.sendRedirect("/users/login?redirectURL=" + requestURI);
            System.out.println(request);
            return false;
        }

        return true;
    }

}
