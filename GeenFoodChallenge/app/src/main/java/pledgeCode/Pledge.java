package pledgeCode;

import com.google.firebase.database.DatabaseReference;

import java.sql.Timestamp;

public class Pledge {
    String name;
    String municipality;
    int co2Pledged;
    Long timeStamp;
    public Pledge(){
        //this is used when retrieving data from firebase, it will recreate a Pledge with it
    }
    public Pledge(String name, String municipality, int co2Pledged,Long timeStamp){
        this.name = name;
        this.municipality = municipality;
        this.co2Pledged = co2Pledged;
        this.timeStamp = timeStamp;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public int getCo2Pledged() {
        return co2Pledged;
    }

    public void setCo2Pledged(int co2Pledged) {
        this.co2Pledged = co2Pledged;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }


}

