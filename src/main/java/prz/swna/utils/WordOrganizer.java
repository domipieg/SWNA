/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prz.swna.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author
 */
public class WordOrganizer {
    public List<String> organize(String article) {
        List<String> results = new ArrayList<>();
        HashMap<String, Integer> wordCounter = new HashMap<>();
        article = article.replaceAll("[^a-zA-Z]", " ");
        article = article.toLowerCase();
        String[] words = article.split("(?<=[,.])|(?=[,.])|\\s+");
        fillHashMap(words, wordCounter);
        fillResultList(wordCounter, results);
        return results;
    }

    private void fillResultList(HashMap<String, Integer> wordCounter, List<String> results) {
        for (Map.Entry<String, Integer> each : wordCounter.entrySet()) {
            String key = each.getKey();
            Integer value = each.getValue();
            results.add(value.toString() + " " + key);
        }
        results.sort((s1, s2) -> {
            String[] first = s1.split(" ");
            String[] second = s2.split(" ");
            if (Integer.parseInt(first[0]) > Integer.parseInt(second[0])) {
                return -1;
            } else if (Integer.parseInt(first[0]) < Integer.parseInt(second[0])) {
                return 1;
            }
            return 0;
        });
    }

    private void fillHashMap(String[] words, HashMap<String, Integer> wordCounter) {
        for (String each : words) {
            if (!wordCounter.containsKey(each)) {
                wordCounter.put(each, 1);
            } else {
                wordCounter.put(each, wordCounter.get(each) + 1);
            }
        }
    }
}
