package models;


import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class Model {
    private static String databaseUrl = "jdbc:mysql://121.193.130.195:3306/HotelBooking";
    public static ConnectionSource connectionSource;
    static {
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl);
            ((JdbcConnectionSource)connectionSource).setUsername("hotel");
            ((JdbcConnectionSource)connectionSource).setPassword("hoteluser");
        } catch (Exception e) {

        }
    }

    public Model() {

    }
}
