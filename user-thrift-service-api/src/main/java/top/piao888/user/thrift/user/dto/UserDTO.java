package top.piao888.user.thrift.user.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserDTO implements Serializable {
    private int id;
    private String username;
    private String password;
    private String mobile;
    private String email;
    private String realName;
}
