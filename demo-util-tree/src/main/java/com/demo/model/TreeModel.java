package com.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author WuHao
 * @description:
 * @date 2023/4/1 21:45
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class TreeModel<M> {

    /**
     * 主键
     */
    private Long id;
    /**
     * 父级id
     */
    private Long parentId;
    /**
     * 显示内容
     */
    private String label;
    /**
     * 子节点
     */
    private List<M> children;

    public TreeModel(Long id, Long parentId, String label) {
        this.id = id;
        this.parentId = parentId;
        this.label = label;
    }
}
