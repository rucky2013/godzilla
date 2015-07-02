package cn.godzilla.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.godzilla.model.OperateLog;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class OperateLogMapperTest {
	
	@Autowired
	OperateLogMapper dao ;
	
	@Test
	public void insert(){
		
		OperateLog model = new OperateLog();
		
		model.setProjectCode("zeus");
		model.setProfile("dev");
		model.setUserName("lizhongwei");
		dao.insert(model);
	}
	
	@Test
	public void insertSelective(){
		
		OperateLog model = new OperateLog();
		
		model.setProjectCode("cupid");
		model.setProfile("dev");
		model.setUserName("lizhongwei");
		dao.insertSelective(model);
	}
	
	@Test
	public void queryList(){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("projectCode", "cupid");
		map.put("profile", "dev");
		List<OperateLog> list = dao.queryList(map);
		
		for(OperateLog log:list){
			System.out.println( log.getProfile());
			System.out.println(log.getProjectCode());
			System.out.println(log.getUserName());
		}
		
	}
	
	
}
	