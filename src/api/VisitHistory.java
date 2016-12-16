package api;

import db.DBConnection;
import db.MySQLDBConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ruoyu Wang on 2016/11/17.
 */
@WebServlet("/history")
public class VisitHistory extends HttpServlet {

    private static DBConnection connection = new MySQLDBConnection();
//    private static DBConnection connection = new MongoDBConnection();

    public VisitHistory() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // allow access only if session exists
            HttpSession session = request.getSession();
            if (session.getAttribute("user") == null) {
                response.setStatus(403);
                return;
            }
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // response.getWriter().append("Served at: ").append(request.getContextPath());

        try {
            if (request.getParameterMap().containsKey("user_id")) {
                String userId = request.getParameter("user_id");
                Set<String> visited_restaurants = connection.getVisitedRestaurants(userId);
                JSONArray array = new JSONArray();
                for (String id : visited_restaurants) {
                    array.put(connection.getRestaurantsById(id, true));
                }
                RpcParser.writeOutput(response, array);
            } else {
                RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // allow access only if session exists
            /*
             * if (!RpcParser.sessionValid(request, connection)) {
			 * response.setStatus(403); return; }
			 */
            JSONObject input = RpcParser.parseInput(request);
            if (input.has("user_id") && input.has("visited")) {
                String userId = (String) input.get("user_id");
                JSONArray array = (JSONArray) input.get("visited");
                List<String> visitedRestaurants = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String businessId = (String) array.get(i);
                    visitedRestaurants.add(businessId);
                }
                connection.unsetVisitedRestaurants(userId, visitedRestaurants);
                RpcParser.writeOutput(response, new JSONObject().put("status", "OK"));
            } else {
                RpcParser.writeOutput(response, new JSONObject().put("status", "InvalidParameter"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
