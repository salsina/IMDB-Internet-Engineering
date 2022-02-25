
import org.json.JSONObject;

import java.util.List;

public class CommentService {
    ErrorHandler errorHandler = new ErrorHandler();

    public  JSONObject AddCommentToMovie(JSONObject jsonObject, List<User> users,List<Movie> movies, List<Comment> comments){
        UserService userService = new UserService();
        MovieService movieService = new MovieService();
        Integer movieId = Integer.parseInt(jsonObject.get("movieId").toString());
        Integer movieIndex = movieService.FindMovieIndex(movieId, movies);
        String userEmail = jsonObject.get("userEmail").toString();

        if(!userService.UserExists(userEmail, users)){
            return errorHandler.fail("UserNotFound");
        }
        if(movieIndex == -1){
            return errorHandler.fail("MovieNotFound");
        }

        Comment newComment = new Comment(
                comments.size() + 1,
                userEmail,
                movieId,
                jsonObject.get("text").toString()
        );
        movies.get(movieIndex).AddComment(newComment);
        comments.add(newComment);
        return errorHandler.success("comment with id "+ newComment.Id.toString()  +" added successfully");
    }

    public Integer FindCommentIndex(Integer id, List<Comment> comments){
        for(Comment comment : comments){
            if(comment.Id == id)
                return comments.indexOf(comment);
        }
        return -1;
    }

}
