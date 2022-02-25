
import org.json.JSONObject;

import java.util.List;

public class CommentVoteService {
    ErrorHandler errorHandler = new ErrorHandler();
    CommentService commentService = new CommentService();
    UserService userService = new UserService();
    public JSONObject VoteComment(JSONObject jsonObject, List<User> users, List<Comment> comments){
        Integer commentId = Integer.parseInt(jsonObject.get("commentId").toString());
        Integer commentIndex = commentService.FindCommentIndex(commentId, comments);
        String userEmail = jsonObject.get("userEmail").toString();
        int vote;
        try {
            vote = Integer.parseInt(jsonObject.get("vote").toString());
        }catch(NumberFormatException e){
            return errorHandler.fail("InvalidVoteValue");
        }

        if(!userService.UserExists(userEmail, users)){
            return errorHandler.fail("UserNotFound");
        }
        if(commentIndex == -1){
            return errorHandler.fail("CommentNotFound");
        }
        if(!(vote == 1 || vote == -1 || vote == 0)){
            return errorHandler.fail("InvalidVoteValue");
        }

        if(comments.get(commentIndex).checkForVoteUpdates(userEmail, vote)){
            return errorHandler.success("comment voted successfully");
        }

        CommentVote newCommentVote = new CommentVote(
                userEmail,
                commentId,
                vote
        );

        comments.get(commentIndex).AddVote(newCommentVote);
        return errorHandler.success("comment voted successfully");
    }

}
