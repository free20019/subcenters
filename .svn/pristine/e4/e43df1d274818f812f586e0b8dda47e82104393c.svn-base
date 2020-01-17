package mvc.service;

import helper.JacksonUtil;
import helper.LogUtil;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class EnterpriseManagementSystemService {
	@Autowired
    protected JdbcTemplate jdbcTemplate;
    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

	public String regionWarning() {
		LogUtil.log("企业管理系统-区域预警", "企业管理系统-区域预警");
		String sql = "select * from (select *, area_num - area_max_num as num from tb_bike_area ORDER BY num DESC)tt";// 展示所有区域
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	
	public String getPointForRegionWarning() {
		String sql = "select * from (select *, area_num - area_max_num as num from tb_bike_area ORDER BY num DESC)tt";// 展示所有区域
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		Map<String,Object> map = new HashMap<String,Object>();
		for(int i=0;i<list.size();i++){
			String areaId = String.valueOf(list.get(i).get("area_id"));
			String result = findqyinfo(areaId);
			List<Map<String, Object>> list1 = (List<Map<String, Object>>) JSON.parse(result);
			map.put(String.valueOf(i), list1);
		}
		return jacksonUtil.toJson(map);
	}

	public String dataWarning(String companyId,String time) {
		LogUtil.log("企业管理系统-数据接口预警", "企业管理系统-数据接口预警");
		// 传进来的时间格式为2019-04-04 15:00:00
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(time == null){
			time = format.format(new Date()).substring(0, 10);
		}
		String currentTime = format.format(new Date()).substring(0,14) + "00:00";
		
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> map1 = new HashMap<String,Object>();
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		
		if(companyId!=null&&!"null".equals(companyId)&&!"".equals(companyId)){
			String sql = "select * from tb_monitor where Companyid = ? and DBTime = ?";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,companyId,currentTime);
			if(0==list.size()){
				if(companyId.equals("ofo")){
					map1.put("BICYCLE_PLATFORM", "ofo");
					map1.put("INTERFACE_ADDRESS", "183.134.253.93:6001/service/pb-bike");
					map1.put("DATA_SOURCES", "101.201.34.153");
					map1.put("INTERFACE_STATUS", "异常");
					map1.put("INTERFACE_DESCRIPTION", "OFO数据接入");
					map1.put("TREATMENT_STATUS", "未处理");
					list1.add(map1);
				}else if(companyId.equals("hellobike")){
					map1.put("BICYCLE_PLATFORM", "hellobike");
					map1.put("INTERFACE_ADDRESS", "183.134.253.93:6002/service/pb-ddc");
					map1.put("DATA_SOURCES", "121.196.218.21");
					map1.put("INTERFACE_STATUS", "异常");
					map1.put("INTERFACE_DESCRIPTION", "哈啰数据接入");
					map1.put("TREATMENT_STATUS", "未处理");
					list1.add(map1);
				}else if(companyId.equals("mb")){
					map1.put("BICYCLE_PLATFORM", "mb");
					map1.put("INTERFACE_ADDRESS", "183.134.253.93:6002/service/pb-ddc");
					map1.put("DATA_SOURCES", "123.206.8.53");
					map1.put("INTERFACE_STATUS", "异常");
					map1.put("INTERFACE_DESCRIPTION", "摩拜数据接入");
					map1.put("TREATMENT_STATUS", "未处理");
					list1.add(map1);
				}else if(companyId.equals("xiaoliu")){
					map1.put("BICYCLE_PLATFORM", "xiaoliu");
					map1.put("INTERFACE_ADDRESS", "183.134.253.93:6002/service/pb-ddc");
					map1.put("DATA_SOURCES", "101.37.147.117");
					map1.put("INTERFACE_STATUS", "异常");
					map1.put("INTERFACE_DESCRIPTION", "小遛数据接入");
					map1.put("TREATMENT_STATUS", "未处理");
					list1.add(map1);
				}else if(companyId.equals("jindy")){
					map1.put("BICYCLE_PLATFORM", "jindy");
					map1.put("INTERFACE_ADDRESS", "183.134.253.93:6002/service/pb-ddc");
					map1.put("DATA_SOURCES", "106.15.59.105");
					map1.put("INTERFACE_STATUS", "异常");
					map1.put("INTERFACE_DESCRIPTION", "筋斗云数据接入");
					map1.put("TREATMENT_STATUS", "未处理");
					list1.add(map1);
				}else if(companyId.equals("lq")){
					map1.put("BICYCLE_PLATFORM", "lq");
					map1.put("INTERFACE_ADDRESS", "183.134.253.93:6001/service/pb-bike");
					map1.put("DATA_SOURCES", "116.62.135.244");
					map1.put("INTERFACE_STATUS", "异常");
					map1.put("INTERFACE_DESCRIPTION", "乐骑数据接入");
					map1.put("TREATMENT_STATUS", "未处理");
					list1.add(map1);
				}
			}
			if(list!=null && !list.isEmpty()){
				int dataCount = Integer.parseInt(String.valueOf(list.get(0).get("DataCount")));
				String BICYCLE_PLATFORM = "";// 单车平台
				String INTERFACE_ADDRESS = "";// 接口地址
				String DATA_SOURCES = "";// 来源
				String INTERFACE_STATUS = "";// 接口状态
				String INTERFACE_DESCRIPTION = "";// 接口描述
				String TREATMENT_STATUS = "";// 处理状态
				if(dataCount>0){
					INTERFACE_STATUS = "正常";
					TREATMENT_STATUS = "已处理";
				}else{
					INTERFACE_STATUS = "异常";
					TREATMENT_STATUS = "未处理";
				}
				if("ofo".equals(companyId)){
					BICYCLE_PLATFORM = "ofo";
					INTERFACE_ADDRESS = "183.134.253.93:6001/service/pb-bike";
					DATA_SOURCES = "101.201.34.153";
					INTERFACE_DESCRIPTION = "OFO数据接入";
				}else if("hellobike".equals(companyId)){
					BICYCLE_PLATFORM = "hellobike";
					INTERFACE_ADDRESS = "183.134.253.93:6002/service/pb-ddc";
					DATA_SOURCES = "121.196.218.21";
					INTERFACE_DESCRIPTION = "哈啰数据接入";
				}else if("mb".equals(companyId)){
					BICYCLE_PLATFORM = "mb";
					INTERFACE_ADDRESS = "183.134.253.93:6001/service/pb-bike";
					DATA_SOURCES = "123.206.8.53";
					INTERFACE_DESCRIPTION = "摩拜数据接入";
				}else if("xiaoliu".equals(companyId)){
					BICYCLE_PLATFORM = "xiaoliu";
					INTERFACE_ADDRESS = "183.134.253.93:6002/service/pb-ddc";
					DATA_SOURCES = "101.37.147.117";
					INTERFACE_DESCRIPTION = "小遛数据接入";
				}else if("jindy".equals(companyId)){
					BICYCLE_PLATFORM = "jindy";
					INTERFACE_ADDRESS = "183.134.253.93:6002/service/pb-ddc";
					DATA_SOURCES = "106.15.59.105";
					INTERFACE_DESCRIPTION = "筋斗云数据接入";
				}else if("lq".equals(companyId)){
					BICYCLE_PLATFORM = "lq";
					INTERFACE_ADDRESS = "183.134.253.93:6001/service/pb-bike";
					DATA_SOURCES = "116.62.135.244";
					INTERFACE_DESCRIPTION = "乐骑数据接入";
				}
				map1.put("BICYCLE_PLATFORM", BICYCLE_PLATFORM);
				map1.put("INTERFACE_ADDRESS", INTERFACE_ADDRESS);
				map1.put("DATA_SOURCES", DATA_SOURCES);
				map1.put("INTERFACE_STATUS", INTERFACE_STATUS);
				map1.put("INTERFACE_DESCRIPTION", INTERFACE_DESCRIPTION);
				map1.put("TREATMENT_STATUS", TREATMENT_STATUS);
				list1.add(map1);
			}
			String echartsSql = "select CompanyId,DataCount,CAST(SUBSTR(DBTime,12,2) AS SIGNED) DBTime from tb_monitor where Companyid = ? and DATE_FORMAT(DBTime,'%Y-%m-%d') = ? GROUP BY DBTime,CompanyId,DataCount";
			List<Map<String, Object>> echartsList = jdbcTemplate.queryForList(echartsSql,companyId,time);
			map.put("list", list1);
			map.put("echartsList", echartsList);
			
		}
		else{
			String sql = "select * from tb_monitor where  DBTime = ?";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,currentTime);
			String BICYCLE_PLATFORM = "";// 单车平台
			String INTERFACE_ADDRESS = "";// 接口地址
			String DATA_SOURCES = "";// 来源
			String INTERFACE_STATUS = "";// 接口状态
			String INTERFACE_DESCRIPTION = "";// 接口描述
			String TREATMENT_STATUS = "";// 处理状态
			if(0 == list.size()){
				Map<String,Object> map2 = new HashMap<String,Object>();
				map2.put("BICYCLE_PLATFORM", "ofo");
				map2.put("INTERFACE_ADDRESS", "183.134.253.93:6001/service/pb-bike");
				map2.put("DATA_SOURCES", "101.201.34.153");
				map2.put("INTERFACE_STATUS", "异常");
				map2.put("INTERFACE_DESCRIPTION", "OFO数据接入");
				map2.put("TREATMENT_STATUS", "未处理");
				list1.add(map2);
				
				Map<String,Object> map3 = new HashMap<String,Object>();
				map3.put("BICYCLE_PLATFORM", "hellobike");
				map3.put("INTERFACE_ADDRESS", "183.134.253.93:6002/service/pb-ddc");
				map3.put("DATA_SOURCES", "121.196.218.21");
				map3.put("INTERFACE_STATUS", "异常");
				map3.put("INTERFACE_DESCRIPTION", "哈啰数据接入");
				map3.put("TREATMENT_STATUS", "未处理");
				list1.add(map3);
				
				Map<String,Object> map4 = new HashMap<String,Object>();
				map4.put("BICYCLE_PLATFORM", "mb");
				map4.put("INTERFACE_ADDRESS", "183.134.253.93:6002/service/pb-ddc");
				map4.put("DATA_SOURCES", "123.206.8.53");
				map4.put("INTERFACE_STATUS", "异常");
				map4.put("INTERFACE_DESCRIPTION", "摩拜数据接入");
				map4.put("TREATMENT_STATUS", "未处理");
				list1.add(map4);
				
				Map<String,Object> map5 = new HashMap<String,Object>();
				map5.put("BICYCLE_PLATFORM", "xiaoliu");
				map5.put("INTERFACE_ADDRESS", "183.134.253.93:6002/service/pb-ddc");
				map5.put("DATA_SOURCES", "101.37.147.117");
				map5.put("INTERFACE_STATUS", "异常");
				map5.put("INTERFACE_DESCRIPTION", "小遛数据接入");
				map5.put("TREATMENT_STATUS", "未处理");
				list1.add(map5);
				
				Map<String,Object> map6 = new HashMap<String,Object>();
				map6.put("BICYCLE_PLATFORM", "jindy");
				map6.put("INTERFACE_ADDRESS", "183.134.253.93:6002/service/pb-ddc");
				map6.put("DATA_SOURCES", "106.15.59.105");
				map6.put("INTERFACE_STATUS", "异常");
				map6.put("INTERFACE_DESCRIPTION", "筋斗云数据接入");
				map6.put("TREATMENT_STATUS", "未处理");
				list1.add(map6);
				
				Map<String,Object> map7 = new HashMap<String,Object>();
				map7.put("BICYCLE_PLATFORM", "lq");
				map7.put("INTERFACE_ADDRESS", "183.134.253.93:6001/service/pb-bike");
				map7.put("DATA_SOURCES", "116.62.135.244");
				map7.put("INTERFACE_STATUS", "异常");
				map7.put("INTERFACE_DESCRIPTION", "乐骑数据接入");
				map7.put("TREATMENT_STATUS", "未处理");
				list1.add(map7);
			}
			for(int i=0;i<list.size();i++){
				int dataCount = Integer.parseInt(String.valueOf(list.get(i).get("DataCount")));
				companyId = String.valueOf(list.get(i).get("CompanyId"));
				if(dataCount>0){
					INTERFACE_STATUS = "正常";
					TREATMENT_STATUS = "已处理";
				}else{
					INTERFACE_STATUS = "异常";
					TREATMENT_STATUS = "未处理";
				}
				if("ofo".equals(companyId)){
					BICYCLE_PLATFORM = "ofo";
					INTERFACE_ADDRESS = "183.134.253.93:6001/service/pb-bike";
					DATA_SOURCES = "101.201.34.153";
					INTERFACE_DESCRIPTION = "OFO数据接入";
				}else if("hellobike".equals(companyId)){
					BICYCLE_PLATFORM = "hellobike";
					INTERFACE_ADDRESS = "183.134.253.93:6002/service/pb-ddc";
					DATA_SOURCES = "121.196.218.21";
					INTERFACE_DESCRIPTION = "哈啰数据接入";
				}else if("mb".equals(companyId)){
					BICYCLE_PLATFORM = "mb";
					INTERFACE_ADDRESS = "183.134.253.93:6001/service/pb-bike";
					DATA_SOURCES = "123.206.8.53";
					INTERFACE_DESCRIPTION = "摩拜数据接入";
				}else if("xiaoliu".equals(companyId)){
					BICYCLE_PLATFORM = "xiaoliu";
					INTERFACE_ADDRESS = "183.134.253.93:6002/service/pb-ddc";
					DATA_SOURCES = "101.37.147.117";
					INTERFACE_DESCRIPTION = "小遛数据接入";
				}else if("jindy".equals(companyId)){
					BICYCLE_PLATFORM = "jindy";
					INTERFACE_ADDRESS = "183.134.253.93:6002/service/pb-ddc";
					DATA_SOURCES = "106.15.59.105";
					INTERFACE_DESCRIPTION = "筋斗云数据接入";
				}else if("lq".equals(companyId)){
					BICYCLE_PLATFORM = "lq";
					INTERFACE_ADDRESS = "183.134.253.93:6001/service/pb-bike";
					DATA_SOURCES = "116.62.135.244";
					INTERFACE_DESCRIPTION = "乐骑数据接入";
				}
				Map<String,Object> map2 = new HashMap<String,Object>();
				map2.put("BICYCLE_PLATFORM", BICYCLE_PLATFORM);
				map2.put("INTERFACE_ADDRESS", INTERFACE_ADDRESS);
				map2.put("DATA_SOURCES", DATA_SOURCES);
				map2.put("INTERFACE_STATUS", INTERFACE_STATUS);
				map2.put("INTERFACE_DESCRIPTION", INTERFACE_DESCRIPTION);
				map2.put("TREATMENT_STATUS", TREATMENT_STATUS);
				list1.add(map2);
			}
			String echartsSql = "select CompanyId,DataCount,CAST(SUBSTR(DBTime,12,2) AS SIGNED) DBTime from tb_monitor where DATE_FORMAT(DBTime,'%Y-%m-%d') = ? GROUP BY DBTime,CompanyId,DataCount";
			List<Map<String, Object>> echartsList = jdbcTemplate.queryForList(echartsSql,time);// 这里的数据交由前端处理
			map.put("list", list1);
			map.put("echartsList", echartsList);
		}
		return jacksonUtil.toJson(map);
	}

	public String noticeAnnouncement(String time) {
		LogUtil.log("企业管理系统-通知公告", "企业管理系统-通知公告");
		List <Object> queryList=new  ArrayList<Object>();
		String sql = "select *,DATE_FORMAT(FSSJ,'%Y-%m-%d %H:%i:%s') TIME from tb_tzgg where IS_DEL = 0";
		if(time!=null&&time.length()>0&&!time.isEmpty()&&!"".equals(time)){
			sql += " and DATE_FORMAT(FSSJ,'%Y-%m-%d') = ?";
			queryList.add(time);
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,queryList.toArray());
		
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
		
		for(int i=0;i<list.size();i++){
			String type = String.valueOf(list.get(i).get("TYPE"));
			if(type.equals("0")){
				list1.add(list.get(i));
			}else if(type.equals("1")){
				list2.add(list.get(i));
			}else{
				list3.add(list.get(i));
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("noticeAnnouncementList", list1);// 通知公告
		map.put("lawsList", list2);// 法律法规
		map.put("rewardPunishmentList", list3);// 奖励惩罚
		return jacksonUtil.toJson(map);
	}

	public void getPhotoUrl(HttpServletRequest request, HttpServletResponse response, String dz) {
		try {
			File pf = new File(dz);
			if (!pf.exists()) {
				return;
			}
			double rate = 1;
			int[] results = getImgWidth(pf);
			int widthdist = 0;
			int heightdist = 0;
			if (results == null || results[0] == 0 || results[1] == 0) {
				return;
			} else {
				widthdist = (int) (results[0] * rate);
				heightdist = (int) (results[1] * rate);
			}
			Image src = javax.imageio.ImageIO.read(pf);
			//BufferedImage 操作 图片的大小
			BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist,
					BufferedImage.TYPE_INT_RGB);

			tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0,
					null);
			ServletOutputStream fout = response.getOutputStream();
			ImageIO.write(tag, "jpg", fout);
			fout.close();
	} catch (Exception e) {
	}

	}
	
	public static int[] getImgWidth(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int result[] = { 0, 0 };
		try {
			is = new FileInputStream(file);
			src = javax.imageio.ImageIO.read(is);
			result[0] = src.getWidth(null); // 得到源图宽
			result[1] = src.getHeight(null); // 得到源图高
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String findqyinfo(String id) {
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

	

}
