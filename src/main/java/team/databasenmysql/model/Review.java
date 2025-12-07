package team.databasenmysql.model;

import java.sql.Date;

public class Review {
    private Grade grade;
    private String summary;
    private Date date;
   /* private Book book;
    private User user;*/

    /// needed to change in other classes
/*    public Review(Grade grade) {
        this.grade = grade;
    }*/

    public Review(Grade grade, String summary, Date date) {
        this.grade = grade;
        this.summary = summary;
        this.date = date;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Grade getGrade() {
        return grade;
    }

    public String getSummary() {
        return summary;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Review{" +
                "grade=" + grade +
                '}';
    }
}
