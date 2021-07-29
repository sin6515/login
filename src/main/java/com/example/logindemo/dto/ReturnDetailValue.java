package com.example.logindemo.dto;

import com.alibaba.fastjson.annotation.JSONType;
import com.example.logindemo.entity.UserEntity;
import lombok.Data;

/**
 * @author hrh13
 * @date 2021/7/28
 */
@Data
@JSONType(orders = {"id", "permissionName","name", "reason"})
public class ReturnDetailValue {

    private Integer id;
    private String name;
    private String reason;
    private UserEntity user;
    public ReturnDetailValue(Integer id){
        setId(id);
    }
    public ReturnDetailValue(String name){
        setName(name);
    }
    public ReturnDetailValue(String name,String reason){
        if(!name.equals(null)){
            setName(name);
        }
        setReason(reason);
    }public ReturnDetailValue(Integer id,String reason){
        setId(id);
        setReason(reason);
    }
    public ReturnDetailValue(UserEntity userEntity){
        setUser(userEntity);
    }


}
