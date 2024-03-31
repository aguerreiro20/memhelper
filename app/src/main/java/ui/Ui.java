package ui;

import java.util.Optional;
import java.util.Properties;

/**
 *
 * @author cyrax
 */
public interface Ui {
    String askOption();
    void showMessage(final String message);
    void showDictionary(final Properties props);
    void addTranslation(final Properties props);
    void modifyTranslation(final Properties props);
    void deleteTranslation(final Properties props);
    void remindRecitationRules(final int limit);
    String askTranslation(final String word, final int score, final int max, final int total);
    void feedback(final String message);
    void showFinalScore(double percentage, int score, int limit, String message);
    String askWord();
    void showWord(final String word, final Optional<String> optTranslation);
    void displayVersion(final Properties versionProperties);
    void close();
}
