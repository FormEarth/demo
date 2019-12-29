package com.example.demo.service.impl;

import com.example.demo.common.Dict;
import com.example.demo.entity.ArticleTag;
import com.example.demo.entity.Tag;
import com.example.demo.entity.User;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.ArticleTagMapper;
import com.example.demo.util.MarkdownParser;
import com.example.demo.util.Util;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author raining_heavily
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl<Article> implements ArticleService {

    @Value(value = "${path.blog.hexo}")
    private String blogPath;
    @Value(value = "${image.access.url}")
    private String accessPref;
    @Autowired
    ArticleTagMapper articleTagMapper;
    @Autowired
    ArticleMapper articleMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void articleHandle(Article article) throws SystemException {
        Date date = new Date();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        article.setSummary(MarkdownParser.toText(article.getContent(), 200));
        article.setAuthor(user.getUserId());
        article.setAuthorName(user.getUserName());
        article.setCreateTime(date);
        article.setStatus(Dict.ARTICLE_STATUS_NORMAL);
        article.setSendTime(date);
        articleMapper.insert(article);
        for (Tag tag : article.getTags()) {
            ArticleTag articleTag = new ArticleTag(article.getArticleId(), tag.getTagId());
            articleTagMapper.insert(articleTag);
        }
        if (article.getSaveToFile()) {
            try {
                writeContentToFile(article);
            } catch (IOException e) {
                e.printStackTrace();
                throw new SystemException(ExceptionEnums.FILE_WRITE_FAIL);
            }
            //调用shell使hexo重新生成静态页面
            Util.shellExecute(blogPath,"sh","/c","hexo generate");
        }
    }

    @Override
    public List<Article> queryArticleInHome(long currentPage) {
        return articleMapper.queryArticlesWithPage((currentPage-1)*7);
    }

    @Override
    public Article queryArticleDetailById(long articleId) throws SystemException {
        Article article = articleMapper.queryArticleDetailById(articleId);
        if(article==null){
            throw new SystemException(ExceptionEnums.SOURCE_NOT_FOUND);
        }else if(article.getPersonal()){
            throw new SystemException(ExceptionEnums.SOURCE_NOT_PERMIT);
        }else{
            //阅读量+1
            Article article1 = new Article();
            article1.setArticleId(article.getArticleId());
            article1.setReaderNum(article.getReaderNum()+1);
            articleMapper.updateById(article1);
        }
        return article;
    }

    /**
     * 将文章内容写为md文件
     * @param article
     * @throws IOException
     */
    private void writeContentToFile(Article article) throws IOException {
        LocalDateTime dateTime = LocalDateTime.now();
        StringBuffer sb = new StringBuffer();
        sb.append("---\n");
        sb.append("title: ").append(article.getTitle()).append("\n");
        sb.append("date: ").append(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        //封面全路径
        sb.append("index_img: ").append(accessPref).append(article.getFrontCover()).append("\n");
        sb.append("banner_img: ").append(accessPref).append(article.getFrontCover()).append("\n");
        List<Tag> tags = article.getTags();
        if (tags != null && tags.size() > 0) {
            sb.append("tags:\n");
            for (Tag tag : tags) {
                sb.append("- ").append(tag.getTagText()).append("\n");
            }
        }
        sb.append("---\n");
        sb.append("\n");
        sb.append(article.getContent());

        File file = new File(blogPath + "/source/_posts/" + dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".md");

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(sb.toString());
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
