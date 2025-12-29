package team.databasenmysql.model;

import com.mongodb.MongoClientException;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import team.databasenmysql.model.exceptions.ConnectionException;
import team.databasenmysql.model.exceptions.InsertException;
import team.databasenmysql.model.exceptions.SelectException;
import team.databasenmysql.view.UpdateChoice;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MongoBooksDbImpl implements IBooksDb {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> booksCollection;
    private MongoCollection<Document> usersCollection;
    private User currentUser;

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean connect(String dbName) throws ConnectionException {
       try {
           mongoClient = MongoClients.create("mongodb://bibliotek_app:app123@localhost:27017/" + dbName);
           /* mongoClient = MongoClients.create("mongodb://localhost:27017/"+dbName);*/
           database = mongoClient.getDatabase(dbName);
           booksCollection = database.getCollection("books");
           usersCollection = database.getCollection("users");
           return true;
       }
       catch (MongoClientException e){
           throw new ConnectionException("Could not connect to MongoDB", e);
       }
    }

    @Override
    public void disconnect() {
        if (mongoClient != null) mongoClient.close();
    }

    // ------------------ SELECT ------------------

    @Override
    public List<Book> findBooksByTitle(String title) throws SelectException {
        List<Book> result = new ArrayList<>();
        try {
            FindIterable<Document> docs = booksCollection.find(Filters.regex("Title", ".*" + title + ".*", "i"));
            for (Document doc : docs) {
                result.add(mapDocumentToBook(doc));
            }
            return result;
        }
        catch (MongoException e){
            throw new SelectException("Books with title not found", e);
        }
    }

    @Override
    public List<Book> findBooksByIsbn(String isbn) throws SelectException {
        List<Book> result = new ArrayList<>();
        try {
        Document doc = booksCollection.find(Filters.eq("ISBN", isbn)).first();
        if (doc != null) result.add(mapDocumentToBook(doc));
        return result;}
        catch (MongoException e) {
            throw new SelectException("Book with ISBN not found", e);
        }
    }

    @Override
    public List<Book> findBooksByAuthor(String authorName) throws SelectException {
        List<Book> result = new ArrayList<>();
        try {
        FindIterable<Document> docs = booksCollection.find(Filters.elemMatch("Authors",
                Filters.regex("authorName", ".*" + authorName + ".*", "i")));
        for (Document doc : docs) result.add(mapDocumentToBook(doc));
        return result;}
        catch (MongoException e) {
            throw new SelectException("Books by author not found", e);
        }
    }

    @Override
    public List<Book> findBooksByGrade(String grade) throws SelectException {
        List<Book> result = new ArrayList<>();
        try {
        FindIterable<Document> docs = booksCollection.find(Filters.eq("Reviews.grade", grade));
        for (Document doc : docs) result.add(mapDocumentToBook(doc));
        return result;}
        catch (MongoException e) {
            throw new SelectException("Books with grade not found", e);
        }
    }

    @Override
    public List<Book> findBooksByGenre(String genre) throws SelectException {
        List<Book> result = new ArrayList<>();
        try {
        FindIterable<Document> docs = booksCollection.find(Filters.regex("Genres", ".*" + genre + ".*", "i"));
        for (Document doc : docs) result.add(mapDocumentToBook(doc));
        return result;}
        catch (MongoException e) {
            throw new SelectException("Books with genre not found", e);
        }
    }

    // ------------------ INSERT ------------------

    @Override
    public Book InsertBook(Book book) throws InsertException {
        try {
            int bookId = getNextSequence("bookId");
            Document doc = new Document("_id", bookId)
                    .append("ISBN", book.getIsbn())
                    .append("Title", book.getTitle())
                    .append("Published", book.getPublished().toString());

            // Authors
            List<Document> authors = new ArrayList<>();
            for (Authors a : book.getAuthors()) {
                Document adoc = new Document("authorId", a.getAuthorId())
                        .append("authorName", a.getAuthorName())
                        .append("birthDate", a.getBirthDate().toString());
                authors.add(adoc);
            }
            doc.append("Authors", authors);

            // Genres
            doc.append("Genres", book.getGenres());

            // Reviews (empty initially)
            doc.append("Reviews", new ArrayList<Document>());

            booksCollection.insertOne(doc);
            return book;
        }
        catch (MongoException e) {
            throw new InsertException("Could not insert book: " + book.getTitle(), e);
        }
    }
    private int getNextSequence(String name) {
        MongoCollection<Document> counterCollection =
                database.getCollection("Counter");

        Document filter = new Document("_id", name);
        Document update = new Document("$inc", new Document("seq", 1));

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)
                .upsert(true);

        Document result =
                counterCollection.findOneAndUpdate(filter, update, options);

        return result.getInteger("seq");
    }


    @Override
    public Book DeleteBook(String isbn) {
        try {
            Document doc = booksCollection.findOneAndDelete(Filters.eq("ISBN", isbn));
            return doc != null ? mapDocumentToBook(doc) : null;
        }
        catch (MongoException e){
           throw new RuntimeException();
        }
    }

    @Override
    public Book UppdateBook(UpdateChoice choiceValue, String newValue, String oldValue) throws SelectException {
        // Implementera med MongoDB update
        // Exempel: update title
        String isbn = choiceValue.getIsbn();
        try {
        switch (choiceValue.getMode()) {
            case Title -> booksCollection.updateOne(Filters.eq("ISBN", isbn),
                    new Document("$set", new Document("Title", newValue)));
            case Author -> booksCollection.updateOne(
                    Filters.and(Filters.eq("ISBN", isbn),
                            Filters.elemMatch("Authors", Filters.eq("authorName", oldValue))),
                    new Document("$set", new Document("Authors.$.authorName", newValue)));
            case Genera -> booksCollection.updateOne(
                    Filters.eq("ISBN", isbn),
                    new Document("$set", new Document("Genres.$[elem]", newValue)),
                    new com.mongodb.client.model.UpdateOptions()
                            .arrayFilters(List.of(Filters.eq("elem", oldValue))));
        }
        return findBooksByIsbn(isbn).get(0);}
        catch (MongoException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Review insertReview(Review review, String isbn) {
        try {
        Document rDoc = new Document("ssn", currentUser.getSSN())
                .append("grade", review.getGrade().toString())
                .append("summary", review.getSummary())
                .append("reviewDate", review.getDate().toString());

        booksCollection.updateOne(
                Filters.eq("ISBN", isbn),
                new Document("$push", new Document("Reviews", rDoc))
        );
        return review;} catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    // ------------------ USER ------------------

    @Override
    public User CheckUser(String username, String password) {

        Document doc = usersCollection.find(Filters.and(
                Filters.eq("fullName", username),
                Filters.eq("password", password)
        )).first();
        if (doc != null) {
            currentUser = new User(doc.getString("ssn"), doc.getString("password"), doc.getString("fullName"));
        }
        return currentUser;
    }

    @Override
    public List<Authors> bringAuthors() {
        List<Authors> result = new ArrayList<>();
        FindIterable<Document> docs = booksCollection.find();
        for (Document doc : docs) {
            List<Document> authors = doc.getList("Authors", Document.class, new ArrayList<>());
            if (authors != null) {
                for (Document a : authors) {
                    result.add(new Authors(a.getInteger("authorId",0), a.getString("authorName"),
                            parseSqlDate(a.get("birthDate"))));
                }
            }
        }
        return result;
    }

    // ------------------ MAPPER ------------------
    private Book mapDocumentToBook(Document doc) {
        String isbn = doc.getString("ISBN");
        String title = doc.getString("Title");

        Date published = Date.valueOf(doc.getString("Published"));

        Book book = new Book(isbn, title, published);

        // ---------- Authors ----------
        List<Document> authorDocs =
                doc.getList("Authors", Document.class, new ArrayList<>());

        for (Document aDoc : authorDocs) {
            int authorId = aDoc.getInteger("authorId", 0);
            String authorName = aDoc.getString("authorName");

            Date birthDate = Date.valueOf(aDoc.getString("birthDate"));

            book.addAuthor(new Authors(authorId, authorName, birthDate));
        }

        // ---------- Genres ----------
        List<String> genres =
                doc.getList("Genres", String.class, new ArrayList<>());
        for (String g : genres) book.addGenre(g);

        // ---------- Reviews ----------
        List<Document> reviewDocs =
                doc.getList("Reviews", Document.class, new ArrayList<>());

        for (Document rDoc : reviewDocs) {
            Grade grade = Grade.valueOf(rDoc.getString("grade"));
            String summary = rDoc.getString("summary");
            Date reviewDate = Date.valueOf(rDoc.getString("reviewDate"));
            String ssn = rDoc.getString("ssn");

            book.addReviews(new Review(grade, summary, reviewDate, ssn));
        }

        return book;
    }

    private Date parseSqlDate(Object value) {
        if (value == null) return null;

        if (value instanceof java.util.Date utilDate) {
            return new Date(utilDate.getTime());
        }

        if (value instanceof String str) {
            return Date.valueOf(str); // yyyy-MM-dd
        }

        throw new IllegalArgumentException("Unsupported date type: " + value.getClass());
    }


}
