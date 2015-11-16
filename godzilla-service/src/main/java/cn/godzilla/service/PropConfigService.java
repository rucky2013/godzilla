package cn.godzilla.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.PropBill;
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
	     * 查询  某项目   各个环境下配置   并返回 json格式 数据
	     * 审核状态的配置 status = 1
	     * @param projectCode
	     * @param propTest
	     * @param propQuasiProduct
	     * @param propProduct
	     */
		public void findPropByProjectCode(String projectCode, StringBuilder propTest, StringBuilder propQuasiProduct, StringBuilder propProduct);
	
		
		/**
		 * 获取项目的  所有审核配置项 
		 * 审核状态的配置 status = 1
		 * 根据 projectcode 和 profile
		 * 返回list
		 * @param project_code
		 * @param profile
		 * @return list
		 */
		public List<PropConfig> getPropConfigsByProjectcodeAndProfile(String project_code, String profile) ;
		
		/**
		 * 添加 某项目 各个环境下配置   未审核
		 * 审核状态为 status = 0
		 * @param projectCode
		 * @param propTest
		 * @param propQuasiProduct
		 * @param propProduct
		 * @return 
		 */
		public ReturnCodeEnum addNotVerifyProp(String projectCode, String propTest, String propQuasiProduct, String propProduct);
		
		/**
		 * 查询 所有环境 并加上all选项
		 * @return
		 */
		public Map<String, String> queryAllProfile();

		/**
		 * 查询 /显示  某项目  某人  某环境下  的所有审核配置
		 * @param projectCode
		 * @param createBy
		 * @param profile
		 * @param verifyStatus
		 * @return
		 */
		public List<PropConfig> queryByProjectcodeAndCreatebyAndProfileAndStatus(String projectCode, String createBy, String profile, String verifyStatus);
		
		/**
		 * 查询 /显示  某项目  某人  某环境下  的所有审核配置
		 * @param projectCode
		 * @param createBy
		 * @param profile
		 * @param verifyStatus
		 * @return
		 */
		List<PropConfig> queryByProjectcodeAndCreatebyAndProfileGroupBy(String projectCode, String createBy, String profile, String status);
		/**
	     * 查询  某项目   各个环境下配置   并返回 json格式 数据
	     * 
	     * @param projectCode
	     * @param propTest
	     * @param propQuasiProduct
	     * @param propProduct
		 * @param billId 
	     */
		public void findPropByCreatebyAndProjectcodeAndProfileAndStatus(String createBy, String projectCode, String profile, StringBuilder propTest, StringBuilder propQuasiProduct, StringBuilder propProduct, String status, Long billId);

		@Deprecated
		public ReturnCodeEnum verifyPropByCreatebyAndProjectcodeAndProfile(String createBy, String projectCode, String profile, String status, String auditor_text);
		
		/**
		 * 审核通过  某人 某项目 某环境下的 所有配置
		 * @param createBy
		 * @param projectCode
		 * @param profile
		 * @param status
		 * @param auditor_text
		 * @return
		 */
		public ReturnCodeEnum verifyPropByCreatebyAndProjectcodeAndALLProfile(String createBy, String projectCode, String profile, String status, String auditor_text, Long billId);

		/**
		 * 排序 配置
		 * @param propSort
		 * @return
		 */
		public ReturnCodeEnum resortPropById(String propSort);
		
		/**
		 * 20151110 添加propbill表,重写配置审核
		 * @param projectCode
		 * @return
		 */
		public List<PropBill> queryAllPropBill(String projectCode);

		//=======================
		/**
		 * 将项目pom.xml配置项  替换为所选配置
		 * @param project_code
		 * @param webPath
		 * @param profile
		 * @param parentVersion
		 * @param clientconfig
		 * @return
		 * @throws DocumentException
		 * @throws IOException
		 * @throws Exception
		 */
		 public RpcResult propToPom(String project_code, String webPath, String profile, String parentVersion, ClientConfig clientconfig) throws DocumentException, IOException, Exception;
		
}
