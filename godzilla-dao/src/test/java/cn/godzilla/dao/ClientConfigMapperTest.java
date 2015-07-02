package cn.godzilla.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.godzilla.model.ClientConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class ClientConfigMapperTest {
	
	@Autowired
	ClientConfigMapper dao ;

	@Test
	public void queryDetail(){
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("projectCode", "cupid");
		
		map.put("profile", "dev");
		
		ClientConfig config = dao.queryDetail(map);
		
		if(config != null){
			
			System.out.println(config.getProfile());
			System.out.println(config.getProjectCode());
			System.out.println(config.getRemoteIp());
			System.out.println(config.getId());
			System.out.println(config.getStatus());
		}
		
	}
}
