package com.servlet;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.db.DbDao;

@WebServlet(name = "db", urlPatterns = { "/db" })
public class QueryServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
		DbDao dd = new DbDao("com.mysql.jdbc.Driver"
				, "jdbc:mysql://localhost:3306/Merchant"
				, "root" , "");
		try {
			
			ResultSet rs = null;
			String tableName = "Product";
			
			rs = dd.query("select * from " + tableName);
			String attrName = "tables";
			ResultSetMetaData md = rs.getMetaData();
			
			int columnCount = md.getColumnCount();
			
			List<List<String>> rows = new LinkedList<List<String>>();
			List<String> header = new LinkedList<String>();
			int currency_col = -1;
			int buy_col = -1;
			for (int c = 2; c <= columnCount; c++) {
				
				String columnName = md.getColumnName(c);
				header.add(columnName);
				
				if (columnName.equals("currency"))
					currency_col = c;
				else if (columnName.equals("buy"))
					buy_col = c;
				
				System.out.println(md.getColumnLabel(c) + "  " + md.getColumnName(c));
			}
			
			//rows.add(header);
			String currency_format[] = {"￥", "$", "円", "£"};
			
			while (rs.next()) {
				List<String> row = new LinkedList<String>();
				String currency = currency_format[rs.getShort(currency_col)];
				
				for (int c = 2; c <= columnCount; c++) {
					if (c == currency_col)
						continue;
					
					StringBuilder sb = new StringBuilder();
					
					if (c == buy_col)
						sb.append(currency );
						
					sb.append(rs.getString(c));
					row.add(sb.toString());
				}
				rows.add(row);
			}
			req.setAttribute(attrName, rows);
			
			
			
			req.getRequestDispatcher( "main.jsp").forward(req,resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("doGet");
	}
	
	
}
