
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.List;

public class ActorService {
    ErrorHandler errorHandler = new ErrorHandler();

    public JSONObject AddActor(JSONObject jsonObject, List<Actor> actors) throws java.text.ParseException {
        SimpleDateFormat formatter=new SimpleDateFormat("yyy-MM-dd");
        Actor newActor = new Actor(
                Integer.parseInt(jsonObject.get("id").toString()),
                jsonObject.get("name").toString(),
                formatter.parse(jsonObject.get("birthDate").toString()),
                jsonObject.get("nationality").toString()
        );

        Integer actorId = Integer.parseInt(jsonObject.get("id").toString());
        Integer actorIndex = FindActorIndex(actorId, actors);
        if(actorIndex != -1) {
            return UpdateActor(actorIndex, newActor, actors);
        }
        actors.add(newActor);
        return errorHandler.success("actor added successfully");
    }

    public JSONObject UpdateActor(Integer actorIndex, Actor actor, List<Actor> actors) throws java.text.ParseException {
        actors.set(actorIndex, actor);
        return errorHandler.success("actor updated successfully");
    }
    public static Integer FindActorIndex(Integer id, List<Actor> actors){
        for(Actor actor : actors){
            if(actor.Id == id)
                return actors.indexOf(actor);
        }
        return -1;
    }

}
