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
			String para1 = req.getParameter("product_sell");
			String para2 = req.getParameter("price_sell");
			String para3 = req.getParameter("quantity_sell");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String date = sdf.format(new Date());
			
			if (para1 != null && para2 != null && para3 != null) {
				String product = new String(para1.getBytes("ISO-8859-1"), "utf-8");
				double price = Double.valueOf(new String(para2.getBytes("ISO-8859-1"), "utf-8"));
				int quantity = Integer.valueOf(new String(para3.getBytes("ISO-8859-1"), "utf-8"));
				String comment = new String(req.getParameter("comment").getBytes("ISO-8859-1"), "utf-8");
				int id = 0;
				String sql = "select id from Product where name like \"%"+product+"%\"";
				System.out.println(sql);
				ResultSet rs = dd.query(sql);
				if (rs.next())
				{
					id = rs.getInt(1);
					
					sql = "insert into Sales (product_id, price, quantity, sell_date, comment) values (?,?,?,?,?)";
					
					dd.insert(sql, id, price, quantity, date, comment);
					
				}
				
				dd.closeConn();
				
			} else {
			
				SmartUpload su = new SmartUpload();
				su.init(req, resp);
				su.setAllowedFilesList("jpg,jpeg,png,gif");
				su.upload();

				Request request = su.getRequest();
				String tableName = "Product";
				// System.out.println(request.getParameter("product"));
				String name = new String(request.getParameter("product"));
				int currency = Integer.valueOf(request.getParameter("currency"));
				int quantity = Integer.valueOf(request.getParameter("quantity"));
				double buy = Double.valueOf(request.getParameter("buy"));
				String root = req.getSession().getServletContext().getRealPath("/");
				String photo_path = this.uploadImage(su, root);
				String website = new String(request.getParameter("website"));

				// System.out.println(website.getBytes());
				System.out.println(name + " " + currency + " " + quantity + " " + buy + " " + " " + photo_path + " "
						+ website + " " + date);

				dd.insert(
						"insert into " + tableName + " (name, currency, buy, quantity, photo, website, date_ci) "
								+ "values (?, ?, ?, ?, ?, ?, ?)",
						name, currency, buy, quantity, photo_path, website, new Date());

				dd.closeConn();
			}
			
			QueryServlet.processRequest(req, resp);
			
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
