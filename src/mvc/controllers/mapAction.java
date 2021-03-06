package mvc.controllers;

import helper.DownloadAct;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.TextMessage;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.CommonServer;
import mvc.service.ConsumerService;
import mvc.service.ProducerService;
import mvc.service.bikeServer;
import mvc.service.mapServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/map")
public class mapAction {
	private mapServer mapServer;
	
	public mapServer getMapServer() {
		return mapServer;
	}
	@Autowired
	public void setMapServer(mapServer mapServer) {
		this.mapServer = mapServer;
	}
	@RequestMapping("/getVhic")
	@ResponseBody
	public String getVhic(@RequestParam("vhic") String vhic,HttpServletRequest request){
		String msg = mapServer.getVhic(vhic,request);
		return msg;
	}
	@RequestMapping("/getHistory")
	@ResponseBody
	public String getHistory(HttpServletRequest request){
		String msg = mapServer.getHistory(request);
		return msg;
	}
	@RequestMapping("/getHistoryexcle")
	@ResponseBody
	public String getHistoryexcle(HttpServletRequest request,
								  HttpServletResponse response) throws IOException{
		String a[] = {"公司","车牌号码","GPS时间","车辆状态","是否精确","经度","纬度","方向","GPS速度(KM/H)","总里程"};//导出列明
		String b[] = {"COMP_NAME","VEHICLE_NUM","SPEEDTIME","CLZT","SFJQ","PX","PY","FX","SPEED","MILEAGE"};//导出map中的key
		String gzb = "位置监控-位置信息";//导出sheet名和导出的文件名
		String msg = mapServer.getHistory(request);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/lostFound")
	@ResponseBody
	public String swcz(String qd_stime,String qd_etime,
			String zd_stime,String zd_etime,
			String qd_jwd,String zd_jwd,String type,HttpServletRequest request){
		String msg = mapServer.swcz(qd_stime,qd_etime,zd_stime,zd_etime,qd_jwd,zd_jwd,type,request);
		return msg;
	}
	@RequestMapping("swczexcle")
	@ResponseBody
	public String swczexcle(HttpServletRequest request,
			HttpServletResponse response,String qd_stime,String qd_etime,
			String zd_stime,String zd_etime,
			String qd_jwd,String zd_jwd,String type) throws IOException{
		String a[] = {"车牌号码","经纬度","时间"};//导出列明
		String b[] = {"vehi_no","px","stime"};//导出map中的key
		String gzb = "车辆信息";//导出sheet名和导出的文件名
		String gzb1 = "起点车辆";//导出sheet名和导出的文件名
		String gzb2 = "终点车辆";//导出sheet名和导出的文件名
//		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		String msg = mapServer.swcz(qd_stime.trim(),qd_etime.trim(),zd_stime.trim(),zd_etime.trim(),qd_jwd.trim(),zd_jwd.trim(),type,request);
		List<Map<String, Object>>jg = DownloadAct.parseJSON2Listx(msg,"jg");
		List<Map<String, Object>>qd = DownloadAct.parseJSON2Listx(msg,"qd");
		List<Map<String, Object>>zd = DownloadAct.parseJSON2Listx(msg,"zd");
		DownloadAct.downloadx(request,response,a,b,gzb,gzb1,gzb2,jg,qd,zd);
		return null;
	}
	@RequestMapping("/getComp")
	@ResponseBody
	public String getComp(HttpServletRequest request){
		String msg = mapServer.getComp(request);
		return msg;
	}
	@RequestMapping("/getVhicList")
	@ResponseBody
	public String getVhicList(@RequestParam("id") String id,HttpServletRequest request){
		String msg = mapServer.getVhicList(id,request);
		return msg;
	}
	@RequestMapping("/getOneVhic")
	@ResponseBody
	public String getOneVhic(HttpServletRequest request){
		String msg = mapServer.getOneVhic(request);
		return msg;
	}
	@RequestMapping("/getOneVhicexcle")
	@ResponseBody
	public String getOneVhicexcle(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String a[] = {"车牌号码","分公司","终端类型","精确度","SIM卡号","终端编号","GPS时间","GPS速度","车辆状态","方向"};//导出列明
		String b[] = {"VEHI_NO","COMP_NAME","MDT_SUB_TYPE","SFJQ","VEHI_SIM","MDT_NO","TIME","SPEED","STATETYPE","ANGLE"};//导出map中的key
		String gzb = "位置监控-位置信息";//导出sheet名和导出的文件名
		String msg = mapServer.getOneVhic(request);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/getMonitor")
	@ResponseBody
	public String getMonitor(HttpServletRequest request) {
		String msg = mapServer.qyjk(request);
		return msg;
	}
	@RequestMapping("/areaVhicexcle")
	@ResponseBody
	public String areaVhicexcle(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String a[] = {"车牌号码","分公司","SIM卡号","车速km/h","在线状态","空重车状态","GPS时间"};//导出列明
		String b[] = {"VEHI_NO","COMP_NAME","VEHI_SIM","SPEED","TYPE","STATE","TIME"};//导出map中的key
		String gzb = "位置监控-位置信息";//导出sheet名和导出的文件名
		String msg = mapServer.areaVhic(request);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/getArea")
	@ResponseBody
	public String getArea(HttpServletRequest request) {
		String msg = mapServer.getArea(request);
		return msg;
	}
	@RequestMapping("/addArea")
	@ResponseBody
	public String addArea(HttpServletRequest request) {
		String msg = mapServer.addArea(request);
		return msg;
	}
	@RequestMapping("/editArea")
	@ResponseBody
	public String editArea(HttpServletRequest request) {
		String msg = mapServer.editArea(request);
		return msg;
	}
	@RequestMapping("/delArea")
	@ResponseBody
	public String delArea(HttpServletRequest request) {
		String msg = mapServer.delArea(request);
		return msg;
	}
	@RequestMapping("/importantInfo")
	@ResponseBody
	public String importantInfo(HttpServletRequest request) {
		String msg = mapServer.importantInfo(request);
		return msg;
	}
	@RequestMapping("/getInfo")
	@ResponseBody
	public String getInfo(HttpServletRequest request) {
		String msg = mapServer.getInfo(request);
		return msg;
	}
	@RequestMapping("/addInfo")
	@ResponseBody
	public String addInfo(HttpServletRequest request) {
		String msg = mapServer.addInfo(request);
		return msg;
	}
	@RequestMapping("/editInfo")
	@ResponseBody
	public String editInfo(HttpServletRequest request) {
		String msg = mapServer.editInfo(request);
		return msg;
	}
	@RequestMapping("/delInfo")
	@ResponseBody
	public String delInfo(HttpServletRequest request) {
		String msg = mapServer.delInfo(request);
		return msg;
	}
	@RequestMapping("/getBa_select")
	@ResponseBody
	public String getBa_select(HttpServletRequest request) {
		String msg = mapServer.getBa_select(request);
		return msg;
	}
	@RequestMapping("/getComp_select")
	@ResponseBody
	public String getComp_select(HttpServletRequest request) {
		String msg = mapServer.getComp_select(request);
		return msg;
	}
	@RequestMapping("/getVhic_select")
	@ResponseBody
	public String getVhic_select(HttpServletRequest request) {
		String msg = mapServer.getVhic_select(request);
		return msg;
	}
	@RequestMapping("/sendMessage")
	@ResponseBody
	public String sendMessage(HttpServletRequest request) {
		String msg = mapServer.sendMessage(request);
		return msg;
	}
	@RequestMapping("/oneSendMessage")
	@ResponseBody
	public String oneSendMessage(HttpServletRequest request) {
		String msg = mapServer.oneSendMessage(request);
		return msg;
	}
	@Resource(name = "queueDestination1")
    private Destination destination;

    //队列消息生产者
    @Autowired
    private ProducerService producer;

    //队列消息消费者
    @Autowired
    private ConsumerService consumer;

    @RequestMapping("/SendMessageMQ")
    @ResponseBody
    public void send(HttpServletRequest request) {
        System.out.println(Thread.currentThread().getName()+"------------send to jms Start");
        String msg = mapServer.SendMessageMQ(request);
        producer.sendMessage(msg);
        System.out.println(Thread.currentThread().getName()+"------------send to jms End");
    }

    @RequestMapping(value= "/ReceiveMessageMQ")
    @ResponseBody
    public String receive(){
        System.out.println(Thread.currentThread().getName()+"------------receive from jms Start");
        String tm = consumer.receive(destination);
        System.out.println(Thread.currentThread().getName()+"------------receive from jms End");
        return tm;
    }
    @RequestMapping("/setTerminal")
    @ResponseBody
    public void setTerminal(HttpServletRequest request){
    	 String msg = mapServer.setTerminal(request);
    	 System.out.println(msg);
         producer.sendMessage(msg);
    }
    @RequestMapping("/getUser")
    @ResponseBody
    public String getUser(HttpServletRequest request){
    	String msg = mapServer.getUser(request);
    	return msg;
    }
	@RequestMapping("/getCenter")
	@ResponseBody
	public String getCenter(HttpServletRequest request){
		String msg = mapServer.getCenter(request);
		return msg;
	}
    /**
	 * 独立车辆组表
	 */
	@RequestMapping("/finddlclzb")
	@ResponseBody
	public String finddlclzb(HttpServletRequest request,@RequestParam("name") String name){
		String msg = mapServer.finddlclzb(request,name);
		return msg;
	}
	@RequestMapping("finddlclzbexcle")
	@ResponseBody
	public String finddlclzbexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("name") String name) throws IOException{
		String a[] = {"车辆组名","车牌号码"};//导出列明
		String b[] = {"CLZ_NAME","CLZ_CP"};//导出map中的key
		String gzb = "独立车辆组表";//导出sheet名和导出的文件名
		name = java.net.URLDecoder.decode(name, "UTF-8");
		String msg = mapServer.finddlclzb(request,name);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/adddlclzb")
	@ResponseBody
	public String adddlclzb(HttpServletRequest request){
		String msg = mapServer.adddlclzb(request);
		return msg;
	}
	@RequestMapping("/editdlclzb")
	@ResponseBody
	public String editdlclzb(HttpServletRequest request){
		String msg = mapServer.editdlclzb(request);
		return msg;
	}
	@RequestMapping("/deldlclzb")
	@ResponseBody
	public String deldlclzb(@RequestParam("id") String id){
		String msg = mapServer.deldlclzb(id);
		return msg;
	}
	
	/**
	 * 独立车辆组账号表
	 */
	@RequestMapping("/finddlclzzhb")
	@ResponseBody
	public String finddlclzzhb(@RequestParam("gjz") String gjz){
		String msg = mapServer.finddlclzzhb(gjz);
		return msg;
	}
	@RequestMapping("finddlclzzhbexcle")
	@ResponseBody
	public String finddlclzzhbexcle(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("gjz") String gjz) throws IOException{
		String a[] = {"姓名","用户名","密码","车辆组","页面权限"};//导出列明
		String b[] = {"REAL_NAME","USERNAME","PASSWORD","CLZM","STATION_NAME"};//导出map中的key
		String gzb = "车辆组账号表";//导出sheet名和导出的文件名
		gjz = java.net.URLDecoder.decode(gjz, "UTF-8");
		String msg = mapServer.finddlclzzhb(gjz);
		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}
	@RequestMapping("/adddlclzzhb")
	@ResponseBody
	public String adddlclzzhb(HttpServletRequest request){
		String msg = mapServer.adddlclzzhb(request);
		return msg;
	}
	@RequestMapping("/editdlclzzhb")
	@ResponseBody
	public String editdlclzzhb(HttpServletRequest request){
		String msg = mapServer.editdlclzzhb(request);
		return msg;
	}
	@RequestMapping("/deldlclzzhb")
	@ResponseBody
	public String deldlclzzhb(@RequestParam("id") String id){
		String msg = mapServer.deldlclzzhb(id);
		return msg;
	}
	@RequestMapping("/loginType")
	@ResponseBody
	public String loginType(HttpServletRequest request){
		String msg = mapServer.loginType(request);
		return msg;
	}
	@RequestMapping("/getClz")
	@ResponseBody
	public String getClz(HttpServletRequest request){
		String msg = mapServer.getClz(request);
		return msg;
	}
	@RequestMapping("/getClzTree")
	@ResponseBody
	public String getClzTree(HttpServletRequest request){
		String msg = mapServer.getClzTree(request);
		return msg;
	}
	@RequestMapping("/getClzVhic")
	@ResponseBody
	public String getClzVhic(HttpServletRequest request){
		String msg = mapServer.getClzVhic(request);
		return msg;
	}
	@RequestMapping("/getAlarm")
	@ResponseBody
	public String getAlarm(HttpServletRequest request){
		String msg = mapServer.getAlarm(request);
		return msg;
	}
	@RequestMapping("/multiVehicle")
	@ResponseBody
	public String multiVehicle(HttpServletRequest request){
		String msg = mapServer.multiVehicle(request);
		return msg;
	}
	@RequestMapping({"/getHisVhic"})
	@ResponseBody
	public String getHisVhic(HttpServletRequest request) {
		String msg = mapServer.getHisVhic(request);
		return msg;
	}
	@RequestMapping("/getServiceEvaluation")
	@ResponseBody
	public String getServiceEvaluation(HttpServletRequest request) {
		String msg = mapServer.getServiceEvaluation(request);
		return msg;
	}
	@RequestMapping("/sendTCP")
	@ResponseBody
	public String sendTDP(HttpServletRequest request) {
		String msg = mapServer.sendTDP(request);
		return msg;
	}

	@RequestMapping("/getVehicle")
	@ResponseBody
	public String getVehicle(HttpServletRequest request) {
		String msg = mapServer.getVehicle(request);
		return msg;
	}

	@RequestMapping("/lovecar")
	@ResponseBody
	public String lovecar(HttpServletRequest request) {
		String msg = mapServer.lovecar(request);
		return msg;
	}

	@RequestMapping("/yjcbjcar")
	@ResponseBody
	public String yjcbjcar(HttpServletRequest request) {
		String msg = mapServer.yjcbjcar(request);
		return msg;
	}

	@RequestMapping("/findbjclitem")
	@ResponseBody
	public String findbjclitem(HttpServletRequest request) {
		String msg = mapServer.findbjclitem(request);
		return msg;
	}

	@RequestMapping("/getVehiInfo")
	@ResponseBody
	public String getVehiInfo(HttpServletRequest request) {
		String msg = mapServer.getVehiInfo(request);
		return msg;
	}

	@RequestMapping("/addbjcl")
	@ResponseBody
	public String addbjcl(HttpServletRequest request) {
		String msg = mapServer.addbjcl(request);
		return msg;
	}

	@RequestMapping("/editbjcl")
	@ResponseBody
	public String editbjcl(HttpServletRequest request) {
		String msg = mapServer.editbjcl(request);
		return msg;
	}
}
