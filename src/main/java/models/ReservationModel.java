package models;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "orders")
public class ReservationModel extends Model{
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "room_id")
    private int room_id;

    @DatabaseField(columnName = "user_id")
    private int user_id;

    @DatabaseField(columnName = "start_date")
    private Date start_date;

    @DatabaseField(columnName = "end_date")
    private Date end_date;

    public ReservationModel() {
    }

    public ReservationModel(int room_id, int user_id, Date start_date, Date end_date) {
        this.room_id = room_id;
        this.user_id = user_id;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
}
