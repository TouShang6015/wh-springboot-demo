package com.demo.controller;

import cn.hutool.core.collection.CollUtil;
import com.demo.convert.MenuConvert;
import com.demo.model.Menu;
import com.demo.model.MenuTreeVo;
import com.demo.model.TreeModel;
import com.demo.utils.TreeBuilder;
import com.demo.utils.TreeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WuHao
 * @description:
 * @date 2023/4/1 22:53
 * @Version 1.0
 */
@RestController
@RequestMapping("/tree")
public class TreeController {

    /**
     * 不使用泛型的构建树结构，灵活性差
     * @return
     */
    @GetMapping("/build")
    public List<TreeModel> build() {
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
                .peek(model -> TreeUtil.recursionList(list, model))
                .collect(Collectors.toList());
        return treeList;
    }

    /**
     * 灵活性强的树构建
     * @return
     */
    @GetMapping("/build2")
    public List<MenuTreeVo> build2() {

        List<Menu> menus = CollUtil.newArrayList(
                new Menu(1L, 0L, "首页", 1),
                new Menu(2L, 0L, "系统管理", 2),
                new Menu(3L, 2L, "角色", 3),
                new Menu(4L, 2L, "菜单", 4),
                new Menu(5L, 0L, "用户管理", 5),
                new Menu(6L, 5L, "系统用户", 6),
                new Menu(7L, 5L, "普通用户", 7),
                new Menu(8L, 6L, "管理员列表", 8)
        );

//        List<MenuTreeVo> builder = TreeBuilder.<Menu, MenuTreeVo>build(menus).convert(menu -> menu.stream().map(m -> {
//            MenuTreeVo vo = new MenuTreeVo();
//            vo.setId(m.getId());
//            vo.setParentId(m.getParentId());
//            vo.setLabel(m.getMenuName());
//            vo.setSort(m.getSort());
//            return vo;
//        }).collect(Collectors.toList())).builder();

        List<MenuTreeVo> builder = TreeBuilder.<Menu, MenuTreeVo>build(menus)
                .convert(MenuConvert.INSTANCE::convertTree)
                .builder();

        return builder;

    }

}
