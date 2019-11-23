package pl.pjwstk.nai.model;

public class Film {
    private String filmName;
    private double grade;

    public Film(String filmName, double grade) {
        this.filmName = filmName;
        this.grade = grade;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Film{" +
                "filmName='" + filmName + '\'' +
                ", grade=" + grade +
                '}';
    }
}
