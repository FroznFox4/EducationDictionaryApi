package phoenixit.education.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Word")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final Long id;
    @Column(unique = true)
    private final String word;
    @ElementCollection
    private List<String> meaningWord = new ArrayList<>();

    public Word(Long id, String word, List<String> meaningWord) {
        this.id = id;
        this.word = word;
        this.meaningWord.addAll(meaningWord);
    }

    public Word(Long id, Word word) {
        this.id = id;
        this.word = word.word;
        this.meaningWord.addAll(word.meaningWord);
    }

    public Word() {
        this.id = 0L;
        this.word = "";
    }

    public Long getId() {
        return id;
    }
    public String getWord() {
        return word;
    }
}
