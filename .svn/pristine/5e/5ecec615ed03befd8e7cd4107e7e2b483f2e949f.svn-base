package mvc.service;

import helper.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: xianlehuang
 * @Description:函件管理
 * @date: 2019/8/28 - 8:52
 */
@Service
public class LetterManagementService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

    //转入,转出申请查询
    public String findTransferApplication(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String vehicle=request.getParameter("vehicle");
        String type=request.getParameter("type");
        String tj="";
        if(vehicle!=null&&!vehicle.isEmpty()&&!vehicle.equals("null")&&vehicle.length()>0){
            tj +=" and a.vehicle_no='"+vehicle+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and a.application_time >=to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and a.application_time <=to_date('"+endTime+"  23:59:59','yyyy-MM-dd hh24:mi:ss')";
        }
        if(type!=null&&!type.isEmpty()&&!type.equals("null")&&type.length()>0){
            tj +=" and a.JOIN_TURNOUT ='"+type+"'";
        }
//        //用户能管理的公司
//        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
//        comps=comps==""?"''":comps;
//        tj +=" and (a.OLD_COMPANY in ("+comps+") or a.NEW_COMPANY in ("+comps+"))";

        String sql="select a.*,to_char(v.REGISTER_DATE,'yyyy-MM-dd hh24:mi:ss') REGISTER_DATE from tb_vehicle_application@db69 a"
                + " left join tb_vehicle@db69 v on v.vehi_no=a.vehicle_no where 1=1";
        sql += tj;
        sql += " order by a.application_time desc";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list !=null){
            for (int i = 0; i < list.size(); i++) {
                list.get(i).put("AUDIT_TIME",list.get(i).get("AUDIT_TIME")==null?"":list.get(i).get("AUDIT_TIME").toString().substring(0,19));
                list.get(i).put("APPLICATION_TIME",list.get(i).get("APPLICATION_TIME")==null?"":list.get(i).get("APPLICATION_TIME").toString().substring(0,19));
                list.get(i).put("JOIN_TIME",list.get(i).get("JOIN_TIME")==null?"":list.get(i).get("JOIN_TIME").toString().substring(0,19));
                list.get(i).put("AUDIT_STATUS",list.get(i).get("AUDIT_STATUS")==null?"":String.valueOf(list.get(i).get("AUDIT_STATUS")).equals("2")?"未审核":(String.valueOf(list.get(i).get("AUDIT_STATUS")).equals("0")?"审核通过":"审核不通过"));
            }
        }
        return jacksonUtil.toJson(list);
    }

    //添加转入,转出申请
    public String addTransferApplication(HttpServletRequest request) {
        String time=request.getParameter("time");
        String companyName=request.getParameter("companyName");
        String vehicle=request.getParameter("vehicle");
        String type=request.getParameter("type");
        String[] a=vehicle.replace(")", "").split("\\(");
        int count=0;
        String cx="select count(*) from tb_vehicle_application@db69  where vehicle_no='"+a[0]+"' and audit_status ='2' and JOIN_TURNOUT ='"+type+"'";
        Integer size = jdbcTemplate.queryForObject(cx,Integer.class);
        if(size>0){
            return jacksonUtil.toJson(-1);
        }else{
            String sql="insert into tb_vehicle_application@db69 (VEHICLE_NO,OLD_COMPANY,NEW_COMPANY,APPLICATION_TIME,AUDIT_STATUS,JOIN_TIME,JOIN_TURNOUT)" +
                    "values ('"+a[0]+"','"+a[1]+"','"+companyName+"',sysdate,'2',to_date('"+time+"','yyyy-MM-dd HH24:mi:ss'),'"+type+"')";
            count = jdbcTemplate.update(sql);
            return jacksonUtil.toJson(count);
        }
    }

    //修改转入,转出申请
    public String updateTransferApplication(HttpServletRequest request) {
        String time=request.getParameter("time");
        String companyName=request.getParameter("companyName");
        String vehicle=request.getParameter("vehicle");
        String type=request.getParameter("type");
        String id=request.getParameter("id");
        String[] a=vehicle.replace(")", "").split("\\(");
        int count=0;
        String cx="select count(*) from tb_vehicle_application@db69  where vehicle_no='"+a[0]+"' and audit_status ='2' and JOIN_TURNOUT ='"+type+"' and id !='"+id+"'";
        Integer size = jdbcTemplate.queryForObject(cx,Integer.class);
        if(size>0){
            return jacksonUtil.toJson(-1);
        }else{
            String sql="update tb_vehicle_application@db69 set VEHICLE_NO='"+a[0]+"', OLD_COMPANY='"+a[1]+"',"
                    + " NEW_COMPANY='"+companyName+"',APPLICATION_TIME=sysdate, AUDIT_STATUS='2',JOIN_TIME=to_date('"+time+"','yyyy-MM-dd HH24:mi:ss'), JOIN_TURNOUT='"+type+"' where id='"+id+"'";

            count = jdbcTemplate.update(sql);
            return jacksonUtil.toJson(count);
        }
    }

    //删除转入,转出申请
    public String deleteTransferApplication(HttpServletRequest request) {
        String id = request.getParameter("id");
        int count=0;
        String sql="delete from tb_vehicle_application@db69 where id='"+id+"'";
        count = jdbcTemplate.update(sql);
        return jacksonUtil.toJson(count);
    }

    //报停申请查询
    public String findStopApplication(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String vehicle=request.getParameter("vehicle");
        String tj="";
        if(vehicle!=null&&!vehicle.isEmpty()&&!vehicle.equals("null")&&vehicle.length()>0){
            tj +=" and a.vehicle_no='"+vehicle+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and a.stop_time >=to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and a.stop_time <=to_date('"+endTime+"  23:59:59','yyyy-MM-dd hh24:mi:ss')";
        }
//        //用户能管理的公司
//        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
//        comps=comps==""?"''":comps;
//        tj +=" and a.COMPANY_NAME in ("+comps+")";

        String sql="select a.*,to_char(v.REGISTER_DATE,'yyyy-MM-dd hh24:mi:ss') REGISTER_DATE from tb_vehicle_stop@db69 a"
                + " left join tb_vehicle@db69 v on v.vehi_no=a.vehicle_no where 1=1";
        sql += tj;
        sql += " order by a.stop_time desc";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list !=null){
            for (int i = 0; i < list.size(); i++) {
                list.get(i).put("AUDIT_TIME",list.get(i).get("AUDIT_TIME")==null?"":list.get(i).get("AUDIT_TIME").toString().substring(0,19));
                list.get(i).put("UP_TIME",list.get(i).get("UP_TIME")==null?"":list.get(i).get("UP_TIME").toString().substring(0,19));
                list.get(i).put("STOP_TIME",list.get(i).get("STOP_TIME")==null?"":list.get(i).get("STOP_TIME").toString().substring(0,19));
                list.get(i).put("AUDIT_STATUS",list.get(i).get("AUDIT_STATUS")==null?"":String.valueOf(list.get(i).get("AUDIT_STATUS")).equals("2")?"未审核":(String.valueOf(list.get(i).get("AUDIT_STATUS")).equals("0")?"审核通过":"审核不通过"));
            }
        }
        return jacksonUtil.toJson(list);
    }

    //添加报停申请
    public String addStopApplication(HttpServletRequest request) {
        String time=request.getParameter("time");
        String reason=request.getParameter("reason");
        String vehicle=request.getParameter("vehicle");
        String[] a=vehicle.replace(")", "").split("\\(");
        int count=0;
        String cx="select count(*) from tb_vehicle_stop@db69  where vehicle_no='"+a[0]+"' and audit_status ='2'";
        Integer size = jdbcTemplate.queryForObject(cx,Integer.class);
        if(size>0){
            return jacksonUtil.toJson(-1);
        }else{
            String sql="insert into tb_vehicle_stop@db69 (VEHICLE_NO,COMPANY_NAME,STOP_REASON,STOP_TIME,UP_TIME,AUDIT_STATUS)" +
                    "values ('"+a[0]+"','"+a[1]+"','"+reason+"',to_date('"+time+"','yyyy-MM-dd HH24:mi:ss'),sysdate,'2')";

            count = jdbcTemplate.update(sql);
            return jacksonUtil.toJson(count);
        }
    }

    //修改报停申请
    public String updateStopApplication(HttpServletRequest request) {
        String time=request.getParameter("time");
        String reason=request.getParameter("reason");
        String vehicle=request.getParameter("vehicle");
        String id=request.getParameter("id");
        String[] a=vehicle.replace(")", "").split("\\(");
        int count=0;
        String cx="select count(*) from tb_vehicle_stop@db69  where vehicle_no='"+a[0]+"' and audit_status ='2' and id !='"+id+"'";
        Integer size = jdbcTemplate.queryForObject(cx,Integer.class);
        if(size>0){
            return jacksonUtil.toJson(-1);
        }else{
            String sql="update tb_vehicle_stop@db69 set VEHICLE_NO='"+a[0]+"', COMPANY_NAME='"+a[1]+"',"
                    + " STOP_REASON='"+reason+"',STOP_TIME=to_date('"+time+"','yyyy-MM-dd HH24:mi:ss'),UP_TIME=sysdate, AUDIT_STATUS='2' where id='"+id+"'";
            count = jdbcTemplate.update(sql);
            return jacksonUtil.toJson(count);
        }
    }

    //删除报停申请
    public String deleteStopApplication(HttpServletRequest request) {
        String id = request.getParameter("id");
        int count=0;
        String sql="delete from tb_vehicle_stop@db69 where id='"+id+"'";
        count = jdbcTemplate.update(sql);
        return jacksonUtil.toJson(count);
    }

    //变更申请查询
    public String findChangeApplication(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String vehicle=request.getParameter("vehicle");
        String new_vehicle=request.getParameter("new_vehicle");
        String tj="";
        if(vehicle!=null&&!vehicle.isEmpty()&&!vehicle.equals("null")&&vehicle.length()>0){
            tj +=" and a.OLD_VEHICLE_NO='"+vehicle+"'";
        }
        if(new_vehicle!=null&&!new_vehicle.isEmpty()&&!new_vehicle.equals("null")&&new_vehicle.length()>0){
            tj +=" and a.NEW_VEHICLE_NO='"+new_vehicle+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and a.APPLY_TIME >=to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and a.APPLY_TIME <=to_date('"+endTime+"  23:59:59','yyyy-MM-dd hh24:mi:ss')";
        }

        String sql="select a.*,to_char(v.REGISTER_DATE,'yyyy-MM-dd hh24:mi:ss') REGISTER_DATE from tb_vehicle_change@db69 a"
                + " left join tb_vehicle@db69 v on v.vehi_no=a.OLD_VEHICLE_NO where 1=1";
        sql += tj;
        sql += " order by a.APPLY_TIME desc";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list !=null){
            for (int i = 0; i < list.size(); i++) {
                list.get(i).put("AUDIT_TIME",list.get(i).get("AUDIT_TIME")==null?"":list.get(i).get("AUDIT_TIME").toString().substring(0,19));
                list.get(i).put("APPLY_TIME",list.get(i).get("APPLY_TIME")==null?"":list.get(i).get("APPLY_TIME").toString().substring(0,19));
                list.get(i).put("AUDIT_STATUS",list.get(i).get("AUDIT_STATUS")==null?"":String.valueOf(list.get(i).get("AUDIT_STATUS")).equals("2")?"未审核":(String.valueOf(list.get(i).get("AUDIT_STATUS")).equals("0")?"审核通过":"审核不通过"));
            }
        }
        return jacksonUtil.toJson(list);
    }

    //添加变更申请
    public String addChangeApplication(HttpServletRequest request) {
        String vehicle=request.getParameter("vehicle");
        String new_vehicle=request.getParameter("new_vehicle");
        int count = 0;
        String cx="select count(*) from tb_vehicle_change@db69  where old_vehicle_no='"+vehicle+"' and audit_status ='2'";
        Integer size = jdbcTemplate.queryForObject(cx,Integer.class);
        if(size>0){
            return jacksonUtil.toJson(-1);
        }else{
            String sql="insert into tb_vehicle_change@db69 (OLD_VEHICLE_NO,NEW_VEHICLE_NO,APPLY_TIME,AUDIT_STATUS)" +
                    "values ('"+vehicle+"','"+new_vehicle+"',sysdate,'2')";
            count = jdbcTemplate.update(sql);
            return jacksonUtil.toJson(count);
        }
    }

    //修改变更申请
    public String updateChangeApplication(HttpServletRequest request) {
        String vehicle=request.getParameter("vehicle");
        String new_vehicle=request.getParameter("new_vehicle");
        String id = request.getParameter("id");
        int count = 0;
        String cx="select count(*) from tb_vehicle_change@db69  where old_vehicle_no='"+vehicle+"' and audit_status ='2' and id!='"+id+"'";
        Integer size = jdbcTemplate.queryForObject(cx,Integer.class);
        if(size>0){
            return jacksonUtil.toJson(-1);
        }else{
            String sql="update tb_vehicle_change@db69 set OLD_VEHICLE_NO='"+vehicle+"', NEW_VEHICLE_NO='"+new_vehicle+"',"
                    + " APPLY_TIME=sysdate, AUDIT_STATUS='2' where id='"+id+"'";
            count = jdbcTemplate.update(sql);
            return jacksonUtil.toJson(count);
        }
    }

    //删除变更申请
    public String deleteChangeApplication(HttpServletRequest request) {
        String id = request.getParameter("id");
        int count=0;
        String sql="delete from tb_vehicle_change@db69 where id='"+id+"'";
        count = jdbcTemplate.update(sql);
        return jacksonUtil.toJson(count);
    }
    //数据接入申请查询
    public String findDataApplication(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String vehicle=request.getParameter("vehicle");
        String company=request.getParameter("company");
        String tj="";
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj+=" and al.application_date >=to_date('"+startTime+" 00:00:00','yyyy-MM-dd HH24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj+=" and al.application_date <=to_date('"+endTime+" 23:59:59','yyyy-MM-dd HH24:mi:ss')";
        }
        if(vehicle!=null&&!vehicle.isEmpty()&&!vehicle.equals("null")&&vehicle.length()>0){
            tj+=" and al.vehicle_no ='"+vehicle+"'";
        }
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj+=" and al.company_name ='"+company+"'";
        }
        String sql = "select al.*, u.REAL_NAME from TB_DATA_APPLICATION@db69 al " +
                "  left join tb_user@db69 u on al.audit_person=u.user_id" +
                " where 1=1";
        sql += tj;
        sql +=" order by al.application_date desc";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        if(list !=null){
            for (int i = 0; i < list.size(); i++) {
                list.get(i).put("APPLICATION_DATE",list.get(i).get("APPLICATION_DATE")==null?"":list.get(i).get("APPLICATION_DATE").toString().substring(0,19));
                list.get(i).put("AUDIT_DATE",list.get(i).get("AUDIT_DATE")==null?"":list.get(i).get("AUDIT_DATE").toString().substring(0,19));
                list.get(i).put("AUDIT_STATUS",list.get(i).get("AUDIT_STATUS")==null?"":String.valueOf(list.get(i).get("AUDIT_STATUS")).equals("2")?"未审核":(String.valueOf(list.get(i).get("AUDIT_STATUS")).equals("0")?"审核通过":"审核不通过"));
            }
        }
        return jacksonUtil.toJson(list);
    }
    //添加数据接入申请
    public String addDataApplication(HttpServletRequest request) {
        String vehicle=request.getParameter("vehicle");
        String company=request.getParameter("company");
        String vehicleColor=request.getParameter("vehicleColor");
        String vehicleType=request.getParameter("vehicleType");
        String fuelType=request.getParameter("fuelType");
        String terminalType=request.getParameter("terminalType");
        String terminalModel=request.getParameter("terminalModel");
        String ownerName=request.getParameter("ownerName");
        String ownerPhone=request.getParameter("ownerPhone");
        String dayPerson=request.getParameter("dayPerson");
        String nightPerson=request.getParameter("nightPerson");
        String dayPhone=request.getParameter("dayPhone");
        String nightPhone=request.getParameter("nightPhone");
        int count=0;
        String cx="select count(*) from TB_DATA_APPLICATION@db69  where vehicle_no='"+vehicle+"' and audit_status =2";
        Integer size = jdbcTemplate.queryForObject(cx,Integer.class);
        if(size>0){
            return jacksonUtil.toJson(-1);
        }else{
            String sql="insert into TB_DATA_APPLICATION@db69" +
                    " (VEHICLE_NO, VEHICLE_COLOR, VEHICLE_TYPE, FUEL_TYPE, TERMINAL_TYPE, TERMINAL_MODEL, OWNER_NAME" +
                    ", OWNER_PHONE, DAY_PERSON, NIGHT_PERSON, DAY_PHONE, NIGHT_PHONE, APPLICATION_DATE, COMPANY_NAME) " +
                    "values ('"+vehicle+"','"+vehicleColor+"','"+vehicleType+"','"+fuelType+"','"+terminalType+"','"+terminalModel+"','"+ownerName+"'," +
                    "'"+ownerPhone+"','"+dayPerson+"','"+nightPerson+"','"+dayPhone+"','"+nightPhone+"',sysdate,'"+company+"')";
            count = jdbcTemplate.update(sql);
            return jacksonUtil.toJson(count);
        }
    }
    //修改数据接入申请
    public String updateDataApplication(HttpServletRequest request) {
        String vehicle=request.getParameter("vehicle");
        String company=request.getParameter("company");
        String vehicleColor=request.getParameter("vehicleColor");
        String vehicleType=request.getParameter("vehicleType");
        String fuelType=request.getParameter("fuelType");
        String terminalType=request.getParameter("terminalType");
        String terminalModel=request.getParameter("terminalModel");
        String ownerName=request.getParameter("ownerName");
        String ownerPhone=request.getParameter("ownerPhone");
        String dayPerson=request.getParameter("dayPerson");
        String nightPerson=request.getParameter("nightPerson");
        String dayPhone=request.getParameter("dayPhone");
        String nightPhone=request.getParameter("nightPhone");
        String id = request.getParameter("id");
        int count = 0;
        String cx="select count(*) from TB_DATA_APPLICATION@db69  where vehicle_no='"+vehicle+"' and audit_status =2 and id!='"+id+"'";
        Integer size = jdbcTemplate.queryForObject(cx,Integer.class);
        if(size>0){
            return jacksonUtil.toJson(-1);
        }else{
            String sql="update TB_DATA_APPLICATION@db69 set VEHICLE_NO='"+vehicle+"', VEHICLE_COLOR='"+vehicleColor+"', VEHICLE_TYPE='"+vehicleType+"'" +
                    " ,FUEL_TYPE='"+fuelType+"', TERMINAL_TYPE='"+terminalType+"', TERMINAL_MODEL='"+terminalModel+"', OWNER_NAME='"+ownerName+"'" +
                    " ,OWNER_PHONE='"+ownerPhone+"', DAY_PERSON='"+dayPerson+"', NIGHT_PERSON='"+nightPerson+"', DAY_PHONE='"+dayPhone+"'" +
                    " ,NIGHT_PHONE='"+nightPhone+"', APPLICATION_DATE= sysdate, COMPANY_NAME='"+company+"'" +
                    ", AUDIT_STATUS=2, AUDIT_PERSON='', AUDIT_REASON='', AUDIT_DATE=''" +
                    " where id='"+id+"'";
            count = jdbcTemplate.update(sql);
            return jacksonUtil.toJson(count);
        }
    }
    //删除数据接入申请
    public String deleteDataApplication(HttpServletRequest request) {
        String id = request.getParameter("id");
        int count=0;
        String sql="delete from TB_DATA_APPLICATION@db69 where id='"+id+"'";
        count = jdbcTemplate.update(sql);
        return jacksonUtil.toJson(count);
    }

    //转入,转出统计
    public String findTransferStatistics(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String vehicle=request.getParameter("vehicle");
        String company=request.getParameter("company");
        String status=request.getParameter("status");
        String tj = "";
        if(vehicle!=null&&!vehicle.isEmpty()&&!vehicle.equals("null")&&vehicle.length()>0){
            tj +=" and a.vehicle_no='"+vehicle+"'";
        }
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj +=" and a.OLD_COMPANY='"+company+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and a.APPLICATION_TIME >=to_date('"+startTime+"','yyyy-MM-dd hh24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and a.APPLICATION_TIME <=to_date('"+endTime+"','yyyy-MM-dd hh24:mi:ss')";
        }
        if(status!=null&&!status.isEmpty()&&!status.equals("null")&&status.length()>0){
            tj +=" and a.JOIN_TURNOUT='"+status+"'";
        }
//        //用户能管理的公司
//        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
//        comps=comps==""?"''":comps;
//        tj +=" and (a.OLD_COMPANY in ("+comps+") or a.NEW_COMPANY in ("+comps+"))";

        String sql="select a.vehicle_no,a.old_company,a.new_company,v.AREA_NAME,count(a.vehicle_no) count from TB_VEHICLE_APPLICATION@db69 a" +
                " left join (select v.* from tb_global_vehicle@db69 v where v.industry=090 and v.business_scope_code = 1400  AND v.STATUS_NAME='营运' AND v.PLATE_NUMBER LIKE '浙A%') v on a.vehicle_no=v.PLATE_NUMBER" +
                " where a.AUDIT_STATUS='0'";
        sql +=tj;
        sql +=" group by a.vehicle_no,a.old_company,a.new_company,v.AREA_NAME order by a.vehicle_no";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    //报停统计
    public String findStopStatistics(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String vehicle=request.getParameter("vehicle");
        String company=request.getParameter("company");
        String tj = "";
        if(vehicle!=null&&!vehicle.isEmpty()&&!vehicle.equals("null")&&vehicle.length()>0){
            tj +=" and a.vehicle_no='"+vehicle+"'";
        }
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj +=" and a.COMPANY_NAME='"+company+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and a.UP_TIME >=to_date('"+startTime+"','yyyy-MM-dd hh24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and a.UP_TIME <=to_date('"+endTime+"','yyyy-MM-dd hh24:mi:ss')";
        }
//        //用户能管理的公司
//        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
//        comps=comps==""?"''":comps;
//        tj +=" and a.COMPANY_NAME in ("+comps+")";

        String sql="select a.vehicle_no,a.COMPANY_NAME,v.AREA_NAME,count(a.vehicle_no) COUNT from TB_VEHICLE_STOP@db69 a" +
                " left join (select v.* from tb_global_vehicle@db69 v where v.industry=090 and v.business_scope_code = 1400  AND v.STATUS_NAME='营运' AND v.PLATE_NUMBER LIKE '浙A%') v on a.vehicle_no=v.PLATE_NUMBER" +
                " where  a.AUDIT_STATUS='0'";
        sql +=tj;
        sql +=" group by a.vehicle_no,a.COMPANY_NAME,v.AREA_NAME";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    //变更统计
    public String findChangeStatistics(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String vehicle=request.getParameter("vehicle");
        String new_vehicle=request.getParameter("new_vehicle");
        String tj = "";
        if(vehicle!=null&&!vehicle.isEmpty()&&!vehicle.equals("null")&&vehicle.length()>0){
            tj +=" and a.old_vehicle_no='"+vehicle+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and a.APPLY_TIME >=to_date('"+startTime+"','yyyy-MM-dd hh24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and a.APPLY_TIME <=to_date('"+endTime+"','yyyy-MM-dd hh24:mi:ss')";
        }
        if(new_vehicle!=null&&!new_vehicle.isEmpty()&&!new_vehicle.equals("null")&&new_vehicle.length()>0){
            tj +=" and a.NEW_VEHICLE_NO='"+new_vehicle+"'";
        }
        String sql="select a.old_vehicle_no,a.NEW_VEHICLE_NO,v.AREA_NAME,count(a.old_vehicle_no) count from TB_VEHICLE_CHANGE@db69 a" +
                " left join (select v.* from tb_global_vehicle@db69 v where v.industry=090 and v.business_scope_code = 1400  AND v.STATUS_NAME='营运' AND v.PLATE_NUMBER LIKE '浙A%') v on a.old_vehicle_no=v.PLATE_NUMBER" +
                " where a.AUDIT_STATUS='0'";
        sql +=tj;
        sql +=" group by a.old_vehicle_no,a.NEW_VEHICLE_NO,v.AREA_NAME  order by a.old_vehicle_no desc";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }
}
