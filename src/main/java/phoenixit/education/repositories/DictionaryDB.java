package phoenixit.education.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import phoenixit.education.models.Word;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DictionaryDB extends JpaRepository<Word, Long> {
    @Transactional
    @Query(value = "SELECT w FROM Word w WHERE w.word IN :words")
    List<Word> findAllByWords(@Param("words") List<String> words);
}
