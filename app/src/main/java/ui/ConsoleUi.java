package ui;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import model.Dictionary;

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
        System.out.println("Bonjour et bienvenu au programme d'apprentissage d'une langue");
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
        System.out.println("7 - Afficher la version de l'application");
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
    public void showDictionary(final Dictionary dictionary) {
        int i = 1;
        char tmp, letter = 'a';
        String padding;
        System.out.println("---------------------------------");
        List<String> keys = dictionary.getSortedKeys();
        for (String key : keys) {
            tmp = Character.toUpperCase(key.charAt(0));
            padding = i > 9 ? i > 99 ? "" : " " : "  ";
            if (tmp != letter) {
                letter = tmp;
                System.out.println(String.format("%s%d) [%c] %s = %s", padding, i, letter, key, dictionary.get(key)));
            } else {
                System.out.println(String.format("%s%d)     %s = %s", padding, i, key, dictionary.get(key)));
            }
            i++;
        }
        System.out.println("---------------------------------");
    }

    @Override
    public void addTranslation(final Dictionary dictionary) {
        String word, translation;
        while (true) {
            System.out.println("Entrez le mot a traduire (ou '0' pour sauvegarder et retourner au menu principal):");
            word = sc.nextLine();
            if (word.equals("0")) {
                clearTerminal();
                break;
            } else if (dictionary.contains(word)) {
                clearTerminal();
                System.out.println(String.format("Ce mot existe deja! Sa traduction est: %s", dictionary.get(word)));
            } else {
                System.out.println("Entrez sa traduction:");
                translation = sc.nextLine();
                clearTerminal();
                dictionary.put(word, translation);
                System.out.println("Traduction ajoutee!");
                System.out.println(String.format("%s = %s", word, translation));
                System.out.println("");
            }
        }
    }

    @Override
    public void modifyTranslation(final Dictionary dictionary) {
        String word, translation;
        word = "";
        while (true) {
            System.out.println("Entrez le mot a changer (ou '0' pour sauvegarder et retourner au menu principal):");
            word = sc.nextLine();
            if (word.equals("0")) {
                clearTerminal();
                break;
            } else if (!dictionary.contains(word)) {
                clearTerminal();
                System.out.println("Ce mot n'existe pas, vous devez d'abord l'ajouter au dictionnaire: " + word);
                System.out.println("");
            } else {
                System.out.println(String.format(
                        "Entrez sa nouvelle traduction (ancienne: '%s'):", dictionary.get(word)));
                translation = sc.nextLine();
                clearTerminal();
                dictionary.put(word, translation);
                System.out.println("Traduction modifiee!");
                System.out.println(String.format("%s = %s", word, translation));
                System.out.println("");
            }
        }
    }

    @Override
    public void deleteTranslation(final Dictionary dictionary) {
        String word;
        while (true) {
            System.out.println("Entrez le mot a supprimer (ou '0' pour sauvegarder et retourner au menu principal):");
            word = sc.nextLine();
            if (word.equals("0")) {
                clearTerminal();
                break;
            } else if (!dictionary.contains(word)) {
                clearTerminal();
                System.out.println(String.format("Ce terme (%s) n'existe pas, vous devez l'ajouter d'abord", word));
                System.out.println("");
            } else {
                clearTerminal();
                dictionary.remove(word);
                System.out.println(String.format("Traduction supprimee! (%s)", word));
                System.out.println("");
            }
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
        System.out.println(String.format("Score: %d/%d (max:%d), question %d", score, total-1, max, total));
        System.out.println(String.format("'%s' = ?", word));
        return sc.nextLine();
    }

    @Override
    public void feedback(final String message) {
        clearTerminal();
        System.out.println(message);
        System.out.println("");
    }

    @Override
    public void showFinalScore(double correctness, double completion, int score, int index, int limit, String message) {
        System.out.println("---------- Fin du jeu ! ----------");
        System.out.println(String.format("- Score final: %.2f%s (%d/%d)", correctness, "%", score, index));
        System.out.println(String.format("- Completion:  %.2f%s (%d/%d)", completion, "%", index, limit));
        System.out.println(String.format("- Appreciation du jury: %s", message));
        System.out.println("----------------------------------");
    }

    @Override
    public void searchWord(final Dictionary dictionary) {
        String word;
        clearTerminal();
        while (true) {
            System.out.println("Entrez un mot a chercher:");
            word = sc.nextLine();
            clearTerminal();
            if (word.equals("0")) {
                break;
            } else if (dictionary.contains(word)) {
                System.out.println("Traduction:");
                System.out.println(String.format("%s = %s", word, dictionary.get(word)));
                System.out.println("");
            } else {
                System.out.println(String.format("'%s' n'existe pas dans le dictionnaire", word));
                System.out.println("");
            }
        }
    }

    @Override
    public void displayVersion(final Properties versionProps) {
        System.out.println("----- Version de l'application -----");
        List<String> keys = versionProps.keySet().stream().map(String::valueOf).sorted().collect(Collectors.toList());
        for (String key : keys) {
            System.out.println(String.format("- %s: %s", key, versionProps.get(key)));
        }
    }

    @Override
    public void close() {
        sc.close();
    }
}
