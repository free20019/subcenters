package mvc.service;

import helper.Client;
import helper.GeometryHandler;
import helper.JacksonUtil;
import helper.LogUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mvc.entity.TbCompany;
import mvc.mapper.bikeDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Geometry;


@Service
public class bikeServer{
	@Resource
	bikeDao bikeDao;
	private int pageSize = 30;
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public String getPunish(HttpServletRequest request){
		LogUtil.log("服务监督系统-处罚信息查询", "服务监督系统-处罚信息查询");
		String marked = request.getParameter("marked");
		String company = request.getParameter("company");
		String p = request.getParameter("page");
		String page = (Integer.parseInt(p)-1) * pageSize+"";
		List<Map<String, Object>> list = bikeDao.getPunish(marked,company,page);
		return jacksonUtil.toJson(list);
	}
	public String getqykh(HttpServletRequest request){
		LogUtil.log("服务监督系统-企业考核结果查询", "服务监督系统-企业考核结果查询");
		String year = request.getParameter("year");
		String quarter = request.getParameter("quarter");
		List<Map<String, Object>> list = bikeDao.getqykh(year,quarter);
		return jacksonUtil.toJson(list);
	}
	public String getDeposit(){
		LogUtil.log("押金管理系统-押金账户查询", "押金管理系统-押金账户查询");
		return jacksonUtil.toJson(bikeDao.getDeposit());
	}
	public String getcomp(){
		LogUtil.log("信息查询系统-综合查询-企业基本信息", "信息查询系统-综合查询-企业基本信息");
		return jacksonUtil.toJson(bikeDao.getcomp());
	}
	public String getPerson(HttpServletRequest request){
		LogUtil.log("信息查询系统-综合查询-运维人员信息", "信息查询系统-综合查询-运维人员信息");
		String compname = request.getParameter("compname");
		String name = request.getParameter("name");
		String p = request.getParameter("page");
		String page = (Integer.parseInt(p)-1) * pageSize+"";
		String pageSize = "30"; 
		List<Map<String, Object>> list = bikeDao.getPerson(compname,name,page,pageSize);
		return jacksonUtil.toJson(list);
	}
	public String getPersonExcle(HttpServletRequest request){
		LogUtil.log("信息查询系统-综合查询-运维人员信息导出", "信息查询系统-综合查询-运维人员信息导出");
		String compname = request.getParameter("compname");
		String name = request.getParameter("name");
		String page = "1";
		String pageSize = "30000"; 
		List<Map<String, Object>> list = bikeDao.getPerson(compname,name,page,pageSize);
		return jacksonUtil.toJson(list);
	}
	public String getBike(HttpServletRequest request){
		LogUtil.log("信息查询系统-综合查询-车辆基本信息", "信息查询系统-综合查询-车辆基本信息");
		String compname = request.getParameter("compname");
		String bikeno = request.getParameter("bikeno");
		String state = request.getParameter("state");
		String p = request.getParameter("page");
		String page = (Integer.parseInt(p)-1) * pageSize+"";
		System.out.println(compname+"  "+bikeno+"  "+state);
		List<Map<String, Object>> list = bikeDao.getBike(compname,bikeno,state,page,"30");
		List<Map<String, Object>> listCount = bikeDao.getBikeCount(compname,bikeno,state);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("count", listCount.get(0).get("c"));
		return jacksonUtil.toJson(map);
	}
	public String getBikeExcel(HttpServletRequest request){
		LogUtil.log("信息查询系统-综合查询-车辆基本信息导出", "信息查询系统-综合查询-车辆基本信息导出");
		String compname = request.getParameter("compname");
		String bikeno = request.getParameter("bikeno");
		String state = request.getParameter("state");
		String page = "1";
		String pageSize = "1000000";
		List<Map<String, Object>> list = bikeDao.getBike(compname,bikeno,state,page,pageSize);
		for (int i = 0; i < list.size(); i++) {
			if(String.valueOf(list.get(i).get("Flag")).equals("0")){
				list.get(i).put("CZBS", "新增");
			}else{
				list.get(i).put("CZBS", "更新");
			}
			if(String.valueOf(list.get(i).get("isOut")).equals("0")){
				list.get(i).put("ZT", "有效");
			}else{
				list.get(i).put("ZT", "无效(减量车)");
			}
			list.get(i).put("SJ", String.valueOf(list.get(i).get("UpdateTime")));
		}
		return jacksonUtil.toJson(list);
	}
	public String getKeepBike(){
		LogUtil.log("信息查询系统-综合查询-备案车辆统计", "信息查询系统-综合查询-备案车辆统计");
		List<Map<String, Object>> list = bikeDao.getKeepBike();
		DecimalFormat df = new DecimalFormat("0.00");
		for (int i = 0; i < list.size(); i++) {
			String bal = df.format(Double.parseDouble(String.valueOf(list.get(i).get("wbas")))/
					Double.parseDouble(String.valueOf(list.get(i).get("ycxs")))*100)+"%";
			list.get(i).put("bal", bal);
		}
		return jacksonUtil.toJson(list);
	}
	public String getKeepBikeQuery(HttpServletRequest request){
		LogUtil.log("信息查询系统-综合查询-备案车辆查询", "信息查询系统-综合查询-备案车辆查询");
		String compname = request.getParameter("compname");
		String bikeno = request.getParameter("bikeno");
		String p = String.valueOf(request.getParameter("page")).equals("null")?"1":request.getParameter("page");
		String page = (Integer.parseInt(p)-1) * pageSize+"";
		String pageSize = String.valueOf(request.getParameter("page")).equals("null")?"10000":"30"; 
		List<Map<String, Object>> list = bikeDao.getKeepBikeQuery(compname,bikeno,page,pageSize);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("ba", String.valueOf(list.get(i).get("babj")).equals("0")?"是":"否");
			list.get(i).put("smsj", String.valueOf(list.get(i).get("sj")));
		}
		return jacksonUtil.toJson(list);
	}
	public String getProgram(HttpServletRequest request){
		LogUtil.log("信息查询系统-综合查询-小程序查询", "信息查询系统-综合查询-小程序查询");
		String stime = request.getParameter("stime")+" 00:00:00";
		String etime = request.getParameter("etime")+" 23:59:59";
		String area = request.getParameter("area");
		List<Map<String, Object>> list = bikeDao.getProgram(stime,etime,area);
		return jacksonUtil.toJson(list);
	}
	public String getbikeAnalysis(HttpServletRequest request) {
		LogUtil.log("数据分析系统-运行情况分析-车辆周转率/量分析", "数据分析系统-运行情况分析-车辆周转率/量分析");
		DecimalFormat df = new DecimalFormat("0.00");
		String time = request.getParameter("time");
		List<Map<String, Object>> list = bikeDao.getbikeAnalysis(time);
		List<Map<String, Object>> ofo = getanalysis(list, "ofo");
		Map<String, String> ofomap = new HashMap<String, String>();
		List<String> szo=new ArrayList<String>(),
				szh=new ArrayList<String>(),
				szm=new ArrayList<String>(),
				slq=new ArrayList<String>(),
				sjdy=new ArrayList<String>(),
				szh1=new ArrayList<String>(),
				szxl=new ArrayList<String>(),
				szxc=new ArrayList<String>();
		List<String> lzo=new ArrayList<String>(),
				lzh=new ArrayList<String>(),
				lzm=new ArrayList<String>(),
				llq=new ArrayList<String>(),
				ljdy=new ArrayList<String>(),
				lzh1=new ArrayList<String>(),
				lzxl=new ArrayList<String>(),
				lzxc=new ArrayList<String>();
		List<String> szr=new ArrayList<String>();
		ofomap.put("CompanyId", "OFO");
		Map<String, String> ofoliangmap = new HashMap<String, String>();
		ofoliangmap.put("CompanyId", "OFO");
		for (int i = 0; i < ofo.size(); i++) {
			szo.add(df.format(Double.parseDouble(String.valueOf(ofo.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(ofo.get(i).get("Vehicle_no")))));
			lzo.add(String.valueOf(ofo.get(i).get("Order_num")).equals("null")?"0":String.valueOf(ofo.get(i).get("Order_num")));
			szr.add((i+1)+"日");
			ofoliangmap.put(i+"", String.valueOf(ofo.get(i).get("Order_num")).equals("null")?"0":String.valueOf(ofo.get(i).get("Order_num")));
			ofomap.put(i+"", df.format(Double.parseDouble(String.valueOf(ofo.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(ofo.get(i).get("Vehicle_no")))));
		}
		List<Map<String, Object>> hellobike = getanalysis(list, "hellobike");
		Map<String, String> hellobikemap = new HashMap<String, String>();
		Map<String, String> hellobikeliangmap = new HashMap<String, String>();
		hellobikemap.put("CompanyId", "哈啰单车");
		hellobikeliangmap.put("CompanyId", "哈啰单车");
		for (int i = 0; i < hellobike.size(); i++) {
			hellobikeliangmap.put(i+"", String.valueOf(hellobike.get(i).get("Order_num")).equals("null")?"0":String.valueOf(hellobike.get(i).get("Order_num")));
			szh.add(df.format(Double.parseDouble(String.valueOf(hellobike.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(hellobike.get(i).get("Vehicle_no")))));
			lzh.add(String.valueOf(hellobike.get(i).get("Order_num")).equals("null")?"0":String.valueOf(hellobike.get(i).get("Order_num")));
			hellobikemap.put(i+"", df.format(Double.parseDouble(String.valueOf(hellobike.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(hellobike.get(i).get("Vehicle_no")))));
		}
		List<Map<String, Object>> mb = getanalysis(list, "mb");
		Map<String, String> mbmap = new HashMap<String, String>();
		Map<String, String> mbliangmap = new HashMap<String, String>();
		mbmap.put("CompanyId", "摩拜");
		mbliangmap.put("CompanyId", "摩拜");
		for (int i = 0; i < mb.size(); i++) {
			mbliangmap.put(i+"", String.valueOf(mb.get(i).get("Order_num")).equals("null")?"0":String.valueOf(mb.get(i).get("Order_num")));
			szm.add(df.format(Double.parseDouble(String.valueOf(mb.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(mb.get(i).get("Vehicle_no")))));
			lzm.add(String.valueOf(mb.get(i).get("Order_num")).equals("null")?"0":String.valueOf(mb.get(i).get("Order_num")));
			mbmap.put(i+"", df.format(Double.parseDouble(String.valueOf(mb.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(mb.get(i).get("Vehicle_no")))));
		}
		List<Map<String, Object>> leqi = getanalysis(list, "leqi");
		Map<String, String> leqimap = new HashMap<String, String>();
		Map<String, String> leqiliangmap = new HashMap<String, String>();
		leqimap.put("CompanyId", "乐骑");
		leqiliangmap.put("CompanyId", "乐骑");
		for (int i = 0; i < leqi.size(); i++) {
			leqiliangmap.put(i+"", String.valueOf(leqi.get(i).get("Order_num")).equals("null")?"0":String.valueOf(leqi.get(i).get("Order_num")));
			slq.add(df.format(Double.parseDouble(String.valueOf(leqi.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(leqi.get(i).get("Vehicle_no")))));
			llq.add(String.valueOf(leqi.get(i).get("Order_num")).equals("null")?"0":String.valueOf(leqi.get(i).get("Order_num")));
			leqimap.put(i+"", df.format(Double.parseDouble(String.valueOf(leqi.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(leqi.get(i).get("Vehicle_no")))));
		}
		List<Map<String, Object>> jindy = getanalysis(list, "jindy");
		Map<String, String> jindymap = new HashMap<String, String>();
		Map<String, String> jindyliangmap = new HashMap<String, String>();
		jindymap.put("CompanyId", "筋斗云");
		jindyliangmap.put("CompanyId", "筋斗云");
		for (int i = 0; i < jindy.size(); i++) {
			jindyliangmap.put(i+"", String.valueOf(jindy.get(i).get("Order_num")).equals("null")?"0":String.valueOf(jindy.get(i).get("Order_num")));
			sjdy.add(df.format(Double.parseDouble(String.valueOf(jindy.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(jindy.get(i).get("Vehicle_no")))));
			ljdy.add(String.valueOf(jindy.get(i).get("Order_num")).equals("null")?"0":String.valueOf(jindy.get(i).get("Order_num")));
			jindymap.put(i+"", df.format(Double.parseDouble(String.valueOf(jindy.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(jindy.get(i).get("Vehicle_no")))));
		}
		List<Map<String, Object>> hellobike1 = getanalysis(list, "hellobike1");
		Map<String, String> hellobike1map = new HashMap<String, String>();
		Map<String, String> hellobike1liangmap = new HashMap<String, String>();
		hellobike1map.put("CompanyId", "哈啰电单车");
		hellobike1liangmap.put("CompanyId", "哈啰电单车");
		for (int i = 0; i < hellobike1.size(); i++) {
			hellobike1liangmap.put(i+"", String.valueOf(hellobike1.get(i).get("Order_num")).equals("null")?"0":String.valueOf(hellobike1.get(i).get("Order_num")));
			szh1.add(df.format(Double.parseDouble(String.valueOf(hellobike1.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(hellobike1.get(i).get("Vehicle_no")))));
			lzh1.add(String.valueOf(hellobike1.get(i).get("Order_num")).equals("null")?"0":String.valueOf(hellobike1.get(i).get("Order_num")));
			hellobike1map.put(i+"", df.format(Double.parseDouble(String.valueOf(hellobike1.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(hellobike1.get(i).get("Vehicle_no")))));
		}
		List<Map<String, Object>> xiaoliu = getanalysis(list, "xiaoliu");
		Map<String, String> xiaoliumap = new HashMap<String, String>();
		Map<String, String> xiaoliuliangmap = new HashMap<String, String>();
		xiaoliumap.put("CompanyId", "小遛");
		xiaoliuliangmap.put("CompanyId", "小遛");
		for (int i = 0; i < xiaoliu.size(); i++) {
			xiaoliuliangmap.put(i+"", String.valueOf(xiaoliu.get(i).get("Order_num")).equals("null")?"0":String.valueOf(xiaoliu.get(i).get("Order_num")));
			szxl.add(df.format(Double.parseDouble(String.valueOf(xiaoliu.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(xiaoliu.get(i).get("Vehicle_no")))));
			lzxl.add(String.valueOf(xiaoliu.get(i).get("Order_num")).equals("null")?"0":String.valueOf(xiaoliu.get(i).get("Order_num")));
			xiaoliumap.put(i+"", df.format(Double.parseDouble(String.valueOf(xiaoliu.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(xiaoliu.get(i).get("Vehicle_no")))));
		}
		/*公共自行车*/
		List<Map<String, Object>> zxc = getanalysis(list, "zxc");
		Map<String, String> zxcmap = new HashMap<String, String>();
		Map<String, String> zxcliangmap = new HashMap<String, String>();
		zxcmap.put("CompanyId", "公共自行车");
		zxcliangmap.put("CompanyId", "公共自行车");
		for (int i = 0; i < zxc.size(); i++) {
			zxcliangmap.put(i+"", String.valueOf(zxc.get(i).get("Order_num")).equals("null")?"0":String.valueOf(zxc.get(i).get("Order_num")));
			szxc.add(df.format(Double.parseDouble(String.valueOf(zxc.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(zxc.get(i).get("Vehicle_no")))));
			lzxc.add(String.valueOf(zxc.get(i).get("Order_num")).equals("null")?"0":String.valueOf(zxc.get(i).get("Order_num")));
			zxcmap.put(i+"", df.format(Double.parseDouble(String.valueOf(zxc.get(i).get("Order_num")))/Double.parseDouble(String.valueOf(zxc.get(i).get("Vehicle_no")))));
		}
		List<Map<String, String>> Result = new ArrayList<Map<String,String>>();
		Result.add(ofomap);
		Result.add(hellobikemap);
		Result.add(mbmap);
		Result.add(leqimap);
		Result.add(jindymap);
		Result.add(hellobike1map);
		Result.add(xiaoliumap);
		Result.add(zxcmap);
		List<Map<String, String>> liang = new ArrayList<Map<String,String>>();
		liang.add(ofoliangmap);
		liang.add(hellobikeliangmap);
		liang.add(mbliangmap);
		liang.add(leqiliangmap);
		liang.add(jindyliangmap);
		liang.add(hellobike1liangmap);
		liang.add(xiaoliuliangmap);
		liang.add(zxcliangmap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", Result);
		map.put("liang", liang);
		map.put("szo", szo);
		map.put("szh", szh);
		map.put("szm", szm);
		map.put("slq", slq);
		map.put("sjdy", sjdy);
		map.put("szh1", szh1);
		map.put("szxl", szxl);
		map.put("szr", szr);
		map.put("szxc", szxc);
		map.put("lzo", lzo);
		map.put("lzh", lzh);
		map.put("lzm", lzm);
		map.put("llq", llq);
		map.put("ljdy", ljdy);
		map.put("lzh1", lzh1);
		map.put("lzxl", lzxl);
		map.put("lzxc", lzxc);
		return jacksonUtil.toJson(map);
	}
	public List<Map<String, Object>> getanalysis(List<Map<String, Object>> list,String id){
		List<Map<String, Object>> Result = new ArrayList<Map<String,Object>>();
		for(int i=0; i<list.size(); i++){
			if(list.get(i).get("CompanyId").toString().equals(id)){
				Result.add(list.get(i));
			}
		}
		return Result;
	}
	public String getDataAccess(HttpServletRequest request){
		LogUtil.log("数据分析系统-数据接入监控", "数据分析系统-数据接入监控");
		String stime = request.getParameter("time")+" 00:00:00";
		String etime = request.getParameter("time")+" 23:59:59";
		List<Map<String, Object>> list = bikeDao.getDataAccess(stime,etime);
		return jacksonUtil.toJson(list);
	}
	public String getUser(HttpServletRequest request){
		LogUtil.log("用户权限-用户管理", "用户权限-用户管理");
		String name = request.getParameter("name");
		List<Map<String, Object>> userList = bikeDao.getUser(name);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		System.out.println("enter");
		for(int i=0;i<userList.size();i++){
			Map<String,Object> map = new HashMap<String,Object>();
			String valueOf = String.valueOf(userList.get(i).get("qx"));
			String str = valueOf.replaceAll("[0-9]", "").replaceAll("[a-z]", "").replaceAll(":", "").replaceAll("'", "").replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("//", "").replaceAll("-", "").replaceAll("\\]", "").replaceAll(",,,", "").replaceAll("\"", "").replaceAll(",/", "");
			map.put("qx", str);
			map.put("username", String.valueOf(userList.get(i).get("username")));
			map.put("password", String.valueOf(userList.get(i).get("password")));
			map.put("phone", String.valueOf(userList.get(i).get("phone")));
			map.put("xm", String.valueOf(userList.get(i).get("xm")));
			map.put("id", String.valueOf(userList.get(i).get("id")));
			list.add(map);
		}
		return jacksonUtil.toJson(list);
	}
	public String addUser(HttpServletRequest request){
		LogUtil.log("用户权限-用户管理-添加用户", "用户权限-用户管理-添加用户");
		String username = request.getParameter("username");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		String companyId = request.getParameter("companyId");
		int count = bikeDao.addUser(username,name,phone,password,companyId);
		Map<String, String> map = new HashMap<String, String>();
		if(count > 0)
			map.put("info", "添加成功");
		else map.put("info", "添加失败");
		return jacksonUtil.toJson(map);
	}
	public String editUser(HttpServletRequest request){
		LogUtil.log("用户权限-用户管理-修改用户", "用户权限-用户管理-修改用户");
		String username = request.getParameter("username");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		String id = request.getParameter("id");
		int count = bikeDao.editUser(username,name,phone,password,id);
		Map<String, String> map = new HashMap<String, String>();
		if(count > 0)
			map.put("info", "修改成功");
		else map.put("info", "修改失败");
		return jacksonUtil.toJson(map);
	}
	public String delUser(HttpServletRequest request){
		LogUtil.log("用户权限-用户管理-删除用户", "用户权限-用户管理-删除用户");
		String id = request.getParameter("id");
		int count = bikeDao.delUser(id);
		Map<String, String> map = new HashMap<String, String>();
		if(count > 0)
			map.put("info", "删除成功");
		else map.put("info", "删除失败");
		return jacksonUtil.toJson(map);
	}
	public String getArlyWarning(HttpServletRequest request){
		String p = request.getParameter("page");
		String page = (Integer.parseInt(p)-1) * pageSize+"";
		return jacksonUtil.toJson(bikeDao.getArlyWarning(page));
	}
	public String ArlyWarningHandle(HttpServletRequest request){
		String id = request.getParameter("id");
		int count = bikeDao.ArlyWarningHandle(id);
		Map<String, String> map = new HashMap<String, String>();
		if(count > 0)
			map.put("info", "处理成功");
		else map.put("info", "处理失败");
		return jacksonUtil.toJson(map);
	}
	public String addPower(HttpServletRequest request){
		LogUtil.log("用户权限-权限管理-添加用户权限", "用户权限-权限管理-添加用户权限");
		String id = request.getParameter("id");
		String rcjg =request.getParameter("rcjg");
		String jczc =request.getParameter("jczc");
		String xxcx =request.getParameter("xxcx");
		String xypj =request.getParameter("xypj");
		String yjzh =request.getParameter("yjzh");
		String yhqx =request.getParameter("yhqx");
		String qygl =request.getParameter("qygl");
		
		
		StringBuffer qxlist = new StringBuffer("["),list = new StringBuffer("[");
		if(qygl.indexOf("区域预警")>=0||qygl.indexOf("数据接口预警")>=0||qygl.indexOf("通知公告")>=0){
			qxlist.append("{id:'qyglxt', title:'企业管理系统', icon: 'icon-richangjiance'},");
			if(qygl.indexOf("区域预警")>=0){
				list.append("{id:'qyyj', title:'区域预警', icon: 'icon-cheliangdongtaijiankong', href: '/qyglxt/qygl'},");
			}
			if(qygl.indexOf("数据接口预警")>=0){
				list.append("{id:'sjjkyj', title:'数据接口预警', icon: 'icon-yunweirenyuanjiankong', href: '/qyglxt/sjjkyj'},");
			}
			if(qygl.indexOf("通知公告")>=0){
				list.append("{id:'tzgg', title:'通知信息发布', icon: 'icon-quyujiankong', href: '/qyglxt/tzgg'},");
			}
		}
		if(rcjg.indexOf("车辆动态监控")>=0||rcjg.indexOf("运维人员监控")>=0||rcjg.indexOf("区域监控")>=0||rcjg.indexOf("未使用车辆监控")>=0){
			qxlist.append("{id:'rcjgxt', title:'日常监管系统', icon: 'icon-richangjiance'},");
			if(rcjg.indexOf("车辆动态监控")>=0){
				list.append("{id:'cldtjk', title:'车辆动态监控', icon: 'icon-cheliangdongtaijiankong', href: '/rcjgxt/cldtjk'},");
			}
			if(rcjg.indexOf("运维人员监控")>=0){
				list.append("{id:'ywryjk', title:'运维人员监控', icon: 'icon-yunweirenyuanjiankong', href: '/rcjgxt/ywryjk'},");
			}
			if(rcjg.indexOf("区域监控")>=0){
				list.append("{id:'qyjk', title:'区域监控', icon: 'icon-quyujiankong', href: '/rcjgxt/qyjk'},");
			}
			if(rcjg.indexOf("未使用车辆监控")>=0){
				list.append("{id:'wsycljk', title:'未使用车辆监控', icon: 'icon-quyujiankong', href: '/rcjgxt/qyjk'},");
			}
		}
		if(jczc.indexOf("车辆热点运行图")>=0||jczc.indexOf("车辆OD流向图")>=0||jczc.indexOf("车辆周转率分析")>=0
				||jczc.indexOf("车辆周转量分析")>=0||jczc.indexOf("运行情况统计")>=0||jczc.indexOf("数据接入监控")>=0||jczc.indexOf("未使用车辆分析")>=0){
			qxlist.append("{id:'jczcxt', title:'决策支持系统', icon: 'icon-juecezhichixitong'},");
			if(jczc.indexOf("车辆热点运行图")>=0){
				list.append("{id:'clrdyxt', title:'车辆热点运行图', icon: 'icon-redian', href: '/jczcxt/clrdyxt'},");
			}
			if(jczc.indexOf("车辆OD流向图")>=0){
				list.append("{id:'clodlxt', title:'车辆OD流向图', icon: 'icon-zijinliuxiang', href: '/jczcxt/clodlxt'},");
			}
			if(jczc.indexOf("车辆周转率分析")>=0){
				list.append("{id:'clzzlvfx', title:'车辆周转率分析', icon: 'icon-fenxi', href: '/jczcxt/clzzlvfx'},");
			}
			if(jczc.indexOf("车辆周转量分析")>=0){
				list.append("{id:'clzzliangfx', title:'车辆周转量分析', icon: 'icon-fenxi', href: '/jczcxt/clzzliangfx'},");
			}
			if(jczc.indexOf("运行情况统计")>=0){
				list.append("{id:'yxqktj', title:'运行情况统计', icon: 'icon-fenxi', href: '/jczcxt/yxqktj'},");
			}
			if(jczc.indexOf("数据接入监控")>=0){
				list.append("{id:'sjjrjk', title:'数据接入监控', icon: 'icon-jiankong', href: '/jczcxt/sjjrjk'},");
			}
			if(jczc.indexOf("未使用车辆分析")>=0){
				list.append("{id:'wsycl', title:'数据接入监控', icon: 'icon-jiankong', href: '/jczcxt/sjjrjk'},");
			}
		}
		if(xxcx.indexOf("企业基本信息")>=0||xxcx.indexOf("运维人员信息")>=0||xxcx.indexOf("车辆基本信息")>=0
				||xxcx.indexOf("备案车辆查询")>=0||xxcx.indexOf("小程序查询")>=0||xxcx.indexOf("停车区域管理")>=0
				||xxcx.indexOf("重点区域管理")>=0||xxcx.indexOf("禁停区域管理")>=0||xxcx.indexOf("禁投区域管理")>=0){
			qxlist.append("{id:'xxcxxt', title:'信息查询系统', icon: 'icon-xinxichaxunxitong'},");
			if(xxcx.indexOf("企业基本信息")>=0||xxcx.indexOf("运维人员信息")>=0||xxcx.indexOf("车辆基本信息")>=0
				||xxcx.indexOf("备案车辆查询")>=0||xxcx.indexOf("小程序查询")>=0){
				list.append("{id:'xxzhcx', title:'综合查询', icon: 'icon-kefu', fullPath: '/xxcxxt/xxzhcx'},");
				if(xxcx.indexOf("企业基本信息")>=0){
					list.append("{id:'qyjbxx', title:'企业基本信息', icon: 'icon-qiye', href: '/xxcxxt/xxzhcx/qyjbxx'},");
				}
				if(xxcx.indexOf("运维人员信息")>=0){
					list.append("{id:'ywryxx', title:'运维人员信息', icon: 'icon-yunweirenyuanjiankong', href: '/xxcxxt/xxzhcx/ywryxx'},");
				}
				if(xxcx.indexOf("车辆基本信息")>=0){
					list.append("{id:'cljbxx', title:'车辆基本信息', icon: 'icon-cheliang', href: '/xxcxxt/xxzhcx/cljbxx'},");
				}
				if(xxcx.indexOf("备案车辆查询")>=0){
					list.append("{id:'baclcx', title:'备案车辆查询', icon: 'icon-cheliangpaiban', href: '/xxcxxt/xxzhcx/baclcx'},");
				}
				if(xxcx.indexOf("小程序查询")>=0){
					list.append("{id:'xcxcx', title:'小程序查询', icon: 'icon-xiaochengxu', href: '/xxcxxt/xxzhcx/xcxcx'},");
				}
				
				list.append("{id:'xxqygl', title:'区域管理', icon: 'icon-kefu', fullPath: '/xxcxxt/xxqygl'},");
				
				if(xxcx.indexOf("停车区域管理")>=0){
					list.append("{id:'xxtcqygl', title:'停车区域管理', icon: 'icon-tingche', href: '/xxcxxt/xxqygl/xxtcqygl'},");
				}
				if(xxcx.indexOf("重点区域管理")>=0){
					list.append("{id:'xxzdqygl', title:'重点区域管理', icon: 'icon-zhongdian', href: '/xxcxxt/xxqygl/xxzdqygl'},");
				}
				if(xxcx.indexOf("禁停区域管理")>=0){
					list.append("{id:'xxjtqygl', title:'禁停区域管理', icon: 'icon-jinzhi', href: '/xxcxxt/xxqygl/xxjtqygl'},");
				}
				if(xxcx.indexOf("禁投区域管理")>=0){
					list.append("{id:'xxjintqygl', title:'禁投区域管理', icon: 'icon-jinzhi', href: '/xxcxxt/xxqygl/xxjintqygl'},");
				}
			}
		}
		if(xypj.indexOf("处罚信息查询")>=0||xypj.indexOf("企业考核结果查询")>=0||xypj.indexOf("通知信息发布")>=0){
			qxlist.append("{id:'xypjxt', title:'信用评价系统', icon: 'icon-xinyongpingjiaxitong'},");
			if(xypj.indexOf("处罚信息查询")>=0){
				list.append("{id:'cfxxcx', title:'处罚信息查询', icon: 'icon-gongshichufa', href: '/xypjxt/cfxxcx'},");
			}
			if(xypj.indexOf("企业考核结果查询")>=0){
				list.append("{id:'qykhjgcx', title:'企业考核结果查询', icon: 'icon-kaohe', href: '/xypjxt/qykhjgcx'},");
			}
			if(xypj.indexOf("通知信息发布")>=0){
				list.append("{id:'tzxxfb', title:'企业考核结果查询', icon: 'icon-kaohe', href: '/xypjxt/qykhjgcx'},");
			}
			list.append(",");
		}
		if(yjzh.indexOf("押金账户查询")>=0){
			qxlist.append("{id:'yjzhxt', title:'押金账户系统', icon: 'icon-yajinzhanghuxitong'},");
			if(yjzh.indexOf("押金账户查询")>=0){
				list.append("{id:'yjzhcx', title:'押金账户查询', icon: 'icon-yajinjinchu', href: '/yjzhxt/yjzhcx'},");
			}
			list.append(",");
		}
		if(yhqx.indexOf("用户管理")>=0||yhqx.indexOf("权限管理")>=0){
			qxlist.append("{id:'yhqx', title:'用户权限', icon: 'icon-yonghu'},");
			if(yhqx.indexOf("用户管理")>=0){
				list.append("{id:'yhguanl', title:'用户管理', icon: 'icon-user', href: '/yhqx/yhguanl'},");
			}
			if(yhqx.indexOf("权限管理")>=0){
				list.append("{id:'qxguanl', title:'权限管理', icon: 'icon-authority', href: '/yhqx/qxguanl'}");
			}
			if(yhqx.indexOf("日志管理")>=0){
				list.append("{id:'rzguanl', title:'日志管理', icon: 'icon-authority', href: '/yhqx/qxguanl'}");
			}
			list.append(",");
		}
		list.append("]");
		qxlist.append("]");
		
		int count = bikeDao.addPower(qxlist.toString(),list.toString(),id);
		Map<String, String> map = new HashMap<String, String>();
		if(count > 0)
			map.put("info", "设置成功");
		else map.put("info", "设置失败");
		return jacksonUtil.toJson(map);
		
		
		
//		StringBuffer qxlist = new StringBuffer("["),list = new StringBuffer("[");
//		if(rcjg.indexOf("车辆动态监控")>=0||rcjg.indexOf("运维人员监控")>=0||rcjg.indexOf("区域监控")>=0){
//			qxlist.append("{id:'rcjgxt', title:'日常监管系统', icon: 'icon-richangjiance'},");
//			list.append("rcjgxt: [");
//			if(rcjg.indexOf("车辆动态监控")>=0){
//				list.append("{id:'cldtjk', title:'车辆动态监控', icon: 'icon-cheliangdongtaijiankong', href: '/rcjgxt/cldtjk'},");
//			}
//			if(rcjg.indexOf("运维人员监控")>=0){
//				list.append("{id:'ywryjk', title:'运维人员监控', icon: 'icon-yunweirenyuanjiankong', href: '/rcjgxt/ywryjk'},");
//			}
//			if(rcjg.indexOf("区域监控")>=0){
//				list.append("{id:'qyjk', title:'区域监控', icon: 'icon-quyujiankong', href: '/rcjgxt/qyjk'}");
//			}
//			list.append("],");
//		}
//		if(jczc.indexOf("车辆热点运行图")>=0||jczc.indexOf("车辆OD流向图")>=0||jczc.indexOf("车辆周转率分析")>=0
//				||jczc.indexOf("车辆周转量分析")>=0||jczc.indexOf("运行情况统计")>=0||jczc.indexOf("数据接入监控")>=0){
//			qxlist.append("{id:'jczcxt', title:'决策支持系统', icon: 'icon-juecezhichixitong'},");
//			list.append("jczcxt: [");
//			if(jczc.indexOf("车辆热点运行图")>=0){
//				list.append("{id:'clrdyxt', title:'车辆热点运行图', icon: 'icon-redian', href: '/jczcxt/clrdyxt'},");
//			}
//			if(jczc.indexOf("车辆OD流向图")>=0){
//				list.append("{id:'clodlxt', title:'车辆OD流向图', icon: 'icon-zijinliuxiang', href: '/jczcxt/clodlxt'},");
//			}
//			if(jczc.indexOf("车辆周转率分析")>=0){
//				list.append("{id:'clzzlvfx', title:'车辆周转率分析', icon: 'icon-fenxi', href: '/jczcxt/clzzlvfx'},");
//			}
//			if(jczc.indexOf("车辆周转量分析")>=0){
//				list.append("{id:'clzzliangfx', title:'车辆周转量分析', icon: 'icon-fenxi', href: '/jczcxt/clzzliangfx'},");
//			}
//			if(jczc.indexOf("运行情况统计")>=0){
//				list.append("{id:'yxqktj', title:'运行情况统计', icon: 'icon-fenxi', href: '/jczcxt/yxqktj'},");
//			}
//			if(jczc.indexOf("数据接入监控")>=0){
//				list.append("{id:'sjjrjk', title:'数据接入监控', icon: 'icon-jiankong', href: '/jczcxt/sjjrjk'},");
//			}
//			list.append("],");
//		}
//		if(xxcx.indexOf("企业基本信息")>=0||xxcx.indexOf("运维人员信息")>=0||xxcx.indexOf("车辆基本信息")>=0
//				||xxcx.indexOf("备案车辆查询")>=0||xxcx.indexOf("小程序查询")>=0||xxcx.indexOf("停车区域管理")>=0
//				||xxcx.indexOf("重点区域管理")>=0||xxcx.indexOf("禁停区域管理")>=0||xxcx.indexOf("禁投区域管理")>=0){
//			qxlist.append("{id:'xxcxxt', title:'信息查询系统', icon: 'icon-xinxichaxunxitong'},");
//			list.append("xxcxxt: [");
//			if(xxcx.indexOf("企业基本信息")>=0||xxcx.indexOf("运维人员信息")>=0||xxcx.indexOf("车辆基本信息")>=0
//				||xxcx.indexOf("备案车辆查询")>=0||xxcx.indexOf("小程序查询")>=0){
//				list.append("{id:'xxzhcx', title:'综合查询', icon: 'icon-kefu', fullPath: '/xxcxxt/xxzhcx', children: [");
//				if(xxcx.indexOf("企业基本信息")>=0){
//					list.append("{id:'qyjbxx', title:'企业基本信息', icon: 'icon-qiye', href: '/xxcxxt/xxzhcx/qyjbxx'},");
//				}
//				if(xxcx.indexOf("运维人员信息")>=0){
//					list.append("{id:'ywryxx', title:'运维人员信息', icon: 'icon-yunweirenyuanjiankong', href: '/xxcxxt/xxzhcx/ywryxx'},");
//				}
//				if(xxcx.indexOf("车辆基本信息")>=0){
//					list.append("{id:'cljbxx', title:'车辆基本信息', icon: 'icon-cheliang', href: '/xxcxxt/xxzhcx/cljbxx'},");
//				}
//				if(xxcx.indexOf("备案车辆查询")>=0){
//					list.append("{id:'baclcx', title:'备案车辆查询', icon: 'icon-cheliangpaiban', href: '/xxcxxt/xxzhcx/baclcx'},");
//				}
//				if(xxcx.indexOf("小程序查询")>=0){
//					list.append("{id:'xcxcx', title:'小程序查询', icon: 'icon-xiaochengxu', href: '/xxcxxt/xxzhcx/xcxcx'},");
//				}
//				list.append("]},");
//			}
//			if(rcjg.indexOf("停车区域管理")>=0||rcjg.indexOf("重点区域管理")>=0||rcjg.indexOf("禁停区域管理")>=0||rcjg.indexOf("禁投区域管理")>=0){
//				list.append("{id:'xxqygl', title:'区域管理', icon: 'icon-kefu', fullPath: '/xxcxxt/xxqygl', children:[");
//				if(rcjg.indexOf("停车区域管理")>=0){
//					list.append("{id:'xxtcqygl', title:'停车区域管理', icon: 'icon-tingche', href: '/xxcxxt/xxqygl/xxtcqygl'},");
//				}
//				if(rcjg.indexOf("重点区域管理")>=0){
//					list.append("{id:'xxzdqygl', title:'重点区域管理', icon: 'icon-zhongdian', href: '/xxcxxt/xxqygl/xxzdqygl'},");
//				}
//				if(rcjg.indexOf("禁停区域管理")>=0){
//					list.append("{id:'xxjtqygl', title:'禁停区域管理', icon: 'icon-jinzhi', href: '/xxcxxt/xxqygl/xxjtqygl'},");
//				}
//				if(rcjg.indexOf("禁投区域管理")>=0){
//					list.append("{id:'xxjintqygl', title:'禁投区域管理', icon: 'icon-jinzhi', href: '/xxcxxt/xxqygl/xxjintqygl'},");
//				}
//				list.append("]}");
//			}
//			list.append("],");
//		}
//		if(xypj.indexOf("处罚信息查询")>=0||xypj.indexOf("企业考核结果查询")>=0){
//			qxlist.append("{id:'xypjxt', title:'信用评价系统', icon: 'icon-xinyongpingjiaxitong'},");
//			list.append("xypjxt: [");
//			if(xypj.indexOf("处罚信息查询")>=0){
//				list.append("{id:'cfxxcx', title:'处罚信息查询', icon: 'icon-gongshichufa', href: '/xypjxt/cfxxcx'},");
//			}
//			if(xypj.indexOf("企业考核结果查询")>=0){
//				list.append("{id:'qykhjgcx', title:'企业考核结果查询', icon: 'icon-kaohe', href: '/xypjxt/qykhjgcx'},");
//			}
//			list.append("],");
//		}
//		if(yjzh.indexOf("押金账户查询")>=0){
//			qxlist.append("{id:'yjzhxt', title:'押金账户系统', icon: 'icon-yajinzhanghuxitong'},");
//			list.append("yjzhxt: [");
//			if(yjzh.indexOf("押金账户查询")>=0){
//				list.append("{id:'yjzhcx', title:'押金账户查询', icon: 'icon-yajinjinchu', href: '/yjzhxt/yjzhcx'},");
//			}
//			list.append("],");
//		}
//		if(yhqx.indexOf("用户管理")>=0||yhqx.indexOf("权限管理")>=0){
//			qxlist.append("{id:'yhqx', title:'用户权限', icon: 'icon-yonghu'},");
//			list.append("yhqx: [");
//			if(yhqx.indexOf("用户管理")>=0){
//				list.append("{id:'yhguanl', title:'用户管理', icon: 'icon-user', href: '/yhqx/yhguanl'},");
//			}
//			if(yhqx.indexOf("权限管理")>=0){
//				list.append("{id:'qxguanl', title:'权限管理', icon: 'icon-authority', href: '/yhqx/qxguanl'}");
//			}
//			list.append("],");
//		}
//		qxlist.append("]");
//		int count = bikeDao.addPower(qxlist.toString(),list.toString(),id);
//		Map<String, String> map = new HashMap<String, String>();
//		if(count > 0)
//			map.put("info", "设置成功");
//		else map.put("info", "设置失败");
//		return jacksonUtil.toJson(map);
	}
	public String getBicycleInfo(){
		Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,0);
        Date d=cal.getTime();
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        String time=sp.format(d);
        System.out.println(time);
        String table = "tb_bike_order_"+time.substring(2, 4)+time.substring(5, 7);
        String table1 = "tb_electric_bike_order_"+time.substring(2, 4)+time.substring(5, 7);
		List<Map<String, Object>> list = bikeDao.getBicycleNo();
		List<Map<String, Object>> list1 = bikeDao.getBicycleActive(time,table,table1);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("NO", list);
		map.put("ACTIVE", list1);
		return jacksonUtil.toJson(map);
	}
	public String getBicycleDistribution(){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String time = df.format(new Date());
		List<Map<String, Object>> list = bikeDao.getBicycleDistribution(time);
		for (int i = 0; i < list.size(); i++) {
			if(String.valueOf(list.get(i).get("Count2")).equals("null")){
				list.get(i).put("Count", String.valueOf(list.get(i).get("Count1")));
			}else{
				list.get(i).put("Count", Integer.parseInt(String.valueOf(list.get(i).get("Count1")))+Integer.parseInt(String.valueOf(list.get(i).get("Count2"))));
			}
		}
		return jacksonUtil.toJson(list);
	}
	public String login(HttpServletRequest request){
		LogUtil.log("用户登录", "用户登录");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String yzm = request.getParameter("yzm").toUpperCase();
		Map<String, Object> map = new HashMap<String, Object>();
		String session_yzm = String.valueOf(request.getSession().getAttribute("validateCode"));
		if(!session_yzm.equals(yzm)){
			map.put("info", "cw");
			return jacksonUtil.toJson(map);
		}
		List<Map<String, Object>> list = bikeDao.login(username, password);
		if(list.size() > 0){
			HttpSession session = request.getSession();
			String companyId = String.valueOf(list.get(0).get("companyid"));
			String name = String.valueOf(list.get(0).get("username"));
			session.setAttribute("companyId", companyId);
			session.setAttribute("name", name);
		}
		map.put("info", list.size());
		return jacksonUtil.toJson(map);
	}
	public String getBicycleOrderNo(){
		Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d=cal.getTime();
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        String time=sp.format(d);
        String table = "tb_bike_order_"+time.substring(2, 4)+time.substring(5, 7);
        String table1 = "tb_electric_bike_order_"+time.substring(2, 4)+time.substring(5, 7);
		List<Map<String, Object>> list = bikeDao.getbicycleOrderNo(time, table);
		List<Map<String, Object>> list1 = bikeDao.getEbicycleOrderNo(time, table1);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("BIKE", list);
		map.put("EBIKE", list1);
		return jacksonUtil.toJson(map);
	}
	public String getTurnoverRate(){
		Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d=cal.getTime();
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        String time=sp.format(d);
		return jacksonUtil.toJson(bikeDao.getTurnoverRate(time));
	}
	public String getBicycleOrderTurn(){
		Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d=cal.getTime();
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        String time=sp.format(d);
		List<Map<String, Object>> list = bikeDao.getBicycleOrderTurn(time);
		List<Map<String, Object>> list1 = bikeDao.getEBicycleOrderTurn(time);
		List<Map<String, Object>> list2 = bikeDao.getPBicycleOrderTurn(time);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("BIKE", list);
		map.put("EBIKE", list1);
		map.put("PBIKE", list2);
		return jacksonUtil.toJson(map);
	}
	public String getNotUsed(HttpServletRequest request){
		LogUtil.log("数据分析系统-租用车辆分析", "数据分析系统-租用车辆分析");
		String time = request.getParameter("time");
		List<Map<String, Object>> list = bikeDao.getNotUsed(time);
		return jacksonUtil.toJson(list);
	}
	public String getLogin(HttpServletRequest request) {
		String name = String.valueOf(request.getSession().getAttribute("name"));
		Map<String, String> map = new HashMap<String, String>();
		map.put("info", name);
		return jacksonUtil.toJson(map);
	}
	public String getUserType() {
		return jacksonUtil.toJson(bikeDao.getUserType());
	}
	public String getLog(int page) {
		Map<String, Object> map = new HashMap<String, Object>();
		int p = (page-1) * pageSize;
		List<Map<String, Object>> list = bikeDao.getLog(p);
		int count = bikeDao.getLogCount();
		map.put("list", list);
		map.put("count", count);
		return jacksonUtil.toJson(map);
	}
}
