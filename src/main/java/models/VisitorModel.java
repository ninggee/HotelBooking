package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "visitors")
public class VisitorModel extends Model {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "gender")
    private String gender;

    @DatabaseField(columnName = "identity_card")
    private String identityCard;

    public VisitorModel() {
    }

    public VisitorModel(String gender, String identityCard) {
        this.gender = gender;
        this.identityCard = identityCard;
    }

    public VisitorModel(int id, String gender, String identityCard) {
        this.id = id;
        this.gender = gender;
        this.identityCard = identityCard;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }
}
