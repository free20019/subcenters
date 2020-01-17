package mvc.controllers;

import javax.servlet.http.HttpServletRequest;

import mvc.service.AreaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
// 信息查询系统/区域管理
@Controller
@RequestMapping("/area")
public class AreaAction {
    @Autowired
    private AreaService areaService;

    //停车区域管理
    @RequestMapping("/findArea")
    @ResponseBody
    public String findArea(String type){
        String msg = areaService.findArea(type);
        return msg;
    }

    //增加区域
    @RequestMapping("/addArea")
    @ResponseBody
    public String addArea(HttpServletRequest request){ 	
        String info = "";
        int msg = areaService.addArea(request);
        if (msg != 0) {
            info = "添加成功";
        } else {
            info = "添加失败";
        }
        return info;
    }

    //修改区域
    @RequestMapping(value = "/editArea")
    @ResponseBody
    public String editArea(String areaName,String areaDescribe,String areaMaxNum,String areaId){ 	
        String info = "";
        int msg = areaService.editArea(areaName,areaDescribe,areaMaxNum,areaId);
        if (msg != 0) {
            info = "修改成功";
        } else {
            info = "修改失败";
        }
        return info;
    }

    //删除区域
    @RequestMapping(value = "/delArea")
    @ResponseBody
    public String delArea(String areaId) {
        String info = "";
        int msg = areaService.delArea(areaId);
        if (msg != 0) {
            info = "删除成功";
        } else {
            info = "删除失败";
        }
        return info;
    }
}
