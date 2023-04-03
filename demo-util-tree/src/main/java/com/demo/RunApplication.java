package com.demo;

import cn.hutool.core.collection.CollUtil;
import com.demo.model.TreeModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WuHao
 * @description:
 * @date 2023/4/1 21:55
 * @Version 1.0
 */
@SpringBootApplication
public class RunApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
        List<TreeModel> list = CollUtil.newArrayList(
                new TreeModel(1L, 0L, "1"),
                new TreeModel(2L, 1L, "2"),
                new TreeModel(3L, 2L, "3"),
                new TreeModel(4L, 2L, "4"),
                new TreeModel(5L, 0L, "5"),
                new TreeModel(6L, 5L, "6")
        );

        List<Long> allIdList = list.stream().map(TreeModel::getId).distinct().collect(Collectors.toList());

        List<TreeModel> treeList = list.stream()
                // 这个filter是过滤出顶级列表，也就是跟节点的元素
                .filter(model -> !allIdList.contains(model.getParentId()))
                // 为根节点元素，设置子节点，通过递归
                .peek(model -> recursionList(list, model))
                .collect(Collectors.toList());
        System.err.println(treeList);
    }


    /**
     * 递归设置子节点
     *
     * <h1>2个必要条件</h1>
     * <p>* 1、自己调用自己</p>
     * <p>* 2、有结束条件</p>
     *
     * @param list
     * @param item
     */
    private static void recursionList(List<TreeModel> list, TreeModel item) {
        // 不要给自己找麻烦，我们先不考虑是否为空，最后检查代码一起修改

        // 第一次进入，list为所有元素   item为顶级节点中的每一个元素
        // 我们需要设置item中的children属性，为子节点赋值
        // 根据item中的id，找到所有parentId与item的id对应的元素，parentId = id
        List<TreeModel> childrenList = list.stream().filter(model -> item.getId().equals(model.getParentId())).collect(Collectors.toList());
        item.setChildren(childrenList);
        // 接着来，我们现在应该遍历子节点了，为子节点的每一个元素，设置children值
        // 我们把下一次遍历，当做第一次进入
        // 步骤也是一样的
        // 先遍历
        for (TreeModel childrenItem : childrenList) {
            // 到这里，children 为 所有元素，childrenItem 为顶级节点中的每一个元素
            // 一样的，上述操作，1. 过滤出parentId = item.id 的所有元素
            // 我们这里就可以使用递归了，实现一个条件 自己调用自己
            recursionList(childrenList, childrenItem);

            // 现在这个方法没有条件，让他停止，for循环中不停的调用，会导致StackOverflow
            // 思考一下，最终停止的条件，是不是在元素中找不到对应的子节点，就不进行递归调用了
            // 所以在调用前，判断一下 只有子节点数量大于0的时候，才会递归调用，否则方法就出去
            if (childrenList.stream().anyMatch(one -> childrenItem.getId().equals(one.getParentId()))) {
                recursionList(childrenList, childrenItem);
            }

        }
    }

}
