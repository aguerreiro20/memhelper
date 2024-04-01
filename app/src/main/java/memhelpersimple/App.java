package memhelpersimple;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import ui.ConsoleUi;
import ui.Ui;

/**
 *
 * @author cyrax
 */
public class App {

    private Ui ui;
    Properties props;
    Properties versionProps;
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public App() throws IOException {
        ui = new ConsoleUi();
        props = new Properties();
        props.load(new FileInputStream("dictionnaire.properties"));
        versionProps = new Properties();
        versionProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("version.properties"));
    }

    private void recite() {
        String word, translation, message;
        final int limit = Math.min(props.size(), 10);
        int score = 0;
        int i = 0;
        boolean tilTheEnd = true;
        List<String> keys = props.keySet().stream().map(String::valueOf).collect(Collectors.toList());
        Collections.shuffle(keys);
        ui.remindRecitationRules(limit);
        while (tilTheEnd && i < limit) {
            word = keys.get(i);
            translation = ui.askTranslation(word, score, limit, i);
            if (translation.equals("0")) {
                tilTheEnd = false;
            } else {
                if (Arrays.stream(props.getProperty(word).split(",")).anyMatch(translation::equals)) {
                    score++;
                    ui.feedback(String.format("C'est correct! (%s = %s)", word, props.get(word)));
                } else {
                    ui.feedback(String.format("Faux! Reponse correcte: %s = %s", word, props.get(word)));
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
    }

    private void saveProps() {
        try {
            props.store(new FileOutputStream("dictionnaire.properties"), null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, "Fichier introuvable", ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, "Erreur d'I/O", ex);
        }
    }

    private void searchWord() {
        ui.searchWord(props);
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
                    ui.showDictionary(props);
                case "2" -> {
                    ui.addTranslation(props);
                    saveProps();
                }
                case "3" -> {
                    ui.modifyTranslation(props);
                    saveProps();
                }
                case "4" -> {
                    ui.deleteTranslation(props);
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
