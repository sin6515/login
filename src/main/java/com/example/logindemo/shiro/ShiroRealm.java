package com.example.logindemo.shiro;

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
    private RedisService redisService;
    @Override
    public boolean supports(AuthenticationToken token) {
            return true;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Integer employeeId = (Integer) principals.getPrimaryPrincipal();
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
            Integer employeeId = redisService.findTokenId((String) authenticationToken.getPrincipal());
            return new SimpleAuthenticationInfo(employeeId, authenticationToken.getCredentials(), getName());
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        Integer employeeId = (Integer) principals.getPrimaryPrincipal();
        redisService.updateEmployeeRedis(employeeId);
        if (redisService.findCategoryByEmployeeRedis(employeeId).equals(ADMIN)) {
            return true;
        }
        return super.isPermitted(principals, permission);
    }
}
