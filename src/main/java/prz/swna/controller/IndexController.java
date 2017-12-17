/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prz.swna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import prz.swna.model.AddedWord;
import prz.swna.utils.HtmlParser;
import prz.swna.utils.WordOrganizer;
import prz.swna.repositories.WordRepository;
import prz.swna.security.ApplicationUser;
import prz.swna.security.ApplicationUserRepository;
import prz.swna.security.JWTAuthenticationFilter;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.json.JSONObject;
import static prz.swna.security.SecurityConstants.HEADER_STRING;
import static prz.swna.security.SecurityConstants.SECRET;
import static prz.swna.security.SecurityConstants.TOKEN_PREFIX;

/**
 *
 * @author
 */
@RestController
public class IndexController {

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

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

    @RequestMapping(value = "/api/sendWord", method = POST)
    public void sendWord(@RequestBody String wordToSave, HttpServletRequest request) {
        AddedWord word = new AddedWord();

        String token = request.getHeader(HEADER_STRING);
        String userName = "";
        if (userName != null) {
            userName = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
        }

        ApplicationUser user = applicationUserRepository.findByUsername(userName);

        for (AddedWord each : wordRepository.findAll()) {
            if (each.getUserId() == user.getId() && wordToSave.equals(each.getWord())) {
                return ;
            }
        }
        
        word.setWord(wordToSave);
        word.setUserId(user.getId());
        wordRepository.save(word);

    }

    @RequestMapping(value = "/api/getAddedWords", method = GET)
    public List<AddedWord> getAddedWords(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        String userName = "";
        if (userName != null) {
            userName = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
        }

        ApplicationUser user = applicationUserRepository.findByUsername(userName);
        List<AddedWord> resultList = new ArrayList<AddedWord>();
        for (AddedWord each : wordRepository.findAll()) {
            if (each.getUserId() == user.getId()) {
                resultList.add(each);
            }
        }

        return resultList;
    }

    @RequestMapping(value = "/api/englishWord", method = POST, produces = "text/html; charset=utf-8")
    @ResponseBody
    public String getEnglishWord(@RequestBody String sourceText) {
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        Translation translation
                = translate.translate(
                        sourceText,
                        TranslateOption.sourceLanguage("en"),
                        TranslateOption.targetLanguage("pl"));
        return JSONObject.quote(translation.getTranslatedText());
    }
}
