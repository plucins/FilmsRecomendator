package pl.pjwstk.nai.model;

public class Film {
    private String filmName;
    private int grade;

    public Film(String filmName, int grade) {
        this.filmName = filmName;
        this.grade = grade;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
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
