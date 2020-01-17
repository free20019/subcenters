package mvc.controllers;



import mvc.service.CommonServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/common")
public class CommonAction {
	private CommonServer commonService;

	public CommonServer getCommonServer() {
		return commonService;
	}

	@Autowired
	public void setCommonServer(CommonServer commonService) {
		this.commonService = commonService;
	}

	//车牌
	@RequestMapping("/vehicle")
	@ResponseBody
	public String getVehicle(String type){
		String msg=commonService.getVehicle(type);
		return msg;
	}

	//公司
	@RequestMapping("/company")
	@ResponseBody
	public String getCompany(String type){
		String msg=commonService.getCompany(type);
		return msg;
	}

	//车牌号变更申请_新车牌
	@RequestMapping("/newVehicle")
	@ResponseBody
	public String getNewVehicle(){
		String msg=commonService.getNewVehicle();
		return msg;
	}

	//车辆组
	@RequestMapping("/groupVehicle")
	@ResponseBody
	public String getGroupVehicle(HttpServletRequest request){
		String msg=commonService.getGroupVehicle(request);
		return msg;
	}

	//岗位
	@RequestMapping("/station")
	@ResponseBody
	public String getStation(){
		String msg=commonService.getStation();
		return msg;
	}

	//用户管理公司
	@RequestMapping("/userCompany")
	@ResponseBody
	public String getUserCompany(){
		String msg=commonService.getUserCompany();
		return msg;
	}

	//功能列表
	@RequestMapping("/power")
	@ResponseBody
	public String getPower(){
		String msg=commonService.getPower();
		return msg;
	}

	//登录
    @RequestMapping(value = "/login")
    @ResponseBody
    public String login(HttpServletRequest request,@RequestParam("username") String username,@RequestParam("password") String password) {
		return commonService.login(request,username,password);
    }

	//获取权限
	@RequestMapping("/index")
	@ResponseBody
	public String getIndex(HttpServletRequest request){
		String msg=commonService.getIndex(request);
		return msg;
	}

	//燃料类型
	@RequestMapping("/fuelType")
	@ResponseBody
	public String getFuelType(){
		String msg=commonService.getFuelType();
		return msg;
	}

	//终端类型
	@RequestMapping("/terminalType")
	@ResponseBody
	public String getTerminalType(){
		String msg=commonService.getTerminalType();
		return msg;
	}

	//车辆类型
	@RequestMapping("/vehicleType")
	@ResponseBody
	public String getVehicleType(){
		String msg=commonService.getVehicleType();
		return msg;
	}

	//车辆组
	@RequestMapping("/vehicleGroup")
	@ResponseBody
	public String getVehicleGroup(String userId){
		String msg=commonService.getVehicleGroup(userId);
		return msg;
	}

	//车辆组车辆
	@RequestMapping("/groupVehicles")
	@ResponseBody
	public String getGroupVehicles(String id){
		String msg=commonService.getGroupVehicles(id);
		return msg;
	}
}
