package cn.godzilla.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.godzilla.model.MvnCmdLog;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class MvnCmdLogMapperTest {
	
	@Autowired
	MvnCmdLogMapper dao ;
	
	@Test
	public void insert(){
		MvnCmdLog mvnCmdLog = new MvnCmdLog();
		mvnCmdLog.setCommands("mvn clean install");
		mvnCmdLog.setProjectCode("zeus");
		mvnCmdLog.setUserName("zhongweili");
		mvnCmdLog.setResultInfo("Success");
		mvnCmdLog.setCreateTime(new Date());
		mvnCmdLog.setProfile("dev-test");
		dao.insert(mvnCmdLog);
	}
	
	@Test
	public void insertSelective(){
		
		MvnCmdLog mvnCmdLog = new MvnCmdLog();
		
		mvnCmdLog.setCommands("mvn clean deploy");
		
		mvnCmdLog.setProjectCode("cupid");
		
		mvnCmdLog.setUserName("zhongweili");
		
		mvnCmdLog.setResultInfo("Success");
		
		mvnCmdLog.setCreateTime(new Date());
		
		mvnCmdLog.setProfile("test");
		
		dao.insertSelective(mvnCmdLog);
	}
	
	@Test
	public void queryList(){
		
		List<MvnCmdLog> list = dao.queryList();
		
		for (MvnCmdLog mvnCmdLog:list) {
			
			System.out.println(mvnCmdLog.getCommands());
			System.out.println(mvnCmdLog.getProfile());
		}
	}
}
	