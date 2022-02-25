
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comment {
    Integer Id, Likes, Dislikes;
    String UserEmail;
    Integer MovieId;
    String Text;
    LocalDateTime CreationDate;
    List<CommentVote> Votes;

    public Comment(Integer id, String userEmail, Integer movieId, String text) {
        Id = id;
        UserEmail = userEmail;
        MovieId = movieId;
        Text = text;
        CreationDate = LocalDateTime.now();
        Likes = 0;
        Dislikes = 0;
        Votes = new ArrayList<CommentVote>();
    }

    public void AddVote(CommentVote commentVote){
        Votes.add(commentVote);
        UpdateVotes();
    }

    private void UpdateVotes(){
        Likes = 0;
        Dislikes = 0;
        for(CommentVote vote : Votes){
            if(vote.Vote == 1)
                Likes += 1;
            else if(vote.Vote == -1)
                Dislikes += 1;
        }
    }

    public boolean checkForVoteUpdates(String userEmail, Integer vote){
        for(CommentVote cmntVote : Votes){
            if(cmntVote.UserEmail.equals(userEmail)) {
                cmntVote.Vote = vote;
                UpdateVotes();
                return true;
            }
        }
        return false;
    }

}
