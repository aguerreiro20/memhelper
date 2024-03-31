package ui;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import memhelpersimple.App;

/**
 *
 * @author cyrax
 */
public class ConsoleUi implements Ui {

    private static final Logger logger = Logger.getLogger(ConsoleUi.class.getName());
    private final Scanner sc;

    public ConsoleUi() {
        sc = new Scanner(System.in);
        clearTerminal();
        System.out.println("Bonjour et bienvenu au programme d'apprentissage du portugais");
    }

    private void clearTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void showMenu() {
        System.out.println("=================================");
        System.out.println("1 - Afficher le dictionnaire en entier");
        System.out.println("2 - Ajouter une traduction");
        System.out.println("3 - Modifier une traduction");
        System.out.println("4 - Supprimer une entree");
        System.out.println("5 - Reciter!");
        System.out.println("6 - Chercher un mot");
        System.out.println("0 - Quitter le programme");
        System.out.println("=================================");
    }

    @Override
    public String askOption() {
        String option;
        showMenu();
        System.out.println("Que souhaitez vous faire ?");
        option = sc.nextLine();
        clearTerminal();
        return option;
    }

    @Override
    public void showMessage(final String message) {
        System.out.println(message);
    }

    @Override
    public void showDictionary(final Properties props) {
        int i = 1;
        char tmp, letter='a';
        System.out.println("---------------------------------");
        List<String> words = props.keySet().stream().map(String::valueOf).sorted().collect(Collectors.toList());
        for (String word : words) {
            tmp = Character.toUpperCase(word.charAt(0));
            if (tmp != letter) {
                letter = tmp;
                System.out.println(String.format("%d) [%c] %s = %s", i, letter, word, props.get(word)));
            } else {
                System.out.println(String.format("%d)     %s = %s", i, word, props.get(word)));
            }
            i++;
        }
        System.out.println("---------------------------------");
    }

    @Override
    public void addTranslation(final Properties props) {
        String word, translation;
        System.out.println("Entrez le mot a traduire:");
        word = sc.nextLine();
        if (props.containsKey(word)) {
            System.out.println(String.format("Ce mot existe deja! Sa traduction est: %s", props.getProperty(word)));
        } else {
            System.out.println("Entrez sa traduction:");
            translation = sc.nextLine();
            props.setProperty(word, translation);
            System.out.println("Traduction ajoutee!");
            System.out.println(String.format("%s = %s", word, translation));
        }
    }

    @Override
    public void modifyTranslation(final Properties props) {
        String word, translation;
        System.out.println("Entrez le mot a changer:");
        word = sc.nextLine();
        if (!props.containsKey(word)) {
            System.out.println("Ce mot n'existe pas, vous devez l'ajouter d'abord");
        } else {
            System.out.println("Entrez sa nouvelle traduction:");
            translation = sc.nextLine();
            props.setProperty(word, translation);
            System.out.println("Traduction modifiee!");
            System.out.println(String.format("%s = %s", word, translation));
        }
    }

    @Override
    public void deleteTranslation(final Properties props) {
        String word;
        System.out.println("Entrez le mot a supprimer:");
        word = sc.nextLine();
        if (!props.containsKey(word)) {
            System.out.println("Ce mot n'exfinal String wordiste pas, vous devez l'ajouter d'abord");
        } else {
            props.remove(word);
            System.out.println("Traduction supprimee!");
        }
    }

    @Override
    public void remindRecitationRules(final int limit) {
        System.out.println("--- Recitation - regles ---");
        System.out.println("Pour chaque mot, ecrivez la traduction (exacte)");
        System.out.println(String.format("Le jeu va jusqu'a %d mais vous pouvez arreter a tout moment en "
                + "ecrivant '0' au lieu de la traduction", limit));
        System.out.println("Bonne chance !");
        System.out.println("---------------------------");
    }

    @Override
    public String askTranslation(final String word, final int score, final int max, final int total) {
        System.out.println(String.format("Score: %d/%d, question %d", score, max, total + 1));
        System.out.println(String.format("'%s' = ?", word));
        return sc.nextLine();
    }

    @Override
    public void feedback(final String message) {
        clearTerminal();
        System.out.println(message);
    }

    @Override
    public void showFinalScore(double percentage, int score, int limit, String message) {
        System.out.println("---------- Fin du jeu ! ----------");
        System.out.println(String.format("- Score final: %d/%d", score, limit));
        System.out.println(String.format("- Pourcentage de reussite: %.2f%s", percentage, "%"));
        System.out.println(String.format("- Appreciation du jury: %s", message));
        System.out.println("----------------------------------");
    }

    @Override
    public String askWord() {
        System.out.println("Entrez un mot a chercher:");
        return sc.nextLine();
    }

    @Override
    public void showWord(final String word, final Optional<String> optTranslation) {
        if (optTranslation.isEmpty()) {
            System.out.println(String.format("'%s' n'existe pas dans le dictionnaire", word));
        } else {
            System.out.println("Traduction:");
            System.out.println(String.format("%s = %s", word, optTranslation.get()));
        }
    }
}
