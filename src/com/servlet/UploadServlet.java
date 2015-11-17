package com.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.db.DbDao;
import com.jsp.smart.Request;
import com.jsp.smart.SmartUpload;
import com.jsp.smart.SmartUploadException;

@WebServlet(name = "upload", urlPatterns = { "/upload" })
public class UploadServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
		DbDao dd = new DbDao("com.mysql.jdbc.Driver"
				, "jdbc:mysql://localhost:3306/Merchant"
				, "root" , "");
		try {
			SmartUpload su = new SmartUpload();
			su.init(req, resp);
			su.setAllowedFilesList("jpg,jpeg,png,gif");
			su.upload();
			
			Request request = su.getRequest();
			ResultSet rs = null;
			String tableName = "Product";
			//System.out.println(request.getParameter("product"));
			String name = new String(request.getParameter("product"));
			double price = Double.valueOf(request.getParameter("price"));
			int currency = Integer.valueOf(request.getParameter("currency"));
			int quantity = Integer.valueOf(request.getParameter("quantity"));
			double buy = Double.valueOf(request.getParameter("buy"));
			double sell = Double.valueOf(request.getParameter("sell"));
			String root = req.getSession().getServletContext().getRealPath("/");
			String photo_path = this.uploadImage(su, root);
			String website = new String(request.getParameter("website"));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String date = sdf.format(new Date());
			
			//System.out.println(website.getBytes());
			System.out.println(name+ " "+ price+ " "+currency+" "+
			quantity+" "+buy+" "+ sell+ " "+photo_path+" "+ website+ " " + date);
			
			dd.insert("insert into "+tableName+
					" (name, price, currency, buy, sell, quantity, photo, website, date_ci) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)", name, price, currency, buy, sell,
					quantity, photo_path, website, new Date());
			
			
			
			req.getRequestDispatcher( "main.jsp").forward(req,resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("doGet");
	}
	
	private String uploadImage(SmartUpload su, String root) throws ServletException, IOException, SmartUploadException {	
		
		
		com.jsp.smart.File file = su.getFiles().getFile(0);
		
		Calendar calendar = Calendar.getInstance();
		String filename = String.valueOf(calendar.getTimeInMillis())+"."+file.getFileExt();
		StringBuilder sb = new StringBuilder();
		sb.append(root);
		sb.append("image/");
		sb.append(filename); //保存路径
		String path = sb.toString();
		file.saveAs(path, SmartUpload.SAVE_PHYSICAL);
		
		return filename;
	}
}
