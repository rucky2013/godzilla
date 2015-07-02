package cn.godzilla.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.godzilla.model.SvnBranchConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class SvnBranchConfigMapperTest {
	
	@Autowired
	SvnBranchConfigMapper dao ;
	
	@Test
	public void insert(){
		SvnBranchConfig config = new SvnBranchConfig();
		
		
		config.setBranchName("cupid-bug");
		config.setBranchUrl("svn://localhost/cupid");
		config.setCreateBy("zhongweili");
		config.setCreateTime(new Date());
		config.setCreateVersion("1.0.0");
		config.setCurrentVersion("2.0.0");
		config.setProjectCode("cupid");
		config.setStatus(0);
		
		dao.insert(config);
	}
	
	@Test
	public void update(){
		
		SvnBranchConfig config = new SvnBranchConfig();
		
		config.setId(1L);
		config.setBranchName("cupid-bug");
		config.setBranchUrl("svn://localhost/cupid");
		config.setCreateBy("zhongweili");
		config.setCreateTime(new Date());
		config.setCreateVersion("1.1.0");
		config.setCurrentVersion("2.1.0");
		config.setProjectCode("cupid");
		config.setStatus(0);
		
		dao.update(config);
	}
	
	@Test
	public void queryListByProjectCode(){
		
		List<SvnBranchConfig> list = dao.queryListByProjectCode("cupid");
		
		for (SvnBranchConfig config:list) {
			
			System.out.println(config.getBranchName());
		}
	}
}
	