package com.example.logindemo.entity;

import lombok.Data;

import javax.persistence.*;

import static com.example.logindemo.dto.ConstantValue.FAILED;

/**
 * @author hrh13
 * @date 2021/9/8
 */
@Entity
@Table(name = "operation_record")
@Data
public class OperationRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String operator;
    private String parameter;
    private String result = FAILED;
    private String url;
    @Column(name = "packet_name")
    private String packetName;
    @Column(name = "func_name")
    private String funcName;
    @Column(name = "gmt_create")
    private long gmtCreate;
    @Column(name = "gmt_modified")
    private long gmtModified;
}
