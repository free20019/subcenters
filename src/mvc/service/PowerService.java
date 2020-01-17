package mvc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper.JacksonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class PowerService {
	@Autowired
	protected JdbcTemplate jdbcTemplate;
    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

	public String getPower(String username) {
		String sql = "select qx,qxlist from tb_user where username = ?";
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql,username);
		String qx = String.valueOf(queryForList.get(0).get("qx"));
		String qxlist = String.valueOf(queryForList.get(0).get("qxlist"));
		List parse = (List)JSON.parse(qx);
		List parse1 = (List)JSON.parse(qxlist);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("qx", parse);
		map.put("qxlist", parse1);
		return jacksonUtil.toJson(map);
	}

}
