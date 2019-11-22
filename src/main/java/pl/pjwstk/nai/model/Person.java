package pl.pjwstk.nai.model;

import java.util.List;

public class Person {
    private String firstName;
    private String lastName;
    private String group;
    private List<Film> filmsList;

    public Person() {
    }

    public Person(String firstName, String lastName, String group, List<Film> filmsList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        this.filmsList = filmsList;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Film> getFilmsList() {
        return filmsList;
    }

    public void setFilmsList(List<Film> filmsList) {
        this.filmsList = filmsList;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", group='" + group + '\'' +
                ", filmsList=" + filmsList +
                '}';
    }
}
