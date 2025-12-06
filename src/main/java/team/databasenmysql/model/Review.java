package team.databasenmysql.model;

public class Review {
    private Grade grade;
    private Book book;
    private User user;
    public Review(Grade grade,User user,Book book) {
        this.book = book;
        this.user = user;
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
