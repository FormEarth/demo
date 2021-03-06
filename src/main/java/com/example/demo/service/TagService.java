package com.example.demo.service;

import com.example.demo.entity.Tag;
import com.example.demo.exception.SystemException;

import java.util.List;

/**
 * @author raining_heavily
 */
public interface TagService extends BaseService<Tag> {

    /**
     * 查询所有标签（不分页）
     * @return
     * @throws SystemException
     */
    public List<Tag> queryAllTagsWithoutPage() throws SystemException;

    /**
     * 模糊查询标签
     * @param searchText 搜索文字
     * @return
     * @throws SystemException
     */
    public List<Tag> queryTagsWithText(String searchText) throws SystemException;

    /**
     * 更新指定标签热度
     * @param tagId
     * @return
     */
    public int updateTagHotById(long tagId) throws SystemException;

    /**
     * 批量处理Tag，没有则新增，有则热度+1
     * @param tags
     * @param create 是否是新增，若是新增，已有标签热度+1；不是新增忽略已有标签
     */
    public void handleTagList(List<Tag> tags,boolean create);
}
