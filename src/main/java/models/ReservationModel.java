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

    @DatabaseField(columnName = "visitor_id")
    private int visitor_id;

    @DatabaseField(columnName = "start_date")
    private Date start_date;

    @DatabaseField(columnName = "end_date")
    private Date end_date;

    @DatabaseField(columnName = "add_time")
    private String addTime;

    public ReservationModel() {
    }

    //创建新的reservation的时候不需要指定id
    public ReservationModel(int room_id, int visitor_id, Date start_date, Date end_date) {
        this.room_id = room_id;
        this.visitor_id = visitor_id;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    //更新reservation的时候需要指定id
    public ReservationModel(int id, int room_id, int visitor_id, Date start_date, Date end_date) {
        this.id = id;
        this.room_id = room_id;
        this.visitor_id = visitor_id;
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

    public int getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(int user_id) {
        this.visitor_id = user_id;
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
    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
