/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prz.swna.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import prz.swna.model.Word;

/**
 *
 * @author
 */
@Repository
public interface WordRepository extends CrudRepository<Word, Long> {

}