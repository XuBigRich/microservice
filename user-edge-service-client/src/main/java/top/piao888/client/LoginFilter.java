package top.piao888.client;


import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import top.piao888.user.thrift.user.dto.UserDTO;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
           UserDTO userDTO= requestUserInfo(token);
        }
    }

    private UserDTO requestUserInfo(String token) throws IOException {
        String urlString="http://www.piao888.top/user/authentication";
        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpPost对象
        HttpPost post = new HttpPost(urlString);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        HttpResponse response=httpClient.execute(post);
        if(response.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
            throw  new RuntimeException("request user info failed! StatusLine"+response.getStatusLine());
        }
        InputStream inputStream=response.getEntity().getContent();
        byte[] temp=new byte[1024];
        StringBuffer sb=new StringBuffer();
        while(inputStream.read(temp)!=-1){
            int len=0;
            sb.append(new String(temp,0,len));
        }
         return null;
    }

    public void destroy() {

    }
}
