package cn.godzilla.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.godzilla.model.PropConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class PropConfigMapperTest {
	
	@Autowired
	PropConfigMapper dao ;
	
	@Test
	public void insert(){
		PropConfig config = new PropConfig();
		
		config.setAuditor("lzw");
		config.setAuditorText("审核通过");
		config.setCreateBy("lizhongwei");
		config.setLastValue("lastvalue");
		config.setProfile("dev");
		config.setProjectCode("cupid");
		config.setProKey("jdbc.url");
		config.setProValue("localhost:3306/godzilla_db");
		config.setRemark("remark");
		config.setStatus(1);
		config.setUpdateTime(new Date());
		
		dao.insert(config);
	}
	
	@Test
	public void insertSelective(){
		
		PropConfig config = new PropConfig();
		
		config.setAuditor("lzw2");
		config.setAuditorText("审核未通过");
		config.setCreateBy("lizhongwei");
		config.setLastValue("lastvalue");
		config.setProfile("test");
		config.setProjectCode("cupid");
		config.setProKey("jdbc.url");
		config.setProValue("localhost:3306/godzilla_db");
		config.setRemark("remark");
		config.setStatus(1);
		config.setUpdateTime(new Date());
		
		
		dao.insertSelective(config);
	}
	
	@Test
	public void queryList(){
		
		Map<String,String> map = new HashMap<String, String>();
		
		map.put("projectCode", "cupid");
		map.put("profile", "dev");
		
		map.put("auditor", "lizw");
		map.put("status", "1");
		List<PropConfig> list = dao.queryList(map);
		
		for (PropConfig config:list) {
			
			System.out.println(config.getProKey());
			System.out.println(config.getProValue());
			System.out.println(config.getAuditor());
		}
	}
	
	@Test
	public void queryDetailById(){
		
		PropConfig config = dao.queryDetailById(1L);
		
		System.out.println(config.getProjectCode());
		
		System.out.println(config.getProKey());
	}
	
	@Test
	public void queryDetailByKey(){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("projectCode", "cupid");
		map.put("profile", "test");
		map.put("proKey", "jdbc.url");
		
		PropConfig config = dao.queryDetailByKey(map);
		
		System.out.println(config.getAuditor());
		System.out.println(config.getProfile());
		System.out.println(config.getProValue());
	}
	
	@Test
	public void update(){
		
		PropConfig config = new PropConfig();
		
		config.setAuditor("lizw");
		config.setProValue("localhost:8800");
		config.setId(1L);
		
		dao.update(config);
	}
}
	