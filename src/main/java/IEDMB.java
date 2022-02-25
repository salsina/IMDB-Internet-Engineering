import org.json.*;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class IEDMB {
    List<Actor> actors = new ArrayList<>();
    List<Movie> movies = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Comment> comments = new ArrayList<>();
    static ErrorHandler errorHandler = new ErrorHandler();
    ActorService actorService = new ActorService();
    MovieService movieService = new MovieService();
    UserService userService = new UserService();
    CommentService commentService = new CommentService();
    RateService rateService = new RateService();
    CommentVoteService commentVoteService = new CommentVoteService();

    public JSONObject InputHandler(String command, JSONObject jsonObject) throws java.text.ParseException {
        switch (command) {
            case "addActor":
                return actorService.AddActor(jsonObject, actors);
            case "addMovie":
                return movieService.AddMovie(jsonObject, movies, actors);
            case "addUser":
                return userService.AddUser(jsonObject, users);
            case "addComment":
                return commentService.AddCommentToMovie(jsonObject, users, movies, comments);
            case "rateMovie":
                return rateService.AddRateToMovie(jsonObject, users, movies);
            case "voteComment":
                return commentVoteService.VoteComment(jsonObject, users, comments);
            case "addToWatchList":
                return userService.AddToWatchList(jsonObject, users, movies);
            case "removeFromWatchList":
                return userService.RemoveFromWatchList(jsonObject, users, movies);
            case "getMoviesList":
                return movieService.GetMoviesList(movies);
            case "getMovieById":
                return movieService.GetMovieById(jsonObject, movies, actors);
            case "getMoviesByGenre":
                return movieService.GetMoviesByGenre(jsonObject, movies);
            case "getWatchList":
                return userService.GetWatchList(jsonObject, users);
            default:
                return errorHandler.fail("InvalidCommand");
        }
    }
    public static void main(String args[]) throws ParseException, java.text.ParseException {

        IEDMB iemdb = new IEDMB();
        System.out.print("System Started.. \nEnter commands:\n");
        Scanner sc= new Scanner(System.in); //System.in is a standard input stream
        while(true) {
            String input = sc.nextLine();
            String[] splittedInput = input.split(" ", 2);
            String command = splittedInput[0];
            String JSONData = "{}";
            if(splittedInput.length > 1)
                JSONData = splittedInput[1];
            JSONObject jsonObject = new JSONObject(JSONData);
            try {
                System.out.println(iemdb.InputHandler(command, jsonObject).toString());
            }catch(Exception e){
                System.out.println(errorHandler.fail("InvalidCommand").toString());
            }
        }
    }

}
