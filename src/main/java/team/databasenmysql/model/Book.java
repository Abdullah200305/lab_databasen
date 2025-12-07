package team.databasenmysql.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a book.
 *
 * @author anderslm@kth.se
 */
public class Book {
    private final String isbn; // should check format
    private final String title;
    private final Date published;
    private final String storyLine = "";

    private ArrayList<Authors> authors = new ArrayList<>();
    private ArrayList<String> genres = new ArrayList<>();
/*    private  Grade grade;
    private List<Review> reviews = new ArrayList<>();*/
    private List<Review> reviews = new ArrayList<>();
    private Review review = new Review(null,null,null);

    public Book(String isbn, String title, Date published, Grade grade) {
        this.isbn = isbn;
        this.title = title;
        this.published = published;
       /* this.grade = grade;*/
    }


    public Book(String isbn, String title, Date published) {
        this(isbn, title, published, null);
    }








    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {return title;}

    public void addAuthor(Authors author){
        authors.add(author);
    }

    public ArrayList<Authors> getAuthors() {
        ArrayList <Authors> temp  = authors;
        return temp;
    }



    public Date getPublished() {
        return published;
    }
    public String getStoryLine() {
        return storyLine;
    }

 /*   public void setReview(Review review) {
        this.review = review;
    }

    public R getReview() {
        return review.getGrade();
    }*/

  /*  public Grade getGrade(){
        System.out.println(grade);
        return grade;}

    public void setGrade(Grade grade) {
        this.grade = grade;
    }*/

    public void addGenre(String genre){
        genres.add(genre);
    }
    public ArrayList<String> getGenres(){
        ArrayList <String> temp  = genres;
        return temp;
    }



    public void addReviews(Review review){
        reviews.add(review);
    }
    public List<Review> getReviews() {
        return reviews;
    }

    @Override
    public String toString() {
        return title + ", " + isbn + ", Author:" + getAuthors() + "," + published.toString()+", Grade: "  + "," + getGenres();
    }
}
