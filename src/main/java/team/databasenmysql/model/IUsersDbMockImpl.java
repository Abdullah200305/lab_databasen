package team.databasenmysql.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IUsersDbMockImpl implements IUsersDb{
   /* private final User user;
    private final Connection conn;
    public IUsersDbMockImpl(User user,Connection conn) {
        this.user = user;
        this.conn =conn;
    }
    public User getUser() {
        return user;
    }

    @Override
    public User CheckUser(String User,String password){
        User result = null;
        System.out.println(password);
        String sql = String.format("SELECT FULL_NAME,PASSKODE,SSN\n" +
                "FROM t_customer\n" +
                "WHERE FULL_NAME = '%s'\n" +
                "  AND PASSKODE = '%s';\n",User,password);
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                result = new User(rs.getString(3),rs.getString(2),rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(result);
        user = result;
        return result;
    };*/
}
