package pl.pjwstk.nai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.pjwstk.nai.model.Film;
import pl.pjwstk.nai.model.Person;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DataConverter {

    private static final String CSV_FILE_LOCAL = "C:\\Users\\slawo\\Downloads\\dane_filmowe.csv";

    public List<Person> convertData() {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        List<Person> personList = new ArrayList<>();
        int lineNumber = 0;

        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(CSV_FILE_LOCAL), StandardCharsets.UTF_8));
            //skip header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] record = line.split(cvsSplitBy);

                ArrayList singleLine = new ArrayList(Arrays.asList(record));

                if (!extractFirstName(singleLine.get(0).toString()).equals("")) {

                    Person p = new Person();
                    p.setFirstName(extractFirstName(singleLine.get(0).toString()));
                    p.setLastName(extractLastName(singleLine.get(0).toString()));
                    p.setGroup(extractGroupName(singleLine.get(1).toString()));
                    p.setFilmsList(extractFilmList(singleLine));

                    personList.add(p);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return personList;
    }

    public Map<Person, HashMap<Film, Double>> getMappedData() {
        List<Person> personList = convertData();
        Map<Person, HashMap<Film, Double>> mapToReturn = new HashMap<>();

        personList.forEach(u -> {
            HashMap<Film, Double> filmDoubleHashMap = new HashMap<>();
            u.getFilmsList().forEach(k -> {
                filmDoubleHashMap.put(k, k.getGrade());
            });

            mapToReturn.put(u, filmDoubleHashMap );
        });

        return mapToReturn;
    }


    private String extractFirstName(String line) {
        String[] split = line.split(" ");
        if (split.length > 1) {
            return capitalize(split[0].toLowerCase());
        }
        return "";
    }

    private String extractLastName(String line) {
        String[] split = line.split(" ");
        if (split.length > 1) {
            return capitalize(split[1].toLowerCase());
        }
        return "";
    }

    private String extractGroupName(String line) {
        if (line != null) {
            return line.toLowerCase();
        }
        return "n/d";
    }

    private List<Film> extractFilmList(ArrayList record) {
        List<Film> films = new ArrayList<>();

        for (int i = 2; i < record.size() - 1; i++) {
            if (!record.get(i).equals("") && !record.get(i + 1).equals("")) {
                Film f = new Film(record.get(i).toString().toLowerCase(), round(Integer.parseInt(record.get(i + 1).toString().toLowerCase()) * 0.1, 2));
                films.add(f);
                i++;
            }
        }

        return films;
    }

    private String capitalize(String str) {
        if (str == null) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String convertToJson(List<Person> personList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(personList);
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
