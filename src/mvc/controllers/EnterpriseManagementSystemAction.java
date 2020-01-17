package mvc.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mvc.service.EnterpriseManagementSystemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
// 企业管理系统
@Controller
@RequestMapping("/enterpriseManagementSystem")
public class EnterpriseManagementSystemAction {

	@Autowired
	private EnterpriseManagementSystemService enterpriseManagementSystemService;
	
	// 区域预警
	@RequestMapping("/regionWarning")
	@ResponseBody
	public String regionWarning(){
		String msg = enterpriseManagementSystemService.regionWarning();
        return msg;
	}
	
	@RequestMapping("/getPointForRegionWarning")
	@ResponseBody
	public String getPointForRegionWarning(HttpServletRequest request){
		return enterpriseManagementSystemService.getPointForRegionWarning();
	}
	
	@RequestMapping("/findqyinfo")
	@ResponseBody
	public String findqyinfo(HttpServletRequest request){
		String id = request.getParameter("id");
		String msg = enterpriseManagementSystemService.findqyinfo(id);
		return msg;
	}
	
	// 数据接口预警
	@RequestMapping("/dataWarning")
	@ResponseBody
	public String dataWarning(HttpServletRequest request){
		HttpSession session = request.getSession();
		String companyId = String.valueOf(session.getAttribute("companyId"));
		String time = request.getParameter("time");
		String msg = enterpriseManagementSystemService.dataWarning(companyId,time);
		return msg;
	}
	
	// 通知公告
	@RequestMapping("/noticeAnnouncement")
	@ResponseBody
	public String noticeAnnouncement(HttpServletRequest request){
		String time = request.getParameter("time");
		String msg = enterpriseManagementSystemService.noticeAnnouncement(time);
		return msg;
	}
	
	// 获取图片流
	@RequestMapping("/getPhotoUrl")
	@ResponseBody
	public void getPhotoUrl(HttpServletRequest request,HttpServletResponse response){
		String photo = request.getParameter("photo");
		enterpriseManagementSystemService.getPhotoUrl(request,response,photo);
	}
}
