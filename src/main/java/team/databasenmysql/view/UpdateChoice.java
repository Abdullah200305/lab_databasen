package team.databasenmysql.view;

import team.databasenmysql.model.SearchMode;

public class UpdateChoice {
    private final String isbn;
    private final SearchMode mode;
    private String old_item;
    private String new_item;

    public UpdateChoice(String isbn, SearchMode mode) {
        this.isbn = isbn;
        this.mode = mode;
    }

    public void setNew_item(String new_item) {
        this.new_item = new_item;
    }

    public void setOld_item(String old_item) {
        this.old_item = old_item;
    }

    public String getNew_item() {
        return new_item;
    }

    public String getOld_item() {
        return old_item;
    }

    public String getIsbn() {
        return isbn;
    }

    public SearchMode getMode() {
        return mode;
    }
}
