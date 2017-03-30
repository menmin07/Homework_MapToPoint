package kg.app.kuba.homework_maptopoint;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by admin on 29.03.2017.
 */

public class Pointinfo implements Serializable {

    private String placeName;
    private String description;
    private String position;
    private String dateofmark;
    private String timeofmark;

    public void setPlaceName(String placeName){
        this.placeName = placeName;
    }
        public String getPlaceName(){
        return placeName;
    }


    public void setDescription(String description){
        this.description = description;
    }
        public String getDescription(){
        return description;
    }

    public void setPosition(String position){
        this.position = position;
    }
    public String getPosition(){
        return position;
    }


    public void setDateofmark(String dateofmark){
        this.dateofmark = dateofmark;
    }
    public String getDateofmark(){
        return dateofmark;
    }


    public void setTimeofmark(String timeofmark){
        this.timeofmark = timeofmark;
    }
    public String getTimeofmark(){
        return timeofmark;
    }


    public Pointinfo(){
        placeName = " ";
        description = " ";
        position = " ";
        dateofmark = " ";
        timeofmark = " ";
    }

}
