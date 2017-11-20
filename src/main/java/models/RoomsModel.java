package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "rooms")
public class RoomsModel extends Model {
    @DatabaseField(generatedId = true)
    private int id ;

    @DatabaseField(columnName = "room_number")
    private int room_number;

    @DatabaseField(columnName = "room_type")
    private String room_type;

    @DatabaseField(columnName = "price")
    private int price;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "is_ordered")
    private boolean is_ordered;

    public RoomsModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoom_number() {
        return room_number;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_ordered() {
        return is_ordered;
    }

    public void setIs_ordered(boolean is_order) {
        this.is_ordered = is_order;
    }
}
