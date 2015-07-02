package cn.godzilla.dao;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.godzilla.model.ProjStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class ProjStatusMapperTest {
	
	@Autowired
	ProjStatusMapper dao ;
	
	@Test
	public void insert(){
		
		ProjStatus model = new ProjStatus();
		
		model.setProjectCode("cupid");
		model.setProfile("dev");
		model.setOperateStaff("zhongweili");
		dao.insert(model);
	}
	
	@Test
	public void insertSelective(){
		
		ProjStatus model = new ProjStatus();
		
		model.setProjectCode("zeus");
		model.setProfile("dev");
		model.setOperateStaff("zhongweili");
		dao.insertSelective(model);
	}
	
	@Test
	public void queryList(){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("projectCode", "cupid");
		map.put("operateStaff", "zhongweili");
		map.put("profile", "dev");
		
		ProjStatus model = null ;
		
		model = dao.queryDetail(map);
		
		System.out.println(model.getOperateStaff());
		System.out.println(model.getProfile());
		System.out.println(model.getProjectCode());
	}
	
	@Test
	public void update(){
		

		ProjStatus model = new ProjStatus();
		
		model.setId(6L);
		model.setProcessRate(25);
		model.setCurrentStatus(3);
		dao.update(model);
	}
}
	