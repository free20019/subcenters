package mvc.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataItem{
	private List<Map<String, Object>> vehilist = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> arealist = new ArrayList<Map<String, Object>>();
	private Map<String, Object> num = new HashMap<String, Object>();
	private List<Map<String, Object>> l2 = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> l3 = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> l4 = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> l5 = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> l6 = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> l7 = new ArrayList<Map<String, Object>>();
	public List<Map<String, Object>> getVehilist() {
		return vehilist;
	}
	public void setVehilist(List<Map<String, Object>> vehilist) {
		this.vehilist = vehilist;
	}
	public List<Map<String, Object>> getArealist() {
		return arealist;
	}
	public void setArealist(List<Map<String, Object>> arealist) {
		this.arealist = arealist;
	}
	public List<Map<String, Object>> getL2() {
		return l2;
	}
	public void setL2(List<Map<String, Object>> l2) {
		this.l2 = l2;
	}
	public List<Map<String, Object>> getL3() {
		return l3;
	}
	public void setL3(List<Map<String, Object>> l3) {
		this.l3 = l3;
	}
	public List<Map<String, Object>> getL4() {
		return l4;
	}
	public void setL4(List<Map<String, Object>> l4) {
		this.l4 = l4;
	}
	public List<Map<String, Object>> getL5() {
		return l5;
	}
	public void setL5(List<Map<String, Object>> l5) {
		this.l5 = l5;
	}
	public List<Map<String, Object>> getL6() {
		return l6;
	}
	public void setL6(List<Map<String, Object>> l6) {
		this.l6 = l6;
	}
	public List<Map<String, Object>> getL7() {
		return l7;
	}
	public void setL7(List<Map<String, Object>> l7) {
		this.l7 = l7;
	}
	public Map<String, Object> getNum() {
		return num;
	}
	public void setNum(Map<String, Object> num) {
		this.num = num;
	}
	
}