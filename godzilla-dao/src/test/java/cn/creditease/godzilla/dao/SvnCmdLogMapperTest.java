package cn.creditease.godzilla.dao;

import java.util.Date;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.creditease.godzilla.dao.SvnCmdLogMapper;
import cn.creditease.godzilla.model.SvnCmdLog;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class SvnCmdLogMapperTest {
	
	@Autowired
	private SvnCmdLogMapper svnCmdLogMapper ;
	
	@Test
	public void insert(){
		
		SvnCmdLog log = new SvnCmdLog();
		log.setCommands("svn checkout http://svn.creditease.cn --username zhongweili2@creditease --password 123456");
		
		log.setCreateTime(new Date(System.currentTimeMillis()));
		log.setRealName("李忠伟");
		log.setRepositoryUrl("http://svn.creditease.cn");
		log.setUserName("zhongweili2");
		
		svnCmdLogMapper.insert(log);
		
	}

}
