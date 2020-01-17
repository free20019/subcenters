package mvc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import mvc.entity.TbCompany;

public interface dailySupervisionDao {
	public List<Map<String, Object>> getAllDot();
	public List<Map<String, Object>> getAllDotForEBike();  //电单车
	public List<Map<String, Object>> getDotByCompany(@Param("zoom") Integer zoom,@Param("companyid") String companyid,@Param("time") String time); //根据公司查询 点位信息
	public List<Map<String, Object>> getDotByCompanyForEBike(@Param("zoom") Integer zoom,@Param("companyid") String companyid,@Param("time") String time); //根据公司查询 点位信息  电单车
	public List<Map<String, Object>> getNoDotByCompany(@Param("zoom") Integer zoom,@Param("companyid") String companyid,@Param("time") String time,@Param("day") String day); //根据公司查询 几天未使用的车辆
	public List<Map<String, Object>> getNoDotByCompanyForEBike(@Param("zoom") Integer zoom,@Param("companyid") String companyid,@Param("time") String time,@Param("day") String day); //根据公司查询  几天未使用的车辆  电单车
	public List<Map<String, Object>> oneBike(@Param("bike") String bike);  //查找单个车辆
	public List<Map<String, Object>> oneBikeForEBike(@Param("bike") String bike);  //区域监控    电单车
	public List<Map<String, Object>> findPerson(@Param("name") String name);  //查询运维人员
	public List<Map<String, Object>> qyjk();  //区域监控 
	public List<Map<String, Object>> getZxc(@Param("name") String name);  //查询公共自行车
	public List<Map<String, Object>> getYwryCount();  //运维人员信息统计
}
