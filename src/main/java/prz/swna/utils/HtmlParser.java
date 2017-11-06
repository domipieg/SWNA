/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prz.swna.utils;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author
 */
public class HtmlParser {
    
    public String parse(String url) throws IOException {
        String html = Jsoup.connect(url).get().html();
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");
        StringBuilder stringBuilder = new StringBuilder();
        for (Element p : paragraphs) {
            stringBuilder.append(p.text());
        }
        return stringBuilder.toString();
    }
    
}
