package com.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author WuHao
 * @description:
 * @date 2023/4/1 23:42
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuTreeVo extends TreeModel<MenuTreeVo> {

    private Long id;

    private Long parentId;

    private String menuName;

    private Integer sort;

}
