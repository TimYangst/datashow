package action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.reqRatesCounter;

@WebServlet(urlPatterns = {"/showerror"})
public class ErrorShowAction extends HttpServlet{
	
	/**
	 * Constructor of the object.
	 */
	public ErrorShowAction() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String appname = request.getParameter("name");
		String day  = (request.getParameter("day"));
		String error = getErrorInfo(appname, day);
		PrintWriter out = response.getWriter();
		out.print(error);
		out.flush();
		out.close();
	}

	private String getErrorInfo(String appname, String day) {
		// TODO Auto-generated method stub
		String p =  this.getClass().getClassLoader().getResource("/").getPath();
		String path = p + appname + ".err";
		String line;
		String[] strs;
		try {
			File file  = new File(path);
			int[] maxrate = new int[1];
			int[] minrate = new int[1];
			if (!file.exists())
				reqRatesCounter.counter(maxrate, minrate,  p + appname);
			Scanner insanner = new Scanner(file);
			while (insanner.hasNext()){
				line = insanner.nextLine();
				strs =  line.split("\t");
			    String tmp = day+".txt";
			    //System.out.println("@" +tmp + " " +strs[0]);
				if (strs[0].equals(tmp)){
					String res = "[";
					String[] strs1 = strs[1].split(";");
					String[] strs2;
					for (String str : strs1){
						strs2 = str.split(",");
						res += "{status : " + strs2[0] + " , url : '" + strs2[1] + "'},";
					}
					res += "]";
					return res;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
