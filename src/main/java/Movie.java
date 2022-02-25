
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Movie {
    Integer Id, AgeLimit, Duration;
    float IMDBRate;
    String Name, Summary, Director;
    String ReleaseDate;
    List<String> Writers;
    List<String> Genres;
    List<Integer> Cast;
    List<Comment> Comments;
    List<Rate> Rates;
    BigDecimal Score;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Movie(Integer id, String name, Integer ageLimit, float imdbRate, String summary, String director,
                 Integer duration,Date releaseDate, List<String> writers, List<String> genres, List<Integer> cast){
        Id = id;
        Name = name;
        IMDBRate = imdbRate;
        AgeLimit = ageLimit;
        Summary = summary;
        Director = director;
        Duration = duration;
        ReleaseDate = dateFormat.format(releaseDate);
        Writers = writers;
        Genres = genres;
        Cast = cast;
        Comments = new ArrayList<Comment>();
        Rates = new ArrayList<Rate>();
        Score = null;
    }

    public void AddComment(Comment comment){
        Comments.add(comment);
    }

    public void AddRate(Rate rate){
        Rates.add(rate);
        UpdateScores();
    }
    private void UpdateScores(){
        double sum = 0;
        for(Rate rate : Rates){
            sum += rate.Score;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        BigDecimal ans = new BigDecimal((sum / Rates.size()));
        Score = ans.setScale(1, RoundingMode.HALF_UP);
    }

    public boolean checkForRateUpdates(String userEmail, Integer score){
        for(Rate rate : Rates){
            if(rate.UserEmail.equals(userEmail)) {
                rate.Score = score;
                UpdateScores();
                return true;
            }
        }
        return false;
    }


    public JSONObject getMovieJson(){
        JSONObject movieJson = new JSONObject();
        movieJson.put("movieId", Id);
        movieJson.put("name", Name);
        movieJson.put("director", Director);
        movieJson.put("genres", Genres);
        movieJson.put("rating", Score);
        return movieJson;
    }

    public List<JSONObject> GetCastInfo(List<Actor> actors){
        List<JSONObject> castInfo = new ArrayList<JSONObject>();
        for(Integer actorId : Cast){
            JSONObject info = new JSONObject();
            Integer actorIndex = ActorService.FindActorIndex(actorId, actors);
            Actor actor = actors.get(actorIndex);
            info.put("actorId", actor.Id);
            info.put("name", actor.Name);
            castInfo.add(info);
        }
        return castInfo;
    }
    public List<JSONObject> GetCommentsInfo(){
        List<JSONObject> commentInfo = new ArrayList<JSONObject>();
        for(Comment comment : Comments){
            JSONObject info = new JSONObject();
            info.put("commentId", comment.Id);
            info.put("userEmail", comment.UserEmail);
            info.put("text", comment.Text);
            info.put("like", comment.Likes);
            info.put("dislike", comment.Dislikes);
            commentInfo.add(info);
        }
        return commentInfo;
    }

    public JSONObject GetInfo(List<Actor> actors){
        JSONObject movieJson = new JSONObject();
        movieJson.put("movieId", Id);
        movieJson.put("name", Name);
        movieJson.put("summary", Summary);
        movieJson.put("releaseDate", ReleaseDate);
        movieJson.put("director", Director);
        movieJson.put("writers", Writers);
        movieJson.put("genres", Genres);
        movieJson.put("cast", GetCastInfo(actors));
        movieJson.put("rating", Score);
        movieJson.put("duration", Duration);
        movieJson.put("ageLimit", AgeLimit);
        movieJson.put("comments", GetCommentsInfo());
        return movieJson;
    }

    public boolean HasGenre(String genre){
        return Genres.contains(genre);
    }

    public JSONObject getMovieJsonGenre(){
        JSONObject movieJson = new JSONObject();
        movieJson.put("movieId", Id);
        movieJson.put("name", Name);
        movieJson.put("director", Director);
        movieJson.put("genres", Genres);
        if(Score == null)
            movieJson.put("rating", JSONObject.NULL);
        else
            movieJson.put("rating", Score);
        return movieJson;
    }

}
