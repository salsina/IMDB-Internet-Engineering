import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;


class MovieServiceTest {
    JSONObject jsonObject;
    Movie movie;
    IEDMB iemdb;

    @BeforeEach
    void setUp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse("2000-06-14", formatter);
        SimpleDateFormat formatter2=new SimpleDateFormat("yyy-MM-dd");
        Date releaseDate = new Date();
        try {
            releaseDate = formatter2.parse("1972-03-14");
        }catch (ParseException e){}
        List<String> emptyListString = new ArrayList<String>();
        List<String> GenreList = new ArrayList<String>();
        List<Integer> emptyListInteger = new ArrayList<Integer>();
        movie = new Movie(1, "The Godfather", 14, (float)9.2, "The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.", "Francis Ford Coppola",
                175 ,releaseDate, emptyListString, Arrays.asList("Mystery", "Drama"), emptyListInteger);
        iemdb = new IEDMB();
    }

    @Test
    void getMoviesByGenre_EmptyMoviesList() {
        List<JSONObject> moviesList = new ArrayList<JSONObject>();
        JSONObject moviesListJson = new JSONObject();
        moviesListJson.put("MoviesListByGenre", moviesList);

        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", moviesListJson);
        expectedOutput.put("success", true);

        jsonObject = new JSONObject("{\"genre\": \"Mystery\"}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("getMoviesByGenre", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    void getMoviesByGenre_OneElementMoviesList() {
        iemdb.movies.add(movie);
        List<JSONObject> moviesList = new ArrayList<JSONObject>();
        moviesList.add(movie.getMovieJsonGenre());
        JSONObject moviesListJson = new JSONObject();
        moviesListJson.put("MoviesListByGenre", moviesList);

        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", moviesListJson);
        expectedOutput.put("success", true);

        jsonObject = new JSONObject("{\"genre\": \"Mystery\"}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("getMoviesByGenre", jsonObject).toString());
        }catch(Exception e){}
    }

    @AfterEach
    void tearDown() {
        iemdb = null;
    }
}