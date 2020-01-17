package mvc.controllers;
import helper.CheckCode;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
    
/** 
 * 产生验证码图片的servlet 
 * @author Administrator 
 * 
 */
public class VerifyCodeServletTwist extends HttpServlet {   
//	private Logger logger = LoggerFactory.getLogger(this.getClass());
  private static final long serialVersionUID = -5051097528828603895L; 
   
  /** 
  * @param request 
  * @param response 
  * @throws ServletException 
  * @throws java.io.IOException 
  */
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {   
    
    CheckCode cc =  new CheckCode().createCheckCode();
    // 将四位数字的验证码保存到Session中。   
//    logger.info(cc.getCheckCodeStr());
    HttpSession session = request.getSession();   
    session.setAttribute("validateCode", cc.getCheckCodeStr()); 
    // 禁止图像缓存。   
    response.setHeader("Pragma", "no-cache");   
    response.setHeader("Cache-Control", "no-cache");   
    response.setDateHeader("Expires", 0);   
    
    response.setContentType("image/jpeg");   
    // 将图像输出到Servlet输出流中。   
    ServletOutputStream sos = response.getOutputStream();   
    ImageIO.write(cc.getBuffImg(), "jpeg", sos); 
    sos.close();   
  }   
}  