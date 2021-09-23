package com.example.logindemo.entity;

import com.example.logindemo.view.LoginRequest;
import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import static com.example.logindemo.dto.ConstantValue.FAILED;
import static com.example.logindemo.dto.ConstantValue.SUCCEED;

/**
 * @author hrh13
 * @date 2021/9/23
 */
@Entity
@Data
@Table(name = "login_record")
public class LoginRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "login_id")
    private Integer loginId;
    @Column(name = "login_account")
    private String account;
    @Column(name = "login_name")
    private String loginName;
    @Column(name = "login_identity")
    private String loginIdentity;
    @Column(name = "login_result")
    private String loginResult;
    @Column(name = "login_ip")
    private String loginIp;
    @Column(name = "login_time")
    private long loginTime;
    @Column(name = "gmt_create")
    private long gmtCreate;
    @Column(name = "gmt_modified")
    private long gmtModified;

    public LoginRecordEntity(EmployeeEntity entity) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request;
        if (attributes != null) {
            request = attributes.getRequest();
            this.setLoginIp(request.getRemoteAddr());
        }
        this.setLoginId(entity.getId());
        this.setAccount(entity.getAccount());
        this.setLoginName(entity.getNickname());
        this.setLoginIdentity("employee");
        this.setLoginResult(SUCCEED);
        this.setGmtCreate(System.currentTimeMillis());
    }

    public LoginRecordEntity() {

    }

    public LoginRecordEntity(UserEntity entity) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request;
        if (attributes != null) {
            request = attributes.getRequest();
            this.setLoginIp(request.getRemoteAddr());
        }
        this.setLoginId(entity.getId());
        this.setAccount(entity.getAccount());
        this.setLoginName(entity.getNickname());
        this.setLoginIdentity("user");
        this.setLoginResult(SUCCEED);
        this.setGmtCreate(System.currentTimeMillis());
    }

    public LoginRecordEntity(LoginRequest entity) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request;
        if (attributes != null) {
            request = attributes.getRequest();
            this.setLoginIp(request.getRemoteAddr());
            if (request.getRequestURI().toString().contains("user")) {
                this.setLoginIdentity("user");
            } else {
                this.setLoginIdentity("employee");
            }
        }
        this.setLoginResult(FAILED);
        this.setAccount(entity.getAccount());
        this.setGmtCreate(System.currentTimeMillis());
    }
}
