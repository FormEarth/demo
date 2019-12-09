package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 模糊查询标签，没有值会默认查最热门的标签
     * @param searchText
     * @return
     */
    List<Tag> queryTagsWithText(@Param("searchText") String searchText);

    /**
     * 更新指定标签热度
     * @param tagId
     * @return
     */
    int updateTagHotById(long tagId);
}
