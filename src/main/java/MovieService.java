
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MovieService {
    ErrorHandler errorHandler = new ErrorHandler();
    public Integer FindMovieIndex(Integer id, List<Movie> movies){
        for(Movie movie : movies){
            if(movie.Id == id)
                return movies.indexOf(movie);
        }
        return -1;
    }
    public boolean IsCastValid(List<Integer> cast, List<Actor> actors){
        for(Integer actorId : cast){
            if(ActorService.FindActorIndex(actorId, actors) == -1)
                return false;
        }
        return true;
    }
    public JSONObject GetMovieById(JSONObject jsonObject, List<Movie> movies, List<Actor> actors){
        Integer movieId = Integer.parseInt(jsonObject.get("movieId").toString());
        Integer movieIndex = FindMovieIndex(movieId, movies);
        if(movieIndex == -1){
            return errorHandler.fail("MovieNotFound");
        }
        JSONObject movieInfoJson = new JSONObject();
        movieInfoJson = movies.get(movieIndex).GetInfo(actors);
        return errorHandler.success(movieInfoJson);
    }
    public JSONObject AddMovie(JSONObject jsonObject, List<Movie> movies, List<Actor> actors) throws java.text.ParseException {
        SimpleDateFormat formatter=new SimpleDateFormat("yyy-MM-dd");
        JSONArray writers = jsonObject.getJSONArray("writers");
        JSONArray genres = jsonObject.getJSONArray("genres");
        String cast = jsonObject.get("cast").toString();
        List<String> writersList = Functions.ConvertStringToStringList(writers);
        List<String> genresList = Functions.ConvertStringToStringList(genres);
        List<Integer> castList = Functions.ConvertStringToIntList(cast);

        if(!IsCastValid(castList, actors)){
            return errorHandler.fail("ActorNotFound");
        }
        Movie newMovie = new Movie(
                Integer.parseInt(jsonObject.get("id").toString()),
                jsonObject.get("name").toString(),
                Integer.parseInt(jsonObject.get("ageLimit").toString()),
                Float.parseFloat(jsonObject.get("imdbRate").toString()),
                jsonObject.get("summary").toString(),
                jsonObject.get("director").toString(),
                Integer.parseInt(jsonObject.get("duration").toString()),
                formatter.parse(jsonObject.get("releaseDate").toString()),
                writersList,
                genresList,
                castList
        );

        Integer movieId = Integer.parseInt(jsonObject.get("id").toString());
        Integer movieIndex = FindMovieIndex(movieId, movies);
        if(movieIndex != -1) {
            return UpdateMovie(movieIndex, newMovie, movies);
        }
        movies.add(newMovie);
        return errorHandler.success("movie added successfully");
    }

    public JSONObject UpdateMovie(Integer movieIndex, Movie movie, List<Movie> movies) throws java.text.ParseException {
        movies.set(movieIndex, movie);
        return errorHandler.success("movie updated successfully");
    }

    public JSONObject GetMoviesList(List<Movie> movies) throws java.text.ParseException {
        JSONObject moviesListJson = new JSONObject();
        List<JSONObject> moviesList = new ArrayList<JSONObject>();
        for(Movie movie : movies)
            moviesList.add(movie.getMovieJson());
        moviesListJson.put("MoviesList", moviesList);
        return errorHandler.success(moviesListJson);
    }

    public JSONObject GetMoviesByGenre(JSONObject jsonObject, List<Movie> movies) throws java.text.ParseException {
        String movieGenre = jsonObject.get("genre").toString();
        JSONObject moviesListJson = new JSONObject();
        List<JSONObject> moviesList = new ArrayList<JSONObject>();
        for(Movie movie : movies){
            if(movie.HasGenre(movieGenre)) {
                moviesList.add(movie.getMovieJsonGenre());
            }
        }
        moviesListJson.put("MoviesListByGenre", moviesList);
        return errorHandler.success(moviesListJson);
    }

}
