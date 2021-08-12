package com.example.logindemo.shiro;

import com.example.logindemo.service.EmployeeService;
import com.example.logindemo.service.RedisService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.logindemo.dto.ConstantValue.ADMIN;

/**
 * @author hrh13
 * @date 2021/8/10
 */
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RedisService redisService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String employeeAccount = String.valueOf(principals.getPrimaryPrincipal());
        Integer employeeId = employeeService.findByEmployeeAccount(employeeAccount).getId();
        redisService.updateEmployeeRedis(employeeId);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<String> permissionCode = redisService.findPermissionByEmployeeRedis(employeeId);
        if (permissionCode == null) {
            return null;
        }
        info.addStringPermissions(permissionCode);
        return info;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        String userPwd = new String((char[]) authenticationToken.getCredentials());
        return new SimpleAuthenticationInfo(userName, userPwd, getName());
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        String employeeAccount = String.valueOf(principals.getPrimaryPrincipal());
        Integer employeeId = employeeService.findByEmployeeAccount(employeeAccount).getId();
        if (redisService.findCategoryByEmployeeRedis(employeeId).equals(ADMIN)) {
            return true;
        }
        return super.isPermitted(principals, permission);
    }
}
