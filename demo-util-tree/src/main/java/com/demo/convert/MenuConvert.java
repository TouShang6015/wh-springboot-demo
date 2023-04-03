package com.demo.convert;

import com.demo.model.Menu;
import com.demo.model.MenuTreeVo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author WuHao
 * @description:
 * @date 2023/4/1 23:52
 * @Version 1.0
 */
@Mapper(builder = @Builder(disableBuilder = true))
public interface MenuConvert {

    MenuConvert INSTANCE = Mappers.getMapper(MenuConvert.class);

    @Mappings({
            @Mapping(target = "label", source = "menuName"),
    })
    MenuTreeVo convertTree(Menu param);

    List<MenuTreeVo> convertTree(List<Menu> param);

}
