package com.example.logindemo.dto;

import lombok.Data;

import java.util.List;

/**
 * @author hrh13
 * @date 2021/8/20
 */
@Data
public class UpdateDto {
    private List<Integer> idDelete;
    private List<Integer> idAdd;

    public UpdateDto(List<Integer> idDelete, List<Integer> idAdd) {
        this.setIdAdd(idAdd);
        this.setIdDelete(idDelete);
    }
}
