package top.piao888.contoller;

import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.piao888.dto.UserDTO;
import top.piao888.redis.RedisClient;
import top.piao888.response.LoginResponse;
import top.piao888.response.Response;
import top.piao888.thrift.ServiceProvider;
import top.piao888.user.thrift.user.UserInfo;

import java.security.MessageDigest;
import java.util.Random;

@Controller
public class UserController {
    @Autowired
    private ServiceProvider serviceProvider;
    @Autowired
    private RedisClient redisClient;
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Response login(@RequestParam("username")String username, @RequestParam("password")String password){
        //1.验证用户名密码
        UserInfo userInfo=null;
        try {
            userInfo=serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if(userInfo==null){
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if(!userInfo.getPassword().equalsIgnoreCase(md5(password))){
            return Response.USERNAME_PASSWORD_INVALID;
        }
        //2.生成token
        String token=getToken();
        //3.缓存用户
        redisClient.set(token,DTO(userInfo),1000000 );
        return new LoginResponse(token);
    }

    private UserDTO DTO(UserInfo userInfo) {
        UserDTO userDTO=new UserDTO();
        BeanUtils.copyProperties(userInfo,userDTO);
        return userDTO;
    }

    private String getToken() {
        return randomCode("0123456789abcdefghigklmnopqrstuvwxyz",32);
    }

    private String randomCode(String s, int size) {
        StringBuffer result=new StringBuffer(size);
        Random random=new Random();
        for(int i=0;i<size;i++){
            int loc=random.nextInt(s.length());
            result.append(s.charAt(loc));
        }
        return result.toString();
    }

    private String md5(String password) {
        try {
            //获取md5的加密方式
            MessageDigest md5=MessageDigest.getInstance("md5");
            //对 密码进行加密
          byte[] md5Bytes=  md5.digest(password.getBytes("utf-8"));
          String result=HexUtils.toHexString(md5Bytes);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}
