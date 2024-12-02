package Components;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Simulator {
    private String id;
    private String title;
    public enum SIMU_TYPE {KINETIC, ELECTROSTATICS}
    SIMU_TYPE simeType;
    String creationDateTime;
    String lastModifiedDateTime;

    public Simulator(String title, SIMU_TYPE simuType){
        this.title = title;
        this.simeType = simuType;
        this.creationDateTime = dateTimeBuilder();
        this.lastModifiedDateTime = "0";
    }

    //SETTERS AND GETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SIMU_TYPE getSimeType() {
        return simeType;
    }

    public void setSimeType(SIMU_TYPE simeType) {
        this.simeType = simeType;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(String lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public String dateTimeBuilder(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }

}
