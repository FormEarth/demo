package com.example.demo.util;

import com.example.demo.common.SystemProperties;
import com.example.demo.entity.Tag;
import com.example.demo.entity.Writing;
import com.vladsch.flexmark.ast.util.TextCollectingVisitor;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.profiles.pegdown.Extensions;
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Markdown转换
 * @author raining_heavily
 * @date 2019/12/2 12:11
 **/
public class MarkdownUtil {
    private static final DataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(Extensions.ALL);
    static final Parser PARSER = Parser.builder(OPTIONS).build();

    /**
     * 去除markdown的标记，获取文本
     *
     * @param markdown markdown字符串
     * @param index    截取位置
     * @return
     */
    public static String toText(String markdown, int index) {
        if (StringUtils.isEmpty(markdown)) {
            return "";
        }
        Node document = PARSER.parse(markdown);
        TextCollectingVisitor textCollectingVisitor = new TextCollectingVisitor();
        String text = textCollectingVisitor.collectAndGetText(document);
        text = text.replaceAll("\n", " ");
        if (index > 0) {
            index = (index > text.length()) ? text.length() : index;
            text = text.substring(0, index);
        }
        return text;
    }

    /**
     * 将文章内容写为md文件
     * @param article
     * @throws IOException
     */
    public static void writeContentToFile(Writing article) throws IOException {
        String accessPref = SystemProperties.INSTANCE.getInstance().getProperty("image.access.url");
        String blogPath = SystemProperties.INSTANCE.getInstance().getProperty("path.blog.hexo");
        LocalDateTime dateTime = LocalDateTime.now();
        StringBuffer sb = new StringBuffer();
        sb.append("---\n");
        sb.append("title: ").append(article.getTitle()).append("\n");
        sb.append("date: ").append(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
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

        File file = new File(blogPath + "/source/_posts/" + article.getWritingId() + ".md");

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(sb.toString());
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
