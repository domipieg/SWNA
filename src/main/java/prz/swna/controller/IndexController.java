package prz.swna.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import prz.swna.model.AddedWord;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.json.JSONObject;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import prz.swna.model.DeletedWord;
import prz.swna.services.WordService;

@RestController
public class IndexController {

    @Autowired
    private WordService wordService;

    @RequestMapping(value = "/api/sendLink", method = POST)
    public List<String> getListFromArtice(@RequestBody String url, HttpServletRequest request) {
        return wordService.getFilteredWords(url, request);
    }

    @RequestMapping(value = "/api/addWord", method = POST)
    public void addWord(@RequestBody String wordToSave, HttpServletRequest request) {
        wordService.addWord(wordToSave, request);
    }

    @RequestMapping(value = "/api/addToDeleteWord", method = POST)
    public void addToDeleteWord(@RequestBody String word, HttpServletRequest request) {
        wordService.addToDeleteWord(word, request);
    }

    @RequestMapping(value = "/api/deleteAddedWord", method = POST)
    public void deleteAddedWord(@RequestBody String word, HttpServletRequest request) {
        wordService.deleteAddedWord(word, request);
    }

    @RequestMapping(value = "/api/deleteDeletedWord", method = POST)
    public void deleteDeletedWord(@RequestBody String word, HttpServletRequest request) {
        wordService.deleteDeletedWord(word, request);
    }

    @RequestMapping(value = "/api/getAddedWords", method = GET)
    public List<AddedWord> getAddedWords(HttpServletRequest request) {
        return wordService.getAddedWords(request);
    }

    @RequestMapping(value = "/api/getDeletedWord", method = GET)
    public List<DeletedWord> getDeletedWords(HttpServletRequest request) {
        return wordService.getDeletedWords(request);
    }

    @RequestMapping(value = "/api/repeated", method = POST)
    public void repeated(@RequestBody Integer id, HttpServletRequest request) {
        wordService.repeated(id, request);
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
