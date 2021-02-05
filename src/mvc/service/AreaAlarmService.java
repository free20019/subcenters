package mvc.service;

import helper.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: xianlehuang
 * @Description:
 * @date: 2021/1/18 - 15:45
 */
@Service
public class AreaAlarmService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private CommonServer commonServer;

    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

    //区域设置查询
    public String findAreaSetting(HttpServletRequest request) {
        try{
            jdbcTemplate.queryForList("drop table aaa");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            jdbcTemplate.queryForList("create table aaa as select * from  tb_user_area_fzx@db69");
        }catch (Exception e){
            e.printStackTrace();
        }
        String sql="select a.* from aaa a"
                + " where 1=1";
        sql += " order by a.DBTIME desc";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list !=null){
            for (int i = 0; i < list.size(); i++) {
                list.get(i).put("DBTIME",list.get(i).get("DBTIME")==null?"":list.get(i).get("DBTIME").toString().substring(0,19));
            }
        }
        return jacksonUtil.toJson(list);
    }

    //添加区域设置
    public String addAreaSetting(HttpServletRequest request) {
        String name=request.getParameter("name");
        String area_size=request.getParameter("area_size");
        String speed=request.getParameter("speed");
        String telephone=request.getParameter("telephone");
        String cars=request.getParameter("cars");
        String area=request.getParameter("area");
        String out_in=request.getParameter("out_in");
//        String describr=request.getParameter("describr");
//        String people=request.getParameter("people");
//        String dbtime=request.getParameter("dbtime");

        String userid = String.valueOf(request.getSession().getAttribute("userid"));
        int count=0;
        String sql="insert into tb_user_area_fzx@db69 (name,area_size,speed,telephone,cars,area,out_in,userid)" +
                "values ('"+name+"','"+area_size+"','"+speed+"','"+telephone+"','"+cars+"','"+area+"','"+out_in+"','"+userid+"')";
        try {
            count = jdbcTemplate.update(sql);
        }catch (Exception e){
            count=0;
        }
        return jacksonUtil.toJson(count);

    }

    //修改区域设置
    public String updateAreaSetting(HttpServletRequest request) {
        String name=request.getParameter("name");
        String area_size=request.getParameter("area_size");
        String speed=request.getParameter("speed");
        String telephone=request.getParameter("telephone");
        String cars=request.getParameter("cars");
        String area=request.getParameter("area");
        String out_in=request.getParameter("out_in");
//        String describr=request.getParameter("describr");
//        String people=request.getParameter("people");
//        String dbtime=request.getParameter("dbtime");
        String id=request.getParameter("id");
        String userid = String.valueOf(request.getSession().getAttribute("userid"));
        String sql="update tb_user_area_fzx@db69 set" +
                " name='"+name+"'," +
                " area_size='"+area_size+"'," +
                " speed='"+speed+"'," +
                " telephone='"+telephone+"'," +
                " cars='"+cars+"'," +
                " area='"+area+"'," +
                " out_in='"+out_in+"'," +
                " userid='"+userid+"'" +
                " where id='"+id+"'";
        int count=0;
        try {
            count = jdbcTemplate.update(sql);
        }catch (Exception e){
            count=0;
        }
        return jacksonUtil.toJson(count);
    }

    //删除区域设置
    public String deleteAreaSetting(HttpServletRequest request) {
        String id = request.getParameter("id");
        int count=0;
        String sql="delete from tb_user_area_fzx@db69 where id='"+id+"'";
        try {
            count = jdbcTemplate.update(sql);
        }catch (Exception e){
            count=0;
        }
        return jacksonUtil.toJson(count);
    }


    public String findMessageAlarm(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String vehicle=request.getParameter("vehicle");
        String company=request.getParameter("company");
//        String msg_status=request.getParameter("msg_status");
        String alarmtype=request.getParameter("alarmtype");
        Integer currentPage=Integer.parseInt(request.getParameter("currentPage"));
        Integer pageSize=Integer.parseInt(request.getParameter("pageSize"));

        String table = startTime.substring(2, 4) + startTime.substring(5, 7);
        String tj="";
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and a.stime >=to_date('"+startTime+"','yyyy-MM-dd hh24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and a.stime <=to_date('"+endTime+"','yyyy-MM-dd hh24:mi:ss')";
        }
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj +=" and a.COMP_NAME = '"+company+"'";
        }
        if(vehicle!=null&&!vehicle.isEmpty()&&!vehicle.equals("null")&&vehicle.length()>0){
            tj +=" and a.vehi_num='"+vehicle+"'";
        }
        if(alarmtype!=null&&!alarmtype.isEmpty()&&!alarmtype.equals("null")&&alarmtype.length()>0){
            tj +=" and a.alarmtype like '%"+alarmtype+"%'";
        }
        //用户能管理的公司
        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
        comps=comps==""?"''":comps;
        //用户管理的車輛
        String vehicles=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic").toString();
        tj +=" and (1=0 or a.COMP_NAME in ("+comps+") "+Transformation(new ArrayList<String>(Arrays.asList(vehicles.split(",")))," a.vehi_num")+" )";

        String sql = " select (select count(1) from (select a.* from tb_msg_alarm_fzx_"+table+"@db69 a" +
//                " left join tb_vehicle@db69 v on v.vehi_no=a.vehi_num" +
                " where 1=1";
        sql += tj;
        sql += ") ) as COUNT, tt.* from (select t.*,rownum as rn from (select  a.* from tb_msg_alarm_fzx_"+table+"@db69 a" +
