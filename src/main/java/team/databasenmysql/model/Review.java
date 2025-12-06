package team.databasenmysql.model;

public class Review {
    private Grade grade;

    public Review(Grade grade) {
        this.grade = grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Grade getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Review{" +
                "grade=" + grade +
                '}';
    }
}
