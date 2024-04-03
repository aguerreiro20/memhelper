package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author cyrax
 */
public class Dictionary {

    private Map<String, String> map;
    private final String path = "dictionnaire.txt";
    private final Logger logger = Logger.getLogger(Dictionary.class.getName());

    public Dictionary() {
        map = new LinkedHashMap();
    }

    public boolean load() {
        if (new File(path).exists()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
                String line;
                String[] array;
                while ((line = br.readLine()) != null) {
                    array = line.split("=");
                    map.put(array[0], array[1]);
                }
            } catch (FileNotFoundException ex) {
                logger.log(Level.SEVERE, "Fichier introuvable", ex);
                return false;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Erreur inconnue", ex);
                return false;
            }
        } else {
            System.out.println("Le fichier '" + path + "' n'existe pas, creation d'une nouvelle.");
            save();
        }
        return true;
    }

    public boolean save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            String line;
            for (Map.Entry entry : map.entrySet()) {
                line = String.format("%s=%s", (String) entry.getKey(), (String) entry.getValue());
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(Dictionary.class.getName()).log(Level.SEVERE, "Erreur inconnue", ex);
            return false;
        }
        return true;
    }

    public boolean contains(final String key) {
        return map.containsKey(key);
    }

    public void put(final String key, String value) {
        map.put(key, value);
    }

    public String get(final String key) {
        return map.get(key);
    }

    public int size() {
        return map.size();
    }

    public Map<String, String> createShortlist(final int limit) {
        List<String> keys = map.keySet().stream().map(String::valueOf).collect(Collectors.toList());
        Collections.shuffle(keys);
        Map<String, String> res = new HashMap();
        for (String key : keys) {
            res.put(key, map.get(key));
        }
        return res;
    }

    public List<String> getSortedKeys() {
        return map.keySet().stream().map(String::valueOf).sorted().collect(Collectors.toList());
    }

    public void remove(final String key) {
        map.remove(key);
    }
}
