import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.mockito.Mockito.*;

import org.mockito.Mock;

class RateServiceTest {
    JSONObject jsonObject;
    User user;
    Movie movie;
    IEDMB iemdb;

    @BeforeEach
    public void setUp(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse("2000-06-14", formatter);
        SimpleDateFormat formatter2=new SimpleDateFormat("yyy-MM-dd");
        Date releaseDate = new Date();
        try {
            releaseDate = formatter2.parse("1972-03-14");
        }catch (ParseException e){}
        List<String> emptyListString = new ArrayList<String>();
        List<Integer> emptyListInteger = new ArrayList<Integer>();
        user = new User("sajjad@ut.ac.ir", "sajjad1234", "sajjad", "Sajjad", localDate);
        movie = new Movie(1, "The Godfather", 14, (float)9.2, "The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.", "Francis Ford Coppola",
                175 ,releaseDate, emptyListString, emptyListString, emptyListInteger);
        iemdb = new IEDMB();
    }

    @Test
    public void addRateToMovie_UserNotExist() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "UserNotFound");
        expectedOutput.put("success", false);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"score\": 8}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("rateMovie", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    public void addRateToMovie_MovieNotExist()  {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "MovieNotFound");
        expectedOutput.put("success", false);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"score\": 8}");
        iemdb.users.add(user);
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("rateMovie", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    public void addRateToMovie_invalidRateScore() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "InvalidRateScore");
        expectedOutput.put("success", false);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"score\": 12}");
        iemdb.users.add(user);
        iemdb.movies.add(movie);
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("rateMovie", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    public void addRateToMovie_SuccessAdd() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "movie rated successfully");
        expectedOutput.put("success", true);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"score\": 8}");
        iemdb.users.add(user);
        iemdb.movies.add(movie);
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("rateMovie", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    public void addRateToMovie_SuccessUpdate() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "movie rated successfully");
        expectedOutput.put("success", true);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"score\": 8}");
        iemdb.users.add(user);
        iemdb.movies.add(movie);
        try{
            iemdb.InputHandler("rateMovie", jsonObject);
        }catch(Exception e){}

        JSONObject jsonObjectUpdated = new JSONObject("{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"score\": 9}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("rateMovie", jsonObjectUpdated).toString());
        }catch(Exception e){}
        assertEquals(jsonObjectUpdated.getInt("score"), iemdb.movies.get(0).Rates.get(0).Score);
    }

    @AfterEach
    public void tearDown() {
        iemdb = null;
    }
}