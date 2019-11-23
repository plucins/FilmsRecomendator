package pl.pjwstk.nai;

import pl.pjwstk.nai.model.Film;
import pl.pjwstk.nai.model.Person;

import java.util.*;

public class SlopeOne {

    private static Map<Film, Map<Film, Double>> diff = new HashMap<>();
    private static Map<Film, Map<Film, Integer>> freq = new HashMap<>();
    private static Map<Person, HashMap<Film, Double>> inputData;
    private static Map<Person, HashMap<Film, Double>> outputData = new HashMap<>();

    public static void slopeOne(String fullName) {

        inputData = new DataConverter().getMappedData();
        buildDifferencesMatrix(inputData);
        predict(inputData);

        printData(outputData, fullName);
    }

    private static List<Film> getPersonFilms() {
        List<Person> personList = new DataConverter().convertData();
        List<Film> films = new ArrayList<>();

        personList.forEach(u -> {
            films.addAll(u.getFilmsList());
        });

        return films;
    }


    private static void buildDifferencesMatrix(Map<Person, HashMap<Film, Double>> data) {
        for (HashMap<Film, Double> user : data.values()) {
            for (Map.Entry<Film, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<Film, Double>());
                    freq.put(e.getKey(), new HashMap<Film, Integer>());
                }
                for (Map.Entry<Film, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (Film j : diff.keySet()) {
            for (Film i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i).doubleValue();
                int count = freq.get(j).get(i).intValue();
                diff.get(j).put(i, oldValue / count);
            }
        }

    }


    private static void predict(Map<Person, HashMap<Film, Double>> data) {
        HashMap<Film, Double> uPred = new HashMap<Film, Double>();
        HashMap<Film, Integer> uFreq = new HashMap<Film, Integer>();
        for (Film j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        for (Map.Entry<Person, HashMap<Film, Double>> e : data.entrySet()) {
            for (Film j : e.getValue().keySet()) {
                for (Film k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j).doubleValue() + e.getValue().get(j).doubleValue();
                        double finalValue = predictedValue * freq.get(k).get(j).intValue();
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j).intValue());
                    } catch (NullPointerException e1) {
                    }
                }
            }
            HashMap<Film, Double> clean = new HashMap<Film, Double>();
            for (Film j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j).doubleValue() / uFreq.get(j).intValue());
                }
            }
            for (Film j : getPersonFilms()) {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else {
                    clean.put(j, -1.0);
                }
            }
            outputData.put(e.getKey(), clean);
        }
    }

    private static void printData(Map<Person, HashMap<Film, Double>> data, String fullName) {
        for (Person user : data.keySet()) {
            if(user.getFirstName().equals(fullName.split(" ")[0]) && user.getLastName().equals(fullName.split(" ")[1])) {

                print(data.get(user), user);
            }
        }
    }

    private static void print(HashMap<Film, Double> hashMap, Person user) {
        Set<Film> mustWatch = new HashSet<>();
        Set<Film> wantWatch = new HashSet<>();
        double maxValue = 1.0;
        double minValue = 0.0;
        boolean isRunning = true;

        while(isRunning) {
            if((mustWatch.size() >= 10) && (wantWatch.size() >= 10)){
                isRunning =false;
            }
            for (Film j : hashMap.keySet()) {
                if (mustWatch.size() < 10 && j.getGrade() == maxValue) {
                    if(!isUserSeenFilm(j , user)) {
                        mustWatch.add(j);
                    }
                }
                if (wantWatch.size() < 10 && j.getGrade() == minValue) {
                    wantWatch.add(j);
                }
            }
            maxValue =- 0.1;
            minValue =+ 0.1;
        }
        System.out.println("Musi zobaczyc");
        mustWatch.forEach( u -> System.out.println(u.getFilmName()+" #" + u.getGrade()));

        System.out.println("Nie chce zobaczyć");
        wantWatch.forEach( u -> System.out.println(u.getFilmName() + " #" + u.getGrade()));
    }

    private static boolean isUserSeenFilm(Film film, Person user) {
        for(Film f: user.getFilmsList()) {
            if(f.getFilmName().equals(film.getFilmName())) {
                return true;
            }
        }

        return false;
    }

}
