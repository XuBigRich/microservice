package top.piao888.response;

import lombok.Data;

@Data
public class LoginResponse extends Response {
    private String token;
    public LoginResponse(String token){
        this.token=token;
    }
}
