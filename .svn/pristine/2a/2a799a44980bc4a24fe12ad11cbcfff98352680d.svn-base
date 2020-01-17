package mvc.controllers;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import mvc.service.DailySupervisionServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/dailySupervision")
public class DailySupervisionAction {
	private DailySupervisionServer dailySupervisionService;

	public DailySupervisionServer getDailySupervisionServer() {
		return dailySupervisionService;
	}

	@Autowired
	public void setDailySupervisionServer(DailySupervisionServer dailySupervisionService) {
		this.dailySupervisionService = dailySupervisionService;
	}
	
	/**
	 * 车辆动态监控 详细车辆聚合模块  访问杜云鹤接口
	 * @param jb
	 * @param companyid
	 * @return
	 */
	
	@RequestMapping("/findhldmar")
	@ResponseBody
	public String findhldmar(@RequestParam("jd") String jd,@RequestParam("wd") String wd
			,@RequestParam("jb") String jb,@RequestParam("companyid") String companyid,@RequestParam("type") String type){
		String msg = dailySupervisionService.findhldmar(jd,wd,jb,companyid,type);
		return msg;
	}
	
	/**
	 * 车辆动态监控 的 聚合模块
	 * @param jb
	 * @param companyid
	 * @return
	 */
	@RequestMapping("/xqrdt")
	@ResponseBody
	public String xqrdt(@RequestParam("jb") String jb,@RequestParam("companyid") String companyid,@RequestParam("type") String type){
		String msg = dailySupervisionService.xqrdt(jb,companyid,type);
		return msg;
	}
	
	/**
	 * 运维人员管理的 查询接口
	 * @param name
	 * @return
	 */
	@RequestMapping("/findperson")
	@ResponseBody
	public String findperson(@RequestParam("name") String name){
		String msg = dailySupervisionService.findperson(name);
		return msg;
	}
	/**
	 * 区域监控
	 * @return
	 */
	@RequestMapping("/qyjk")
	@ResponseBody
	public String qyjk(){
		String msg = dailySupervisionService.qyjk();
		return msg;
	}
	
	/**
	 * 区域监控 查询区域车辆 信息	
	 * @param id
	 * @return
	 */
	@RequestMapping("/findqyinfo")
	@ResponseBody
	public String findqyinfo(@RequestParam("id") String id){
		String msg = dailySupervisionService.findqyinfo(id);
		return msg;
	}
	
	/**
	 * 查询 某个单车
	 * @param bike
	 * @return
	 */
	@RequestMapping("/onebike")
	@ResponseBody
	public String onebike(@RequestParam("bike") String bike,@RequestParam("type") String type){
		String msg = dailySupervisionService.onebike(bike,type);
		return msg;
	}
	

    /**
     * 通知公告发布 →通知公告发布信息
     * @param postData
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/tzgglist")
    @ResponseBody()
    public String tzgglist(HttpServletRequest request,@RequestParam("begin") String begin,
    		@RequestParam("end") String end,@RequestParam("type") String type,@RequestParam("bt") String bt) throws ParseException {
        String msg = dailySupervisionService.tzgglist(request,begin,end,type,bt);
        return msg;
    }
    /**
     * 通知公告发布 →通知公告 填写式添加
     * @param
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/tzgg_insert")
    @ResponseBody
    public String hlwglInsert(HttpServletRequest request,@RequestParam("nr") String nr,
    		@RequestParam("bt") String bt,@RequestParam("user") String user,@RequestParam("fjname") String fjname,
    		@RequestParam("fjaddress") String fjaddress,@RequestParam("type") String type,@RequestParam("inserttype") String inserttype) {
        String msg = "ok";
        msg = dailySupervisionService.tzgg_insert(request,nr,bt,user,fjname,fjaddress,type,inserttype);
        return msg;
    }
    /**
     * 导入
     * @throws IOException 
     */
    @RequestMapping(value = "/importfile")
    @ResponseBody
    public String importfile(HttpServletRequest request,@RequestParam(value = "type")String type) throws IOException {
        String msg = dailySupervisionService.importfile(request,type);
        return msg;
    }
    
    /**
     * 删除通知公告
     * @param
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/tzggDel")
    @ResponseBody
    public String tzggDel(HttpServletRequest request,@RequestParam(value = "id")String id,@RequestParam(value = "user")String user) {
        String msg = "ok";
        msg = dailySupervisionService.tzggDel(request,id,user);
        return msg;
    }
    
    /**
     * 修改通知公告
     * @param
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/updateTzgg")
    @ResponseBody
    public String updateTzgg(HttpServletRequest request,@RequestParam("nr") String nr,
    		@RequestParam("bt") String bt,@RequestParam("user") String user,@RequestParam("fjname") String fjname,
    		@RequestParam("fjaddress") String fjaddress,@RequestParam("type") String type,@RequestParam("id") String id) {
        String msg = "ok";
        msg = dailySupervisionService.updateTzgg(request,nr,bt,user,fjname,fjaddress,type,id);
        return msg;
    }
    
    /**
	 * 车辆动态监控 详细车辆聚合模块  访问杜云鹤接口
	 * @param jb
	 * @param companyid
	 * @return
	 */
	
	@RequestMapping("/findNoHldmar")
	@ResponseBody
	public String findNoHldmar(@RequestParam("jd") String jd,@RequestParam("wd") String wd
			,@RequestParam("jb") String jb,@RequestParam("companyid") String companyid,@RequestParam("type") String type,@RequestParam("time") String time){
		String msg = dailySupervisionService.findNoHldmar(jd,wd,jb,companyid,type,time);
		return msg;
	}
	
	/**
	 * 车辆动态监控 的 聚合模块
	 * @param jb
	 * @param companyid
	 * @return
	 */
	@RequestMapping("/xqrdtByNo")
	@ResponseBody
	public String xqrdtByNo(@RequestParam("jb") String jb,@RequestParam("companyid") String companyid,@RequestParam("type") String type,@RequestParam("time") String time){
		String msg = dailySupervisionService.xqrdtByNo(jb,companyid,type,time);
		return msg;
	}

	/**
	 * 公共自行车点位信息
	 * @param jb
	 * @param companyid
	 * @return
	 */
	@RequestMapping("/getZxc")
	@ResponseBody
	public String getZxc(@RequestParam("name") String name){
		String msg = dailySupervisionService.getZxc(name);
		return msg;
	}
	

	/**
	 * 运维人员统计
	 * @param jb
	 * @param companyid
	 * @return
	 */
	@RequestMapping("/getYwryCount")
	@ResponseBody
	public String getYwryCount(){
		String msg = dailySupervisionService.getYwryCount();
		return msg;
	}

	/**
	  * 拖车
	 * @param company 公司名称
	 * @param bikeno	车辆编号
	 * @param type  电单车 自行车
	 * @param by	备案状态
	 * @param person  执法人员
	 * @param time  执法事件
	 * @param address  执法地址
	 * @param reason  执法原因
	 * @param photo 现场照片
	 * @return
	 */
	@RequestMapping("/trailer")
	@ResponseBody
	public String trailer(@RequestParam("company")String company,@RequestParam("bicycleno")String bicycleno,@RequestParam("type")String type,
			@RequestParam("bytype")String bytype,@RequestParam("person")String person,@RequestParam("time")String time,
			@RequestParam("address")String address,@RequestParam("reason")String reason,@RequestParam("photo")String photo,@RequestParam("lon")String lon,@RequestParam("lat")String lat){
		String msg = dailySupervisionService.trailer(company,bicycleno,type,bytype,person,time,address,reason,photo,lon,lat);
		return msg;
	}
}
