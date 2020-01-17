package mvc.service;

import helper.JacksonUtil;
import helper.LogUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Service
public class AreaService {
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

    //停车区域管理
    public String findArea(String type) {
    	LogUtil.log("信息查询系统-区域管理-停车区域管理", "信息查询系统-区域管理-停车区域管理");
        String str = "select * from tb_bike_area where area_type = ? order by AREA_TIME desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(str,type);
        return jacksonUtil.toJson(list);
    }

    //增加区域
    public int addArea(HttpServletRequest request) {
    	String areaName = request.getParameter("areaName");
    	String areaDescribe = request.getParameter("areaDescribe");
    	String areaSize = request.getParameter("areaSize");
    	String areaCoordinates = request.getParameter("areaCoordinates");
    	
    	String[] split = areaCoordinates.split(";");
    	
    	areaCoordinates += ";" + split[0];
    	
    	String areaMaxNum = request.getParameter("areaMaxNum");
    	String type = request.getParameter("type");
    	LogUtil.log("信息查询系统-区域管理-增加区域", "insert into tb_bike_area (area_name,AREA_DESCRIBE,AREA_COORDINATES,AREA_MAX_NUM,AREA_SIZE,AREA_TYPE) values ("+areaName+","+areaDescribe+","+areaCoordinates+","+areaMaxNum+","+areaSize+","+type+")");
        String str = "insert into tb_bike_area (area_name,AREA_DESCRIBE,AREA_COORDINATES,AREA_MAX_NUM,AREA_SIZE,AREA_TYPE) values (?,?,?,?,?,?)";
        int count = jdbcTemplate.update(str,areaName,areaDescribe,areaCoordinates,areaMaxNum,areaSize,type);
        return count;
    }

    //修改区域
    public int editArea(String areaName,String areaDescribe,String areaMaxNum,String areaId) {  	
    	LogUtil.log("信息查询系统-区域管理-停车区域管理", "update tb_bike_area set area_name="+areaName+",AREA_DESCRIBE="+areaDescribe+",AREA_MAX_NUM="+areaMaxNum+" where area_id="+areaId+"");
        String str = "update tb_bike_area set area_name=?,AREA_DESCRIBE=?,AREA_MAX_NUM=? where area_id=?";
        int count = jdbcTemplate.update(str,areaName,areaDescribe,areaMaxNum,areaId);
        return count;
    }

    //删除区域
    public int delArea(String areaId) {
    	LogUtil.log("信息查询系统-区域管理-删除区域", "delete from tb_bike_area where area_id="+areaId+"");
        String str = "delete from tb_bike_area where area_id=?";
        int count = jdbcTemplate.update(str,areaId);
        return count;
    }
}
