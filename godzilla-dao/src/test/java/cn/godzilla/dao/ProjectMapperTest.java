package cn.godzilla.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.godzilla.model.Project;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationContext.xml"})
public class ProjectMapperTest {
	
	@Autowired
	ProjectMapper dao ;
	
	@Test
	public void insert(){
		
		
		
		Project project = new Project();
		
		project.setCreateBy("zhongweili2");
		project.setManager("zhongweili2");
		project.setProjectCode("cc");
		project.setProjectName("丘比特");
		project.setRepositoryUrl("svn://localhost/cupid");
		
		int result = dao.insert(project);
		
		Assert.assertTrue(result > 0);
		
	}
	
	@Test
	public void insertSelective(){
		
		Project project = new Project();
		
		project.setCreateBy("zhongweili2");
		project.setManager("zhongweili2");
		project.setProjectCode("ee");
		project.setProjectName("丘比特");
		project.setRepositoryUrl("svn://localhost/zeus");
		
		int result = dao.insertSelective(project);
		
		Assert.assertTrue(result > 0);
		
	}
	
	@Test
	public void updateByProCode(){
		
		Project project = new Project();
		
		project.setCreateBy("kk");
		project.setManager("kk");
		project.setProjectCode("ee");
		project.setProjectName("丘比特");
		project.setRepositoryUrl("svn://localhost/zus");
		
		boolean flag = dao.updateByProCode(project);
		
		Assert.assertTrue(flag);
		
	}
	
	@Test
	public void qureyByProCode(){
		
		Project project = dao.qureyByProCode("cc");
		System.out.println(project.getCreateBy());
	}
	
	@Test
	public void queryAll(){
		
		List<Project> list = dao.queryAll();
		
		System.out.println(list.size());
		
	}


}
