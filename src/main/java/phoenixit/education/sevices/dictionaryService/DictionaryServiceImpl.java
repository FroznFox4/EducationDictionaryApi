package phoenixit.education.sevices.dictionaryService;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import phoenixit.education.exceptions.UniqueKeyException;
import phoenixit.education.models.UpdateWord;
import phoenixit.education.models.Word;
import phoenixit.education.repositories.DictionaryDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryDB dictionaryDB;

    @Autowired
    DictionaryServiceImpl(DictionaryDB dictionaryDB) {
        this.dictionaryDB = dictionaryDB;
    }

    @Override
    public List<Word> getWordsByWord(List<String> words) {
        return dictionaryDB.findAllByWords(words);
    }

    @Override
    public List<Word> getWordsById(List<Long> wordsId) {
        return iteratorToList(dictionaryDB.findAllById(wordsId));
    }

    @Override
    public List<Word> createWords(List<Word> words) throws UniqueKeyException {
        try {
            return dictionaryDB.saveAll(words);
        } catch (DataIntegrityViolationException ex) {
            Throwable rootCause = ex.getRootCause();
            if (rootCause instanceof SQLException) {
                if ("23505".equals(((SQLException) rootCause).getSQLState()))
                    throw new UniqueKeyException("Repeated unique key");
            }
        }
        return List.of();
    }

    @Override
    public List<Word> updateWords(List<UpdateWord> words) throws UniqueKeyException {
        var findWords = getWordsByWord(words
                .stream()
                .map(el -> el.getOldWord().getWord())
                .collect(Collectors.toList()));
        var newWords = findWords
                .stream()
                .map(findWord -> {
                    var result = new ArrayList<Word>();
                    words
                            .stream()
                            .filter(updateWord-> updateWord.getOldWord().getWord().equals(findWord.getWord()))
                            .findFirst()
                            .ifPresent(updateWord ->
                                    result.add(new Word(
                                            findWord.getId(),
                                            updateWord.getNewWord())));
                    return (result.size() > 0) ? result.get(0) : new Word();
                })
                .filter(resultWords -> resultWords.getId() > 0)
                .collect(Collectors.toList());
//        Or we can create @Modifiable sql query, but for demonstrate I think it's enough (delete + create)
        deleteWords(newWords.stream().map(Word::getWord).collect(Collectors.toList()));
        return (newWords.size() != words.size()) ? new ArrayList<>() : createWords(newWords);
    }

    @Override
    public List<Word> deleteWords(List<String> words) {
        var findWords = getWordsByWord(words);
        try {
            dictionaryDB.deleteAll(findWords);
            return findWords;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    private <T> List<T> iteratorToList(Iterable<T> iterable) {
        var result = new ArrayList<T>();
        iterable.forEach(result::add);
        return result;
    }
}
