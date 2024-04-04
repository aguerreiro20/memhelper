package memhelpersimple;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import model.Dictionary;
import ui.ConsoleUi;
import ui.Ui;

/**
 *
 * @author cyrax
 */
public class App {

    private Ui ui;
    Dictionary dictionary;
    Properties versionProps;
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public App() throws IOException {
        ui = new ConsoleUi();
        dictionary = new Dictionary();
        dictionary.load();
        versionProps = new Properties();
        versionProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("version.properties"));
    }

    private void recite() {
        String expectedWord, translation, message;
        int score = 0;
        int i = 0;
        final int limit = dictionary.size();
        if (limit > 0) {
        List<String> shortlist = dictionary.createShortlist(limit);
        ui.remindRecitationRules(limit);
        for (String key: shortlist) {
            i++;
            expectedWord = dictionary.get(key);
            translation = ui.askTranslation(key, score, limit, i);
            if (translation.equals("0")) {
                i--;
                break;
            } else {
                if (Arrays.stream(expectedWord.split(",")).anyMatch(translation::equals)) {
                    score++;
                    ui.feedback(String.format("C'est correct! (%s = %s)", key, expectedWord));
                } else {
                    ui.feedback(String.format("Faux! Reponse correcte: %s = %s", key, expectedWord));
                }
            }           
        }
        double correctness = score == 0 ? 0.00 : ((double) score / (double) i) * 100.00;
        double completeness = ((double) i / (double) limit) * 100.00;
        if (correctness < 21) {
            message = "Oh lala, franchement vous deconnez la!";
        } else if (correctness < 41) {
            message = "La honte est quelque chose qui peut pousser quelqu'un a s'ameliorer, "
                    + "et vous devriez en avoir au moins un peu en ce moment ...";
        } else if (correctness < 49) {
            message = "Vous avez encore du travail a faire!";
        } else if (correctness > 98) {
            message = "Parfait!";
        } else if (correctness > 79) {
            message = "Excellent!";
        } else if (correctness > 59) {
            message = "Pas mal, vous avez fait du progres!";
        } else {
            message = "Encore assez moyen tout ca ...";
        }
        ui.showFinalScore(correctness, completeness, score, i, limit, message);
        } else {
            ui.showMessage("Vous ne pouvez pas prendre de test avec un dictionnaire vide."
                    + " Vous devez d'abord le remplir un peu");
        }
    }

    private void saveProps() {
        dictionary.save();
    }

    private void searchWord() {
        ui.searchWord(dictionary);
    }

    private void displayVersion() {
        ui.displayVersion(versionProps);
    }

    public void run() {
        String option;
        boolean end = false;
        while (!end) {
            option = ui.askOption();
            switch (option) {
                case "0" -> {
                    end = true;
                    ui.close();
                }
                case "1" ->
                    ui.showDictionary(dictionary);
                case "2" -> {
                    ui.addTranslation(dictionary);
                    saveProps();
                }
                case "3" -> {
                    ui.modifyTranslation(dictionary);
                    saveProps();
                }
                case "4" -> {
                    ui.deleteTranslation(dictionary);
                    saveProps();
                }
                case "5" ->
                    recite();
                case "6" ->
                    searchWord();
                case "7" ->
                    displayVersion();
                default ->
                    ui.showMessage("L'option '" + option + "' est invalide");
            }
        }
    }

}
