package mvc.controllers;

import mvc.service.ClqkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//决策支持系统
@Controller
@RequestMapping("/clqk")
public class ClqkAction {
	@Autowired
	private ClqkService tlqkService;
	
	//自行车运行热点分布,单车
	@RequestMapping("/findrlt")
	@ResponseBody
	public String findrlt(){
		String msg = tlqkService.findrlt();
		return msg;
	}
	
	//自行车运行热点分布,电单车
	@RequestMapping("/finderlt")
	@ResponseBody
	public String finderlt(){
		String msg = tlqkService.finderlt();
		return msg;
	}
	
	//车辆od流向趋势,左侧列表,单车
    @RequestMapping("/findod")
    @ResponseBody
    public String findod(String time){
        String msg = tlqkService.findod(time);
        return msg;
    }
    
  //车辆od流向趋势,左侧列表,电单车
    @RequestMapping("/findeod")
    @ResponseBody
    public String findeod(String time){
        String msg = tlqkService.findeod(time);
        return msg;
    }
    
    //地图信息,单车
    @RequestMapping("/odgraph")
	@ResponseBody
	public String odgraph(String time, String id){
		String msg = tlqkService.odgraph(time,id);
		return msg;
	}
    
    //地图信息,电单车
    @RequestMapping("/odegraph")
	@ResponseBody
	public String odegraph(String time, String id){
		String msg = tlqkService.odegraph(time,id);
		return msg;
	}
}