package mvc.service;

import helper.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: xianlehuang
 * @Description:
 * @date: 2019/9/2 - 15:23
 */
@Service
public class BasicDataService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

    //平台使用记录查询
    public String findPlatformUsageRecord(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String company=request.getParameter("company");
        String tj="";
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and h.login_time >=to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and h.login_time <=to_date('"+endTime+"  23:59:59','yyyy-MM-dd hh24:mi:ss')";
        }
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj +=" and u.real_name = '"+company+"'";
        }
//        //用户能管理的公司
//        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
//        comps=comps==""?"''":comps;
//        tj +=" and u.real_name in ("+comps+")";

        String sql = "select u.USER_NAME,u.REAL_NAME,to_char(h.login_time,'yyyy-MM-dd HH24:mi:ss') login_time from tb_subcenters_user u,TB_SUBCENTERS_LOGIN_HISTORY h "
                + " where u.id=h.user_id and LOGING_WAY='1' and ACCOUNT_SORT='0'";
        sql +=tj;
        sql +=" union all (select u.USERNAME USER_NAME,u.REAL_NAME,to_char(h.login_time,'yyyy-MM-dd HH24:mi:ss') login_time from tb_clz_user u,TB_SUBCENTERS_LOGIN_HISTORY h " +
                " where u.id=h.user_id and LOGING_WAY='2' and ACCOUNT_SORT='0'";
        sql +=tj;
        sql +=" ) order by login_time desc";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    //车辆组查询
    public String findVehicleGroup(HttpServletRequest request) {
        String group=request.getParameter("group");
        String tj="";
        if(group!=null&&!group.isEmpty()&&!group.equals("null")&&group.length()>0){
            tj +=" and g.group_name = '"+group+"'";
        }
        String id = String.valueOf(request.getSession().getAttribute("userid"));
        String sql="select g.group_name,g.group_id," +
                "(select count(*) from TB_TAXI_VEHICLE_GROUP v where v.group_id=g.group_id) count" +
                " from TB_TAXI_VEHICLE_GROUPNAME g where g.user_id='"+id+"'";
        sql += tj;
        sql += " order by nlssort(g.group_name,'NLS_SORT=SCHINESE_PINYIN_M')";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    //添加车辆组
    public String addVehicleGroup(HttpServletRequest request) {
        String group_name=request.getParameter("group_name");
        String vehicles=request.getParameter("vehicles");
        String userid = String.valueOf(request.getSession().getAttribute("userid"));
        String cx="select count(*) from TB_TAXI_VEHICLE_GROUPNAME where user_id='"+userid+"' and group_name='"+group_name+"'";
        if(jdbcTemplate.queryForObject(cx,Integer.class)>0){
            return jacksonUtil.toJson(-1);
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyMMddHHmmss");
        Date d=new Date();
        String id=sdf.format(d);
        int count = 0;
        String sql="insert into TB_TAXI_VEHICLE_GROUPNAME (group_id,group_name,user_id)" +
                "values ('"+id+"','"+group_name+"','"+userid+"')";
        count = jdbcTemplate.update(sql);
        if(count > 0){
            String[] arr=vehicles.split(",");
            for (int i = 0; i < arr.length; i++) {
                String insert="insert into TB_TAXI_VEHICLE_GROUP (group_id,group_vhic)" +
                        "values ('"+id+"','"+arr[i]+"')";
                jdbcTemplate.update(insert);
            }
        }
        return jacksonUtil.toJson(count);
    }

    //修改车辆组
    public String updateVehicleGroup(HttpServletRequest request) {
        String group_name=request.getParameter("group_name");
        String vehicles=request.getParameter("vehicles");
        String id=request.getParameter("id");
        String userid = String.valueOf(request.getSession().getAttribute("userid"));
        String cx="select count(*) from TB_TAXI_VEHICLE_GROUPNAME where user_id='"+userid+"' and group_name='"+group_name+"' and group_id !='"+id+"'";
        if(jdbcTemplate.queryForObject(cx,Integer.class)>0){
            return jacksonUtil.toJson(-1);
        }
        int count = 0;
        String sql="update TB_TAXI_VEHICLE_GROUPNAME set group_name='"+group_name+"' where group_id = '"+id+"'";
        count = jdbcTemplate.update(sql);
        if(count > 0){
            String delete = "delete from TB_TAXI_VEHICLE_GROUP where group_id='"+id+"'";
            jdbcTemplate.update(delete);
            String[] arr=vehicles.split(",");
            for (int i = 0; i < arr.length; i++) {
                String insert="insert into TB_TAXI_VEHICLE_GROUP (group_id,group_vhic)" +
                        "values ('"+id+"','"+arr[i]+"')";
                jdbcTemplate.update(insert);
            }
        }
        return jacksonUtil.toJson(count);
    }

    //删除车辆组
    public String deleteVehicleGroup(HttpServletRequest request) {
        String id = request.getParameter("id");
        int count=0;
        String sql="delete from TB_TAXI_VEHICLE_GROUPNAME where group_id='"+id+"'";
        count = jdbcTemplate.update(sql);
        String sql2="delete from TB_TAXI_VEHICLE_GROUP where group_id='"+id+"'";
        jdbcTemplate.update(sql2);
        return jacksonUtil.toJson(count);
    }

    //车辆组查询详情
    public String findVehicleGroupDetail(HttpServletRequest request) {
        String id=request.getParameter("id");
        String sql="select * from TB_TAXI_VEHICLE_GROUP g" +
                " left join VW_VEHICLE@db69 v on v.vehi_no=g.group_vhic " +
                " where g.group_id='"+id+"'" +
                " order by g.group_vhic desc";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    //用户管理查询
    public String findUserManage(HttpServletRequest request) {
        String filter=request.getParameter("filter");
        String tj="";
        if(filter!=null&&!filter.isEmpty()&&!filter.equals("null")&&filter.length()>0){
            tj +=" and (u.real_name like '%"+filter+"%' or s.station_name like '%"+filter+"%' or u.user_name like '%"+filter+"%' or u.date_view_type like '%"+filter+"%')";
        }
        String sql="select u.*,s.station_name,s.station_juri,s.station_admin from tb_subcenters_user u" +
                " left join tb_taxi_station s on u.station_id = s.id" +
                " where 1=1";
        sql += tj;
        sql += " order by nlssort(u.real_name,'NLS_SORT=SCHINESE_PINYIN_M')";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    //添加用户
    public String addUserManage(HttpServletRequest request) {
        String real_name = request.getParameter("real_name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String station = request.getParameter("station");
        String company = request.getParameter("company");
        String parent=request.getSession().getAttribute("parent")==null?"":String.valueOf(request.getSession().getAttribute("parent"));
        String cx="select count(*) from tb_subcenters_user where user_name='"+username+"'";
        if(jdbcTemplate.queryForObject(cx,Integer.class)>0){
            return jacksonUtil.toJson(-1);
        }
        String p = findparent(parent);
        int count=0;
        String sql="insert into tb_subcenters_user(user_name,password,date_view_type,real_name,station_id,parent)" +
                " values('"+username+"','"+password+"',?" +
                " ,'"+real_name+"','"+station+"','"+p+"')";
        count = jdbcTemplate.update(sql,company);
        return jacksonUtil.toJson(count);
    }

    //通过parent找到该用户创建了几个用户,
    //若为创建用户在新创建的用户parent为创建用户的parent+01,
    //若创建了用户在该用户创建的最大用户的parent+1;
    private String findparent(String parent) {
        String p = "";
        String sql = "select * from tb_subcenters_user where parent like '"+parent+"%' order by parent desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()==0){
            p = parent+"01";
        }else{
            p = (Integer.parseInt(list.get(0).get("PARENT")==null?"0":list.get(0).get("PARENT").toString())+1)+"";
        }
        return p;
    }

    //修改用户
    public String updateUserManage(HttpServletRequest request) {
        String real_name = request.getParameter("real_name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String station = request.getParameter("station");
        String company = request.getParameter("company");
        String id = request.getParameter("id");
        String cx="select count(*) from tb_subcenters_user where user_name='"+username+"' and id !='"+id+"'";
        if(jdbcTemplate.queryForObject(cx,Integer.class)>0){
            return jacksonUtil.toJson(-1);
        }
        int count=0;
        String sql="update tb_subcenters_user set real_name='"+real_name+"', user_name='"+username+"', password='"+password+"'" +
                ", date_view_type=?, station_id='"+station+
                "' where id='"+id+"'";
        count = jdbcTemplate.update(sql,company);
        return jacksonUtil.toJson(count);
    }

    //删除用户
    public String deleteUserManage(HttpServletRequest request) {
        String id = request.getParameter("id");
        int count=0;
        String sql="delete from tb_subcenters_user  where id='"+id+"'";
        count = jdbcTemplate.update(sql);
        return jacksonUtil.toJson(count);
    }

    //用户详情
    public String findUserManageDetail(HttpServletRequest request) {
        String id = request.getParameter("id");
        String sql="select * from tb_subcenters_user u,tb_taxi_station s where u.station_id = s.id and u.id='"+id+"'";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }
    //岗位管理查询
    public String findJobManage(HttpServletRequest request) {
        String station=request.getParameter("station");
        String tj="";
        if(station!=null&&!station.isEmpty()&&!station.equals("null")&&station.length()>0){
            tj +=" and station_name like '%"+station+"%'";
        }
        String sql="select * from tb_taxi_station where 1=1";
        sql += tj;
        sql += " order by nlssort(station_name,'NLS_SORT=SCHINESE_PINYIN_M')";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }
    //添加岗位
    public String addJobManage(HttpServletRequest request) {
        String station = request.getParameter("station");
        String power_ids = request.getParameter("power_ids");
        String power_names = request.getParameter("power_names");
        String cx="select count(*) from tb_taxi_station where STATION_NAME='"+station+"'";
        if(jdbcTemplate.queryForObject(cx,Integer.class)>0){
            return jacksonUtil.toJson(-1);
        }
        int count=0;
        String sql="insert into tb_taxi_station(STATION_NAME,STATION_JURI,POWER_IDS)" +
                " values('"+station+"','"+power_names+"','"+power_ids+"')";
        count = jdbcTemplate.update(sql);
        return jacksonUtil.toJson(count);
    }
    //修改岗位
    public String updateJobManage(HttpServletRequest request) {
        String station = request.getParameter("station");
        String power_ids = request.getParameter("power_ids");
        String power_names = request.getParameter("power_names");
        String id = request.getParameter("id");
        String cx="select count(*) from tb_taxi_station where STATION_NAME='"+station+"' and id !='"+id+"'";
        if(jdbcTemplate.queryForObject(cx,Integer.class)>0){
            return jacksonUtil.toJson(-1);
        }
        int count=0;
        String sql="update tb_taxi_station set STATION_NAME='"+station+"', STATION_JURI='"+power_names+"', POWER_IDS='"+power_ids+"'" +
                " where id='"+id+"'";
        count = jdbcTemplate.update(sql);
        return jacksonUtil.toJson(count);
    }
    //删除岗位
    public String deleteJobManage(HttpServletRequest request) {
        String id = request.getParameter("id");
        String cx="select count(*) from tb_subcenters_user where station_id='"+id+"'";
        if(jdbcTemplate.queryForObject(cx,Integer.class)>0){
            return jacksonUtil.toJson(-1);
        }
        int count=0;
        String sql="delete from tb_taxi_station where id='"+id+"'";
        count = jdbcTemplate.update(sql);
        return jacksonUtil.toJson(count);
    }
}
