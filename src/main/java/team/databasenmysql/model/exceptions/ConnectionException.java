package team.databasenmysql.model.exceptions;

import java.io.IOException;
import java.sql.SQLException;

public class ConnectionException extends IOException{
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(){
    }
}
