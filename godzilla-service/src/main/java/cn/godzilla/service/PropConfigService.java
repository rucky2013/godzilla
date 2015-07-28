package cn.godzilla.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
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
		 * 获取项目的  所有审核配置项 
		 * @param project_code
		 * @param profile
		 * @return list
		 */
		public List<PropConfig> getPropConfigsByProjectcodeAndProfile(String project_code, String profile) ;
		
	    /**
	     * 查询  某项目   各个环境下配置   并返回 json格式 数据
	     * @param projectCode
	     * @param propTest
	     * @param propQuasiProduct
	     * @param propProduct
	     */
		public void findPropByUsername(String projectCode, StringBuilder propTest, StringBuilder propQuasiProduct, StringBuilder propProduct);
		/**
		 * 添加或者更改 某项目 各个环境下配置 
		 * @param projectCode
		 * @param propTest
		 * @param propQuasiProduct
		 * @param propProduct
		 * @return 
		 */
		public ReturnCodeEnum addOrUpdateProp(String projectCode, String propTest, String propQuasiProduct, String propProduct);
		
		/**
		 * 查询 所有环境 并加上all选项
		 * @return
		 */
		public Map<String, String> queryAllProfile();

		public List<PropConfig> queryByProjectcodeAndCreatebyAndProfile(String projectCode, String createBy, String profile, String verifyStatus);
		
		/**
		 * 审核通过 某配置
		 * 1.验证当前用户 是否有修改项目的权限
		 * 2.审核 某编写人 某项目 某环境下的  所有配置
		 * @param createBy
		 * @param projectCode 
		 * @param profile 
		 * @param status 
		 * @return
		 */
		public ReturnCodeEnum verifyPropById(String createBy, String projectCode, String profile, String status, String auditor_text);
}
