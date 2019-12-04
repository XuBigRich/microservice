package top.piao888.client;


import org.apache.commons.lang.StringUtils;
import top.piao888.user.thrift.user.dto.UserDTO;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1=(HttpServletRequest) servletRequest;
        HttpServletResponse response1=(HttpServletResponse) servletResponse;
        String token=request1.getParameter("token");
        if(StringUtils.isBlank(token)){
            Cookie[] cookies=request1.getCookies();
            if(cookies!=null){
                for(Cookie c:cookies){
                    if(c.getName().equals("token")){
                        token=c.getValue();
                    }
                }
            }
        }
        if(StringUtils.isNotBlank(token)){
//           UserDTO requestUserInfo(token);
        }
    }

    public void destroy() {

    }
}
