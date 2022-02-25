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

class CommentVoteServiceTest {
    JSONObject jsonObject;
    User user;
    Movie movie;
    Comment comment;
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
        user = new User("sajjad1234@gmail.com", "sajjad1234", "sajjad", "Sajjad", localDate);
        movie = new Movie(1, "The Godfather", 14, (float)9.2, "The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.", "Francis Ford Coppola",
                175 ,releaseDate, emptyListString, Arrays.asList("Mystery", "Drama"), emptyListInteger);
        comment = new Comment(1, "sajjad1234@gmail.com", 1, "That was a great movie.");
        iemdb = new IEDMB();
    }

    @Test
    void voteComment_UserNotExist() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "UserNotFound");
        expectedOutput.put("success", false);
        jsonObject = new JSONObject("{\"userEmail\": \"Mahdi@gmail.com\", \"commentId\": 1, \"vote\": 1}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("voteComment", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    void voteComment_InvalidVoteValueString() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "InvalidVoteValue");
        expectedOutput.put("success", false);
        iemdb.users.add(user);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad1234@gmail.com\", \"commentId\": 1, \"vote\": \"like\"}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("voteComment", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    void voteComment_CommentNotExist() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "CommentNotFound");
        expectedOutput.put("success", false);
        iemdb.users.add(user);
        iemdb.comments.add(comment);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad1234@gmail.com\", \"commentId\": 2, \"vote\": 1}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("voteComment", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    void voteComment_InvalidVoteValueNumbersRange() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "InvalidVoteValue");
        expectedOutput.put("success", false);
        iemdb.users.add(user);
        iemdb.comments.add(comment);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad1234@gmail.com\", \"commentId\": 1, \"vote\": 2}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("voteComment", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    void voteComment_LikeSuccess() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "comment voted successfully");
        expectedOutput.put("success", true);
        iemdb.users.add(user);
        iemdb.comments.add(comment);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad1234@gmail.com\", \"commentId\": 1, \"vote\": 1}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("voteComment", jsonObject).toString());
        }catch(Exception e){}
    }
    @Test
    void voteComment_DisLikeSuccess() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "comment voted successfully");
        expectedOutput.put("success", true);
        iemdb.users.add(user);
        iemdb.comments.add(comment);
        jsonObject = new JSONObject("{\"userEmail\": \"sajjad1234@gmail.com\", \"commentId\": 1, \"vote\": -1}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("voteComment", jsonObject).toString());
        }catch(Exception e){}
    }

    @Test
    void voteComment_SuccessUpdate() {
        JSONObject expectedOutput = new JSONObject();
        expectedOutput.put("data", "comment voted successfully");
        expectedOutput.put("success", true);
        iemdb.users.add(user);
        iemdb.comments.add(comment);
        JSONObject jsonObjectLike = new JSONObject("{\"userEmail\": \"sajjad1234@gmail.com\", \"commentId\": 1, \"vote\": 1}");
        try{
            iemdb.InputHandler("voteComment", jsonObjectLike);
        }catch(Exception e){}
        JSONObject jsonObjectDisLike = new JSONObject("{\"userEmail\": \"sajjad1234@gmail.com\", \"commentId\": 1, \"vote\": -1}");
        try{
            assertEquals(expectedOutput.toString(), iemdb.InputHandler("voteComment", jsonObjectDisLike).toString());
            assertEquals(0, iemdb.comments.get(0).Likes);
            assertEquals(1, iemdb.comments.get(0).Dislikes);
        }catch(Exception e){}
    }

    @AfterEach
    void tearDown() {
        iemdb = null;
    }
}