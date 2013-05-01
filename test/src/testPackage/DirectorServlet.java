package testPackage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DirectorServlet
 */
@WebServlet("/DirectorServlet")
public class DirectorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DirectorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String ps = "SELECT * FROM DIRECTOR WHERE DIR_NAME like ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@ginger.umd.edu:1521:dbclass1", "dbclass124", "O2Fjmit4");
			pstmt = conn.prepareStatement(ps);
		    pstmt.setString(1, name);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println
				("<!DOCTYPE html>\n" +
				"<html>\n" +
				"<head><title>Director Results</title></head>\n" +
				"<h2> Enter a director name...Sucka! </h2>\n" + 
				"<form action=\"DirectorServlet\" method=\"post\" name=\"input\" target=\"_self\">\n" +
				"Director Name: <input name=\"name\" type=\"text\">\n" +
				"<br/><input type=\"submit\" value=\"Search\">\n" +
				"</form>\n" +
				"<br/><br/><table border=\"1\">" + 
				"<tr><th>name</th>\n" +
				"<th>real name</th>\n" +
				"<th>height</th>\n" +
				"<th>dob</th>\n" +
				"<th>pob</th>\n" +
				"<th>dod</th>\n" +
				"<th>pod</th>\n" +
				"<th>cod</th></tr>");
				do {
					String dir_name = rset.getString("DIR_NAME");
					String real_name = rset.getString("DIR_REAL_NAME");
					String height = rset.getString("DIR_HEIGHT");
					String dob = rset.getString("DIR_DOB");
					String pob = rset.getString("DIR_POB");
					String dod = rset.getString("DIR_DOD");
					String pod = rset.getString("DIR_POD");
					String cod = rset.getString("DIR_COD");
					
					out.println("<tr><td>" + dir_name + "</td>\n" +
							"<td>" + real_name + "</td>\n" +
							"<td>" + height + "</td>\n" +
							"<td>" + dob + "</td>\n" +
							"<td>" + pob + "</td>\n" +
							"<td>" + dod + "</td>\n" +
							"<td>" + pod + "</td>\n" +
							"<td>" + cod + "</td></tr>");
				} while (rset.next());
				out.println("</table></body></html>");
			} else {
				request.getRequestDispatcher("search_director_again.html").forward(request, response); // Redisplay JSP.
			}
		} catch (Exception e) {
			System.err.println("Oh snap! There's an exception: " + e);
		} finally {
			if (rset != null) try {rset.close();} catch (SQLException ignore) {}
		    if (pstmt != null) try {pstmt.close();} catch (SQLException ignore) {}
		    if (conn != null) try {conn.close();} catch (SQLException ignore) {}
		}
	}

}
