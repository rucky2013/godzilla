package cn.godzilla.dao;

import java.util.Date;



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
		log.setRealName("李忠伟");
		log.setRepositoryUrl("http://svn.godzilla.cn");
		log.setUserName("zhongweili2");
		
		svnCmdLogMapper.insert(log);
		
	}

}
