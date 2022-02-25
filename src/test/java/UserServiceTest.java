import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    JSONObject jsonObject;
    User user, youngUser;
    Movie movie;
    IEDMB iemdb;

    @BeforeEach
    void setUp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse("2000-06-14", formatter);
        LocalDate YoungLocalDate = LocalDate.parse("2018-06-14", formatter);

        SimpleDateFormat formatter2=new SimpleDateFormat("yyy-MM-dd");
        Date releaseDate = new Date();
        try {
            releaseDate = formatter2.parse("1972-03-14");
        }catch (ParseException e){}
        List<String> emptyListString = new ArrayList<String>();
        List<Integer> emptyListInteger = new ArrayList<Integer>();
        user = new User("sajjad@ut.ac.ir", "sajjad1234", "sajjad", "Sajjad", localDate);
        youngUser = new User("saman@ut.ac.ir", "saman1234", "saman", "saman", YoungLocalDate);
        movie = new Movie(1, "The Godfather", 14, (float)9.2, "The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.", "Francis Ford Coppola",
                175 ,releaseDate, emptyListString, emptyListString, emptyListInteger);
        iemdb = new IEDMB();
    }

    @Test
    void addToWatchList_UserNotExist() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "UserNotFound");
        expectedOutput.put("success", false);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("addToWatchList", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    void addToWatchList_MovieNotExist() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "MovieNotFound");
        expectedOutput.put("success", false);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1}");
        iemdb.users.add(user);
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("addToWatchList", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    void addToWatchList_AgeLimitError() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "AgeLimitError");
        expectedOutput.put("success", false);
        jsonObject = new JSONObject("{\"userEmail\": \"saman@ut.ac.ir\", \"movieId\": 1}");
        iemdb.users.add(youngUser);
        iemdb.movies.add(movie);
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("addToWatchList", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    void addToWatchList_Success() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "movie added to watchlist successfully");
        expectedOutput.put("success", true);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1}");
        iemdb.users.add(user);
        iemdb.movies.add(movie);
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("addToWatchList", jsonObject).toString());
            assertEquals(movie.Id, iemdb.users.get(0).watchList.get(0).Id);
        }catch(Exception e){}
    }

    @Test
    void addToWatchList_MovieAlreadyExists() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "MovieAlreadyExists");
        expectedOutput.put("success", false);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1}");
        iemdb.users.add(user);
        iemdb.movies.add(movie);
        try{
            iemdb.InputHandler("addToWatchList", jsonObject);
        }catch(Exception e){}
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("addToWatchList", jsonObject).toString());
        }catch(Exception e){}
    }

    @AfterEach
    void tearDown() {
        iemdb = null;
    }
}