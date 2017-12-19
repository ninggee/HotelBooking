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
    private String identity_card;


    public VisitorModel() {
    }

    public VisitorModel(String gender, String identityCard) {
        this.gender = gender;
        this.identity_card = identityCard;
    }

    public VisitorModel(int id, String gender, String identityCard) {
        this.id = id;
        this.gender = gender;
        this.identity_card = identityCard;
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
        return identity_card;
    }

    public void setIdentityCard(String identity_card) {
        this.identity_card = identity_card;
    }

    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
    }
}
