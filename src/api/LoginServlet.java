package api;

import db.DBConnection;
import db.MySQLDBConnection;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Ruoyu Wang on 2016/12/13.
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final DBConnection connection = new MySQLDBConnection();

    public LoginServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject msg = new JSONObject();
            // get request parameters for userID and password
            String user = request.getParameter("user_id");
            String pwd = request.getParameter("password");
            if (connection.verifyLogin(user, pwd)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                // setting session to expire in 10 minutes
                session.setMaxInactiveInterval(10 * 60);
                // Get user name
                String name = connection.getFirstLastName(user);
                msg.put("status", "OK");
                msg.put("user_id", user);
                msg.put("name", name);
            } else {
                response.setStatus(401);
            }
            RpcParser.writeOutput(response, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject msg = new JSONObject();
            HttpSession session = request.getSession();
            if (session.getAttribute("user") == null) {
                response.setStatus(403);
                msg.put("status", "Session Invalid");
            } else {
                String user = (String) session.getAttribute("user");
                String name = connection.getFirstLastName(user);
                msg.put("status", "OK");
                msg.put("user_id", user);
                msg.put("name", name);
            }
            RpcParser.writeOutput(response, msg);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
