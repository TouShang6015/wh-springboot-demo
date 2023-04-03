package com.demo.model;

import lombok.Data;

/**
 * @author WuHao
 * @description:
 * @date 2023/4/1 23:42
 * @Version 1.0
 */
@Data
public class Menu {

    private Long id;

    private Long parentId;

    private String menuName;

    private Integer sort;

    public Menu() {
    }

    public Menu(Long id, Long parentId, String menuName, Integer sort) {
        this.id = id;
        this.parentId = parentId;
        this.menuName = menuName;
        this.sort = sort;
    }
}
