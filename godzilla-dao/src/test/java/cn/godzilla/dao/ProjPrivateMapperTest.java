package cn.godzilla.dao;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.godzilla.model.ProjPrivate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class ProjPrivateMapperTest {
	
	@Autowired
	ProjPrivateMapper dao ;
	
	@Test
	public void insert(){
		
		ProjPrivate model = new ProjPrivate();
		
		model.setProjectCode("cupid");
		model.setIfVirtual(0);
		model.setUserName("zhongweili");
		model.setVirtualTruckUrl("svn://127.0.0.1");
		dao.insert(model);
	}
	
	@Test
	public void insertSelective(){
		
		ProjPrivate model = new ProjPrivate();
		
		model.setProjectCode("zeus");
		model.setIfVirtual(0);
		model.setUserName("zhongweili");
		model.setVirtualTruckUrl("svn://127.0.0.1");
		
		dao.insertSelective(model);
	}
	
	@Test
	public void update(){
		

		ProjPrivate model = new ProjPrivate();
		
		model.setId(4L);
		
		model.setIfVirtual(1);
		model.setVirtualTruckUrl("svn://localhost");
		
		dao.update(model);
	}
	@Test
	public void queryDetail(){
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("userName", "zhongweili");
		map.put("projectCode", "zeus");
		
		ProjPrivate info = dao.queryDetail(map);
		
		System.out.println(info.getProjectCode());
		System.out.println(info.getUserName());
		
	}
	
}
	