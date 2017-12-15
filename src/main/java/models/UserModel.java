package models;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Example account object that is persisted to disk by the DAO and other example classes.
 */
@DatabaseTable(tableName = "users")
public class UserModel  extends Model{

    // for QueryBuilder to be able to find the fields
    public static final String NAME_FIELD_NAME = "name";
    public static final String PASSWORD_FIELD_NAME = "password";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false)
    private String name;

    @DatabaseField(columnName = PASSWORD_FIELD_NAME)
    private String password;

    @DatabaseField(columnName = "add_time")
    private String add_time;

    @DatabaseField(columnName = "is_admin")
    private boolean is_admin;

    public UserModel() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public boolean isIs_admin() {

        return is_admin;
    }


    public UserModel(String name, String password, boolean is_admin) {
        this.name = name;
        this.password = password;
        this.is_admin = is_admin;
    }

    public UserModel(String name) {
        this.name = name;
    }

    public UserModel(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return name.equals(((UserModel) other).name);
    }
}