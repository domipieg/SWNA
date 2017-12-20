package prz.swna.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import prz.swna.model.DeletedWord;

@Repository
public interface DeletedWordRepository extends CrudRepository<DeletedWord, Long> {

    DeletedWord findByWord(String word);
}