//                " left join tb_vehicle@db69 v on v.vehi_no=a.vehi_num" +
                " where 1=1";
        sql += tj;
        sql += " order by a.stime desc) t  where rownum <= "+(currentPage*pageSize);
        sql += ") tt where tt.rn >"+((currentPage-1)*pageSize);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).put("STIME",list.get(i).get("STIME")==null?"":list.get(i).get("STIME").toString().substring(0,19));
            list.get(i).put("MSG_SEND_TIME",list.get(i).get("MSG_SEND_TIME")==null?"":list.get(i).get("MSG_SEND_TIME").toString().substring(0,19));
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(list!=null&&list.size()>0){
            map.put("count",list.get(0).get("COUNT"));
        }else{
            map.put("count",0);
        }
        map.put("datas",list);
        return jacksonUtil.toJson(map);
    }


    public String Transformation(ArrayList<String> list,String field){
        String loginType = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("loginType")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("loginType").toString();
        String vehicleGroupId = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getParameter("vehicleGroupId")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getParameter("vehicleGroupId").toString();
        //判断登录类型
        if("1".equals(loginType)){
            //判断查询条件是否有车辆组id，有：获取对应车辆组id的车辆，没有：获取改用户下的全部车辆
            if(vehicleGroupId!=null&&!vehicleGroupId.isEmpty()&&!vehicleGroupId.equals("null")){
                list = new ArrayList<String>(Arrays.asList(commonServer.getGroupVehicles(vehicleGroupId).split(",")));
            }
            if(list.size()==1&&"".equals(list.get(0))){
                return " or "+field + "in ('') ";
            }else{
                int init = 900;// 每隔100条循环一次
                int total = list.size();
                int cycelTotal = total / init;
                if (total % init != 0) {
                    cycelTotal += 1;
                    if (total < init) {
                        init = list.size();
                    }
                }
                ArrayList<String> cphm = new ArrayList<String>();
                ArrayList<String> list2 = new ArrayList<String>();
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
                    cphm.add("'"+String.join( "','",list2)+"'");
                    list.removeAll(list2);//移出已经保存过的数据
                    list2.clear();//移出当前保存的数据
                }
                String tj = " or ( ";
                if(cphm.size() ==1){
                    tj += field+" in ("+cphm.get(0)+") ";
                }else{
                    for (int i = 0; i < cphm.size(); i++) {
                        if(i == 0){
                            tj += field +" in ("+cphm.get(i)+") ";
                        }else{
                            tj += " or "+field+" in ("+cphm.get(i)+") ";
                        }
                    }
                }
                tj += " ) ";
                return tj;
            }
        }else{
            return "";
        }
    }

    public String getCompanyVehicleTree(HttpServletRequest request) {
        //用户能管理的公司
        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
        comps=comps==""?"''":comps;
        //用户管理的車輛
        String vehicles=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic").toString();
        String tj =" and (1=0 or v.COMP_NAME in ("+comps+") "+Transformation(new ArrayList<String>(Arrays.asList(vehicles.split(",")))," v.vehi_no")+" )";

        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        String sql = "select b.ba_id,b.ba_name,nvl(a.c,0) c from"
                + " ( select b.ba_id,b.ba_name from TB_BUSI_AREA@db69 b, tb_company@db69 c, vw_vehicle@db69 v where b.ba_id = c.ba_id and b.ba_id = v.ba_id ";
        sql += tj;
        sql += " group by b.ba_id,b.ba_name ) b left join"
                + " (select t.ba_id,count(1) c from TB_BUSI_AREA@db69 t,vw_vehicle@db69 v where t.ba_id = v.ba_id";
        sql += tj;
        sql += "group by t.ba_id) a on a.ba_id = b.ba_id order by ba_name";
        System.out.println("compba:"+sql);
        List<Map<String, Object>> area = jdbcTemplate.queryForList(sql);

        String com = "select b.comp_id,b.comp_name,nvl(a.c,0) c,b.ba_id from tb_company@db69 b left join"
                + " (select t.comp_id,count(1) c from tb_company@db69 t,vw_vehicle@db69 v where t.comp_id = v.comp_id";
        com  += tj;
        com += "  group by t.comp_id) a on a.comp_id = b.comp_id" +
                " where  a.comp_id is not null" +
                " order by comp_name";
        System.out.println("compcomp:"+com);
        List<Map<String, Object>> comp = jdbcTemplate.queryForList(com);

        String vehi = "select distinct v.vehi_no, v.comp_name, v.comp_id from vw_vehicle@db69 v where 1=1";
        vehi +=tj;
        vehi +=" order by vehi_no";
        System.out.println("vehi:"+vehi);
        List<Map<String, Object>> vehicleList = jdbcTemplate.queryForList(vehi);

        for (int i = 0; i < area.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("label", area.get(i).get("BA_NAME")+"("+area.get(i).get("C")+")");
            map.put("value", area.get(i).get("BA_ID"));
            List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
            for (int j = 0; j < comp.size(); j++) {
                if(String.valueOf(area.get(i).get("BA_ID")).equals(String.valueOf(comp.get(j).get("BA_ID")))){
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("label", comp.get(j).get("COMP_NAME")+"("+comp.get(j).get("C")+")");
                    m.put("value", comp.get(j).get("COMP_ID"));
                    List<Map<String, Object>> grandSon = new ArrayList<Map<String, Object>>();
                    for (int k = 0; k < vehicleList.size(); k++) {
                        if(String.valueOf(comp.get(j).get("COMP_ID")).equals(String.valueOf(vehicleList.get(k).get("COMP_ID")))){
                            Map<String, Object> sm = new HashMap<String, Object>();
                            sm.put("label", vehicleList.get(k).get("VEHI_NO"));
                            sm.put("value", vehicleList.get(k).get("VEHI_NO"));
                            grandSon.add(sm);
                        }
                    }
                    if(grandSon.size() > 0 ){
                        m.put("children", grandSon);
                    }
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
}
