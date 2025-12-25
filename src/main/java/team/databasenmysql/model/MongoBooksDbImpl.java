package team.databasenmysql.model;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;
import team.databasenmysql.model.exceptions.ConnectionException;
import team.databasenmysql.model.exceptions.SelectException;

import java.util.ArrayList;
import java.util.List;

public class MongoBooksDbImpl implements IBooksDb{
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> books;
    private MongoCollection<Document> users;
    private MongoCollection<Document> counters;
    private User currentUser;

    @Override
    public boolean connect(String databaseName) throws ConnectionException {
        try {

            // utan app user måste implementeras senare !!!
            // kanske löste ???
            client = MongoClients.create(
                    "mongodb://app_user:password@localhost:27017/" + databaseName
            );

            database = client.getDatabase(databaseName);

            books = database.getCollection("books");
            users = database.getCollection("users");
            counters = database.getCollection("counters");

            return true;

        }
        catch (Exception e){
            throw new ConnectionException("Could not connect to MongoDB", e);
        }
    }

    public void disconnect() throws ConnectionException{
        try {
            if (client != null){
                client.close();
            }
        } catch (Exception e){
            throw new ConnectionException("Error disconnecting MongoDB", e);
        }
    }
    private int getNextSequence(String name) {
        Document doc = counters.findOneAndUpdate(
                Filters.eq("_id", name),
                Updates.inc("seq", 1),
                new FindOneAndUpdateOptions().upsert(true)
                        .returnDocument(ReturnDocument.AFTER)
        );
        return doc.getInteger("seq");
    }
    @Override
    public List<Book> findBooksByTitle(String title) throws SelectException {

        List<Book> result = new ArrayList<>();

        try {
            FindIterable<Document> docs =
                    books.find(Filters.regex("title", title, "i"));

            for (Document doc : docs) {
//                result.add(mapToBook(doc));
            }

        } catch (Exception e) {
            throw new SelectException("Search by title failed", e);
        }

        return result;
    }



    @Override
    public Review insertReview(Review review, String isbn) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
