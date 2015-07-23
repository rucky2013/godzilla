package cn.godzilla.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import cn.godzilla.common.Constant;
import cn.godzilla.model.PropConfig;
import cn.godzilla.model.RpcResult;

public interface PropConfigService extends Constant{
	
	 	public int insert(PropConfig record);

	    public int insertSelective(PropConfig record);
	    
	    public int update(PropConfig record);
	    
	    public PropConfig queryDetailById(long id);
	    
	    public PropConfig queryDetailByKey(Map<String, String> map);
	    
	    public List<PropConfig> queryList(Map<String, String> map);
	    
	    /**
	     * 将项目pom.xml配置项  替换为所选配置
	     * @param project_code
	     * @param profile
	     * @return
	     * @throws IOException 
	     * @throws DocumentException 
	     * @throws Exception 
	     */
	    public RpcResult propToPom(String project_code, String branchname, String profile) throws DocumentException, IOException, Exception;

	    /**
	     * 查询  某项目   各个环境下配置   并返回 json格式 数据
	     * @param projectCode
	     * @param propTest
	     * @param propQuasiProduct
	     * @param propProduct
	     */
		public void findPropByUsername(String projectCode, StringBuilder propTest, StringBuilder propQuasiProduct, StringBuilder propProduct);
}
