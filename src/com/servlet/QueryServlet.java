package com.servlet;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
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
		processRequest(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("doGet");
	}
	
	public static void processRequest(HttpServletRequest req, HttpServletResponse resp) {
		DbDao dd = new DbDao("com.mysql.jdbc.Driver"
				, "jdbc:mysql://localhost:3306/Merchant"
				, "root" , "");
		try {
			
			String product = req.getParameter("product_query");
			String name = "";
			if (product != null)
				name = (new String(product.getBytes("ISO-8859-1"), "utf-8")).trim();
			
			String check = req.getParameter("check");
			ResultSet rs = null;
			
			String sql = "select T1.name name, T1.photo photo, "
					+ "T1.website website, T1.date_ci date_in, "
					+ "T1.buy buy, T1.currency currency, T1.quantity quantity, "
					+ "IFNULL(T2.quantity,0) sale, "
					+ "IFNULL(T2.sale,0)-T1.buy*T3.rate*IFNULL(T2.quantity,0) profit, "
					+ "T1.quantity-IFNULL(T2.quantity,0) remaining, T2.comment comment "
					+ "from (Product T1 left join SaleView T2 on T1.id=T2.id) "
					+ "left join ExchangeRate T3 on T1.currency=T3.currency";
			
			if (!name.equals(""))
				sql = sql+ " where name like \"%"+name+"%\"";
			
			if (check != null) 
				sql = sql + " having remaining > 0";
			
			System.out.println(sql);
			
			rs = dd.query(sql);
			String attrName = "tables";
			ResultSetMetaData md = rs.getMetaData();
			
			int columnCount = md.getColumnCount();
			
			List<List<String>> rows = new LinkedList<List<String>>();
			List<String> header = new LinkedList<String>();
			int currency_col = -1;
			int buy_col = -1;
			for (int c = 1; c <= columnCount; c++) {
				
				String columnName = md.getColumnName(c);
				header.add(columnName);
				
				if (columnName.equals("currency"))
					currency_col = c;
				else if (columnName.equals("buy"))
					buy_col = c;
				
				//System.out.println(md.getColumnLabel(c) + "  " + md.getColumnName(c));
			}
			
			//rows.add(header);
			String currency_format[] = {"$", "￥", "£", "円", "HK$", "€"};
			
			while (rs.next()) {
				List<String> row = new LinkedList<String>();
				String currency = currency_format[rs.getShort(currency_col)];
				
				for (int c = 1; c <= columnCount; c++) {
					if (c == currency_col)
						continue;
					
					StringBuilder sb = new StringBuilder();
					
					if (c == buy_col)
						sb.append(currency );
					
					String s = rs.getString(c);
					if (s == null)
						s = "";
					sb.append(s);
					row.add(sb.toString());
				}
				rows.add(row);
			}
			req.setAttribute(attrName, rows);
			
			rs = dd.query("select Sum(T2.sale-T1.buy*T3.rate*T2.quantity) profit from "
					+ "(Product T1 join SaleView T2 on T1.id = T2.id) "
					+ "join ExchangeRate T3 on T1.currency=T3.currency");
			
			if (rs.next()) {
				DecimalFormat decimalFormat=new DecimalFormat(".00");
				req.setAttribute("profit", decimalFormat.format(rs.getDouble(1)));
			} else {
				req.setAttribute("profit", 0);
			}
			
			dd.closeConn();
			req.getRequestDispatcher( "main.jsp").forward(req,resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
