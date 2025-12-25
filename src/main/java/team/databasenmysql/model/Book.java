package team.databasenmysql.model;

/*import java.sql.Date;*/
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
/**
 * Representation of a Book.
 * @author Abhasan@kth.se
 * A Book has an ISBN, title, published date, authors, genres, and reviews.
 * This class provides methods to add and retrieve authors, genres, and reviews.
 */
public class Book {
    private final String isbn; // should check format
    private final String title;
    private final Date published;
    private ArrayList<Authors> authors = new ArrayList<>();
    private ArrayList<String> genres = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();


    /**
     * Constructs a Book object with ISBN, title, and published date.
     *
     * @param isbn      the ISBN of the book
     * @param title     the title of the book
     * @param published the published date of the book
     */
    public Book(String isbn, String title, Date published) {
        this.isbn = isbn;
        this.title = title;
        this.published = published;
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
