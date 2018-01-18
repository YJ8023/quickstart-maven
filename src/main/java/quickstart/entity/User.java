package quickstart.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * user
 *
 * @author Administrator
 * @create 2018-01-10
 */
public class User implements Serializable{

    private Long id;
    private String loginName;
    private String name;
    private String plainPassword;
    private String password;
    private String salt;
    private String roles;
    private Date registerDate;

    public User(Long id, String loginName, String name, String password, String salt, String roles, Timestamp registerDate) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.roles = roles;
        this.registerDate = registerDate;
    }

    public User(){}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public void setLoginName(String loginName) { this.loginName = loginName; }

    @NotNull
    public String getLoginName() { return loginName; }

    public void setName(String name){ this.name = name; }

    @NotNull
    public String getName() { return name; }

    public void setPlainPassword(String plainPassword) { this.plainPassword = plainPassword; }

    // 不持久化到数据库，也不显示在Restful接口的属性.
    @JsonIgnore
    public String getPlainPassword() { return plainPassword; }

    public void setPassword(String password) { this.password = password; }

    public String getPassword() { return password; }

    public String getSalt() { return salt; }

    public void setSalt(String salt) { this.salt = salt; }

    public String getRoles() { return roles; }

    public void setRoles(String roles) { this.roles = roles; }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getRegisterDate() { return registerDate; }

    public void setRegisterDate(Date registerDate) { this.registerDate = registerDate; }

    public User(Long currentUserId){
        this.id = currentUserId;
    }

    @JsonIgnore
    public List<String> getRoleList() {
        // 角色列表在数据库中实际以逗号分隔字符串存储，因此返回不能修改的List.
        return ImmutableList.copyOf(StringUtils.split(roles, ","));
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
