package ui;

import java.util.Optional;
import java.util.Properties;
import model.Dictionary;

/**
 *
 * @author cyrax
 */
public interface Ui {
    String askOption();
    void showMessage(final String message);
    void showDictionary(final Dictionary dictionary);
    void addTranslation(final Dictionary dictionary);
    void modifyTranslation(final Dictionary dictionary);
    void deleteTranslation(final Dictionary dictionary);
    void remindRecitationRules(final int limit);
    String askTranslation(final String word, final int score, final int max, final int total);
    void feedback(final String message);
    void showFinalScore(double percentage, int score, int limit, String message);
    void searchWord(final Dictionary dictionary);
    void displayVersion(final Properties versionProperties);
    void close();
}
