package mvc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helper.JacksonUtil;
import helper.LogUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClqkService {
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

	//自行车运行热点分布,单车
	public String findrlt() {
		LogUtil.log("决策支持系统-自行车运行热点分布", "决策支持系统-自行车运行热点分布");
		String sql = "select * from tb_bike_status_realtime where positiontime > DATE_SUB(now(),INTERVAL 1 hour)";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
	}
	
	//自行车运行热点分布,电单车
	public String finderlt() {
		LogUtil.log("决策支持系统-自行车运行热点分布", "决策支持系统-自行车运行热点分布");
		String sql = "select * from tb_ebike_status_realtime where positiontime > DATE_SUB(now(),INTERVAL 1 hour)";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
	}

	//车辆od流向趋势,左侧列表,单车
    public String findod(String time) {
    	LogUtil.log("决策支持系统-车辆od流向趋势", "决策支持系统-车辆od流向趋势");
    	List <Object> queryList=new  ArrayList<Object>();
    	String sql = "select OrientID,a.zhongxin,a.area_coordinates,a.area_name from (select DISTINCT OrientID from tb_bike_odgraph where 1 =1";
    	if(time!=null&&!time.isEmpty()&&time.length()>0&&!time.equals("")){
    		sql += " and dbtime=?";
    		queryList.add(time);
    	}
    	sql += ") o,tb_area_min a where o.OrientID=a.area_id LIMIT 0,10";
    	List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,queryList.toArray());
        return jacksonUtil.toJson(list);
    }
    
    //车辆od流向趋势,左侧列表,电单车
    public String findeod(String time) {
    	LogUtil.log("决策支持系统-车辆od流向趋势", "决策支持系统-车辆od流向趋势");
    	List <Object> queryList=new  ArrayList<Object>();
    	String sql = "select OrientID,a.zhongxin,a.area_coordinates,a.area_name from (select DISTINCT OrientID from tb_ebike_odgraph where 1 =1";
    	if(time!=null&&!time.isEmpty()&&time.length()>0&&!time.equals("")){
    		sql += " and dbtime=?";
    		queryList.add(time);
    	}
    	sql += ") o,tb_area_min a where o.OrientID=a.area_id LIMIT 0,10";
    	List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,queryList.toArray());
        return jacksonUtil.toJson(list);
	}

    //地图信息,单车
	public String odgraph(String time,String id){
		LogUtil.log("决策支持系统-车辆od流向趋势", "决策支持系统-车辆od流向趋势");
		List <Object> queryList=new  ArrayList<Object>();
		String sql = "select o.OrientID,o.DestID,o.Count,a.zhongxin,a.area_coordinates,a.area_name from tb_bike_odgraph o,tb_area_min a where o.DestID=a.area_id";
		if(time!=null&&!time.isEmpty()&&time.length()>0&&!time.equals("")){
    		sql += " and dbtime=?";
    		queryList.add(time);
    	}
		if(id!=null&&!id.isEmpty()&&id.length()>0&&!id.equals("")){
    		sql += " and OrientID=?";
    		queryList.add(id);
    	}
		sql += " LIMIT 0,10";		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,queryList.toArray());
		return jacksonUtil.toJson(list);
	}

	//地图信息,电单车
	public String odegraph(String time, String id) {
		LogUtil.log("决策支持系统-车辆od流向趋势", "决策支持系统-车辆od流向趋势");
		List <Object> queryList=new  ArrayList<Object>();
		String sql = "select o.OrientID,o.DestID,o.Count,a.zhongxin,a.area_coordinates,a.area_name from tb_ebike_odgraph o,tb_area_min a where o.DestID=a.area_id";
		if(time!=null&&!time.isEmpty()&&time.length()>0&&!time.equals("")){
    		sql += " and dbtime=?";
    		queryList.add(time);
    	}
		if(id!=null&&!id.isEmpty()&&id.length()>0&&!id.equals("")){
    		sql += " and OrientID=?";
    		queryList.add(id);
    	}
		sql += " LIMIT 0,10";		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,queryList.toArray());
		return jacksonUtil.toJson(list);
	}

}
