
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserService {

    ErrorHandler errorHandler = new ErrorHandler();
    MovieService movieService = new MovieService();
    public boolean UserExists(String email, List<User> users){
        for(User user : users){
            if(user.Email.equals(email))
                return true;
        }
        return false;
    }

    public Integer FindUserIndex(String email, List<User> users){
        for(User user : users){
            if(user.Email.equals(email))
                return users.indexOf(user);
        }
        return -1;
    }

    public JSONObject AddUser(JSONObject jsonObject, List<User> users) throws java.text.ParseException {
        if(UserExists(jsonObject.get("email").toString(), users)){
            return errorHandler.fail("user with that email already exists");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String birthDate = jsonObject.getString("birthDate");
        LocalDate localDate = LocalDate.parse(birthDate, formatter);

        users.add(new User(
                jsonObject.get("email").toString(),
                jsonObject.get("password").toString(),
                jsonObject.get("nickname").toString(),
                jsonObject.get("name").toString(),
                localDate
        ));
        return errorHandler.success("user added successfully");
    }

    public Integer findUserAge(LocalDate birthDate){
        LocalDate today = LocalDate.now();
        Period period = birthDate.until(today);
        return period.getYears();
    }
    public JSONObject AddToWatchList(JSONObject jsonObject, List<User> users, List<Movie> movies){
        String userEmail = jsonObject.get("userEmail").toString();
        Integer movieId = Integer.parseInt(jsonObject.get("movieId").toString());
        Integer movieIndex = movieService.FindMovieIndex(movieId, movies);

        if(!UserExists(userEmail, users)){
            return errorHandler.fail("UserNotFound");
        }
        if(movieIndex == -1){
            return errorHandler.fail("MovieNotFound");
        }

        Integer userIndex = FindUserIndex(userEmail, users);
        User currentUser = users.get(userIndex);
        Movie currentMovie = movies.get(movieIndex);

        Integer userAge = findUserAge(currentUser.BirthDate);
        Integer movieAgeLimit = currentMovie.AgeLimit;
        if(userAge < movieAgeLimit){
            return errorHandler.fail("AgeLimitError");
        }

        for(Movie movie : currentUser.watchList){
            if(movie.Id == currentMovie.Id){
                return errorHandler.fail("MovieAlreadyExists");
            }
        }
        users.get(userIndex).AddToWatchList(currentMovie);
        return errorHandler.success("movie added to watchlist successfully");
    }

    public JSONObject RemoveFromWatchList(JSONObject jsonObject, List<User> users, List<Movie> movies){
        String userEmail = jsonObject.get("userEmail").toString();
        Integer movieId = Integer.parseInt(jsonObject.get("movieId").toString());
        Integer movieIndex = movieService.FindMovieIndex(movieId, movies);

        if(!UserExists(userEmail, users)){
            return errorHandler.fail("UserNotFound");
        }

        Integer userIndex = FindUserIndex(userEmail, users);
        User currentUser = users.get(userIndex);
        Movie currentMovie = movies.get(movieIndex);

        for(Movie movie : currentUser.watchList){
            if(movie.Id == currentMovie.Id){
                users.get(userIndex).RemoveFromWatchList(currentMovie);
                return errorHandler.success("movie removed from watchlist successfully");
            }
        }
        return errorHandler.fail("MovieNotFound");
    }

    public JSONObject GetWatchList(JSONObject jsonObject, List<User> users) throws java.text.ParseException {
        String userEmail = jsonObject.get("userEmail").toString();

        if(!UserExists(userEmail, users)){
            return errorHandler.fail("UserNotFound");
        }

        User user = users.get(FindUserIndex(userEmail, users));
        JSONObject moviesListJson = new JSONObject();
        List<JSONObject> moviesList = new ArrayList<JSONObject>();
        for(Movie movie : user.watchList){
            moviesList.add(movie.getMovieJsonGenre());
        }
        moviesListJson.put("WatchList", moviesList);
        return errorHandler.success(moviesListJson);
    }

}
