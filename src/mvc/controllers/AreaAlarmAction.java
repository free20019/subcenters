package mvc.controllers;

import helper.DownloadAct;
import mvc.service.AreaAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: xianlehuang
 * @Description: 区域报警
 * @date: 2021/1/18 - 15:43
 */
@Controller
@RequestMapping("/areaAlarm")
public class AreaAlarmAction {

    @Autowired
    AreaAlarmService areaAlarmService;

    private DownloadAct downloadAct = new DownloadAct();

    //查询区域设置
    @RequestMapping("/findAreaSetting")
    @ResponseBody
    public String findAreaSetting(HttpServletRequest request){
        String msg = areaAlarmService.findAreaSetting(request);
        return msg;
    }

    //添加区域设置
    @RequestMapping("/addAreaSetting")
    @ResponseBody
    public String addAreaSetting(HttpServletRequest request){
        return areaAlarmService.addAreaSetting(request);
    }

    //修改区域设置
    @RequestMapping("/updateAreaSetting")
    @ResponseBody
    public String updateAreaSetting(HttpServletRequest request){
        return areaAlarmService.updateAreaSetting(request);
    }

    //删除区域设置
    @RequestMapping("/deleteAreaSetting")
    @ResponseBody
    public String deleteAreaSetting(HttpServletRequest request){
        return areaAlarmService.deleteAreaSetting(request);
    }

    //短信报警查询
    @RequestMapping("/findMessageAlarm")
    @ResponseBody
    public String findMessageAlarm(HttpServletRequest request){
        String msg = areaAlarmService.findMessageAlarm(request);
        return msg;
    }

    //短信报警导出
    @RequestMapping(value = "/findMessageAlarmExcel")
    @ResponseBody
    public String findMessageAlarmExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String a[] = {"公司名称","车牌号码","运行时间","短信发送时间","短信内容","速度","联系人","联系人号码","短信发送状态","报警状态","行驶报警"};//导出列明
        String b[] = {"COMP_NAME","VEHI_NUM","STIME","MSG_SEND_TIME","MSGINFO","SPEED","CONTACTOR","PHONE","MSG_STATUS","ALARMTYPE","ALARMFLAG"};//导出map中的key
        String gzb = "短信报警";//导出sheet名和导出的文件名
        List<Map<String, Object>> list= DownloadAct.parseJSON2List(areaAlarmService.findMessageAlarm(request));
        DownloadAct.download(request, response, a, b, gzb, list);
        return null;
    }

    //公司车辆树
    @RequestMapping("/getCompanyVehicleTree")
    @ResponseBody
    public String getCompanyVehicleTree(HttpServletRequest request){
        String msg = areaAlarmService.getCompanyVehicleTree(request);
        return msg;
    }

}
