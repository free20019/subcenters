package mvc.controllers;

import helper.DownloadAct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.CommonServer;
import mvc.service.bikeServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/bike")
public class bikeAction {
	private bikeServer bikeServer;
	
	public bikeServer getBikeServer() {
		return bikeServer;
	}
	@Autowired
	public void setBikeServer(bikeServer bikeServer) {
		this.bikeServer = bikeServer;
	}
	@RequestMapping("/getPunish")
	@ResponseBody
	public String getPunish(HttpServletRequest request){
		return bikeServer.getPunish(request);
	}

	@RequestMapping("/getqykh")
	@ResponseBody
	public String getqykh(HttpServletRequest request){
		return bikeServer.getqykh(request);
	}
	@RequestMapping("/getDeposit")
	@ResponseBody
	public String getDeposit(){
		return bikeServer.getDeposit();
	}
	@RequestMapping("/getcomp")
	@ResponseBody
	public String getcomp(){
		return bikeServer.getcomp();
	}
	@RequestMapping("getcompexcle")
	@ResponseBody
	public String getcompexcle(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String a[] = {"企业名称","法人代表","统一社会信用代码","联系人","注册地址","通讯地址"};//导出列明
		String b[] = {"CompanyName","LegalName","USCC","ContactPerson","RegAddress","ContactAddress"};//导出map中的key
		String gzb = "企业基本信息";//导出sheet名和导出的文件名
		String msg = bikeServer.getcomp();
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/getPerson")
	@ResponseBody
	public String getPerson(HttpServletRequest request){
		return bikeServer.getPerson(request);
	}
	@RequestMapping("getPersonexcle")
	@ResponseBody
	public String getPersonexcle(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String a[] = {"所属公司","姓名","电话","上下班时间","工作职责","责任区域位置"};//导出列明
		String b[] = {"CompanyName_JC","Name","Phone","Time","Duty","Area"};//导出map中的key
		String gzb = "运维人员信息";//导出sheet名和导出的文件名
		String msg = bikeServer.getPersonExcle(request);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/getBike")
	@ResponseBody
	public String getBike(HttpServletRequest request){
		return bikeServer.getBike(request);
	}
	@RequestMapping("getBikeexcle")
	@ResponseBody
	public String getBikeExcel(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String a[] = {"企业名称","车辆编号","状态","操作标识","更新时间"};//导出列明
		String b[] = {"CompanyName_JC","BicycleNo","ZT","CZBS","SJ"};//导出map中的key
		String gzb = "车辆基本信息";//导出sheet名和导出的文件名
		String msg = bikeServer.getBikeExcel(request);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/getKeepBike")
	@ResponseBody
	public String getKeepBike(){
		return bikeServer.getKeepBike();
	}

	@RequestMapping("getKeepBikeexcle")
	@ResponseBody
	public String getKeepBikeExcel(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String a[] = {"平台名称","未备案车辆","已查询车辆","未备案率"};//导出列明
		String b[] = {"CompanyName_JC","wbas","ycxs","bal"};//导出map中的key
		String gzb = "备案车辆统计";//导出sheet名和导出的文件名
		String msg = bikeServer.getKeepBike();
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/getKeepBikeQuery")
	@ResponseBody
	public String getKeepBikeQuery(HttpServletRequest request){
		return bikeServer.getKeepBikeQuery(request);
	}

	@RequestMapping("getKeepBikeQueryexcle")
	@ResponseBody
	public String getKeepBikeQueryExcel(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String a[] = {"平台名称","车辆编号","是否备案","所属区域","扫描时间"};//导出列明
		String b[] = {"CompanyName_JC","BicycleNo","ba","area","smsj"};//导出map中的key
		String gzb = "备案车辆查询";//导出sheet名和导出的文件名
		String msg = bikeServer.getKeepBikeQuery(request);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("getKeepBikeexcle")
	@ResponseBody
	public String getKeepBikeexcle(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String a[] = {"企业名称","未备案车辆","已查询车辆","未本案率"};//导出列明
		String b[] = {"CompanyName_JC","wbas","ycxs","bal"};//导出map中的key
		String gzb = "备案车辆查询";//导出sheet名和导出的文件名
		String msg = bikeServer.getKeepBike();
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/getProgram")
	@ResponseBody
	public String getProgram(HttpServletRequest request){
		return bikeServer.getProgram(request);
	}
	@RequestMapping("getProgramexcle")
	@ResponseBody
	public String getProgramexcle(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String a[] = {"行政区域","单车企业","扫描总量","扫描车辆","已备案车辆","已备案率","未备案车辆","未备案率","减量车辆"};//导出列明
		String b[] = {"area","companyid","smzl","smcl","yba","ybal","wba","wbal","jlc1"};//导出map中的key
		String gzb = "小程序查询";//导出sheet名和导出的文件名
		String msg = bikeServer.getProgram(request);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/getbikeAnalysis")
	@ResponseBody
	public String getbikeAnalysis(HttpServletRequest request){
		return bikeServer.getbikeAnalysis(request);
	}
	@RequestMapping("/getDataAccess")
	@ResponseBody
	public String getDataAccess(HttpServletRequest request){
		return bikeServer.getDataAccess(request);
	}
	@RequestMapping("/getUser")
	@ResponseBody
	public String getUser(HttpServletRequest request){
		return bikeServer.getUser(request);
	}
	@RequestMapping("/addUser")
	@ResponseBody
	public String addUser(HttpServletRequest request){
		return bikeServer.addUser(request);
	}
	@RequestMapping("/editUser")
	@ResponseBody
	public String editUser(HttpServletRequest request){
		return bikeServer.editUser(request);
	}
	@RequestMapping("/delUser")
	@ResponseBody
	public String delUser(HttpServletRequest request){
		return bikeServer.delUser(request);
	}
	@RequestMapping("/getArlyWarning")
	@ResponseBody
	public String getArlyWarning(HttpServletRequest request){
		return bikeServer.getArlyWarning(request);
	}
	@RequestMapping("/ArlyWarningHandle")
	@ResponseBody
	public String ArlyWarningHandle(HttpServletRequest request){
		return bikeServer.ArlyWarningHandle(request);
	}
	@RequestMapping("/addPower")
	@ResponseBody
	public String addPower(HttpServletRequest request){
		return bikeServer.addPower(request);
	}
	@RequestMapping("/getBicycleInfo")
	@ResponseBody
	public String getBicycleInfo(){
		return bikeServer.getBicycleInfo();
	}
	@RequestMapping("/getBicycleDistribution")
	@ResponseBody
	public String getBicycleDistribution(){
		return bikeServer.getBicycleDistribution();
	}
	@RequestMapping("/login")
	@ResponseBody
	public String login(HttpServletRequest request){
		return bikeServer.login(request);
	}
	@RequestMapping("/getBicycleOrderNo")
	@ResponseBody
	public String getBicycleOrderNo(){
		return bikeServer.getBicycleOrderNo();
	}
	@RequestMapping("/getTurnoverRate")
	@ResponseBody
	public String getTurnoverRate(){
		return bikeServer.getTurnoverRate();
	}
	@RequestMapping("/getBicycleOrderTurn")
	@ResponseBody
	public String getBicycleOrderTurn(){
		return bikeServer.getBicycleOrderTurn();
	}
	@RequestMapping("/getNotUsed")
	@ResponseBody
	public String getNotUsed(HttpServletRequest request){
		return bikeServer.getNotUsed(request);
	}
	@RequestMapping("/getLogin")
	@ResponseBody
	public String getLogin(HttpServletRequest request){
		return bikeServer.getLogin(request);
	}
	
	@RequestMapping("/getLog")
	@ResponseBody
	public String getLog(int page){
		return bikeServer.getLog(page);
	}
	
	@RequestMapping("/getUserType")
	@ResponseBody
	public String getUserType(){
		return bikeServer.getUserType();
	}
}
