package top.piao888.response;

import lombok.Data;

import java.io.Serializable;
//@Data
public class Response implements Serializable {
    public static final Response USERNAME_PASSWORD_INVALID=new Response("1001","username or password is invalid");
    public static final Response MOBILE_OR_EMAIL_REQUIRED=new Response("1002","mobile or email is required");
    public static final Response SEND_VERIFYCOOD_FAILED=new Response("1003","send verify cood failed");
    public static final Response SUCCESS=new Response();
    public static final Response FUNCTION=new Response("2000","Function unrealized");

    private String code;
    private String message;
    public Response(String code,String message){
        this.code=code;
        this.message=message;
    }
    public Response(){
        this.code="0";
        this.message="success";
    }

    public static Response exception(Exception e) {
        return new Response("9999",e.getMessage());
    }
}
