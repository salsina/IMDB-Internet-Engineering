import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Functions {

    public static List<Integer> ConvertStringToIntList(String str){
        str = str.substring(1,str.length() - 1);
        List<String> myList = new ArrayList<String>(Arrays.asList(str.split(",")));
        List<Integer> myListInt = myList.stream().map(Integer::parseInt).collect(Collectors.toList());
        return myListInt;
    }

    public static List<String> ConvertStringToStringList(JSONArray arr){
        List<String> myList = new ArrayList<String>();
        for(Object obj : arr){
            myList.add((String)obj);
        }
        return myList;
    }

}
