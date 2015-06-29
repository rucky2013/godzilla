package cn.godzilla.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.godzilla.model.SvnChangeLog;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class SvnChangeLogMapperTest {
	
	@Autowired
	SvnChangeLogMapper dao ;
	
	@Test
	public void insert(){
		SvnChangeLog log = new SvnChangeLog();
		
		log.setCreateTime(new Date());
		
		log.setFileName("truck/cupid/cupid-web/src/org/apache/dao/Test.java");
		
		log.setRepositoryUrl("svn://localhost");
		
		log.setResultInfo("SUCCESS");
		
		log.setResultStatus(1);
		
		log.setType(1);
		
		log.setUserName("zhongweili");
		
		
		dao.insert(log);
	}
	
	@Test
	public void insertSelective(){
		
		SvnChangeLog log = new SvnChangeLog();
		
		log.setCreateTime(new Date());
		
		log.setFileName("truck/cupid/cupid-web/src/org/apache/dao/Test2.java");
		
		log.setRepositoryUrl("svn://127.0.0.1");
		
		log.setResultInfo("false");
		
		log.setResultStatus(2);
		
		log.setType(2);
		
		log.setUserName("lizhongwei");
		
		dao.insertSelective(log);
	}
	
	@Test
	public void queryList(){
		
		List<SvnChangeLog> list = dao.queryList();
		
		for (SvnChangeLog log:list) {
			
			System.out.println(log.getRepositoryUrl());
			System.out.println(log.getFileName());
		}
	}
}
	