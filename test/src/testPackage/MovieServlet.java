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
 * Servlet implementation class MovieServlet
 */
@WebServlet("/MovieServlet")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieServlet() {
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
		String ps = "SELECT * FROM MOVIE WHERE MOV_TITLE like ?";
		
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
				"<head><title>Movie Results</title></head>\n" +
				"<h2> Enter a movie...Sucka! </h2>\n" +
				"<form action=\"MovieServlet\" method=\"post\" name=\"input\" target=\"_self\">\n" +
				"Movie Name: <input name=\"name\" type=\"text\">\n" +
				"<br/><input type=\"submit\" value=\"Search\">\n" +
				"</form>\n" +
				"<br/><br/><table border=\"1\">" + 
				"<tr><th>title</th>\n" +
				"<th>year</th>\n" +
				"<th>rating</th>\n" +
				"<th>category</th>\n" +
				"<th>group_rating</th>\n" +
				"<th>imdb_rating</th></tr>");
				do {
					String title = rset.getString("MOV_TITLE");
					int year = rset.getInt("MOV_YEAR");
					String rating = rset.getString("MOV_RATING");
					String category = rset.getString("MOV_CATEGORY");
					float group_rating = rset.getFloat("GROUP_RATING");
					float imdb_rating = rset.getFloat("IMDB_RATING");
					
					out.println("<tr><td>" + title + "</td>\n" +
							"<td>" + year + "</td>\n" +
							"<td>" + rating + "</td>\n" +
							"<td>" + category + "</td>\n" +
							"<td>" + group_rating + "</td>\n" +
							"<td>" + imdb_rating + "</td></tr>");
				} while (rset.next());
				out.println("</table></body></html>");
			} else {
				request.getRequestDispatcher("search_movie_again.html").forward(request, response); // Redisplay JSP.
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
