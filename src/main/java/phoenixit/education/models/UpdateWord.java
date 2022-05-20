package phoenixit.education.models;

public class UpdateWord {
    private final Word oldWord;
    private final Word newWord;

    public UpdateWord(Word oldWord, Word newWord) {
        this.oldWord = oldWord;
        this.newWord = newWord;
    }

    public Word getOldWord() {
        return oldWord;
    }

    public Word getNewWord() {
        return newWord;
    }
}
