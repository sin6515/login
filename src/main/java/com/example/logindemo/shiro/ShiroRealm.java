package com.example.logindemo.shiro;

import com.example.logindemo.service.EmployeeRoleService;
import com.example.logindemo.service.EmployeeService;
import com.example.logindemo.service.PermissionService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hrh13
 * @date 2021/8/10
 */
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private EmployeeRoleService employeeRoleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private EmployeeService employeeService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String employeeAccount = String.valueOf(principals.getPrimaryPrincipal());
        Integer employeeId = employeeService.findEmployeeId(employeeAccount);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (employeeRoleService.findRoleNameByEmployeeId(employeeId) == null) {
            return null;
        }
        info.addRoles(employeeRoleService.findRoleNameByEmployeeId(employeeId));
        if (permissionService.findPermissionNameByRoleId(employeeRoleService.findRoleIdByEmployeeId(employeeId)) == null) {
            return null;
        }
        info.addStringPermissions(permissionService.findPermissionNameByRoleId(employeeRoleService.findRoleIdByEmployeeId(employeeId)));
        //返回你的权限信息
        return info;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        String userPwd = new String((char[]) authenticationToken.getCredentials());
        return new SimpleAuthenticationInfo(userName, userPwd, getName());
    }
}
