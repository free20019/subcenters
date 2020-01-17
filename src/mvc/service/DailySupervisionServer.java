package mvc.service;


import helper.DownloadAct;
import helper.JacksonUtil;
import helper.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.entity.Vehicle;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import sun.misc.BASE64Decoder;


@Service
public class DailySupervisionServer {
	protected JdbcTemplate jdbcTemplate = null;
	protected JdbcTemplate jdbcTemplate2 = null;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate2() {
		return jdbcTemplate2;
	}

	@Autowired
	public void setJdbcTemplate2(JdbcTemplate jdbcTemplate2) {
		this.jdbcTemplate2 = jdbcTemplate2;
	}
	@Resource
	mvc.mapper.dailySupervisionDao dailySupervisionDao;

	public Boolean isNullOrEmpty(Object str) {
        if (str == null || str.toString().equals("") || str == "null") return true;
        return false;
    }
	
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	
	public String findhldmar(String jd,String wd,String jb,String companyid,String type) {
		LogUtil.log("车辆动态监控-详细车辆聚合模块", "车辆动态监控-详细车辆聚合模块");
		String result = "";
			try {
				URL url = null;
				if(type.equals("0")){
					url = new URL("http://10.74.27.194:6069/api/?longi="+jd+"&lati="+wd+"&zoom="+jb+"&companyid="+companyid);
				}else if(type.equals("1")){
					url = new URL("http://10.74.27.194:6070/api/?longi="+jd+"&lati="+wd+"&zoom="+jb+"&companyid="+companyid);
				}
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1096.1 Safari/536.6");
				conn.setRequestProperty("content-type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				conn.setDoOutput(true); // 需要输出
				if (conn.getResponseCode() == 302) {
					System.out.println(302);
					return null;
				}
				if (conn.getResponseCode() == 200) {
					System.out.println(200);
				}
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "utf-8"));
				StringBuffer sb = new StringBuffer();
				String s = "";
				while ((s = rd.readLine()) != null) {
					sb.append(s);
				}
				// System.out.println(sb);
				if (sb.length() == 0) {
					sb.append("[]");
				}
				result = sb.toString();
				 System.out.println(result);
				rd.close();
				conn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}  
//			return jacksonUtil.toJson(jsonObject);
			return jacksonUtil.toJson(new DownloadAct().parseJSON2List2(result));
	}
	
	public String xqrdt(String jb,String companyid,String type){
		LogUtil.log("车辆动态监控的聚合模块", "车辆动态监控的聚合模块");
		int zoom = Integer.parseInt(jb);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		String time = simpleDateFormat.format(new Date());
		 List<Map<String, Object>> list = null;
		 if(type.equals("0")){
			 if(zoom >=17 && zoom <= 18 ){
				  return jacksonUtil.toJson("");
//			  }else if(zoom >=15 && zoom <= 16 ){
//				  if(companyid.equals("all")){
//					  list = dailySupervisionDao.getAllDot();
//				  }else{
//					  list = dailySupervisionDao.getDotByCompanyid(companyid);
//				  }
			  }else if(companyid.equals("all")){
				  list = dailySupervisionDao.getDotByCompany(zoom,"all",time);
			  }else{
				  list = dailySupervisionDao.getDotByCompany(zoom,companyid,time);
			  }
		 }else if(type.equals("1")){
			 if(zoom >=17 && zoom <= 18 ){
				  return jacksonUtil.toJson("");
//			  }else if(zoom >=15 && zoom <= 16 ){
//				  if(companyid.equals("all")){
//					  list = dailySupervisionDao.getAllDotForEBike();
//				  }else{
//					  list = dailySupervisionDao.getDotByCompanyidForEBike(companyid);
//				  }
			  }else if(companyid.equals("all")){
				  list = dailySupervisionDao.getDotByCompanyForEBike(zoom,"all",time);
			  }else{
				  list = dailySupervisionDao.getDotByCompanyForEBike(zoom,companyid,time);
			  }
		 }
		return jacksonUtil.toJson(list);
	}
	
	/**
	 * 运维人员查询
	 */
	public String findperson(String name){
		LogUtil.log("运维人员管理-查询接口", "运维人员管理-查询接口");
		return jacksonUtil.toJson(dailySupervisionDao.findPerson(name));
	}
	
	/**
	 * 区域监控
	 * @return
	 */
	public String qyjk() {
		LogUtil.log("区域监控", "区域监控");
		return jacksonUtil.toJson(dailySupervisionDao.qyjk());
	}
	
	/**
	 * 区域监控 查询区域车辆 信息	
	 * @param id
	 * @return
	 */
	public String findqyinfo(String id) {
		LogUtil.log("区域监控 查询区域车辆 信息", "区域监控 查询区域车辆 信息");
		String result = "";
			try {
				URL url = new URL("http://10.74.27.194:6069/area/?areaid="+id);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Content-type", "application/json");
				conn.setDoOutput(true); // 需要输出
				conn.connect();
				System.out.println(conn.getResponseCode());
				if (conn.getResponseCode() == 302) {
					System.out.println(302);
					return null;
				}
				if (conn.getResponseCode() == 200) {
					System.out.println(200);
				}
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "utf-8"));
				StringBuffer sb = new StringBuffer();
				String s = "";
				while ((s = rd.readLine()) != null) {
					sb.append(s);
				}
				// System.out.println(sb);
				if (sb.length() == 0) {
					sb.append("[]");
				}
				result = sb.toString();
				System.out.println(result);
				rd.close();
				conn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}  
			return result;
	}
	//查询一辆车
	public String onebike(String bike,String type){
		LogUtil.log("查询 某个单车","查询 某个单车");
		List<Map<String, Object>> list = null;
		if(type.equals("0")){
			 list = dailySupervisionDao.oneBike(bike);
	 	}else if(type.equals("1")){
	 		 list = dailySupervisionDao.oneBikeForEBike(bike);
	 	}
		List<Vehicle>vhic=new ArrayList<Vehicle>();
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
			Vehicle vehi=new Vehicle();
//			vehi.setCompname(list.get(i).get("CompanyName_JC")==null?"":list.get(i).get("CompanyName_JC").toString());
//			vehi.setVehino(list.get(i).get("BicycleNo")==null?"":list.get(i).get("BicycleNo").toString());
//			vehi.setCartype(list.get(i).get("State")==null?"":list.get(i).get("State").toString());//0已租 1未租
//			vehi.setDateTime(list.get(i).get("PositionTime")==null?"":list.get(i).get("PositionTime").toString());
//     	    vehi.setLongi(list.get(i).get("Longitude")==null?0:Double.parseDouble(list.get(i).get("Longitude").toString()));
//     	    vehi.setLati(list.get(i).get("Latitude")==null?0:Double.parseDouble(list.get(i).get("Latitude").toString()));
//     	    vehi.setCompid(list.get(i).get("CompanyId")==null?"":list.get(i).get("CompanyId").toString());
			vhic.add(vehi);
		}
		return jacksonUtil.toJson(vhic);
	}
	
	/**
     * 通知公告发布 →通知公告发布信息
//     * @param postData
     * @return
     */
    public String tzgglist(HttpServletRequest request,String begin,String end,String type,String bt){
    	LogUtil.log("通知公告发布 →通知公告发布信息","通知公告发布 →通知公告发布信息");
//        Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
        
        String tj = "";
        List <Object> queryList=new  ArrayList<Object>();
        if(!isNullOrEmpty(type) && !type.equals("-1")){
            tj += " and type =  ? ";
            queryList.add(type);
        }
        if(!isNullOrEmpty(bt)){
            tj += " and bt like ? ";
            queryList.add("%"+bt+"%");
        }
        if(!isNullOrEmpty(begin ) ){
            tj += " and FSSJ >= ? ";
            queryList.add(begin + " 00:00:00");
        }
        if(!isNullOrEmpty(end )){
            tj += " and FSSJ <= ? ";
            queryList.add(end + " 23:59:59");
        }
        String sql = "select ID,BT,NR,DATE_FORMAT(fssj,'%Y-%m-%d %H:%i:%s') as FSSJ,FJNAME,FJADDRESS,TYPE,LAST_EDIT,IS_DEL from tb_tzgg where IS_DEL = 0 "+tj;
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,queryList.toArray());
        Map<String,Object> resultMap  = new HashMap<String,Object>();
        resultMap.put("data",list);
        return jacksonUtil.toJson(resultMap);
    }
    
    /**
     * 通知公告 →添加通知公告信息
     * @param
     * @return
     */
    public String tzgg_insert(HttpServletRequest request,String nr,String bt,String user,String fjname,String fjaddress,String type,String inserttype){
       
    	String id = UUID.randomUUID().toString();
        LogUtil.log("通知公告 →添加通知公告信息", "insert into tb_tzgg(id,bt,nr,FJNAME,FJADDRESS,LAST_EDIT,TYPE,is_del,fssj,inserttype) values("+id+","+bt+","+nr+","+fjname+","+fjaddress+","+user+","+type+","+0+",now(),"+inserttype+")");
        
        String sql = "insert into tb_tzgg(id,bt,nr,FJNAME,FJADDRESS,LAST_EDIT,TYPE,is_del,fssj,inserttype) values(?,?,?,?,?,?,?,?,now(),?)";
        int count = jdbcTemplate.update(sql,id,bt,nr,fjname,fjaddress,user,type,0,inserttype);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(count >0){
            resultMap.put("msg","添加成功");
        }else{
            resultMap.put("msg","添加失败");
        }
        return jacksonUtil.toJson(resultMap);
    }
    
    /**
     * 删除通知公告
     * @param
     * @return
     */
    public String tzggDel(HttpServletRequest request,String id,String user){
       
        
        LogUtil.log("通知公告 →删除通知公告", "update tb_tzgg set IS_DEL = 1,LAST_EDIT = "+user+" where  id ="+id+"");
        
        String sql = "update tb_tzgg set IS_DEL = 1,LAST_EDIT = ? where  id =?";
        int count = jdbcTemplate.update(sql,user,id);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(count >0){
            resultMap.put("msg","删除成功");
        }else{
            resultMap.put("msg","删除失败");
        }
        return jacksonUtil.toJson(resultMap);
    }
    /**
     *  上传附件
     * @param
     * @return
     */
	@SuppressWarnings("unchecked")
	public String importfile(HttpServletRequest request,String type) {
        Map<String,Object> map = new HashMap<String,Object>();
        String file_path ="D:/tzgg"+File.separator;//文件存储路径    
        file_path = file_path.replace("\\","/");
        String upload_file_path="";
        File file =new File(file_path);
        if(!file.exists() && !file.isDirectory()){ //如果文件夹不存在则创建
            file.mkdir();
            upload_file_path=file_path;
        }else{
            upload_file_path=file_path;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();  // 设置工厂
        factory.setRepository(new File(file_path));// 设置文件存储位置
        factory.setSizeThreshold(2048 * 1024);// 设置大小，如果文件小于设置大小的话，放入内存中，如果大于的话则放入磁盘中
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("utf-8");// 这里就是中文文件名处理的代码，其实只有一行
        List<FileItem> list;
        try {
            list = upload.parseRequest(request);
            for (FileItem item : list) {
                String value = item.getName();
                String prefix = value.substring(value.lastIndexOf("."),value.length());
                String id= UUID.randomUUID().toString();
                String filenames = id+prefix;
                item.write(new File(upload_file_path, filenames));
                map.put("address",file_path+""+filenames);
                map.put("type",type);
                map.put("msg","OK");
            }
        } catch (Exception e) {//上传失败
            e.printStackTrace();
            map.put("msg","NO");
        }
        return jacksonUtil.toJson(map);
    }
	
	 /**
     * 通知公告 →添加通知公告信息
     * @param
     * @return
     */
    public String updateTzgg(HttpServletRequest request,String nr,String bt,String user,String fjname,String fjaddress,String type,String id){
        
        
        LogUtil.log("通知公告 →修改通知公告", "通知公告 →修改通知公告");
        
        String sql = "update tb_tzgg set bt = ? ,nr = ?,FJNAME = ?,FJADDRESS = ?,LAST_EDIT = ?,TYPE = ? where id = ?";
        int count = jdbcTemplate.update(sql,bt,nr,fjname,fjaddress,user,type,id);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(count >0){
            resultMap.put("msg","修改成功");
        }else{
            resultMap.put("msg","修改失败");
        }
        return jacksonUtil.toJson(resultMap);
    }
    //获取点位信息
    public String findNoHldmar(String jd,String wd,String jb,String companyid,String type,String time) {
    	LogUtil.log("车辆动态监控-详细车辆聚合模块", "车辆动态监控-详细车辆聚合模块");
		String result = "";
			try {
				URL url = null;
				if(type.equals("0")){
					url = new URL("http://10.74.27.194:6071/exp/?longi="+jd+"&lati="+wd+"&zoom="+jb+"&companyid="+companyid+"&type="+time);
				}else if(type.equals("1")){
					url = new URL("http://10.74.27.194:6072/exp/?longi="+jd+"&lati="+wd+"&zoom="+jb+"&companyid="+companyid+"&type="+time);
				}
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1096.1 Safari/536.6");
				conn.setRequestProperty("content-type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				conn.setDoOutput(true); // 需要输出
				if (conn.getResponseCode() == 302) {
					System.out.println(302);
					return null;
				}
				if (conn.getResponseCode() == 200) {
					System.out.println(200);
				}
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "utf-8"));
				StringBuffer sb = new StringBuffer();
				String s = "";
				while ((s = rd.readLine()) != null) {
					sb.append(s);
				}
				// System.out.println(sb);
				if (sb.length() == 0) {
					sb.append("[]");
				}
				result = sb.toString();
				 System.out.println(result);
				rd.close();
				conn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}  
//			return jacksonUtil.toJson(jsonObject);
			return jacksonUtil.toJson(new DownloadAct().parseJSON2List2(result));
	}
    
    public String xqrdtByNo(String jb,String companyid,String type,String time){
    	LogUtil.log("车辆动态监控-聚合模块", "车辆动态监控-聚合模块");
		int zoom = Integer.parseInt(jb);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		String now = simpleDateFormat.format(new Date());
		 List<Map<String, Object>> list = null;
		 if(time.equals("5")){
			 time = "0";
		 }else if(time.equals("10")){
			 time = "1";
		 }else if(time.equals("20")){
			 time = "3";
		 }
		 System.out.println(time);
		 if(type.equals("0")){
			 if(zoom >=17 && zoom <= 18 ){
				  return jacksonUtil.toJson("");
//			 }else if(zoom >=15 && zoom <= 16 ){
//				  if(companyid.equals("all")){
//					  list = dailySupervisionDao.getAllDot();
//				  }else{
//					  list = dailySupervisionDao.getDotByCompanyid(companyid);
//				  }
			  }else if(companyid.equals("all")){
				  list = dailySupervisionDao.getNoDotByCompany(zoom,"all",now,time);
			  }else{
				  list = dailySupervisionDao.getNoDotByCompany(zoom,companyid,now,time);
			  }
		 }else if(type.equals("1")){
			 if(zoom >=17 && zoom <= 18 ){
				  return jacksonUtil.toJson("");
//			 }else if(zoom >=15 && zoom <= 16 ){
//				  if(companyid.equals("all")){
//					  list = dailySupervisionDao.getAllDotForEBike();
//				  }else{
//					  list = dailySupervisionDao.getDotByCompanyidForEBike(companyid);
//				  }
			  }else if(companyid.equals("all")){
				  list = dailySupervisionDao.getNoDotByCompanyForEBike(zoom,"all",now,time);
			  }else{
				  list = dailySupervisionDao.getNoDotByCompanyForEBike(zoom,companyid,now,time);
			  }
		 }
		return jacksonUtil.toJson(list);
	}
  //查询一辆车
  	public String getZxc(String name){
  		LogUtil.log("公共自行车点位信息", "公共自行车点位信息");
  		List<Map<String, Object>> list = dailySupervisionDao.getZxc(name);
  		return jacksonUtil.toJson(list);
  	}
  	//运维人员统计
  	public String getYwryCount(){
  		LogUtil.log("运维人员统计", "运维人员统计");
  		List<Map<String, Object>> list = dailySupervisionDao.getYwryCount();
  		return jacksonUtil.toJson(list);
  	}
	/**
	 * 拖车
	 * @param company 公司名称
//	 * @param bikeno	车辆编号
	 * @param type  电单车 自行车
//	 * @param by	备案状态
	 * @param person  执法人员
	 * @param time  执法事件
	 * @param address  执法地址
	 * @param reason  执法原因
	 * @param photo 现场照片
	 * @return
	 */
	public String trailer(String company, String bicycleno, String type,
			String bytype, String person, String time, String address,
			String reason, String photo,String lon,String lat) {
		  Map<String,Object> map = new HashMap<String,Object>();
		  System.out.println(company+","+bicycleno+","+type+","+bytype+","+person+","+time+","+address+","+reason+","+photo);
		  StringBuilder sb=new StringBuilder(time);
		  sb.insert(10," ");
		  
		LogUtil.log("拖车插入", "INSERT INTO tb_bike_trailer (company,bicycleno,type,bytype,person,time,address,reason,photo,lon,lat) VALUES ("+company+","+bicycleno+","+type+","+bytype+","+person+","+sb+","+address+","+reason+","+photo+","+lon+","+lat+")");
		  
		String sql = "INSERT INTO tb_bike_trailer (company,bicycleno,type,bytype,person,time,address,reason,photo,lon,lat) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		int result = jdbcTemplate.update(sql,company,bicycleno,type,bytype,person,sb,address,reason,photo,lon,lat);
		if(result > 0 ){
			map.put("msg", "OK");
		}else{
			map.put("msg", "NO");
		}
		return jacksonUtil.toJson(map);
	}
}
