package prz.swna.services;

import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prz.swna.model.AddedWord;
import prz.swna.model.DeletedWord;
import prz.swna.repositories.AddedWordRepository;
import prz.swna.repositories.DeletedWordRepository;
import prz.swna.security.ApplicationUser;
import prz.swna.security.ApplicationUserRepository;
import static prz.swna.security.SecurityConstants.HEADER_STRING;
import static prz.swna.security.SecurityConstants.SECRET;
import static prz.swna.security.SecurityConstants.TOKEN_PREFIX;
import prz.swna.utils.HtmlParser;
import prz.swna.utils.WordOrganizer;

@Service
public class WordService {

    @Autowired
    private AddedWordRepository addedWordRepository;

    @Autowired
    private DeletedWordRepository deletedWordRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    public List<String> getFilteredWords(String url, HttpServletRequest request) {
        try {
            HtmlParser parser = new HtmlParser();
            WordOrganizer organizer = new WordOrganizer();
            List<String> unfilteredList = organizer.organize(parser.parse(url));
            ApplicationUser user = getUser(request);
            List<String> resultList = new ArrayList<String>(unfilteredList);

            for (String eachString : unfilteredList) {
                for (DeletedWord each : deletedWordRepository.findAll()) {  
                    String subbed = eachString.substring(eachString.indexOf(" ") + 1, eachString.length());
                    if (user.getId() == each.getUserId() && subbed.equals(each.getWord())) {
                        resultList.remove(eachString);
                    }
                }
            }

            return resultList;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void addWord(String wordToSave, HttpServletRequest request) {
        AddedWord word = new AddedWord();
        ApplicationUser user = getUser(request);

        for (AddedWord each : addedWordRepository.findAll()) {
            if (each.getUserId() == user.getId() && wordToSave.equals(each.getWord())) {
                return;
            }
        }

        word.setWord(wordToSave);
        Date date = new Date();
        word.setDate(date.getTime());
        word.setUserId(user.getId());
        addedWordRepository.save(word);
    }
    
    public void deleteAddedWord(String wordToDelete, HttpServletRequest request) {
        ApplicationUser user = getUser(request);

        for (AddedWord each : addedWordRepository.findAll()) {
            if (each.getUserId() == user.getId() && wordToDelete.equals(each.getWord())) {
                addedWordRepository.delete(each);
            }
        }
    }
    
    public void deleteDeletedWord(String wordToDelete, HttpServletRequest request) {
        ApplicationUser user = getUser(request);

        for (DeletedWord each : deletedWordRepository.findAll()) {
            if (each.getUserId() == user.getId() && wordToDelete.equals(each.getWord())) {
                deletedWordRepository.delete(each);
            }
        }
    }


    public void addToDeleteWord(String wordToDelete, HttpServletRequest request) {
        DeletedWord word = new DeletedWord();
        ApplicationUser user = getUser(request);

        for (DeletedWord each : deletedWordRepository.findAll()) {
            if (each.getUserId() == user.getId() && wordToDelete.equals(each.getWord())) {
                return;
            }
        }

        word.setWord(wordToDelete);
        word.setUserId(user.getId());
        deletedWordRepository.save(word);
    }

    public List<AddedWord> getAddedWords(HttpServletRequest request) {
        ApplicationUser user = getUser(request);
        List<AddedWord> resultList = new ArrayList<AddedWord>();
        for (AddedWord each : addedWordRepository.findAll()) {
            if (each.getUserId() == user.getId()) {
                resultList.add(each);
            }
        }
        return resultList;
    }

    public List<DeletedWord> getDeletedWords(HttpServletRequest request) {
        ApplicationUser user = getUser(request);
        List<DeletedWord> resultList = new ArrayList<DeletedWord>();
        for (DeletedWord each : deletedWordRepository.findAll()) {
            if (each.getUserId() == user.getId()) {
                resultList.add(each);
            }
        }
        return resultList;
    }

    public void repeated(Integer id, HttpServletRequest request) {
        AddedWord addedWord = addedWordRepository.findOne(id);
        Date date = new Date();
        addedWord.setDate(date.getTime());
        addedWordRepository.save(addedWord);
    }
    
    private ApplicationUser getUser(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        String userName = "";
        if (userName != null) {
            userName = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
        }

        return applicationUserRepository.findByUsername(userName);
    }

}
