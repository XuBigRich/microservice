package top.piao888.user.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import top.piao888.thrift.user.UserInfo;

@Mapper
@Component
public interface UserMapper {
    @Select("select ID,USERNAME,PASSWORD,REAL_NAME AS realName,MOBILE,EMAIL from PE_USER where ID=#{#id}")
    UserInfo getUserById(@Param("id") int id);
    @Select("select ID,USERNAME,PASSWORD,REAL_NAME AS realName,MOBILE,EMAIL from PE_USER where USERNAME=#{#username}")
    UserInfo getUserByName(@Param("username") String username);
    @Insert("insert into PE_USER value(NULL,#{username},#{password},#{realName},#{mobile},#{email})")
    void registerUser(UserInfo u);
}
