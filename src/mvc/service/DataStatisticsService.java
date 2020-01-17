package mvc.service;

import helper.JacksonUtil;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static mvc.service.mapServer.GetDistance;

/**
 * @author: xianlehuang
 * @Description:数据统计
 * @date: 2019/8/21 - 10:00
 */
@Service
public class DataStatisticsService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    private CommonServer commonServer;
    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

    //数据统计(里程统计，单车里程统计，车辆上线情况统计)
    public String findDataStatistics(HttpServletRequest request) {
        String module = request.getParameter("module");
        String field1 = request.getParameter("field1");
        String field2 = request.getParameter("field2");
        String field3 = request.getParameter("field3");
        String time = request.getParameter("time");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //今天，昨天
        for (int i = 0; i < 2; i++) {
            Map<String, Object> map1=toMap(i,finddcpjfx(module,i,field1,time));
            if(i==0){
                map1.put("message", "今天");
            }else if(i==1){
                map1.put("message", "昨天");
            }
            list.add(map1);
        }
        //前天，上周
        for (int i = 2; i < 4; i++){
            Map<String, Object> map2=toMap(i,findday(module,i,field2,time));
            if(i==2){
                map2.put("message", "前天");
            }else if(i==3){
                map2.put("message", "上周");
            }
            list.add(map2);
        }
        //上周平均
        Map<String, Object> map3=toMap(4,findaverage(module,field2,time));
        map3.put("message", "上周平均");
        list.add(map3);
        //上半月最大最小
        tolist(findmaxmin(module,field3,time),list,field3);
        //上月，上年
        for (int i = 7; i < 9; i++){
            Map<String, Object> map4=toMap(i,findday(module,i,field2,time));
            if(i==7){
                map4.put("message", "上月");
            }else if(i==8){
                map4.put("message", "上年");
            }
            list.add(map4);
        }
        return jacksonUtil.toJson(list);
    }
    //今天，昨天
    public List<Map<String, Object>> finddcpjfx(String module, Integer i, String field1, String time){
        String tj="";
        String sql ="";
        time=findtime(i,time);
        if(module.equals("zxyylfx")){
            if(time!=null&&!time.isEmpty()&&!time.equals("null")&&time.length()>0&&!time.equals("时间")){
                tj += " and db_time >=to_date('"+time+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+time+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            }
            sql += "select "+field1+" COUNT,to_char(db_time,'hh24') TIME from TB_TAXI_RUN_RATE@taxilink105 where 1=1 and  to_char(db_time, 'mi')='00'";
            sql += tj ;
        }else if(module.equals("MileageStatistics")){
            if(time!=null&&!time.isEmpty()&&!time.equals("null")&&time.length()>0&&!time.equals("时间")){
                tj += " and db_time >=to_date('"+time+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+time+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            }
            sql += "select replace("+field1+",'%','')||'%' COUNT,to_char(db_time,'hh24') TIME from TB_TAXI_RUN_INFO_RECORD_TEST@taxilink105 where 1=1 ";
            sql += tj ;
        }else if(module.equals("CyclingMileageStatistics")){
            if(time!=null&&!time.isEmpty()&&!time.equals("null")&&time.length()>0&&!time.equals("时间")){
                tj += " and db_time >=to_date('"+time+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+time+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            }
            sql += "select "+field1+" COUNT,to_char(db_time,'hh24') TIME from TB_TAXI_RUN_INFO_RECORD_TEST@taxilink105 where 1=1 ";
            sql += tj ;
        }else if(module.equals("VehicleOnlineStatistics")||module.equals("zclfx")){
            if(time!=null&&!time.isEmpty()&&!time.equals("null")&&time.length()>0&&!time.equals("时间")){
                tj += " and db_time >=to_date('"+time+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+time+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            }
            sql += "select "+field1+" COUNT,to_char(db_time,'hh24') TIME from TB_TAXI_LOAD_ONLINE_RATE@taxilink105 where 1=1 and  to_char(db_time, 'mi')='00'  and ba_id=0";
            sql += tj ;
        }
        System.out.println("今天昨天="+sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;

    }
    //前天，上周，上月，上年
    public List<Map<String, Object>> findday(String module, Integer i, String field2, String time){
        String tj="";
        String sql ="";
        time=findtime(i,time);
        if(module.equals("zxyylfx")){
            if(time!=null&&!time.isEmpty()&&!time.equals("null")&&time.length()>0&&!time.equals("时间")){
                tj += " and db_time >=to_date('"+time+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+time+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            }
            sql += "select "+field2+" COUNT,to_char(db_time,'hh24') TIME from TB_TAXI_RUN_RATE@taxilink105 where 1=1 and  to_char(db_time, 'mi')='00'";
            sql += tj ;
        }else if(module.equals("MileageStatistics")){
            if(time!=null&&!time.isEmpty()&&!time.equals("null")&&time.length()>0&&!time.equals("时间")){
                tj += " and db_time >=to_date('"+time+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+time+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            }
            sql += "select replace("+field2+",'%','')||'%' COUNT,to_char(db_time,'hh24') TIME from TB_TAXI_RUN_INFO_RECORD_TEST@taxilink105 where 1=1 ";
            sql += tj ;
        }else if(module.equals("CyclingMileageStatistics")){
            if(time!=null&&!time.isEmpty()&&!time.equals("null")&&time.length()>0&&!time.equals("时间")){
                tj += " and db_time >=to_date('"+time+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+time+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            }
            sql += "select "+field2+" COUNT,to_char(db_time,'hh24') TIME from TB_TAXI_RUN_INFO_RECORD_TEST@taxilink105 where 1=1 ";
            sql += tj ;
        }else if(module.equals("VehicleOnlineStatistics")||module.equals("zclfx")){
            if(time!=null&&!time.isEmpty()&&!time.equals("null")&&time.length()>0&&!time.equals("时间")){
                tj += " and db_time >=to_date('"+time+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+time+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            }
            sql += "select "+field2+" COUNT,to_char(db_time,'hh24') TIME from TB_TAXI_LOAD_ONLINE_RATE@taxilink105 where 1=1 and  to_char(db_time, 'mi')='00'  and ba_id=0";
            sql += tj ;
        }
        System.out.println("前天上周 上月上年="+sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;

    }
    //上周平均
    public List<Map<String, Object>> findaverage(String module, String field2, String time){
        String tj="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        if(time!=null&&!time.isEmpty()&&!time.equals("null")&&time.length()>0&&!time.equals("时间")){
//			time = time.replaceAll("-","");
        }else{
            time=sdf.format(new Date());
        }
        try {
            calendar.setTime(sdf.parse(time));
        }catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.add(Calendar.DATE, -7);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String stime = sdf.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String etime = sdf.format(calendar.getTime());
        String sql = "";
        if(module.equals("zxyylfx")){
            tj += " and db_time >=to_date('"+stime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+etime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            sql+= "select ROUND(sum(replace("+field2+",'%',''))/count("+field2+"))||'%' COUNT,to_char(db_time, 'hh24') TIME from TB_TAXI_RUN_RATE@taxilink105 "
                    + " where 1=1  and  to_char(db_time, 'mi')='00'";
            sql+=tj;
            sql+= " group by to_char(db_time, 'hh24') order by to_char(db_time, 'hh24')";
        }else if(module.equals("MileageStatistics")){
            tj += " and db_time >=to_date('"+stime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+etime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            sql+= "select ROUND(sum(replace("+field2+",'%',''))/count("+field2+"))||'%' COUNT,to_char(db_time, 'hh24') TIME from TB_TAXI_RUN_INFO_RECORD_TEST@taxilink105 "
                    + " where 1=1  and  to_char(db_time, 'mi')='00'";
            sql+=tj;
            sql+= " group by to_char(db_time, 'hh24') order by to_char(db_time, 'hh24')";
        }else if(module.equals("CyclingMileageStatistics")){
            tj += " and db_time >=to_date('"+stime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+etime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            sql+= "select ROUND(sum("+field2+")/count("+field2+"),2) COUNT,to_char(db_time, 'hh24') TIME from TB_TAXI_RUN_INFO_RECORD_TEST@taxilink105 "
                    + " where 1=1  and  to_char(db_time, 'mi')='00'";
            sql+=tj;
            sql+= " group by to_char(db_time, 'hh24') order by to_char(db_time, 'hh24')";
        }else if(module.equals("VehicleOnlineStatistics")||module.equals("zclfx")){
            tj += " and db_time >=to_date('"+stime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and db_time <=to_date('"+etime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            sql+= "select ROUND(sum(replace("+field2+",'%',''))/count("+field2+"))||'%' COUNT,to_char(db_time, 'hh24') TIME from TB_TAXI_LOAD_ONLINE_RATE@taxilink105 "
                    + " where 1=1  and  to_char(db_time, 'mi')='00'  and ba_id=0";
            sql+=tj;
            sql+= " group by to_char(db_time, 'hh24') order by to_char(db_time, 'hh24')";
        }

        System.out.println("平均  " + sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;

    }
    //上半月最大最小
    public List<Map<String, Object>> findmaxmin(String module,String field3, String time){
        String tj="";
        time=findtime(1,time);
        String sql = "";
        if(time!=null&&!time.isEmpty()&&!time.equals("null")&&time.length()>0&&!time.equals("时间")){
            tj += " and to_char(enddate, 'yyyy-mm-dd') ='"+time+"'";
        }
        if(module.equals("VehicleOnlineStatistics")||module.equals("zclfx")){
            sql += "select * from TB_AREA_HALF_MONTH_ONLINE_RATE@taxilink105  where 1=1 and ba_id=0 and rownum<2";
            sql += tj;
            sql +=" order by id desc";
        }else if(module.equals("zxyylfx")||module.equals("MileageStatistics")||module.equals("CyclingMileageStatistics")){
            sql += "select * from TB_HALF_MONTH_ONLINE_RUN_RATE@taxilink105  where 1=1";
            sql += tj;
        }
        System.out.println("findmaxmin="+sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    //将查询出的数据转换为指定格式{y1:"",y2:""....}
    private Map<String, Object> toMap(int m,List<Map<String, Object>> list) {
        String[] str = { "00", "01", "02", "03", "04", "05",
                "06", "07", "08", "09", "10", "11", "12", "13",
                "14", "15", "16", "17", "18", "19", "20", "21",
                "22", "23", };
        Map<String, Object> map = new HashMap<String, Object>();
        int a = 0;
        for (int i = 0; i < list.size(); i++) {
            if ((a=Arrays.asList(str).indexOf(list.get(i).get("TIME")))>-1) {
                map.put("y" + a, list.get(i).get("COUNT"));
            }
        }
        for (int i = 0; i < 24; i++) {
            if (map.get("y" + i) == null) {
                map.put("y" + i, "");
            }
        }
        return map;
    }

    //区分开前半月最大，前半月最小，并以指定格式输出[{},{}]
    private void tolist(List<Map<String, Object>> listmaxmin,
                        List<Map<String, Object>> list, String field3) {
        String[] max = { "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "" };
        String[] min = { "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "" };
        if (listmaxmin.size() != 0) {
            max = String.valueOf(listmaxmin.get(0).get(field3+"_MAX")).split(
                    ";");
            min = String.valueOf(listmaxmin.get(0).get(field3+"_MIN")).split(
                    ";");
        }
        Map<String, Object> maxMap = new HashMap<String, Object>();
        for (int j = 0; j < max.length; j++) {
            maxMap.put("y" + j, max[j]);
        }
        maxMap.put("message", "前半月最大");

        Map<String, Object> minMap = new HashMap<String, Object>();
        for (int j = 0; j < min.length; j++) {
            minMap.put("y" + j, min[j]);
        }
        minMap.put("message", "前半月最小");
        list.add(maxMap);
        list.add(minMap);
    }

    //计算出今天,昨天,前天,上周,上周平均,前半月最大,前半月最小,上月,上年Query所需time
    private String findtime(Integer i, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        if(time!=null&&!time.isEmpty()&&!time.equals("null")&&time.length()>0&&!time.equals("时间")){
//			time = time.replaceAll("-","");
        }else{
            calendar.setTime(new Date());
            time=sdf.format(calendar.getTime());
        }

        try {
            calendar.setTime(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(i==0){
            return time;
        }else if(i==1){
            calendar.add(Calendar.DATE, -1);
            Date date = calendar.getTime();
            time=sdf.format(date);
            return time;
        }else if(i==2){
            calendar.add(Calendar.DATE, -2);
            Date date = calendar.getTime();
            time=sdf.format(date);
            return time;
        }else if(i==3){
            calendar.add(Calendar.DATE, -7);
            Date date = calendar.getTime();
            time=sdf.format(date);
            return time;
        }else if(i==7){
            calendar.add(Calendar.MONDAY, -1);
            Date date = calendar.getTime();
            time=sdf.format(date);
            return time;
        }else if(i==8){
            calendar.add(Calendar.YEAR, -1);
            Date date = calendar.getTime();
            time=sdf.format(date);
            return time;
        }
        return null;
    }

    //单车里程统计
    public String findCyclingMileageStatistics(HttpServletRequest request) {
        DecimalFormat df = new DecimalFormat("0.000");
        String vhic = request.getParameter("vehicle");
        String stime = request.getParameter("startTime");
        String etime = request.getParameter("endTime");
        String[] vehicles = vhic.split(",");
        Map<String, Object> map = new HashMap<String, Object>();
        String vehicle_mileage="";
        List<Map<String, Object>> listAll = new ArrayList<Map<String,Object>>();
        for (int j = 0; j < vehicles.length; j++) {
            String table = stime.substring(2, 4) + stime.substring(5, 7);
            List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
            try {
                String sql = "select c.comp_name,t.vehicle_num,t.px,t.py,t.speed,t.direction,t.speed_time,state from HZGPS_TAXI.TB_GPS_"+table+"@db69 t,tb_company@db69 c,vw_vehicle@db69 v where"
                        + " t.vehicle_num = v.vehi_no and c.comp_id = v.comp_id and vehicle_num = ? and"
                        + " speed_time>= to_date(?,'yyyy-mm-dd hh24:mi:ss')"
                        + " and speed_time<= to_date(?,'yyyy-mm-dd hh24:mi:ss')"
                        + " and px>110 and py>25 and carstate = '0' order by speed_time";
                System.out.println(sql+"  "+vehicles[j]+"  "+stime+"  "+etime);
                list = jdbcTemplate.queryForList(sql,vehicles[j],stime,etime);
            } catch (Exception e) {
                return jacksonUtil.toJson(list);
            }
            double dou = 0.0;
            for (int i = 0; i < list.size(); i++) {
                list.get(i).put("DIRECTION", String.valueOf(list.get(i).get("DIRECTION")));
                list.get(i).put("SPEED_TIME", list.get(i).get("SPEED_TIME")==null?"":String.valueOf(list.get(i).get("SPEED_TIME")).substring(0,19));
                if(i == 0){
                    list.get(i).put("MILEAGE", "0");
//                    list.get(i).put("ADDRESS",list.get(i).get("PX")==null||list.get(i).get("PY")==null?"":getAdd(list.get(i).get("PY").toString()+","+list.get(i).get("PX").toString()));
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
//                    list.get(i).put("ADDRESS",list.get(i).get("PX")==null||list.get(i).get("PY")==null?"":(list.get(i).get("PX").equals(list.get(i-1).get("PX"))&&list.get(i).get("PY").equals(list.get(i-1).get("PY"))?list.get(i-1).get("ADDRESS"):getAdd(list.get(i).get("PY").toString()+","+list.get(i).get("PX").toString())));
                }
                if(i==list.size()-1){
                    if(j==0){
                        vehicle_mileage += vehicles[j]+":"+list.get(i).get("MILEAGE").toString();
                    }else{
                        vehicle_mileage += ";"+vehicles[j]+":"+list.get(i).get("MILEAGE").toString();
                    }
                }
            }
            listAll.addAll(list);
        }
        map.put("count",vehicle_mileage);
        map.put("datas",listAll);
        return jacksonUtil.toJson(map);
    }

    //经纬度转为地址
    public static String getAdd(String formattedAmapPoints) {
        String res = "";
//		String locationUrl = "https://restapi.amap.com/v3/geocode/regeo?output=json&location="+formattedAmapPoints+"&key=d0face2397384365119fbf558c48c10a&radius=1000&batch=true";
        String locationUrl = "http://api.map.baidu.com/geocoder/v2/?ak=ELYtYXEH1WmDrK1wDsEWRNkGviRL0ZEo&callback=renderReverse&location=" + formattedAmapPoints + "&output=json&pois=1";
        try {
            URL url = new URL(locationUrl);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
        } catch (Exception e) {
            System.out.println("error in wapaction,and e is " + e.getMessage());
        }
        if(res.length()>31){
            System.out.println(res.substring(29,res.length()-2));
            if(!"".equals(res)){
                Map<String, Object> map = parseJSON2Map(res.substring(29, res.length() - 2));
                String address = String.valueOf(parseJSON2Map(map.get("result").toString()).get("formatted_address"));
                return address;
            }
        }
        return res;

    }

    //json to map
    public static Map<String, Object> parseJSON2Map(String jsonStr){
        Map<String, Object> map = new HashMap<String, Object>();
        net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k);
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                Iterator<net.sf.json.JSONObject> it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    net.sf.json.JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    //无营数据车辆
    public String findNoCampDataVehicle(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String company=request.getParameter("company");
        String tj="";
        String tj2="";
        String riqi = startTime.substring(0, 4);
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj2 +=" and t.COMP_NAME='"+company+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and t.xiache >=to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and t.xiache <=to_date('"+endTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss')";
        }
        //用户能管理的公司
        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
        comps=comps==""?"''":comps;
//        tj2 +=" and t.COMP_NAME in ("+comps+")";
        //用户管理的車輛
        String vehicles=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic").toString();
        tj2 +=" and (1=0 or t.COMP_NAME in ("+comps+") "+Transformation(new ArrayList<String>(Arrays.asList(vehicles.split(","))),"t.vehi_no")+" )";

        String sql = "select vehi_no,comp_name,own_name,own_tel,vehi_sim from VW_VEHICLE@db69 t where  1=1 " ;
        sql += tj2;
        sql+=" minus " +
                " select vehi_no,comp_name,own_name,own_tel,vehi_sim from (select * from" +
                " (select distinct (vhic) c from TB_CITIZEN_"+riqi+"@db69 t" +
                " where 1=1";
        sql += tj;
        sql +=  ") a, VW_VEHICLE@db69 t where '浙'||a.c=t.vehi_no " ;
//        sql += tj2;
        sql+=") order by vehi_no";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    //未上线车辆查询
    public String findUnlistedVehicle(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String company=request.getParameter("company");
        String tj="";
        String tj2="";
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj +=" and v.comp_name='"+company+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj2 +=" and db_time >=to_date('"+startTime+"','yyyy-MM-dd')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj2 +=" and db_time <=to_date('"+endTime+"','yyyy-MM-dd')";
        }
        //用户能管理的公司
        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
        comps=comps==""?"''":comps;
//        tj +=" and v.comp_name in ("+comps+")";
        //用户管理的車輛
        String vehicles=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic").toString();
        tj +=" and (1=0 or v.comp_name in ("+comps+") "+Transformation(new ArrayList<String>(Arrays.asList(vehicles.split(","))),"v.VEHI_NO")+" )";

        long ts = getDaySub(startTime, endTime)+1;
        String sql = " select l.TIME,v.VEHI_NO,v.VEHI_NUM,v.COMP_NAME,v.VEHI_SIM,v.HOME_TEL,v.NIGHT_TEL,v.OWN_NAME,v.MT_NAME,v.MDT_SUB_TYPE" +
                "  from (select vehi_no,to_char(max(online_time),'yyyy-MM-dd hh24:mi:ss') TIME,count(1) c from TB_TAXI_NOT_ONLINE@db69" +
                " where online_time<=sysdate";
        sql+=tj2;
        sql += "  group by vehi_no)l" +
                " left join VW_VEHICLE@db69 v on  v.VEHI_NO=l.VEHI_NO  " +
                " where v.VEHI_NO is not null and l.c >="+ts;
        sql += tj;
        sql += " order by l.TIME desc";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }
    //查询开始时间，结束时间的间隔天数
    private static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate;
        Date endDate;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); //得到前一天
        Date date = null;
        try {
            date = format.parse(format.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);
            if(endDate.getTime()>date.getTime()){
                day=(date.getTime()-beginDate.getTime())/(24*60*60*1000);
            }else{
                day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    //上线无营运车辆
    public String findNoOperatingVehicleOnLine(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String company=request.getParameter("company");
        String riqi = startTime.substring(2, 4)+startTime.substring(5, 7);
        String tj="";
        String tj2="";
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj +=" and v.ba_name='"+company+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and ol.db_time >=to_date('"+startTime+"','yyyy-MM-dd')";
            tj2 +=" and shangche>=to_date('"+startTime+" 00:00:00','yyyy-MM-dd HH24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and ol.db_time <=to_date('"+endTime+"','yyyy-MM-dd')";
            tj2 +=" and shangche<=to_date('"+endTime+" 23:59:59','yyyy-MM-dd HH24:mi:ss')";
        }
        //用户能管理的公司
        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
        comps=comps==""?"''":comps;
//        tj +=" and v.ba_name in ("+comps+")";
        //用户管理的車輛
        String vehicles=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic").toString();
        tj +=" and (1=0 or v.ba_name in ("+comps+") "+Transformation(new ArrayList<String>(Arrays.asList(vehicles.split(","))),"v.vehi_no")+" )";

        String sql="select v.vehi_no,v.comp_name,v.own_name,v.own_tel,v.vehi_sim,v.ba_name,to_char(max(ol.db_time),'yyyy-MM-dd') online_time" +
                " from TB_TAXI_ONLINE_"+riqi+"@db69 ol,VW_VEHICLE@db69 v" +
                " where  v.vehi_no = ol.vehi_no and v.vehi_no in (select distinct '浙'||vhic vhic from TB_JJQ_STATIC@db69 "+
                " where '浙'||vhic NOT in (select distinct '浙'||vhic from TB_JJQ_STATIC@db69  where \"Y/N\" ='Y'";
        sql += tj2;
        sql += "))";
        sql += tj;
        sql += "group by v.vehi_no,v.comp_name,v.own_name,v.own_tel,v.vehi_sim,v.ba_name order by v.vehi_no";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    //营运未上线车辆导出
    public String findOperatingUnlisted(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String company=request.getParameter("company");
        long ts = getDaySub(startTime, endTime)+1;
        String tj="";
        String tj2="";
        String tj3="";
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj3 +=" and v.ba_name='"+company+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and db_time >=to_date('"+startTime+"','yyyy-MM-dd')";
            tj2 +=" and shangche>=to_date('"+startTime+" 00:00:00','yyyy-MM-dd HH24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and db_time <=to_date('"+endTime+"','yyyy-MM-dd')";
            tj2 +=" and shangche<=to_date('"+endTime+" 23:59:59','yyyy-MM-dd HH24:mi:ss')";
        }
        //用户能管理的公司
        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
        comps=comps==""?"''":comps;
//        tj3 +=" and v.ba_name in ("+comps+")";

        //用户管理的車輛
        String vehicles=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic").toString();
        tj3 +=" and (1=0 or v.ba_name in ("+comps+") "+Transformation(new ArrayList<String>(Arrays.asList(vehicles.split(","))),"a.vehi_no")+" )";


        String sql="select a.*,v.* "+
                " from (select vehi_no,to_char(max(online_time),'yyyy-MM-dd hh24:mi:ss') online_time from TB_TAXI_NOT_ONLINE@db69" +
                " where vehi_no is not null ";
        sql += tj;
        sql += " and vehi_no in  (select distinct '浙'||vhic from TB_JJQ_STATIC@db69  where \"Y/N\" ='Y'";
        sql += tj2;
        sql += ") group by vehi_no having count(1)>="+ts+" ) a";
        sql +=" left join (select vehi_no vehi_num,comp_name,own_name,own_tel,vehi_sim,ba_name from VW_VEHICLE@db69 ) v" +
                " on v.vehi_num=a.vehi_no where 1=1" ;
        sql += tj3;
        sql += " order by a.vehi_no";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    //未上线未营运车辆
    public String findUnoperatedVehicle(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String company=request.getParameter("company");
        long ts = getDaySub(startTime, endTime)+1;
        String tj="";
        String tj2="";
        String tj3="";
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj3 +=" and v.ba_name='"+company+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and db_time >=to_date('"+startTime+"','yyyy-MM-dd')";
            tj2 +=" and shangche>=to_date('"+startTime+" 00:00:00','yyyy-MM-dd HH24:mi:ss')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and db_time <=to_date('"+endTime+"','yyyy-MM-dd')";
            tj2 +=" and shangche<=to_date('"+endTime+" 23:59:59','yyyy-MM-dd HH24:mi:ss')";
        }
        //用户能管理的公司
        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
        comps=comps==""?"''":comps;
//        tj3 +=" and v.ba_name in ("+comps+")";
        //用户管理的車輛
        String vehicles=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic").toString();
        tj3 +=" and (1=0 or v.ba_name in ("+comps+") "+Transformation(new ArrayList<String>(Arrays.asList(vehicles.split(","))),"v.vehi_no")+" )";

        String sql="select v.vehi_no,v.vehi_num,v.comp_name,v.own_name,v.own_tel,v.vehi_sim,v.ba_name from VW_VEHICLE@db69 v" +
                " where v.vehi_no in (select vehi_no from TB_TAXI_NOT_ONLINE@db69 where vehi_no is not null";
        sql += tj;
        sql += " group by vehi_no having count(1)>="+ts+" )" +
                " and vehi_no not in (select distinct '浙'||vhic from TB_JJQ_STATIC@db69  where \"Y/N\" ='Y'";
        sql += tj2;
        sql += ")";
        sql += tj3;
        sql += " order by v.vehi_no";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    //无空重车变化车辆
    public String findNoEmptyVehicleChange(HttpServletRequest request) {
//        String startTime=request.getParameter("startTime");
//        String endTime=request.getParameter("endTime");
//        String company=request.getParameter("company");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date dBegin = null,dEnd = null;
//        try {
//            dBegin = sdf.parse(startTime);
//            dEnd = sdf.parse(endTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        List<Date> lDate = findDates(dBegin, dEnd);
//        String riqi = "";
//        for (Date date : lDate) {
//            riqi += sdf.format(date)+",";
//        }
//        if(riqi.equals("")){
//            return "[]";
//        }
//        String[] a = riqi.substring(0, riqi.length()-1).split(",");
//        String tj="";
//        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
//            tj +=" and v.ba_name='"+company+"'";
//        }
//        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
//            tj +=" and x.xztime >=to_date('"+startTime+"','yyyy-MM-dd')";
//        }
//        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
//            tj +=" and x.xztime <=to_date('"+endTime+"','yyyy-MM-dd')";
//        }
//        //用户能管理的公司
//        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
//        comps=comps==""?"''":comps;
//        //用户管理的車輛
//        String vehicles=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic").toString();
//        tj +=" and (1=0 or v.ba_name in ("+comps+") "+Transformation(new ArrayList<String>(Arrays.asList(vehicles.split(","))),"v.vehi_no")+" )";
//
//        String sql="select ";
//        for (int i = 0; i < a.length; i++) {
//            sql += " m"+(i+1)+".state as m"+(i+1)+",";
//        }
//        sql += " a.* from (select distinct x.vehi_no, v.comp_name, v.mt_name, v.mdt_sub_type from TB_TJ@db69 x ,vw_vehicle@db69 v "
//                + " where state is not null and x.vehi_no = v.VEHI_NO";
//        sql += tj;
//        sql +=")a ";
//        for (int i = 0; i < a.length; i++) {
//            sql +=" left join  (select state,vehi_no from TB_TJ@db69 where xztime=to_date('"+a[i]+"','yyyy-mm-dd'))m"+(i+1)+
//                    " on a.vehi_no = m"+(i+1)+".vehi_no";
//        }
//        sql += " order by a.vehi_no";
//        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
//        return jacksonUtil.toJson(list);
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String company=request.getParameter("company");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = null,dEnd = null;
        try {
            dBegin = sdf.parse(startTime);
            dEnd = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Date> lDate = findDates(dBegin, dEnd);
        String riqi = "";
        for (Date date : lDate) {
            riqi += sdf.format(date)+",";
        }
        if(riqi.equals("")){
            return "[]";
        }
        String[] a = riqi.substring(0, riqi.length()-1).split(",");
        String tj="";
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj +=" and v.ba_name='"+company+"'";
        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and x.db_time >=to_date('"+startTime+"','yyyy-MM-dd')";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and x.db_time <=to_date('"+endTime+"','yyyy-MM-dd')";
        }
        //用户能管理的公司
        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
        comps=comps==""?"''":comps;
        //用户管理的車輛
        String vehicles=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic").toString();
        tj +=" and (1=0 or v.ba_name in ("+comps+") "+Transformation(new ArrayList<String>(Arrays.asList(vehicles.split(","))),"v.vehi_no")+" )";

        String sql="select ";
        for (int i = 0; i < a.length; i++) {
            sql += " m"+(i+1)+".state as m"+(i+1)+",";
        }
        sql += " a.* from (select distinct x.vehicle_no vehi_no, v.comp_name, v.mt_name, v.mdt_sub_type from tb_taxi_gzfx_history@db69 x ,vw_vehicle@db69 v "
                + " where empty_heavy =1 and x.vehicle_no = v.VEHI_NO";
        sql += tj;
        sql +=")a ";
        for (int i = 0; i < a.length; i++) {
            sql +=" left join  (select case when empty_heavy =1 then '全天空重车' else '' end state,vehicle_no vehi_no from tb_taxi_gzfx_history@db69 where db_time=to_date('"+a[i]+"','yyyy-mm-dd'))m"+(i+1)+
                    " on a.vehi_no = m"+(i+1)+".vehi_no";
        }
        sql += " order by a.vehi_no";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    //查询出两个日期间的所有日期
    public List<Date> findDates(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        if (cal.getTime().before(endDate) || cal.getTime().equals(endDate)) {
            lDate.add(beginDate);// 把开始时间加入集合
            boolean bContinue = true;
            while (bContinue) {
                // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
                cal.add(Calendar.DAY_OF_MONTH, 1);
                // 测试此日期是否在指定日期之后
                if (endDate.after(cal.getTime())) {
                    lDate.add(cal.getTime());
                } else {
                    break;
                }
            }
            if ( !beginDate.equals(endDate)) {
                lDate.add(endDate);// 把结束时间加入集合
            }
        }
        return lDate;
    }
    //无签到签退查询
    public String findUnSignInAndSignOff(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String company=request.getParameter("company");
        String tj="";
        String tj2="";
        String tj3="";
//        if(vehicle!=null&&!vehicle.isEmpty()&&!vehicle.equals("null")&&vehicle.length()>0){
//            tj2 +=" and t.vehi_no='"+vehicle+"'";
//        }
        if(company!=null&&!company.isEmpty()&&!company.equals("null")&&company.length()>0){
            tj2 +=" and t.COMP_NAME='"+company+"'";
        }
//        if(area!=null&&!area.isEmpty()&&!area.equals("null")&&area.length()>0){
//            if(area.equals("主城区")){
//                tj3 += " and (g.AREA_NAME like '%上城%' or g.AREA_NAME like '%下城%' "
//                        + "or g.AREA_NAME like '%江干%' or g.AREA_NAME like '%拱墅%' "
//                        + "or g.AREA_NAME like '%下沙%' "
//                        + "or g.AREA_NAME like '%西湖%' or g.AREA_NAME like '%滨江%')";
//            } else {
//                tj3 += " and g.AREA_NAME like '%"+area+"%'";
//            }
//        }
        if(startTime!=null&&!startTime.isEmpty()&&!startTime.equals("null")&&startTime.length()>0){
            tj +=" and (t.LOGINTIME >=to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') " +
                    " or t.LOGOUTTIME >=to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss'))";
        }
        if(endTime!=null&&!endTime.isEmpty()&&!endTime.equals("null")&&endTime.length()>0){
            tj +=" and (t.LOGINTIME <=to_date('"+endTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss')" +
                    " or t.LOGOUTTIME <=to_date('"+endTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') )";
        }
        //用户能管理的公司
        String comps=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("comps").toString();
        comps=comps==""?"''":comps;
        //用户管理的車輛
        String vehicles=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic")==null?"":((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute("groupVhic").toString();
        tj2 +=" and (1=0 or t.COMP_NAME in ("+comps+") "+Transformation(new ArrayList<String>(Arrays.asList(vehicles.split(","))),"t.vehi_no")+" )";



        String sql = "select * from (select vehi_no,comp_name,own_name,own_tel,vehi_sim from" +
                " VW_VEHICLE@db69 t where 1=1 " ;
        sql += tj2;
        sql+=" minus " +
                " select vehi_no,comp_name,own_name,own_tel,vehi_sim from (select * from" +
                " (select distinct (OPERATION_VEHICLENO) c from tb_ic_operate@db69 t where 1=1";
        sql += tj;
        sql +=  ") a, VW_VEHICLE@db69 t where a.c=t.vehi_no" ;
//        sql += tj2;
        sql+=")) v" +
                " left join (select to_char(max(LOGINTIME),'yyyy-MM-dd hh24:mi:ss') LOGINTIME, to_char(max(LOGOUTTIME),'yyyy-MM-dd hh24:mi:ss') LOGOUTTIME,OPERATION_VEHICLENO from tb_ic_operate@db69 t group by OPERATION_VEHICLENO) i on v.vehi_no=i.OPERATION_VEHICLENO" +
                " left join (select plate_number,area_name from tb_global_vehicle@db69 where industry=090 and business_scope_code = 1400  AND STATUS_NAME='营运' AND PLATE_NUMBER LIKE '浙A%') g on v.vehi_no=g.plate_number where 1=1 ";
        sql += tj3;
        sql += " order by v.vehi_no ";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
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


}

