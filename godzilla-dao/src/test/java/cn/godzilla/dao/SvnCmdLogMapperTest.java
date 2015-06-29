package cn.godzilla.dao;

import java.util.Date;



import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.godzilla.model.SvnCmdLog;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class SvnCmdLogMapperTest {
	
	@Autowired
	private SvnCmdLogMapper svnCmdLogMapper ;
	
	@Test
	public void insert(){
		
		SvnCmdLog log = new SvnCmdLog();
		log.setCommands("svn checkout http://svn.godzilla.cn --username zhongweili2@godzilla.cn --password 123456");
		
		log.setCreateTime(new Date(System.currentTimeMillis()));
		log.setRealName("lizhongwei");
		log.setRepositoryUrl("svn://127.0.0.1");
		log.setUserName("zhongweili2");
		
		svnCmdLogMapper.insert(log);
		
	}
	
	@Test 
	public void insertSelective(){
		SvnCmdLog log = new SvnCmdLog();
		
		log.setCommands("svn checkout http://svn.godzilla.cn --username zhongweili2@godzilla.cn --password 123456");
		
		log.setCreateTime(new Date(System.currentTimeMillis()));
		log.setRealName("zhongweili");
		log.setRepositoryUrl("svn://localhost");
		log.setUserName("zhongweili2");
		
		svnCmdLogMapper.insertSelective(log);
		
	}
	
	@Test
	public void queryList(){
		
		List<SvnCmdLog> list = svnCmdLogMapper.queryList();
		
		for(SvnCmdLog log:list){
			
			System.out.println(log.getCommands());
			System.out.println(log.getRealName());
			System.out.println(log.getRepositoryUrl());
			
		}
	}

}
