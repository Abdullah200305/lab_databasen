package team.databasenmysql.model;

import java.sql.Date;

public class Review {
    private Grade grade;
    private String summary;
    private Date date;
    private String ssn;

    public Review(){
        this.grade = null;
        this.summary = null;
        this.date = null;
    }
    public Review(Grade grade, String summary, Date date) {
        this.grade = grade;
        this.summary = summary;
        this.date = date;
    }
    public Review(Grade grade, String summary, Date date, String ssn) {
        this.grade = grade;
        this.summary = summary;
        this.date = date;
        this.ssn = ssn;
    }

    public String getSsn() {
        return ssn;
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
        return grade.toString();
    }
}
