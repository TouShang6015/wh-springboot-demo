package com.demo.utils;

import com.demo.model.TreeModel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WuHao
 * @description:
 * @date 2023/4/1 23:09
 * @Version 1.0
 */
public class TreeBuilder<Model, TM extends TreeModel<TM>> {

    // 假设我们是 menu 菜单实体，那需要转换成 MenuTreeVo  树实体VO类

    // Model就是原始实体，TM是继承自TreeModel的实体类，如Menu 与 MenuTreeVo

    private List<Model> modelList;

    private List<TM> treeList;

    public static <Model, TM extends TreeModel<TM>> TreeBuilder<Model, TM> build(List<Model> list) {
        TreeBuilder<Model, TM> builder = new TreeBuilder<>();
        builder.modelList = Optional.ofNullable(list).orElseGet(ArrayList::new);
        return builder;
    }

    // 1. 构建 设置属性
    // 2. 将model属性转换成TM

    /**
     * 转换方法
     * <p>这个方法的意义</p>
     * <p>* 我们可以动态的将实体，转换成我们需要的TreeMode实体类</p>
     * <p>* 不同的类转换方式不同</p>
     *
     * <p>比如菜单实体Menu，菜单名称这个字段是menuName</p>
     * <p>部门实体，部门名称是deptName</p>
     * <p>他们字段都不相同，我们可以在使用这个方法时灵活的赋值</p>
     *
     * @param convertFunction
     * @return
     */
    public TreeBuilder<Model, TM> convert(Function<List<Model>, List<TM>> convertFunction) {
        this.treeList = convertFunction.apply(this.modelList);
        return this;
    }

    public List<TM> builder() {
        return new ConvertUtil<TM>(this.treeList).convertTree();
    }


    @AllArgsConstructor
    private static class ConvertUtil<TM extends TreeModel<TM>> {

        private List<TM> treeList;

        /**
         * 核心方法
         * @return
         */
        private List<TM> convertTree() {
            List<Long> allIdList = this.treeList.stream().map(TreeModel::getId).distinct().collect(Collectors.toList());
            return this.treeList.stream()
                    .filter(model -> !allIdList.contains(model.getParentId()))
                    .peek(model -> recursionList(this.treeList, model))
                    .collect(Collectors.toList());
        }

        /**
         * 递归设置子节点
         * @param list
         * @param item
         */
        public void recursionList(List<TM> list, TM item) {
            List<TM> childrenList = list.stream().filter(model -> item.getId().equals(model.getParentId())).collect(Collectors.toList());
            item.setChildren(childrenList);
            for (TM childrenItem : childrenList) {
                if (list.stream().filter(one -> one.getParentId().equals(childrenItem.getId())).count() > 0) {
                    recursionList(list, childrenItem);
                }
            }
        }

    }


}
