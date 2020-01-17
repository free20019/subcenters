package mvc.service;

import helper.Client;
import helper.GeometryHandler;
import helper.JacksonUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import mvc.cache.DataItem;
import mvc.cache.GisData;
import mvc.entity.TbCompany;
import mvc.entity.Vehicle;
import mvc.mapper.bikeDao;
import mvc.mapper.mapDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.com.activeMQ.Sender;

import com.service.GpsServicesDelegate;
import com.vividsolutions.jts.geom.Geometry;


@Service
public class mapServer{
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	private static  String WS_URL = "http://192.168.0.102:9086/EWS/GpsServicesPort?WSDL";
	private static final double EARTH_RADIUS = 6378137;
	public String getVhic(String vhic,HttpServletRequest request){
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = "";
		if(loginType.equals("0")){
			String comps = String.valueOf(request.getSession().getAttribute("comps"));
			if(comps.equals("null")) return jacksonUtil.toJson(list);
			sql = "select vehi_no,MDT_NO,VEHI_SIM from vw_vehicle@db69 t where vehi_no"
					+ " like '%"+vhic+"%' or mdt_no like '%"+vhic+"%' or vehi_sim like '%"+vhic+"%'";
				sql += " and comp_name in ("+comps+") order by vehi_no";
			list = jdbcTemplate.queryForList(sql);
		}else if(loginType.equals("1")){
			String vehi_no = String.valueOf(request.getSession().getAttribute("groupVhic"));
			if(vehi_no.equals("null")){
				return jacksonUtil.toJson(list);
			}else{
				List<String> l = new ArrayList<String>();
				String[] cphm = vehi_no.split(",");
				for (int i = 0; i < cphm.length; i++) {
					l.add( "'" + cphm[i] +"'");
				}
				List<String> zzjg = Transformation(l);
				sql = "select vehi_no,MDT_NO,VEHI_SIM from vw_vehicle@db69 t where vehi_no"
						+ " like '%"+vhic+"%' or mdt_no like '%"+vhic+"%' or vehi_sim like '%"+vhic+"%'";
				sql += " and (";
				if(zzjg.size() ==1){
					sql += " vehi_no in ("+zzjg.get(0)+") ";
				}else{
					for (int i = 0; i < zzjg.size(); i++) {
						if(i == 0){
							sql += " vehi_no in ("+zzjg.get(i)+")";
						}else{
							sql += " or vehi_no in ("+zzjg.get(i)+")";
						}
					}
				}
				sql += " ) order by vehi_no";
			}
		}else{
			return jacksonUtil.toJson(list);
		}
		list = jdbcTemplate.queryForList(sql);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("INFO", list.get(i).get("VEHI_NO")+"("+list.get(i).get("MDT_NO")+","+list.get(i).get("VEHI_SIM")+")");
		}
		return jacksonUtil.toJson(list);
	}
	public String getHistory(HttpServletRequest request) {
		DecimalFormat df = new DecimalFormat("0.000");
		String vhic = request.getParameter("vhic");
		String stime = request.getParameter("stime");
		String etime = request.getParameter("etime");
		String table = stime.substring(2, 4) + stime.substring(5, 7);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			String sql = "select c.comp_name,t.vehicle_num,t.px,t.py,t.speed,t.direction,t.speed_time,state,"
					+ "own_name,own_tel from HZGPS_TAXI.TB_GPS_"+table+"@db69 t,tb_company@db69 c,vw_vehicle@db69 v where"
					+ " t.vehicle_num = v.vehi_no and c.comp_id = v.comp_id and vehicle_num = ? and"
					+ " speed_time>= to_date(?,'yyyy-mm-dd hh24:mi:ss')"
					+ " and speed_time<= to_date(?,'yyyy-mm-dd hh24:mi:ss')"
					+ " and px>110 and py>25 and carstate = '0' order by speed_time";
			System.out.println(sql+"  "+vhic+"  "+stime+"  "+etime);
			list = jdbcTemplate.queryForList(sql,vhic,stime,etime);
		} catch (Exception e) {
			return jacksonUtil.toJson(list);
		}
		double dou = 0.0;
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("DIRECTION", String.valueOf(list.get(i).get("DIRECTION")));
			if(i == 0){
				list.get(i).put("MILEAGE", "0");
			}else{
				try {
					dou = GetDistance(
							Double.parseDouble(list.get(i-1).get("PX").toString()), 
							Double.parseDouble(list.get(i-1).get("PY").toString()), 
							Double.parseDouble(list.get(i).get("PX").toString()), 
							Double.parseDouble(list.get(i).get("PY").toString()));
					list.get(i).put("MILEAGE", df.format(dou/1000+
							Double.parseDouble(list.get(i-1).get("MILEAGE").toString())));
				} catch (Exception e) {
					list.get(i).put("MILEAGE", "0");
				}
			}
		}
		return jacksonUtil.toJson(list);
	}
	private static double rad(double d) {
       return d * Math.PI / 180.0;  
    }  
	public static double GetDistance(double lng1, double lat1, double lng2, double lat2) {  
	        double radLat1 = rad(lat1);  
	        double radLat2 = rad(lat2);  
	        double a = radLat1 - radLat2;  
	        double b = rad(lng1) - rad(lng2);  
	        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +   
	         Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	        s = s * EARTH_RADIUS;  
	        s = Math.round(s * 10000) / 10000;  
	        return s;  
    }
	public String swcz(String qd_stime, String qd_etime, String zd_stime,
			String zd_etime, String qd_jwd, String zd_jwd,String type,HttpServletRequest request) {
		/*type=0全部车辆     type=1账号内车辆*/
		String vhicList = "";
		if(type.equals("1")){
			String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
			if(loginType.equals("0")){
				String comps = String.valueOf(request.getSession().getAttribute("comps"));
				String sql = "select vehi_no from vw_vehicle@db69 where where comp_name in ('"+comps+"')";
				List<Map<String, Object>> l = jdbcTemplate.queryForList(sql);
				for (int i = 0; i < l.size(); i++) {
					vhicList += String.valueOf(l.get(i).get("VEHI_NO"))+",";
				}
			}else if(loginType.equals("1")){
				vhicList = String.valueOf(request.getSession().getAttribute("groupVhic"));
			}
		}
		List<String> list = new ArrayList<String>();
		String s1 = "",s2 = "",s3 = "";
		List<Vehicle> listqd = new ArrayList<Vehicle>();
		List<Vehicle> listzd = new ArrayList<Vehicle>();
		if(qd_jwd!=null&&qd_jwd.length()>0){
			s1=swczff(qd_stime,qd_etime,qd_jwd,"1");
			JSONObject json = JSONObject.fromObject(s1);
	        list.add(json.getString("result0"));
	        JSONObject json1 =  JSONObject.fromObject(json.getString("result0"));
	        Set setqd = json1.keySet();
	        Iterator it = setqd.iterator();
	    	 String a = "";
	    	 while(it.hasNext()) {
	    		 a=(String) it.next();
	    		 if(type.equals("1")){
	    			 if(vhicList.indexOf(a)>=0){
	    	    		 Vehicle v = new Vehicle();
	    	    		 v.setVehi_no(a);
	    	    		 v.setStime(json1.get(a).toString().split("\\[")[0].substring(0, json1.get(a).toString().split("\\[")[0].length()-1));
	    	    		 v.setPx(json1.get(a).toString().split("\\[")[1].substring(0, json1.get(a).toString().split("\\[")[1].length()-1));
	    	    		 listqd.add(v);
	    			 }
	    		 }else{
		    		 Vehicle v = new Vehicle();
		    		 v.setVehi_no(a);
		    		 v.setStime(json1.get(a).toString().split("\\[")[0].substring(0, json1.get(a).toString().split("\\[")[0].length()-1));
		    		 v.setPx(json1.get(a).toString().split("\\[")[1].substring(0, json1.get(a).toString().split("\\[")[1].length()-1));
		    		 listqd.add(v);
	    		 }
	    	 }  
		}
		
		if(zd_jwd!=null&&zd_jwd.length()>0){
			s2=swczff(zd_stime,zd_etime,zd_jwd,"2");
			JSONObject json = JSONObject.fromObject(s2);
	        list.add(json.getString("result0"));
	        JSONObject json1 =  JSONObject.fromObject(json.getString("result0"));
	        Set setzd = json1.keySet();
	        Iterator it = setzd.iterator();
	    	 String a = "";
	    	 while(it.hasNext()) {
	    		 a=(String) it.next();
	    		 if(type.equals("1")){
	    			 if(vhicList.indexOf(a)>=0){
	    				 Vehicle v = new Vehicle();
	    	    		 v.setVehi_no(a);
	    	    		 v.setStime(json1.get(a).toString().split("\\[")[0].substring(0, json1.get(a).toString().split("\\[")[0].length()-1));
	    	    		 v.setPx(json1.get(a).toString().split("\\[")[1].substring(0, json1.get(a).toString().split("\\[")[1].length()-1));
	    	    		 listzd.add(v);
	    			 }
	    		 }else{
	    			 Vehicle v = new Vehicle();
		    		 v.setVehi_no(a);
		    		 v.setStime(json1.get(a).toString().split("\\[")[0].substring(0, json1.get(a).toString().split("\\[")[0].length()-1));
		    		 v.setPx(json1.get(a).toString().split("\\[")[1].substring(0, json1.get(a).toString().split("\\[")[1].length()-1));
		    		 listzd.add(v);
	    		 }
	    	 }
		}
		List<Vehicle> listjg = new ArrayList<Vehicle>();
		if(list.size()==2){
			Set aSet = new HashSet();
        	JSONObject json11=JSONObject.fromObject(list.get(0));
        	JSONObject json2;
        	 json2=JSONObject.fromObject(list.get(1));
        	 Set s11=json11.keySet();
        	 Set s22=json2.keySet();
        	 aSet.addAll(s11);
        	 aSet.retainAll(s22);
        	 Iterator it = aSet.iterator();
	    	 String a = "";
	    	 while(it.hasNext()) {
	    		 a=(String) it.next();
	    		 if(type.equals("1")){
	    			 if(vhicList.indexOf(a)>=0){
	    				 Vehicle v = new Vehicle();
	    				 v.setVehi_no(a);
	    	    		 v.setStime(json11.get(a).toString().split("\\[")[0].substring(0, json11.get(a).toString().split("\\[")[0].length()-1));
	    	    		 v.setPx(json11.get(a).toString().split("\\[")[1].substring(0, json11.get(a).toString().split("\\[")[1].length()-1));
	    	    		 listjg.add(v);
	    			 }
	    		 }else{
	    			 Vehicle v = new Vehicle();
    				 v.setVehi_no(a);
		    		 v.setStime(json11.get(a).toString().split("\\[")[0].substring(0, json11.get(a).toString().split("\\[")[0].length()-1));
		    		 v.setPx(json11.get(a).toString().split("\\[")[1].substring(0, json11.get(a).toString().split("\\[")[1].length()-1));
		    		 listjg.add(v);
	    		 }
	    	 }
        }else{
        	JSONObject json1;
        	JSONObject json2;
        	JSONObject json3;
        	json1=JSONObject.fromObject(list.get(0));
       	 	json2=JSONObject.fromObject(list.get(1));
       	 	json3=JSONObject.fromObject(list.get(2));
       	 	Set aSet = new HashSet();
	       	 Set s11=json1.keySet();
	    	 Set s22=json2.keySet();
	    	 Set s33=json3.keySet();
	    	 aSet.addAll(s11);
	    	 aSet.retainAll(s22);
	    	 aSet.retainAll(s33);
	    	 Iterator it = aSet.iterator();  
	    	 String a = "";
	    	 while(it.hasNext()) {
	    		 a=(String) it.next();
	    		 if(type.equals("1")){
	    			 if(vhicList.indexOf(a)>=0){
	    				 Vehicle v = new Vehicle();
	    	    		 v.setVehi_no(a);
	    	    		 v.setStime(json1.get(a).toString().split("\\[")[0].substring(0, json1.get(a).toString().split("\\[")[0].length()-1));
	    	    		 v.setPx(json1.get(a).toString().split("\\[")[1].substring(0, json1.get(a).toString().split("\\[")[1].length()-1));
	    	    		 listjg.add(v);
	    			 }
	    		 }else{
	    			 Vehicle v = new Vehicle();
		    		 v.setVehi_no(a);
		    		 v.setStime(json1.get(a).toString().split("\\[")[0].substring(0, json1.get(a).toString().split("\\[")[0].length()-1));
		    		 v.setPx(json1.get(a).toString().split("\\[")[1].substring(0, json1.get(a).toString().split("\\[")[1].length()-1));
		    		 listjg.add(v);
	    		 }
	    	 }
        }
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("qd", listqd);
		map.put("zd", listzd);
		map.put("jg", listjg);
		return jacksonUtil.toJson(map);
	}
	public String swczff(String stime,String etime,String jwd,String obj){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long st = 0,et = 0;
		try {
			st = sdf.parse(stime).getTime();
			et = sdf.parse(etime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		URL url = null;
		try {
			url = new URL(WS_URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
        QName qname = new QName("http://service.com/", "GpsServicesService");
        javax.xml.ws.Service service = javax.xml.ws.Service.create(url, qname);
        GpsServicesDelegate gpsService = service.getPort(GpsServicesDelegate.class);
        Map<String, Object> req_ctx = ((BindingProvider)gpsService).getRequestContext();
        List qy = new ArrayList();
        Calendar calendar = Calendar.getInstance() ,calendar2 = Calendar.getInstance();
        calendar.set(2016, 4, 31, 18, 30, 22);
		calendar2.set(2016, 4,31, 18, 50, 22);
        qy.add(jwd);
        System.out.println(qy+"  # "+st+" # # "+et);
        String  result = gpsService.swcz3Day(qy, st, et);
        return result;
	}
	public String getComp(HttpServletRequest request){
		String comps = String.valueOf(request.getSession().getAttribute("comps"));
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(comps.equals("null")) return jacksonUtil.toJson(list);
		String sql = "select b.ba_id,b.ba_name,nvl(a.c,0) c from"
				+ " ( select b.ba_id,b.ba_name from TB_BUSI_AREA@db69 b, tb_company@db69 c where b.ba_id = c.ba_id  ";
			sql += " and comp_name in ("+comps+")";
			sql += " group by b.ba_id,b.ba_name ) b left join"
				+ " (select t.ba_id,count(1) c from TB_BUSI_AREA@db69 t,vw_vehicle@db69 v where t.ba_id"
				+ " = v.ba_id group by t.ba_id) a on a.ba_id = b.ba_id order by ba_name";
		List<Map<String, Object>> area = jdbcTemplate.queryForList(sql);
    	System.out.println(area);
    	System.out.println(sql);
		String com = "select b.comp_id,b.comp_name,nvl(a.c,0) c,b.ba_id from tb_company@db69 b left join"
				+ " (select t.comp_id,count(1) c from tb_company@db69 t,vw_vehicle@db69 v where t.comp_id"
				+ " = v.comp_id group by t.comp_id) a on a.comp_id = b.comp_id";
				com += " where b.comp_name in ("+comps+")";
				com += "order by comp_name";
		List<Map<String, Object>> comp = jdbcTemplate.queryForList(com);
		list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < area.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("label", area.get(i).get("BA_NAME")+"("+area.get(i).get("C")+")");
			map.put("value", area.get(i).get("BA_ID"));
			List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < comp.size(); j++) {
				if(String.valueOf(area.get(i).get("ba_id")).equals(String.valueOf(comp.get(j).get("ba_id")))){
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("label", comp.get(j).get("COMP_NAME")+"("+comp.get(j).get("C")+")");
					m.put("value", comp.get(j).get("COMP_ID"));
					children.add(m);
				}
			}
			if(children.size() > 0 ){
				map.put("children", children);
			}
			list.add(map);
		}
		return jacksonUtil.toJson(list);
	}
	public String getVhicList(String id,HttpServletRequest request){
		String comps = String.valueOf(request.getSession().getAttribute("comps"));
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(comps.equals("null")) return jacksonUtil.toJson(list);
		String sql = "select v.vehi_no,v.ba_name,v.comp_name,v.mdt_no,v.vehi_sim,v.mdt_sub_type,s.px,s.py,s.stime,s.speed"
				+ ",s.state,s.angle,s.carstate,s.alarmstatus,own_name,own_tel from vw_vehicle@db69 v"
				+ " left join tb_mdt_status@db69 s on v.mdt_no = s.mdt_no  where 1=1";
		if(!String.valueOf(id).equals("null")){
			if(id.length() == 5){
				sql += " and v.ba_id = '"+id+"'";
			}else{
				sql += " and v.comp_id = '"+id+"'";
			}
		}
		sql += " and v.comp_name in ("+comps+")";
		sql += " order by vehi_no";
		list = jdbcTemplate.queryForList(sql);
		String bb = "";
		for(int i=0; i<list.size();i++){
			if(!String.valueOf(list.get(i).get("STIME")).equals("null")&&jisuan(String.valueOf(list.get(i).get("STIME")))){
				if(String.valueOf(list.get(i).get("STATE")).equals("1")){
					list.get(i).put("VEHI_DONOT", "tw-radius-heavyCar");
					list.get(i).put("FX", utilFx(String.valueOf(list.get(i).get("ANGLE")),"z"));
				}else{
					list.get(i).put("VEHI_DONOT", "tw-radius-emptyCar");
					list.get(i).put("FX", utilFx(String.valueOf(list.get(i).get("ANGLE")),"k"));
				}
			}else{
				list.get(i).put("VEHI_DONOT", "tw-radius-offLine");
				list.get(i).put("FX", utilFx(String.valueOf(list.get(i).get("ANGLE")),"l"));
			}
			bb = String.valueOf(list.get(i).get("ALARMSTATUS"));
			if(bb.length() == 8){
				bb = bb.substring(7, 8);
				if(bb.equals("1") || bb.equals("3") || bb.equals("5") || bb.equals("7")
						|| bb.equals("9") || bb.equals("B") || bb.equals("D") || bb.equals("F")){
					list.get(i).put("VEHI_DONOT", "tw-radius-callPolice");
	            }
			}
			list.get(i).put("ANGLE", Fx(String.valueOf(list.get(i).get("ANGLE"))));
		}
		return jacksonUtil.toJson(list);
	}
	public String utilFx(String fx,String icon){
		try {
			if(Integer.parseInt(fx) == 0 ||Integer.parseInt(fx) == 360){
				return icon + "1";
			}else if(Integer.parseInt(fx) >0 &&Integer.parseInt(fx) <90){
				return icon + "2";
			}else if(Integer.parseInt(fx) == 90){
				return icon + "3";
			}else if(Integer.parseInt(fx) >90 &&Integer.parseInt(fx) <180){
				return icon + "4";
			}else if(Integer.parseInt(fx) == 180){
				return icon + "5";
			}else if(Integer.parseInt(fx) >180 &&Integer.parseInt(fx) <270){
				return icon + "6";
			}else if(Integer.parseInt(fx) == 270){
				return icon + "7";
			}else if(Integer.parseInt(fx) >270 &&Integer.parseInt(fx) <360){
				return icon + "8";
			}
		} catch (Exception e) {
			return icon + "1";
		}
		return icon + "1";
	}
	public boolean jisuan(String date1){ 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        double result=0;
        try {
            Date start = sdf.parse(date1);
            Date end = new Date();
            long cha = end.getTime() - start.getTime();
            result = cha * 1.0 / (1000 * 60);
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(result<=30){ 
            return true; 
        }else{ 
            return false; 
        } 
    }
	public String getOneVhic(HttpServletRequest request){
		String comps = String.valueOf(request.getSession().getAttribute("comps"));
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		String groupVhic = String.valueOf(request.getSession().getAttribute("groupVhic"));
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(loginType.equals("null")) return jacksonUtil.toJson(list);
		String type = String.valueOf(request.getParameter("type"));
		String vhic = String.valueOf(request.getParameter("vhic"));
		if(type.equals("null")||vhic.equals("null")){
			return jacksonUtil.toJson(list);
		}
		String sql = "select v.vehi_no,v.ba_name,v.comp_name,v.mdt_no,v.vehi_sim,v.mdt_sub_type,s.px,s.py,s.stime,s.speed"
				+ ",s.state,s.angle,s.carstate,s.alarmstatus,own_name,own_tel from vw_vehicle@db69 v left join tb_mdt_status@db69 s on v.mdt_no = s.mdt_no  where 1=1";
		if(type.equals("1")){
			sql += " and v.vehi_no = '"+vhic+"'";
			if(loginType.equals("0")){
				sql += " and v.comp_name in ("+comps+") order by v.vehi_no";
			}else if(loginType.equals("1")){
				List<String> l = new ArrayList<String>();
				String[] cphm = groupVhic.split(",");
				for (int i = 0; i < cphm.length; i++) {
					l.add( "'" + cphm[i] +"'");
				}
				List<String> zzjg = Transformation(l);
				sql += " and (";
				if(zzjg.size() ==1){
					sql += " vehi_no in ("+zzjg.get(0)+") ";
				}else{
					for (int i = 0; i < zzjg.size(); i++) {
						if(i == 0){
							sql += " vehi_no in ("+zzjg.get(i)+")";
						}else{
							sql += " or vehi_no in ("+zzjg.get(i)+")";
						}
					}
				}
				sql += " ) order by v.vehi_no";
			}else{
				return jacksonUtil.toJson(list);
			}
		}else if(type.equals("2")){
			List<String> trans = new ArrayList<String>();
			String[] v = vhic.split(",");
			for (int i = 0; i < v.length; i++) {
				trans.add(v[i]);
			}
			List<String> l = Transformation(trans);
			sql += " and (";
			if(l.size() ==1){
				sql += " v.vehi_no in ("+l.get(0)+") ";
			}else{
				for (int i = 0; i < l.size(); i++) {
					if(i == 0){
						sql += " v.vehi_no in ("+l.get(i)+")";
					}else{
						sql += " or v.vehi_no in ("+l.get(i)+")";
					}
				}
			}
			sql += " )";
			sql += " and v.comp_name in ("+comps+") order by v.vehi_no";
		}
		list = jdbcTemplate.queryForList(sql);
		for (int i = 0; i < list.size(); i++) {
			if(!String.valueOf(list.get(i).get("STIME")).equals("null")&&jisuan(String.valueOf(list.get(i).get("STIME")))){
				list.get(i).put("TYPE", "0");
				if(String.valueOf(list.get(i).get("STATE")).equals("1")){
					list.get(i).put("VEHI_DONOT", "tw-radius-heavyCar");
					list.get(i).put("STATETYPE", "重车");
				}else{
					list.get(i).put("STATETYPE", "空车");
					list.get(i).put("VEHI_DONOT", "tw-radius-emptyCar");
				}
			}else{
				list.get(i).put("STATETYPE", "离线");
				list.get(i).put("TYPE", "1");
				list.get(i).put("VEHI_DONOT", "tw-radius-offLine");
			}
			list.get(i).put("SFJQ", String.valueOf(list.get(i).get("CARSTATE")).equals("0")?"精确":"非精确");
			list.get(i).put("ANGLE", Fx(String.valueOf(list.get(i).get("ANGLE"))));
			list.get(i).put("TIME", String.valueOf(list.get(i).get("STIME")));
		}
		return jacksonUtil.toJson(list);
	}
	public List<String> Transformation(List<String> list){
		int init = 900;// 每隔100条循环一次 
        int total = list.size();
        int cycelTotal = total / init;
        if (total % init != 0) {
            cycelTotal += 1;
            if (total < init) {
                init = list.size();
            }
        }
        List<String> cphm = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        for (int i = 0; i < cycelTotal; i++) {
            for (int j = 0; j < init; j++) {
            	try {
                    if (list.get(j) == null) {  
                        break;  
                    }  
                    list2.add(list.get(j));
				} catch (Exception e) {
				}
            }
            String mdt_no = "";
            for (int j = 0; j < list2.size(); j++) {
            	mdt_no += list2.get(j)+",";
			}
            cphm.add(mdt_no.substring(0, mdt_no.length() - 1));
            list.removeAll(list2);//移出已经保存过的数据  
            list2.clear();//移出当前保存的数据  
        }
        return cphm;
	}
	public String Fx(String fx){
		try {
			if(Integer.parseInt(fx) == 0 ||Integer.parseInt(fx) == 360){
				return "正北";
			}else if(Integer.parseInt(fx) >0 &&Integer.parseInt(fx) <90){
				return "东北";
			}else if(Integer.parseInt(fx) == 90){
				return "正东";
			}else if(Integer.parseInt(fx) >90 &&Integer.parseInt(fx) <180){
				return "东南";
			}else if(Integer.parseInt(fx) == 180){
				return "正南";
			}else if(Integer.parseInt(fx) >180 &&Integer.parseInt(fx) <270){
				return "西南";
			}else if(Integer.parseInt(fx) == 270){
				return "正西";
			}else if(Integer.parseInt(fx) >270 &&Integer.parseInt(fx) <360){
				return "西北";
			}
		} catch (Exception e) {
			return "正北";
		}
		return "正北";
	}
	public String areaVhic(HttpServletRequest request){
		String comps = String.valueOf(request.getSession().getAttribute("comps"));
		String ID = String.valueOf(request.getParameter("id"));
		DataItem data = (DataItem) GisData.map.get("data");
		List<Map<String, Object>> vehilist = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> arealist = jdbcTemplate.queryForList("select * from TB_UPDOWNAREA@db69 where id = ?",ID);
		for (int i = 0; i < data.getVehilist().size(); i++) {
			if(comps.indexOf(String.valueOf(data.getVehilist().get(i).get("COMP_NAME")))>=0){
				data.getVehilist().get(i).put("ANGLE", String.valueOf(data.getVehilist().get(i).get("ANGLE")));
				vehilist.add(data.getVehilist().get(i));
			}
		}
		StringBuffer vhic = new StringBuffer();
		for(int k=0;k<arealist.size();k++){
			for(int j=0;j<vehilist.size();j++){
				arealist.get(k).put("AREAVHIC", new ArrayList<String>());
				if(comps.indexOf(String.valueOf(vehilist.get(j).get("COMP_NAME")))>=0){
					if(rectContains(vehilist.get(j), arealist.get(k))){
						vhic.append("'"+String.valueOf(vehilist.get(j).get("VEHI_NO"))+"',");
					}
				}
			}
		}
		String sql = "select v.vehi_no,v.ba_name,v.comp_name,v.mdt_no,v.vehi_sim,v.mdt_sub_type,s.px,s.py,s.stime,s.speed"
				+ ",s.state,s.angle,s.carstate,s.alarmstatus,own_name,own_tel from vw_vehicle@db69 v left join tb_mdt_status@db69 s on v.mdt_no = s.mdt_no  where 1=1";
		sql += " and v.vehi_no in ("+vhic.substring(0, vhic.length()-1)+") order by v.vehi_no";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		for (int i = 0; i < list.size(); i++) {
			if(jisuan(String.valueOf(list.get(i).get("stime")))){
				list.get(i).put("TYPE", "在线");
			}else{
				list.get(i).put("TYPE", "离线");
			}
			list.get(i).put("ANGLE", Fx(String.valueOf(list.get(i).get("ANGLE"))));
			list.get(i).put("TIME", String.valueOf(list.get(i).get("STIME")));
			list.get(i).put("STATE", String.valueOf(list.get(i).get("STATE")).equals("1")?"重车":"空车");
		}
		return jacksonUtil.toJson(list);
	}
	public String qyjk(HttpServletRequest request) {
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		String comps = String.valueOf(request.getSession().getAttribute("comps"));
		String userid = String.valueOf(request.getSession().getAttribute("userid"));
		String groupVhic = String.valueOf(request.getSession().getAttribute("groupVhic"));
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(loginType.equals("null")) return jacksonUtil.toJson(list);
		DataItem data = (DataItem) GisData.map.get("data");
		if(null != data){
			int onnum = 0;
			int offnum = 0;
			int hnum = 0;
			int nnum = 0;
			Map map = new HashMap();
			List<Map<String, Object>> vehilist = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> arealist = new ArrayList<Map<String,Object>>();
			if(loginType.equals("0")){
				for (int i = 0; i < data.getVehilist().size(); i++) {
					if(comps.indexOf(String.valueOf(data.getVehilist().get(i).get("COMP_NAME")))>=0){
						data.getVehilist().get(i).put("ANGLE", String.valueOf(data.getVehilist().get(i).get("ANGLE")));
						vehilist.add(data.getVehilist().get(i));
						Map<String, Object> va = data.getVehilist().get(i);
						if (String.valueOf(va.get("PX")).equals("null")
								|| "0.0".equals(String.valueOf(va.get("PX")))
								|| "null".equals(String.valueOf(va.get("PY")))
								|| "0.0".equals(String.valueOf(va.get("PY")))) {
							offnum++;
						} else {
							if (String.valueOf(va.get("TYPE")).equals("0")) {
								onnum++;
								if (String.valueOf(va.get("STATE")).equals("0")) {
									nnum++;
								} else {
									hnum++;
								}
							} else {
								offnum++;
							}
						}
					}
				}
				for (int i = 0; i < data.getArealist().size(); i++) {
					if(String.valueOf(data.getArealist().get(i).get("USER_ID")).equals(userid)&&String.valueOf(data.getArealist().get(i).get("USER_TYPE")).equals(loginType)){
						arealist.add(data.getArealist().get(i));
					}
				}
				for(int k=0;k<arealist.size();k++){
					List<String> l = new ArrayList<String>();
					for(int j=0;j<vehilist.size();j++){
						arealist.get(k).put("AREAVHIC", new ArrayList<String>());
						if(comps.indexOf(String.valueOf(vehilist.get(j).get("COMP_NAME")))>=0){
							if(rectContains(vehilist.get(j), arealist.get(k))){
								l.add(String.valueOf(vehilist.get(j).get("VEHI_NO")));
							}
						}
					}
					arealist.get(k).put("AREAVHIC", l);
				}
			}else if(loginType.equals("1")){
				for (int i = 0; i < data.getVehilist().size(); i++) {
					if(groupVhic.indexOf(String.valueOf(data.getVehilist().get(i).get("VEHI_NO")))>=0){
						data.getVehilist().get(i).put("ANGLE", String.valueOf(data.getVehilist().get(i).get("ANGLE")));
						vehilist.add(data.getVehilist().get(i));
						Map<String, Object> va = data.getVehilist().get(i);
						if (String.valueOf(va.get("PX")).equals("null")
								|| "0.0".equals(String.valueOf(va.get("PX")))
								|| "null".equals(String.valueOf(va.get("PY")))
								|| "0.0".equals(String.valueOf(va.get("PY")))) {
							offnum++;
						} else {
							if (String.valueOf(va.get("TYPE")).equals("0")) {
								onnum++;
								if (String.valueOf(va.get("STATE")).equals("0")) {
									nnum++;
								} else {
									hnum++;
								}
							} else {
								offnum++;
							}
						}
					}
				}
				for (int i = 0; i < data.getArealist().size(); i++) {
					if(String.valueOf(data.getArealist().get(i).get("USER_ID")).equals(userid)&&String.valueOf(data.getArealist().get(i).get("USER_TYPE")).equals(loginType)){
						arealist.add(data.getArealist().get(i));
					}
				}
				for(int k=0;k<arealist.size();k++){
					List<String> l = new ArrayList<String>();
					for(int j=0;j<vehilist.size();j++){
						arealist.get(k).put("AREAVHIC", new ArrayList<String>());
						if(groupVhic.indexOf(String.valueOf(vehilist.get(j).get("VEHI_NO")))>=0){
							if(rectContains(vehilist.get(j), arealist.get(k))){
								l.add(String.valueOf(vehilist.get(j).get("VEHI_NO")));
							}
						}
					}
					arealist.get(k).put("AREAVHIC", l);
				}
			}else{
				return jacksonUtil.toJson(map);
			}
			
			map.put("vehilist", vehilist);
			map.put("arealist",  arealist);
			Map<String, Object> num = new HashMap<String, Object>();
			int total = vehilist.size();
			num.put("total", total);
			num.put("online", onnum);
			num.put("notOnline", offnum);
			num.put("heavyCar", hnum);
			num.put("emptyCar", nnum);
			map.put("num",  num);
			System.out.println(total);
			return jacksonUtil.toJson(map);
		}
		
		return "{}";
	}
	private boolean rectContains(Map<String, Object> vehicle, Map<String, Object> area) {
		String[] zbs = area.get("AREA_COORDINATES").toString().split(";");//120.123,30.123;123.123,33.211;
		Geometry geometry=GeometryHandler.getGeometryObject(area.get("AREA_COORDINATES").toString()+";"+zbs[0]);
		String xy = (String.valueOf(vehicle.get("PX")).equals("null")?"0":vehicle.get("PX")) +"," + (String.valueOf(vehicle.get("PY")).equals("null")?0:vehicle.get("PY"));
		Geometry g2=GeometryHandler.getGeometryObject(xy);
		return geometry.contains(g2);
	}
	public String getArea(HttpServletRequest request) {
		String userid = String.valueOf(request.getSession().getAttribute("userid"));
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		String sql = "select * from TB_UPDOWNAREA@db69 where user_type = '"+loginType+"'";
		if(!userid.equals("null")){
			sql += " and user_id = '"+userid+"'";
		}
		sql += " order by db_time desc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		String nums = "";
		for(int i=0;i<list.size();i++){
			nums = String.valueOf(list.get(i).get("ALARMNUM")).equals("null")?"10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;":list.get(i).get("ALARMNUM").toString();
			for(int i1=0;i1<nums.split(";").length;i1++){
				list.get(i).put("YJS", nums.split(";")[getybjnum()]+"");
			}
		}
		return jacksonUtil.toJson(list);
	}
	public int getybjnum(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		//System.out.println(sdf.format(new Date()));
		String t = sdf.format(new Date());
		int a = timenum(t);
		if(a==timenum("23:58")||a==timenum("23:59")||a==timenum("00:02")||a==timenum("00:01")){
			return 0;
		}else if(a>=timenum("00:03")&&a<=timenum("00:57")){
			return 1;
		}else if(a>=timenum("00:58")&&a<=timenum("01:02")){
			return 2;
		}else if(a>=timenum("01:03")&&a<=timenum("01:57")){
			return 3;
		}else if(a>=timenum("01:58")&&a<=timenum("02:02")){
			return 4;
		}else if(a>=timenum("02:03")&&a<=timenum("02:57")){
			return 5;
		}else if(a>=timenum("02:58")&&a<=timenum("03:02")){
			return 6;
		}else if(a>=timenum("03:03")&&a<=timenum("03:57")){
			return 7;
		}else if(a>=timenum("03:58")&&a<=timenum("04:02")){
			return 8;
		}else if(a>=timenum("04:03")&&a<=timenum("04:57")){
			return 9;
		}else if(a>=timenum("04:58")&&a<=timenum("05:02")){
			return 10;
		}else if(a>=timenum("05:03")&&a<=timenum("05:57")){
			return 11;
		}else if(a>=timenum("05:58")&&a<=timenum("06:02")){
			return 12;
		}else if(a>=timenum("06:03")&&a<=timenum("06:57")){
			return 13;
		}else if(a>=timenum("06:58")&&a<=timenum("07:02")){
			return 14;
		}else if(a>=timenum("07:03")&&a<=timenum("07:57")){
			return 15;
		}else if(a>=timenum("07:58")&&a<=timenum("08:02")){
			return 16;
		}else if(a>=timenum("08:03")&&a<=timenum("08:57")){
			return 17;
		}else if(a>=timenum("08:58")&&a<=timenum("09:02")){
			return 18;
		}else if(a>=timenum("09:03")&&a<=timenum("09:57")){
			return 19;
		}else if(a>=timenum("09:58")&&a<=timenum("10:02")){
			return 20;
		}else if(a>=timenum("10:03")&&a<=timenum("10:57")){
			return 21;
		}else if(a>=timenum("10:58")&&a<=timenum("11:02")){
			return 22;
		}else if(a>=timenum("11:03")&&a<=timenum("11:57")){
			return 23;
		}else if(a>=timenum("11:58")&&a<=timenum("12:02")){
			return 24;
		}else if(a>=timenum("12:03")&&a<=timenum("12:57")){
			return 25;
		}else if(a>=timenum("12:58")&&a<=timenum("13:02")){
			return 26;
		}else if(a>=timenum("13:03")&&a<=timenum("13:57")){
			return 27;
		}else if(a>=timenum("13:58")&&a<=timenum("14:02")){
			return 28;
		}else if(a>=timenum("14:03")&&a<=timenum("14:57")){
			return 29;
		}else if(a>=timenum("14:58")&&a<=timenum("15:02")){
			return 30;
		}else if(a>=timenum("15:03")&&a<=timenum("15:57")){
			return 31;
		}else if(a>=timenum("15:58")&&a<=timenum("16:02")){
			return 32;
		}else if(a>=timenum("16:03")&&a<=timenum("16:57")){
			return 33;
		}else if(a>=timenum("16:58")&&a<=timenum("17:02")){
			return 34;
		}else if(a>=timenum("17:03")&&a<=timenum("17:57")){
			return 35;
		}else if(a>=timenum("17:58")&&a<=timenum("18:02")){
			return 36;
		}else if(a>=timenum("18:03")&&a<=timenum("18:57")){
			return 37;
		}else if(a>=timenum("18:58")&&a<=timenum("19:02")){
			return 38;
		}else if(a>=timenum("19:03")&&a<=timenum("19:57")){
			return 39;
		}else if(a>=timenum("19:58")&&a<=timenum("20:02")){
			return 40;
		}else if(a>=timenum("20:03")&&a<=timenum("20:57")){
			return 41;
		}else if(a>=timenum("20:58")&&a<=timenum("21:02")){
			return 42;
		}else if(a>=timenum("21:03")&&a<=timenum("21:57")){
			return 43;
		}else if(a>=timenum("21:58")&&a<=timenum("22:02")){
			return 44;
		}else if(a>=timenum("22:03")&&a<=timenum("22:57")){
			return 45;
		}else if(a>=timenum("22:58")&&a<=timenum("23:02")){
			return 46;
		}else if(a>=timenum("23:03")&&a<=timenum("23:57")){
			return 47;
		}else{
			return 0;
		}
	}
	public int timenum(String arg){
		return Integer.parseInt(arg.split(":")[0])*60+Integer.parseInt(arg.split(":")[1]);
	}
	public String addArea(HttpServletRequest request) {
		String area_name = request.getParameter("name");
		String area_ms = request.getParameter("ms");
		String jwd = request.getParameter("jwd");
		String size = request.getParameter("size");
		String userid = String.valueOf(request.getSession().getAttribute("userid"));
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		String id = UUID.randomUUID().toString();
		String sql = "insert into TB_UPDOWNAREA@db69 (id,AREA_NAME,AREA_DESCRIBE,AREA_COORDINATES,ALARMNUM,AREA_SIZE,user_id,user_type) values("
				+ "'"+id+"','"+area_name+"','"+area_ms+"','"+jwd+"','10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;10;','"+size+"','"+userid+"','"+loginType+"')";
		int c = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		map.put("info", "添加失败");
		if(c>0) map.put("info", "添加成功");
		return jacksonUtil.toJson(map);
	}
	public String editArea(HttpServletRequest request) {
		String area_name = request.getParameter("name");
		String area_ms = request.getParameter("ms");
		String jwd = request.getParameter("jwd");
		String size = request.getParameter("size");
		String id = request.getParameter("id");
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		String sql = "update TB_UPDOWNAREA@db69 set AREA_NAME='"+area_name+"',AREA_DESCRIBE='"+area_ms+"',AREA_COORDINATES='"+jwd+"',AREA_SIZE='"+size+"',user_type='"+loginType+"' where id='"+id+"'";
		int c = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		map.put("info", "修改失败");
		if(c>0) map.put("info", "修改成功");
		return jacksonUtil.toJson(map);
	}
	public String delArea(HttpServletRequest request) {
		String id = request.getParameter("id");
		String sql = "delete from TB_UPDOWNAREA@db69 where id='"+id+"'";
		int c = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		map.put("info", "删除失败");
		if(c>0) map.put("info", "删除成功");
		return jacksonUtil.toJson(map);
	}
	public String importantInfo(HttpServletRequest request) {
		String vhic = String.valueOf(request.getParameter("vhic"));
		String time = String.valueOf(request.getParameter("time"));
		String sql = "select * from tb_ic_operate@db69 t where (logintime >= to_date('"+time+"','yyyy-mm-dd hh24:mi:ss') "
				+ "or logouttime >= to_date('"+time+"','yyyy-mm-dd hh24:mi:ss'))";
//				+ " and  operation_vehicleno in ("+vhic+") order by logintime desc,logouttime desc";
		List<String> trans = new ArrayList<String>();
		String[] v = vhic.split(",");
		for (int i = 0; i < v.length; i++) {
			trans.add(v[i]);
		}
		List<String> l = Transformation(trans);
		sql += " and (";
		if(l.size() ==1){
			sql += " operation_vehicleno in ("+l.get(0)+") ";
		}else{
			for (int i = 0; i < l.size(); i++) {
				if(i == 0){
					sql += " operation_vehicleno in ("+l.get(i)+")";
				}else{
					sql += " or operation_vehicleno in ("+l.get(i)+")";
				}
			}
		}
		sql += " ) order by logintime desc,logouttime desc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	public String queryVhic(HttpServletRequest request,String sp,String vehi_no) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		String comps = String.valueOf(request.getSession().getAttribute("comps"));
		if(comps.equals("null")) return jacksonUtil.toJson(result);
		String sql="select v.*,s.stime from vw_vehicle@db69 v left join tb_mdt_status@db69 s on v.mdt_no = s.mdt_no where 1=1 ";
		if(sp.equals("海康")){
			sql += " and (mt_name like '%"+sp+"%' or mdt_sub_type like '%"+sp+"%')";
		}else{
			sql += " and (mdt_sub_type like '%"+sp+"%' or mdt_sub_type like '%DH%')";
		}
		sql += " and v.vehi_no like '%"+vehi_no+"%' and comp_name in ("+comps+")";
		sql += " order by v.vehi_no ";
		System.out.println(sql);
		result = jdbcTemplate.queryForList(sql);
		for(int i=0; i<result.size(); i++){
			if(String.valueOf(result.get(i).get("STIME")).equals("null")){
				result.get(i).put("STIME", 0);
			}
		}
		return jacksonUtil.toJson(result);
	}
	public String getInfo(HttpServletRequest request) {
		String userid = String.valueOf(request.getSession().getAttribute("userid"));
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		String sql = "select * from tb_msg@db69 where userid = '"+userid+"' and usertype = '"+loginType+"' order by db_time desc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	public String addInfo(HttpServletRequest request) {
		String info = request.getParameter("info");
		String id = UUID.randomUUID().toString();
		String userid = String.valueOf(request.getSession().getAttribute("userid"));
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		String sql = "insert into tb_msg@db69 (id,info,userid,usertype) values('"+id+"','"+info+"','"+userid+"','"+loginType+"')";
		int c = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		map.put("info", "添加失败");
		if(c>0) map.put("info", "添加成功");
		return jacksonUtil.toJson(map);
	}
	public String editInfo(HttpServletRequest request) {
		String info = request.getParameter("info");
		String id = request.getParameter("id");
		String sql = "update tb_msg@db69 set info = '"+info+"' where id = '"+id+"'";
		int c = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		map.put("info", "修改失败");
		if(c>0) map.put("info", "修改成功");
		return jacksonUtil.toJson(map);
	}
	public String delInfo(HttpServletRequest request) {
		String id = request.getParameter("id");
		String sql = "delete from tb_msg@db69 where id = '"+id+"'";
		int c = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		map.put("info", "删除失败");
		if(c>0) map.put("info", "删除成功");
		return jacksonUtil.toJson(map);
	}
	public String getBa_select(HttpServletRequest request) {
		String comp = String.valueOf(request.getSession().getAttribute("comps"));
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(comp.equals("null")){
			return jacksonUtil.toJson(list);
		}
		String sql = "select b.ba_id,b.ba_name from TB_BUSI_AREA@db69 b where ba_id in"
				+ " (select ba_id from TB_COMPANY@db69 where comp_name in ("+comp+")) order by ba_name";
		list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	public String getComp_select(HttpServletRequest request) {
		String comp = String.valueOf(request.getSession().getAttribute("comps"));
		String ba_id = String.valueOf(request.getParameter("id"));
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(comp.equals("null")){
			return jacksonUtil.toJson(list);
		}
		String sql = "select comp_name,comp_id from tb_company@db69 where ba_id = ?";
		list = jdbcTemplate.queryForList(sql,ba_id);
		return jacksonUtil.toJson(list);
	}
	public String getVhic_select(HttpServletRequest request) {
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = "select vehi_no,vehi_id,comp_id from vw_vehicle@db69 where 1=1";
		if(loginType.equals("0")){
			String ba_id = String.valueOf(request.getParameter("ba_id"));
			String comp_id = String.valueOf(request.getParameter("comp_id"));
			if(!ba_id.equals("null")&&ba_id.length()>1){
				sql += " and ba_id = '"+ba_id+"'";
			}
			if(!comp_id.equals("null")&&comp_id.length()>1){
				sql += " and comp_id = '"+comp_id+"'";
			}
		}else if(loginType.equals("1")){
			String id = String.valueOf(request.getParameter("id"));
			String vhic = jdbcTemplate.queryForObject("select clz_cp from tb_clz where id = '"+id+"'", String.class);
			List<String> l = new ArrayList<String>();
			String[] cphm = vhic.split(",");
			for (int i = 0; i < cphm.length; i++) {
				l.add( "'" + cphm[i] +"'");
			}
			List<String> zzjg = Transformation(l);
			sql += " and (";
			if(zzjg.size() ==1){
				sql += " vehi_no in ("+zzjg.get(0)+") ";
			}else{
				for (int i = 0; i < zzjg.size(); i++) {
					if(i == 0){
						sql += " vehi_no in ("+zzjg.get(i)+")";
					}else{
						sql += " or vehi_no in ("+zzjg.get(i)+")";
					}
				}
			}
			sql += " )";
		}else{
			return jacksonUtil.toJson(list);
		}
		sql += " order by vehi_no";
		list = jdbcTemplate.queryForList(sql);
		List<Map<String, Object>> list1 = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("COMP_ID", "0");
		map.put("VEHI_ID", "0");
		map.put("VEHI_NO", "所有车辆");
		list1.add(map);
		list1.addAll(list);
//		for (int i = 0; i < list.size(); i++) {
//			list1.add(list.get(i));
//		}
		return jacksonUtil.toJson(list1);
	}
	public String sendMessage(HttpServletRequest request){
		String vehi_no = String.valueOf(request.getParameter("vehi_no"));
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		String nr = String.valueOf(request.getParameter("nr"));
		if(!vehi_no.equals("null")&&vehi_no.length()>2&&vehi_no.indexOf("所有")<0){
			String sql = "select mdt_no from vw_vehicle@db69 where vehi_no in ("+vehi_no+")";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			System.out.println(list);
			send(list,nr);
			return "";
		}
		if(loginType.equals("0")){
			String ba_id = String.valueOf(request.getParameter("ba_id"));
			String comp_id = String.valueOf(request.getParameter("comp_id"));
			if(!comp_id.equals("null")&&comp_id.length()>2){
				String sql = "select mdt_no from vw_vehicle@db69 where comp_id = '"+comp_id+"'";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
				send(list,nr);
				return "";
			}
			if(!ba_id.equals("null")&&ba_id.length()>2){
				String sql = "select mdt_no from vw_vehicle@db69 where ba_id = '"+ba_id+"'";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
				send(list,nr);
				return "";
			}
		}else if(loginType.equals("1")){
			String clz = String.valueOf(request.getParameter("clz"));
			String vhic = jdbcTemplate.queryForObject("select clz_cp from tb_clz where id = '"+clz+"'", String.class);
			List<String> l = new ArrayList<String>();
			String[] cphm = vhic.split(",");
			for (int i = 0; i < cphm.length; i++) {
				l.add( "'" + cphm[i] +"'");
			}
			String sql = "select mdt_no from vw_vehicle@db69 where 1=1 ";
			List<String> zzjg = Transformation(l);
			sql += " and (";
			if(zzjg.size() ==1){
				sql += " vehi_no in ("+zzjg.get(0)+") ";
			}else{
				for (int i = 0; i < zzjg.size(); i++) {
					if(i == 0){
						sql += " vehi_no in ("+zzjg.get(i)+")";
					}else{
						sql += " or vehi_no in ("+zzjg.get(i)+")";
					}
				}
			}
			sql += " )";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			send(list,nr);
		}
		return "";
	}
	public String oneSendMessage(HttpServletRequest request){
		String vehi_no = String.valueOf(request.getParameter("vehi_no"));
		String nr = String.valueOf(request.getParameter("nr"));
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("cmd", "0x8300");
        map1.put("flag", "0");
        map1.put("content", nr);
        map1.put("isu", vehi_no);
        String josn1 = jacksonUtil.toJson(map1);
        Sender.StartSend("192.168.0.102", "hz_taxi_905_gj", josn1);
		return "";
	}
	/**
	 * 将list数据每100条发送一次
	 */
	public void send(List<Map<String, Object>> list,String nr){
		System.out.println(nr);
		int init = 100;// 每隔100条循环一次 
        int total = list.size();
        int cycelTotal = total / init;
        if (total % init != 0) {
            cycelTotal += 1;
            if (total < init) {
                init = list.size();
            }
        }
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("cmd", "0x8300");
        map1.put("flag", "0");
        map1.put("content", nr);
        List<Map<String, Object>> list2 = new ArrayList();
        for (int i = 0; i < cycelTotal; i++) {
            for (int j = 0; j < init; j++) {
            	try {
                    if (list.get(j) == null) {
                        break;  
                    }
                    list2.add(list.get(j));
				} catch (Exception e) {
				}
            }
            System.out.println("保存100条数据到数据库....");
            String mdt_no = "";
            for (int j = 0; j < list2.size(); j++) {
            	mdt_no += list2.get(j).get("MDT_NO")+",";
            	String id = UUID.randomUUID().toString();
				jdbcTemplate.update("insert into tb_sendMessage@db69 (id,content,mdt_no) values('"+id+"','"+nr+"','"+list2.get(j).get("MDT_NO")+"')");
			}
            map1.put("isu", mdt_no.substring(0, mdt_no.length()-1));
            String josn1 = jacksonUtil.toJson(map1);
            Sender.StartSend("192.168.0.102", "hz_taxi_905_gj", josn1);
            list.removeAll(list2);//移出已经保存过的数据  
            list2.clear();//移出当前保存的数据  
        }
	}
	public String SendMessageMQ(HttpServletRequest request){
		String isu = request.getParameter("isu");
		String type = request.getParameter("type");
		isu = "101000970963";
		Map<String, String> map = new HashMap<String, String>();
		map.put("cmd", "0x8104");
		map.put("isu", isu);
		map.put("serialNumber", type);
		return jacksonUtil.toJson(map);
	}
	public String setTerminal(HttpServletRequest request){
		String isu = request.getParameter("isu");
		String type = request.getParameter("type");
		String param = request.getParameter("param");
		Map<String, Object> map = new HashMap<String, Object>();
		isu = "101000970963";
		map.put("cmd", "0x8103");
		map.put("isu", isu);
		map.put("serialNumber", type);
		map.put("paramBody", swutchMap(param));
		return jacksonUtil.toJson(map);
	}
	public Map<String, String> swutchMap(String s){
		Map<String, String> map = new HashMap<String, String>();
    	String a = s.replace("{", "").replace("}", "").replaceAll("\"", "");
    	System.out.println(a);
    	String[] b = a.split(",");
    	for (int i = 0; i < b.length; i++) {
			map.put(b[i].split(":")[0], b[i].split(":")[1]);
		}
		return map;
	}
	public String getUser(HttpServletRequest request) {
		String realname = String.valueOf(request.getSession().getAttribute("realname"));
		String power_ids = String.valueOf(request.getSession().getAttribute("power_ids"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("realname", realname);
		map.put("power", power_ids);
		return jacksonUtil.toJson(map);
	}
	/*独立车俩组表*/
	public String finddlclzb(String name){
		String sql = "select id,clz_name,clz_cp from tb_clz where 1=1";
		if(name!=null&&name.length()>0){
			sql += " and (clz_name like '%"+name+"%' or clz_cp like '%"+name+"%')";
		}
		sql += " order by to_number(id) desc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("VEHI", String.valueOf(list.get(i).get("CLZ_CP")).length()<120?String.valueOf(list.get(i).get("CLZ_CP")):String.valueOf(list.get(i).get("CLZ_CP")).substring(0, 110)+"...");
		}
		return jacksonUtil.toJson(list);
	}
	public String adddlclzb(HttpServletRequest request){
		String CLZ_NAME = String.valueOf(request.getParameter("CLZ_NAME"));
		String CLZ_CP = String.valueOf(request.getParameter("CLZ_CP")).replaceAll("，", ",");
		int ID = max_id("id", "tb_clz");
		String sql = "insert into tb_clz (CLZ_NAME,CLZ_CP,ID) values ('"+CLZ_NAME+
				"',?,'"+ID+"')";
		int count = jdbcTemplate.update(sql,CLZ_CP);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "添加成功");
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String editdlclzb(HttpServletRequest request){
		String CLZ_NAME = String.valueOf(request.getParameter("CLZ_NAME"));
		String CLZ_CP = String.valueOf(request.getParameter("CLZ_CP")).replaceAll("，", ",");
		String ID = String.valueOf(request.getParameter("ID"));
		String sql = "update tb_clz set CLZ_NAME='"+CLZ_NAME+"',CLZ_CP=? where ID='"+ID+"'";
		int count = jdbcTemplate.update(sql,CLZ_CP);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "修改成功");
		}else{
			map.put("info", "修改失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String deldlclzb(String id){
		String[] ids = id.split(",");
		String is = "";
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}
		String sql = "delete from tb_clz where id in ("+is.substring(0, is.length()-1)+")";
		int count = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "删除成功");
		}else{
			map.put("info", "删除失败");
		}
		return jacksonUtil.toJson(map);
	}
	/*独立车俩组账号表*/
	public String finddlclzzhb(String name){
		String sql = "select u.*,s.station_name from tb_clz_user u,TB_TAXI_STATION s where u.station_id = s.id";
		if(name!=null&&name.length()>0){
			sql += " and (username like '%"+name+"%' or clzm like '%"+name+"%')";
		}
		sql += " order by to_number(u.id) desc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	public String adddlclzzhb(HttpServletRequest request){
		String USERNAME = String.valueOf(request.getParameter("USERNAME"));
		String PASSWORD = String.valueOf(request.getParameter("PASSWORD"));
		String REALNAME = String.valueOf(request.getParameter("REALNAME"));
		String CLZS = String.valueOf(request.getParameter("CLZM"));
		String STATION_ID = String.valueOf(request.getParameter("STATION_ID"));
		String[] zm = CLZS.split(",");
		String m = "";
		for(int i=0; i<zm.length; i++){
			m += "'" + zm[i] + "',";
		}
		String clzm = jdbcTemplate.queryForObject("select wm_concat(clz_name) clz_name from tb_clz where id in("+m.substring(0, m.length()-1)+")", String.class);
		int ID = max_id("id", "tb_clz_user");
		String sql = "insert into tb_clz_user (username,password,ID,clzs,clzm,real_name,station_id) values ('"+USERNAME+
				"','"+PASSWORD+"','"+ID+"','"+CLZS+"','"+clzm+"','"+REALNAME+"','"+STATION_ID+"')";
		int count = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "添加成功");
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String editdlclzzhb(HttpServletRequest request){
		String USERNAME = String.valueOf(request.getParameter("USERNAME"));
		String PASSWORD = String.valueOf(request.getParameter("PASSWORD"));
		String REALNAME = String.valueOf(request.getParameter("REALNAME"));
		String CLZS = String.valueOf(request.getParameter("CLZM"));
		String ID = String.valueOf(request.getParameter("ID"));
		String STATION_ID = String.valueOf(request.getParameter("STATION_ID"));
		String[] zm = CLZS.split(",");
		String m = "";
		for(int i=0; i<zm.length; i++){
			m += "'" + zm[i] + "',";
		}
		String clzm = jdbcTemplate.queryForObject("select wm_concat(clz_name) clz_name from tb_clz where id in("+m.substring(0, m.length()-1)+")", String.class);
		String sql = "update tb_clz_user set USERNAME='"+USERNAME+"',PASSWORD='"+PASSWORD+"'"
				+ ",CLZS='"+CLZS+"',CLZM='"+clzm+"',REAL_NAME='"+REALNAME+"',STATION_ID='"+STATION_ID+"' where ID='"+ID+"'";
		int count = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "修改成功");
		}else{
			map.put("info", "修改失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String deldlclzzhb(String id){
		String[] ids = id.split(",");
		String is = "";
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}
		String sql = "delete from tb_clz_user where id in ("+is.substring(0, is.length()-1)+")";
		int count = jdbcTemplate.update(sql);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "删除成功");
		}else{
			map.put("info", "删除失败");
		}
		return jacksonUtil.toJson(map);
	}
	public int max_id(String id,String table){
		int i = 1;
		String sql = "select "+id+" from "+table+" order by to_number("+id+") desc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			i = Integer.parseInt(list.get(0).get(id).toString())+1;
		}
		return i;
	}
	public String loginType(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		String userid = String.valueOf(request.getSession().getAttribute("userid"));
		map.put("loginType", loginType);
		map.put("userId", userid);
		return jacksonUtil.toJson(map);
	}
	public String getClz(HttpServletRequest request){
		String groupId = String.valueOf(request.getSession().getAttribute("GroupId"));
		String[] group = groupId.split(",");
		String sql = "select * from tb_clz where 1=1";
		String name = "";
		for (int i = 0; i < group.length; i++) {
			name += "'"+ group[i]+"',";
		}
		sql += " and id in ("+name.substring(0, name.length()-1)+")";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	public String getClzTree(HttpServletRequest request){
		String groupId = String.valueOf(request.getSession().getAttribute("GroupId"));
		String[] group = groupId.split(",");
		String sql = "select * from tb_clz where 1=1";
		String name = "";
		for (int i = 0; i < group.length; i++) {
			name += "'"+ group[i]+"',";
		}
		sql += " and id in ("+name.substring(0, name.length()-1)+")";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		List<Map<String, String>> list1 = new ArrayList<Map<String,String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("label", list.get(i).get("CLZ_NAME")+"("+String.valueOf(list.get(i).get("CLZ_CP")).split(",").length+")");
			map.put("clz_id", String.valueOf(list.get(i).get("id")));
			map.put("cp", String.valueOf(list.get(i).get("CLZ_CP")));
			list1.add(map);
		}
		return jacksonUtil.toJson(list1);
	}
	public String getClzVhic(HttpServletRequest request) {
		String cp = String.valueOf(request.getParameter("cp"));
		String sql = "select v.vehi_no,v.ba_name,v.comp_name,v.mdt_no,v.vehi_sim,v.mdt_sub_type,s.px,s.py,s.stime,s.speed"
				+ ",s.state,s.angle,s.carstate,s.alarmstatus,own_name,own_tel from vw_vehicle@db69 v left join tb_mdt_status@db69 s on v.mdt_no = s.mdt_no  where 1=1";
		List<String> l = new ArrayList<String>();
		String[] cphm = cp.split(",");
		for (int i = 0; i < cphm.length; i++) {
			l.add( "'" + cphm[i] +"'");
		}
		List<String> zzjg = Transformation(l);
		sql += " and (";
		if(zzjg.size() ==1){
			sql += " vehi_no in ("+zzjg.get(0)+") ";
		}else{
			for (int i = 0; i < zzjg.size(); i++) {
				if(i == 0){
					sql += " vehi_no in ("+zzjg.get(i)+")";
				}else{
					sql += " or vehi_no in ("+zzjg.get(i)+")";
				}
			}
		}
		sql += " )";
		sql += " order by vehi_no";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);String bb = "";
		for(int i=0; i<list.size();i++){
			if(!String.valueOf(list.get(i).get("STIME")).equals("null")&&jisuan(String.valueOf(list.get(i).get("STIME")))){
				if(String.valueOf(list.get(i).get("STATE")).equals("1")){
					list.get(i).put("VEHI_DONOT", "tw-radius-heavyCar");
					list.get(i).put("FX", utilFx(String.valueOf(list.get(i).get("ANGLE")),"z"));
				}else{
					list.get(i).put("VEHI_DONOT", "tw-radius-emptyCar");
					list.get(i).put("FX", utilFx(String.valueOf(list.get(i).get("ANGLE")),"k"));
				}
			}else{
				list.get(i).put("VEHI_DONOT", "tw-radius-offLine");
				list.get(i).put("FX", utilFx(String.valueOf(list.get(i).get("ANGLE")),"l"));
			}
			bb = String.valueOf(list.get(i).get("ALARMSTATUS"));
			if(bb.length() == 8){
				bb = bb.substring(7, 8);
				if(bb.equals("1") || bb.equals("3") || bb.equals("5") || bb.equals("7")
						|| bb.equals("9") || bb.equals("B") || bb.equals("D") || bb.equals("F")){
					list.get(i).put("VEHI_DONOT", "tw-radius-callPolice");
	            }
			}
			list.get(i).put("ANGLE", Fx(String.valueOf(list.get(i).get("ANGLE"))));
		}
		return jacksonUtil.toJson(list);
	}
	public String getAlarm(HttpServletRequest request){
		String comps = String.valueOf(request.getSession().getAttribute("comps"));
		String loginType = String.valueOf(request.getSession().getAttribute("loginType"));
		String groupVhic = String.valueOf(request.getSession().getAttribute("groupVhic"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date(System.currentTimeMillis() - 1000*60*3));
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyMM");
		String riqi = sdf1.format(new Date());
		String sql = "select vehicle_num,max(alarmstatus) alarmstatus from TB_GPS_"+riqi+"@db69 t,vw_vehicle@db69 v where"
				+ " t.vehicle_num = v.vehi_no and speed_time > to_date('"+time+"','yyyy-mm-dd hh24:mi:ss') ";
		if(loginType.equals("0")){
			sql += " and v.comp_name in ("+comps+")";
		}else if(loginType.equals("1")){
			List<String> l = new ArrayList<String>();
			String[] cphm = groupVhic.split(",");
			for (int i = 0; i < cphm.length; i++) {
				l.add( "'" + cphm[i] +"'");
			}
			List<String> zzjg = Transformation(l);
			sql += " and (";
			if(zzjg.size() ==1){
				sql += " vehi_no in ("+zzjg.get(0)+") ";
			}else{
				for (int i = 0; i < zzjg.size(); i++) {
					if(i == 0){
						sql += " vehi_no in ("+zzjg.get(i)+")";
					}else{
						sql += " or vehi_no in ("+zzjg.get(i)+")";
					}
				}
			}
			sql += " )";
		}else{
			return jacksonUtil.toJson(list);
		}
		sql += " and alarmstatus != '00000000' group by vehicle_num order by vehicle_num";
		list = jdbcTemplate.queryForList(sql);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("ALARMSTATUS", hexString2binaryString(String.valueOf(list.get(i).get("ALARMSTATUS"))));
		}
		return jacksonUtil.toJson(list);
	}
	 public static String hexString2binaryString(String hexString) { 
        if (hexString == null || hexString.length() % 2 != 0) 
          return null; 
        String bString = "", tmp; 
        for (int i = 0; i < hexString.length(); i++) { 
          tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16)); 
          bString += tmp.substring(tmp.length() - 4); 
        } 
        String b = "";
        for (int i = bString.length(); i < 32; i++) {
        	b += "0";
		}
        String s = b+bString; 
        int j = 0;
        String alarm = "";
        for (int i = 0; i < 32; i++) {
        	j = i+1;
        	if(i == 0){ if(s.substring(i, j).equals("1")) alarm += "";}
        	if(i == 1){ if(s.substring(i, j).equals("1")) alarm += "";}
        	if(i == 2){ if(s.substring(i, j).equals("1")) alarm += "";}
        	if(i == 3){ if(s.substring(i, j).equals("1")) alarm += "";}
        	if(i == 4){ if(s.substring(i, j).equals("1")) alarm += "计价器实时时钟超过规定的误差范围;";}
        	if(i == 5){ if(s.substring(i, j).equals("1")) alarm += "录音设备故障;";}
        	if(i == 6){ if(s.substring(i, j).equals("1")) alarm += "ISU存储异常;";}
        	if(i == 7){ if(s.substring(i, j).equals("1")) alarm += "车辆非法位移;";}
        	if(i == 8){ if(s.substring(i, j).equals("1")) alarm += "车辆非法点火;";}
        	if(i == 9){ if(s.substring(i, j).equals("1")) alarm += "车速传感器故障;";}
        	if(i == 10){ if(s.substring(i, j).equals("1")) alarm += "禁行路段行驶;";}
        	if(i == 11){ if(s.substring(i, j).equals("1")) alarm += "路段行驶时间不足/过长;";}
        	if(i == 12){ if(s.substring(i, j).equals("1")) alarm += "进出区域/路线;";}
        	if(i == 13){ if(s.substring(i, j).equals("1")) alarm += "超时停车;";}
        	if(i == 14){ if(s.substring(i, j).equals("1")) alarm += "当天累计驾驶超市;";}
        	if(i == 15){ if(s.substring(i, j).equals("1")) alarm += "连续驾驶超时;";}
        	if(i == 16){ if(s.substring(i, j).equals("1")) alarm += "超速报警;";}
        	if(i == 17){ if(s.substring(i, j).equals("1")) alarm += "LED顶灯故障;";}
        	if(i == 18){ if(s.substring(i, j).equals("1")) alarm += "安全访问模块故障;";}
        	if(i == 19){ if(s.substring(i, j).equals("1")) alarm += "液晶(LCD)显示屏故障;";}
        	if(i == 20){ if(s.substring(i, j).equals("1")) alarm += "LED广告屏故障;";}
        	if(i == 21){ if(s.substring(i, j).equals("1")) alarm += "服务评价器故障(前后排);";}
        	if(i == 22){ if(s.substring(i, j).equals("1")) alarm += "计价器故障;";}
        	if(i == 23){ if(s.substring(i, j).equals("1")) alarm += "摄像头故障;";}
        	if(i == 24){ if(s.substring(i, j).equals("1")) alarm += "语音合成(TTS)模块故障;";}
        	if(i == 25){ if(s.substring(i, j).equals("1")) alarm += "液晶(LCD)显示ISU故障;";}
        	if(i == 26){ if(s.substring(i, j).equals("1")) alarm += "ISU主电源掉电;";}
        	if(i == 27){ if(s.substring(i, j).equals("1")) alarm += "ISU主电源欠压;";}
        	if(i == 28){ if(s.substring(i, j).equals("1")) alarm += "卫星定位天线短路;";}
        	if(i == 29){ if(s.substring(i, j).equals("1")) alarm += "卫星定位天线未接或被剪断;";}
        	if(i == 30){ if(s.substring(i, j).equals("1")) alarm += "预警;";}
        	if(i == 31){ if(s.substring(i, j).equals("1")) alarm += "紧急报警;";}
		}
        return alarm; 
      }
	public String multiVehicle(HttpServletRequest request) {
		String vhic = request.getParameter("vhic");
		String[] param = vhic.split(";");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String vehi_no = "",time = "",table="";
			String sql = "select c.comp_name,t.vehicle_num,t.px,t.py,t.speed,t.direction,t.speed_time,state,"
					+ "own_name,own_tel from HZGPS_TAXI.TB_GPS_"+table+"@db69 t,tb_company@db69 c,vw_vehicle@db69 v where"
					+ " t.vehicle_num = v.vehi_no and c.comp_id = v.comp_id and vehicle_num = ? and"
					+ " speed_time>= to_date(?,'yyyy-mm-dd hh24:mi:ss')"
					+ " and speed_time<= to_date(?,'yyyy-mm-dd hh24:mi:ss')"
					+ " and px>110 and py>25 and carstate = '0' order by speed_time";
		for (int i = 0; i < param.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			vehi_no = param[i].split(",")[0];
			time = param[i].split(",")[1];
			table = time.substring(2, 4) + time.substring(5, 7);
			List<Map<String, Object>> monitor = jdbcTemplate.queryForList("select v.vehi_no,v.ba_name,v.comp_name,v.mdt_no,v.vehi_sim,v.mdt_sub_type,s.px,s.py,s.stime,s.speed"
				+ ",s.state,s.angle,s.carstate,s.alarmstatus,own_name,own_tel from vw_vehicle@db69 v left join tb_mdt_status@db69 s on v.mdt_no = s.mdt_no  where v.vehi_no = '"+vehi_no+"'");
			List<Map<String, Object>> line = jdbcTemplate.queryForList("select px,py from HZGPS_TAXI.TB_GPS_"+table+"@db69 where speed_time >= to_date('"+time+"','yyyy-mm-dd hh24:mi:ss') "
					+ "and px>110 and py>25 and carstate = '0' and vehicle_num = '"+vehi_no+"' order by speed_time");
			map.put("monitor", monitor);
			map.put("line", line);
			list.add(map);
		}
		return jacksonUtil.toJson(list);
	}
}
