
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    String Email;
    String Password;
    String Nickname;
    String Name;
    LocalDate BirthDate;
    List<Movie> watchList;
    public User(String email, String password, String nickname, String name, LocalDate birthDate){
        Email = email;
        Password = password;
        Nickname = nickname;
        Name = name;
        BirthDate = birthDate;
        watchList = new ArrayList<Movie>();
    }

    public void AddToWatchList(Movie movie){
        watchList.add(movie);
    }
    public void RemoveFromWatchList(Movie movie){
        watchList.remove(movie);
    }

}
