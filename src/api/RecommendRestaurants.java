package api;

import db.DBConnection;
import db.MySQLDBConnection;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Ruoyu Wang on 2016/11/17.
 */
@WebServlet("/recommendation")
public class RecommendRestaurants extends HttpServlet {

    private static DBConnection connection = new MySQLDBConnection();
//    private static DBConnection connection = new MongoDBConnection();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // allow access only if session exists
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.setStatus(403);
            return;
        }
        JSONArray array = null;

        if (request.getParameterMap().containsKey("user_id")) {
            String userId = request.getParameter("user_id");
            array = connection.recommendRestaurants(userId);
        }
        RpcParser.writeOutput(response, array);
    }
}
