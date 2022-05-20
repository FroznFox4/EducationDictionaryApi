package phoenixit.education.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import phoenixit.education.exceptions.UniqueKeyException;
import phoenixit.education.models.UpdateWord;
import phoenixit.education.models.Word;
import phoenixit.education.sevices.dictionaryService.DictionaryService;

import java.util.ArrayList;
import java.util.List;

/***
 * Controller for education dictionary
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;

    }
    /***
     * Creating words
     * Example for body:
     *  [{
     * 	    "word": "mew",
     * 	    "meaningWord": ["mew", "mew3"]
     *  }]
     * @param words words with meanings
     * @return
     *  List of success created words
     */
    @PostMapping("/createWords")
    public ResponseEntity<?> createWords(@RequestBody ArrayList<Word> words) {
        try {
            var result = dictionaryService.createWords(words);
            return (result.size() > 0)
                    ? ResponseEntity.ok().body(result)
                    : ResponseEntity.internalServerError().body(result);
        } catch (UniqueKeyException ex) {
            var error = ex.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

    /***
     * Get word
     * Example for body:
     *  ["mew"]
     * @param word word
     * @return word with meanings
     */
    @GetMapping("/getWords")
    public ResponseEntity<?> getWords(@RequestBody List<String> word) {
        return ResponseEntity.ok().body(dictionaryService.getWordsByWord(word));
    }

    /***
     * Update words
     * Example for body:
     *  [{
     * 	    "oldWord": {
     * 		    "word": "mew"
    *       },
     * 	    "newWord": {
     * 		    "word": "mew",
     * 		    "meaningWord": ["mew", "mew3", "mew4"]
     *      }
     *  }]
     * @param words List of Object where 2 fields are contained:
     *              1. The data that needs to be changed.
     *              2. New data.
     * @return
     *  List of success updated words
     */
    @PatchMapping("/updateWords")
    public ResponseEntity<?> updateWords(@RequestBody List<UpdateWord> words) {
        try {
            var result = dictionaryService.updateWords(words);
            return (!result.isEmpty())
                    ? ResponseEntity.ok().body(result)
                    : ResponseEntity.badRequest().body(result);
        } catch (UniqueKeyException ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /***
     * Delete words
     * Example for body:
     *  ["mew"]
     * @param words Array of words for deleting
     * @return Array of successfully deleted words
     */
    @DeleteMapping("/deleteWords")
    public ResponseEntity<?> deleteWords(@RequestBody List<String> words) {
        var result = dictionaryService.deleteWords(words);
        return (!result.isEmpty())
                ? ResponseEntity.ok().body(result)
                : ResponseEntity.badRequest().body(result);
    }
}
