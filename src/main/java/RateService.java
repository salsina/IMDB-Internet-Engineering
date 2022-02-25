
import org.json.JSONObject;

import java.util.List;

public class RateService {
    UserService userService = new UserService();
    MovieService movieService = new MovieService();
    ErrorHandler errorHandler = new ErrorHandler();

    public JSONObject AddRateToMovie(JSONObject jsonObject, List<User> users, List<Movie> movies){
        Integer movieId = Integer.parseInt(jsonObject.get("movieId").toString());
        Integer movieIndex = movieService.FindMovieIndex(movieId, movies);
        String userEmail = jsonObject.get("userEmail").toString();

        if(!userService.UserExists(userEmail, users)){
            return errorHandler.fail("UserNotFound");
        }
        if(movieIndex == -1){
            return errorHandler.fail("MovieNotFound");
        }

        int score = Integer.parseInt(jsonObject.get("score").toString());
        if(score < 1 || score > 10){
            return errorHandler.fail("InvalidRateScore");
        }

        if(movies.get(movieIndex).checkForRateUpdates(userEmail, score)){
            return errorHandler.success("movie rated successfully");
        }

        Rate newRate = new Rate(
                userEmail,
                movieId,
                score
        );

        movies.get(movieIndex).AddRate(newRate);
        return errorHandler.success("movie rated successfully");
    }
}
