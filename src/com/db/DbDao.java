package com.db;

import java.sql.*;

public class DbDao
{
	private Connection conn;
	private String driver;
	private String url;
	private String username;
	private String pass;
	public DbDao()
	{
	}
	public DbDao(String driver , String url
		, String username , String pass)
	{
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.pass = pass;
	}
	
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getDriver() {
		return (this.driver);
	}
	public String getUrl() {
		return (this.url);
	}
	public String getUsername() {
		return (this.username);
	}
	public String getPass() {
		return (this.pass);
	}
	
	public Connection getConnection() throws Exception
	{
		if (conn == null)
		{
			Class.forName(this.driver);
			conn = DriverManager.getConnection(url+"?user="+username+
					"&password="+pass+"&useUnicode=true&characterEncoding=utf-8");
		}
		return conn;
	}
	
	public boolean insert(String sql , Object... args)
		throws Exception
	{
		PreparedStatement pstmt = getConnection().prepareStatement(sql);
		
		for (int i = 0; i < args.length ; i++ )
		{
			pstmt.setObject( i + 1 , args[i]);
		}
		if (pstmt.executeUpdate() != 1)
		{
			return false;
		}
		pstmt.close();
		return true;
	}
	
	public ResultSet query(String sql , Object... args)
		throws Exception
	{
		PreparedStatement pstmt = getConnection().prepareStatement(sql);
		for (int i = 0; i < args.length ; i++ )
		{
			pstmt.setObject( i + 1 , args[i]);
		}
		return pstmt.executeQuery();
	}
	
	public void modify(String sql , Object... args)
		throws Exception
	{
		PreparedStatement pstmt = getConnection().prepareStatement(sql);
		for (int i = 0; i < args.length ; i++ )
		{
			pstmt.setObject( i + 1 , args[i]);
		}
		pstmt.executeUpdate();
		pstmt.close();
	}
	
	public void closeConn()
		throws Exception
	{
		if (conn != null && !conn.isClosed())
		{
			conn.close();
		}
	}
}
