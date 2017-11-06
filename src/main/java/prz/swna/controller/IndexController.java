/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prz.swna.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RestController;
import prz.swna.utils.HtmlParser;
import prz.swna.utils.WordOrganizer;

/**
 *
 * @author
 */
@RestController
public class IndexController {

    @RequestMapping(value = "/api/sendLink", method = POST)
    public List<String> list(@RequestBody String url) {
        try {
            HtmlParser parser = new HtmlParser();
            WordOrganizer organizer = new WordOrganizer();
            return organizer.organize(parser.parse(url));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
