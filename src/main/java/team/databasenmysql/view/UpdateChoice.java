package team.databasenmysql.view;

import team.databasenmysql.model.SearchMode;

public class UpdateChoice {
    private final String isbn;
    private final SearchMode mode;

    public UpdateChoice(String isbn, SearchMode mode) {
        this.isbn = isbn;
        this.mode = mode;
    }

    public String getIsbn() {
        return isbn;
    }

    public SearchMode getMode() {
        return mode;
    }
}
