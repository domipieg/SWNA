package prz.swna.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import prz.swna.model.AddedWord;

@Repository
public interface AddedWordRepository extends CrudRepository<AddedWord, Integer> {

    AddedWord findByWord(String word);
}
