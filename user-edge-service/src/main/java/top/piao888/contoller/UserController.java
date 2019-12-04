package top.piao888.contoller;

import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.piao888.user.thrift.user.dto.UserDTO;
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
    //发送验证码
    @RequestMapping(value = "/sendVerifyCode",method = RequestMethod.POST)
    @ResponseBody
    public Response sendVerifyCode( @RequestParam(value = "email" ,required = false) String email,
                                     @RequestParam(value = "mobile",required = false)String mobile) throws TException {
        if(StringUtils.isBlank(mobile)&&StringUtils.isBlank(email)){
            return Response.MOBILE_OR_EMAIL_REQUIRED;
        }
        if(StringUtils.isBlank(mobile)){
            Random random=new Random();
            Double a=random.nextDouble();
            int code= (int) (a*10000);
            if( serviceProvider.getMessageService().send(email,String.valueOf(code))){
                redisClient.set(email,code);
                return Response.SUCCESS;
            }else{
                return Response.SEND_VERIFYCOOD_FAILED;
            }
        }else{
            return Response.FUNCTION;
        }
    }
    //注册
    @PostMapping("/register")
    @ResponseBody
    public Response register(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam(value = "email" ,required = false) String email,
                             @RequestParam(value = "mobile",required = false)String mobile,
                             @RequestParam("verifyCode") String verifyCode){
        if(StringUtils.isBlank(mobile)&&StringUtils.isBlank(email)){
            return Response.MOBILE_OR_EMAIL_REQUIRED;
        }
        if(!StringUtils.isBlank(mobile)){
            return Response.FUNCTION;
        }else{
           String code= redisClient.get(email).toString();
           if(!verifyCode.equals(code)){
                return Response.VERIFY_COOD_INVALID;
           }
        }
        UserInfo userInfo=new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(md5(password));
        userInfo.setEmail(email);
        userInfo.setMobile(mobile);
        try {
            serviceProvider.getUserService().regiserUser(userInfo);
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }
        return Response.SUCCESS;
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
