package mvc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import mvc.entity.TbCompany;

public interface bikeDao {
	public List<Map<String, Object>> getPunish(@Param("marked") String marked,@Param("company") String company,@Param("page") String page);
	public List<Map<String, Object>> getqykh(@Param("year") String year,@Param("quarter") String quarter);
	public List<Map<String, Object>> getDeposit();
	public List<Map<String, Object>> getcomp();
	public List<Map<String, Object>> getPerson(@Param("compname") String compname,@Param("name") String name,@Param("page") String page,@Param("pageSize") String pageSize);
	public List<Map<String, Object>> getBike(@Param("compname") String compname,@Param("bikeno") String bikeno,@Param("state") String state,@Param("page") String page,@Param("pageSize") String pageSize);
	public List<Map<String, Object>> getBikeCount(@Param("compname") String compname,@Param("bikeno") String bikeno,@Param("state") String state);
	public List<Map<String, Object>> getKeepBike();
	public List<Map<String, Object>> getKeepBikeQuery(@Param("compname") String compname,@Param("bikeno") String bikeno,@Param("page") String page,@Param("pageSize") String pageSize);
	public List<Map<String, Object>> getProgram(@Param("stime") String stime,@Param("etime") String etime,@Param("area") String area);
	public List<Map<String, Object>> getbikeAnalysis(String time);
	public List<Map<String, Object>> getDataAccess(@Param("stime") String stime,@Param("etime") String etime);
	public List<Map<String, Object>> getUser(@Param("name") String name);
	public int addUser(@Param("username") String username,@Param("name") String name,@Param("phone") String phone,@Param("password") String password,@Param("companyId") String companyId);
	public int editUser(@Param("username") String username,@Param("name") String name,@Param("phone") String phone
			,@Param("password") String password,@Param("id") String id);
	public int delUser(@Param("id") String id);
	public List<Map<String, Object>> getArlyWarning(@Param("page") String page);
	public int ArlyWarningHandle(@Param("id") String id);
	public int addPower(@Param("qxlist") String qxlist,@Param("list") String list,@Param("id") String id);
	public List<Map<String, Object>> getBicycleNo();
	public List<Map<String, Object>> getBicycleActive(@Param("time") String time,@Param("table") String table,@Param("table1") String table1);
	public List<Map<String, Object>> getBicycleDistribution(@Param("time") String time);
	public List<Map<String, Object>> login(@Param("username") String username,@Param("password") String password);
	public List<Map<String, Object>> getbicycleOrderNo(@Param("time") String time,@Param("table") String table);
	public List<Map<String, Object>> getEbicycleOrderNo(@Param("time") String time,@Param("table") String table);
	public List<Map<String, Object>> getTurnoverRate(@Param("time") String time);
	public List<Map<String, Object>> getBicycleOrderTurn(@Param("time") String time);
	public List<Map<String, Object>> getEBicycleOrderTurn(@Param("time") String time);
	public List<Map<String, Object>> getPBicycleOrderTurn(@Param("time") String time);
	public List<Map<String, Object>> getNotUsed(@Param("time") String time);
	public List<Map<String, Object>> getUserType();
	public List<Map<String, Object>> getLog(Integer page);
	public int getLogCount();
}
