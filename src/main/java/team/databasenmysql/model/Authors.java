package team.databasenmysql.model;

import java.util.ArrayList;
import java.util.Date;
///  by Chefen
public class Authors {
    private int authorId;
    private String authorName;
    private Date birthDate;

    public Authors(int authorId, String authorName, Date birthDate) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.birthDate = birthDate;
    }
    public Authors(String authorName) {
        this(-1,authorName,new Date());
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return authorName + " (" + birthDate + ")";
    }
}
