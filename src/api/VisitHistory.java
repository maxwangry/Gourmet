package api;

import db.DBConnection;
import db.MySQLDBConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruoyu Wang on 2016/11/17.
 */
public class VisitHistory extends HttpServlet {

    private static DBConnection connection = new MySQLDBConnection();
//    private static DBConnection connection = new MongoDBConnection();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject input = RpcParser.parseInput(request);
            if (input.has("user_id") && input.has("visited")) {
                String userId = (String) input.get("user_id");
                JSONArray array = (JSONArray) input.get("visited");
                List<String> visitedRestaurants = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String businessId = (String) array.get(i);
                    visitedRestaurants.add(businessId);
                }
                connection.setVisitedRestaurants(userId, visitedRestaurants);
                RpcParser.writeOutput(response, new JSONObject().put("status", "OK"));
            } else {
                RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
