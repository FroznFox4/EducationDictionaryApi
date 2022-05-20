package phoenixit.education.sevices.dictionaryService;

import phoenixit.education.exceptions.UniqueKeyException;
import phoenixit.education.models.UpdateWord;
import phoenixit.education.models.Word;

import java.util.List;

public interface DictionaryService {
    List<Word> getWordsById(List<Long> wordsId);
    List<Word> getWordsByWord(List<String> words);
    List<Word> createWords(List<Word> words) throws UniqueKeyException;
    List<Word> updateWords(List<UpdateWord> words) throws UniqueKeyException;
    List<Word> deleteWords(List<String> words);
}
