package team.databasenmysql.model;

import java.util.ArrayList;
import java.util.Date;
///  by Chefen
public class Authors {
    private int authorId;
    private String name;
    private Date birthDate;

    public Authors(int authorId, String authorName, Date birthDate) {
        this.authorId = authorId;
        this.name = authorName;
        this.birthDate = birthDate;
    }
    public Authors(String authorName){
        this(-1,authorName,null);
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return name;
    }
}
