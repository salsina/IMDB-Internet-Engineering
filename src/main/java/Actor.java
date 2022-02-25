
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Actor {
    public Integer Id;
    String Name;
    Date BirthDate;
    String Nationality;
    public Actor(Integer id, String name, Date birthDate, String nationality){
        Id = id;
        Name = name;
        BirthDate = birthDate;
        Nationality = nationality;
    }
}
