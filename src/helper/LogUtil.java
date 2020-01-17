package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class LogUtil {
	
	public static void log(String model, String s){
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		HttpSession session = request.getSession();
//		String username = String.valueOf(session.getAttribute("name"));
//		if(username != null && !"".equals(username)){
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String time = format.format(new Date());
//			try {
//				Connection con = null;
//				PreparedStatement stmt = null;
//				Class.forName("com.mysql.jdbc.Driver");
//				con = DriverManager.getConnection("jdbc:mysql://10.74.27.195:3306/bike?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=FALSE", "root", "123456");
////				 con = DriverManager.getConnection("jdbc.jdbcUrl=jdbc:mysql://192.168.11.33:3306/bike?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=FALSE", "root", "twkj@2019");
//				String sql = "insert into tb_log (username,handle,content,dbtime) values (?,?,?,?)";
//				stmt = con.prepareStatement(sql);
//				
//				stmt.setString(1, username);
//				stmt.setString(2, model);
//				stmt.setString(3, s);
//				stmt.setString(4, time);
//				
//				stmt.executeUpdate();
//				con.close();
//				stmt.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}

}
