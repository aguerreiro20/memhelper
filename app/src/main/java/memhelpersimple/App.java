package memhelpersimple;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
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
        final int limit = Math.min(dictionary.size(), 10);
        if (limit > 0) {
        Map<String, String> shortlist = dictionary.createShortlist(limit);
        ui.remindRecitationRules(limit);
        for (String key: shortlist.keySet()) {
            expectedWord = shortlist.get(key);
            translation = ui.askTranslation(key, score, limit, i);
            if (translation.equals("0")) {
                break;
            } else {
                if (Arrays.stream(expectedWord.split(",")).anyMatch(translation::equals)) {
                    score++;
                    ui.feedback(String.format("C'est correct! (%s = %s)", key, expectedWord));
                } else {
                    ui.feedback(String.format("Faux! Reponse correcte: %s = %s", key, expectedWord));
                }
                i++;
            }            
        }
        double percentage = ((double) score / (double) limit) * 100.00;
        if (percentage < 20) {
            message = "Oh lala, franchement vous deconnez la!";
        } else if (percentage < 40) {
            message = "La honte est quelque chose qui peut pousser quelqu'un a s'ameliorer, "
                    + "et vous devriez en avoir au moins un peu en ce moment ...";
        } else if (percentage < 50) {
            message = "Vous avez encore du travail a faire!";
        } else if (percentage > 98) {
            message = "Parfait!";
        } else if (percentage > 80) {
            message = "Excellent!";
        } else if (percentage > 60) {
            message = "Pas mal, vous avez fait du progres!";
        } else {
            message = "Encore assez moyen tout ca ...";
        }
        ui.showFinalScore(percentage, score, limit, message);
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
