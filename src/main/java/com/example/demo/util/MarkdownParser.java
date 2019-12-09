package com.example.demo.util;

import com.vladsch.flexmark.ast.util.TextCollectingVisitor;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.profiles.pegdown.Extensions;
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import org.springframework.util.StringUtils;

/**
 * @author raining_heavily
 * @date 2019/12/2 12:11
 **/
public class MarkdownParser {
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
            index = (index > text.length()) ? text.length() :index ;
            text = text.substring(0, index);
        }
        System.out.println("-----------------:"+text);
        return text;
    }
}
