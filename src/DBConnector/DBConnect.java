package DBConnector;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Security.AESDec;

public class DBConnect {
	
	//  DB ��ȣȭ �ʿ� ����
	private AESDec aes;
	private String propFile;
	private Properties prop;
	private FileInputStream fis;
	private String read_key;
	private Properties key;
	private FileInputStream fis_key;
	private String aes_key;
	
	//DB ����
	private String dbURL = "jdbc:mysql://localhost:3307/randompassword?serverTimezone=UTC"; // localhost:3307 ��Ʈ�� ������ ��ġ�� db�ּ�
	private String dbID = "android";    //  DB ���̵�
	private String dbPassword = "";    //  DB ��й�ȣ
	private Connection conn; // connection:db�������ϰ� ���ִ� ��ü
	private PreparedStatement pstmt;
	private PreparedStatement pstmt2;
	private ResultSet rs;
	private String sql = "";
	private String sql2 = "";
	private String returns;
	
	public DBConnect() {
		try {
			//  ���� ��ȣȭ 	DB password read
			propFile = "/volume1/Tomcat/RandomPassword/WEB-INF/classes/Security/password.properties";
			prop = new Properties();
			fis = new FileInputStream(propFile);
			prop.load(new BufferedInputStream(fis));

			//�ܺο� ����� ���Ű read
			read_key = "/volume1/Tomcat/key.properties";
			key = new Properties();
			fis_key = new FileInputStream(read_key);
			key.load(new BufferedInputStream(fis_key));

			aes_key = key.getProperty("key");
			if(aes_key != null)
				aes = new AESDec(aes_key);

			if(aes != null)
				dbPassword = aes.aesDecode(prop.getProperty("password"));
			if(fis != null) {
				fis.close();
			}
			if(fis_key != null) {
				fis_key.close();
			}
		} catch (FileNotFoundException e) { //����ó�� ,�������� ����
			System.err.println("BbsDAO FileNotFoundException error");	
		} catch (IOException e) {
			System.err.println("BbsDAO IOException error");
		} catch (InvalidKeyException e) {
			System.err.println("BbsDAO InvalidKeyException error");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("BbsDAO NoSuchAlgorithmException error");
		} catch (NoSuchPaddingException e) {
			System.err.println("BbsDAO NoSuchPaddingException error");
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("BbsDAO InvalidAlgorithmParameterException error");
		} catch (IllegalBlockSizeException e) {
			System.err.println("BbsDAO IllegalBlockSizeException error");
		} catch (BadPaddingException e) {
			System.err.println("BbsDAO BadPaddingException error");
		}
	}
	
	public String findPw(String id) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			sql = "select id, pw from users where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				returns = rs.getString("pw");
			}
			else
				returns = "noID";
		} catch(Exception e) {
			e.printStackTrace();
			returns = "error";
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	
	public String update(String id, String pwd) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			sql = "update users set pw=? where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pwd);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
			returns = "ok";
			
		} catch(Exception e) {
			e.printStackTrace();
			returns = "error";
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	public String joindb(String id, String pwd) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			sql = "select id from users where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("id").equals(id)) { // �̹� ���̵� �ִ� ���
					returns = "fail";
				} 
			} else { // �Է��� ���̵� ���� ���
				sql2 = "insert into users (id,pw) values(?,?)";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, id);
				pstmt2.setString(2, pwd);
				pstmt2.executeUpdate();

				returns = "ok";
			}
		} catch (Exception e) {
			e.printStackTrace();
			returns = "error";
		} finally {
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
			if (pstmt2 != null)try {pstmt2.close();} catch (SQLException ex) {}
			if (rs != null)try {rs.close();} catch (SQLException ex) {}
		}
		return returns;
	}

	public String logindb(String id, String pwd) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			sql = "select id, pw from users where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("id").equals(id) && rs.getString("pw").equals(pwd)) {
					returns = "true";// �α��� ����
				} else {
					returns = "false"; // �α��� ����
				}
			} else {
				returns = "noId"; // ���̵� �Ǵ� ��й�ȣ ���� X
			}
		} catch (Exception e) {
			returns = "error";
		} finally {
			if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
}
