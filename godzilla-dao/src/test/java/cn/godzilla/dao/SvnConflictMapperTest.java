package cn.godzilla.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.godzilla.model.SvnConflict;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class SvnConflictMapperTest {
	
	@Autowired
	SvnConflictMapper dao ;
	
	@Test
	public void insert(){
		SvnConflict log = new SvnConflict();
		
		
		log.setUserName("zhongweili");
		log.setBranchUrl("svn://localhost/branch");
		log.setFileName("svn://localhost/branch/java/src/main/test.java");
		log.setProjectCode("zeus");
		log.setResultInfo("success");
		log.setStatus(1);
		log.setTrunkUrl("svn://localhost/trunk");
		
		dao.insert(log);
	}
	
	@Test
	public void insertSelective(){
		
		SvnConflict log = new SvnConflict();
		
		log.setUserName("lizhongwei");
		log.setBranchUrl("svn://localhost/branch");
		log.setFileName("svn://localhost/branch/java/src/main/test2.java");
		log.setProjectCode("cupid");
		log.setResultInfo("success");
		log.setStatus(1);
		log.setTrunkUrl("svn://localhost/trunk");
		
		dao.insertSelective(log);
	}
	
	@Test
	public void queryList(){
		
		Map<String,String> map = new HashMap<String, String>();
		
		map.put("projectCode", "cupid");
		map.put("branchUrl", "svn://localhost/branch");
		
		List<SvnConflict> list = dao.queryList(map);
		
		for (SvnConflict log:list) {
			
			System.out.println(log.getFileName());
		}
	}
}
	