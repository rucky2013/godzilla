package cn.godzilla.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import cn.godzilla.common.Constant;
import cn.godzilla.common.ReturnCodeEnum;
import cn.godzilla.model.ClientConfig;
import cn.godzilla.model.PropConfig;
import cn.godzilla.model.RpcResult;

public interface PropConfigProviderService extends Constant{
	
	    /**
	     * 将项目pom.xml配置项  替换为所选配置
	     * @param project_code
	     * @param profile
	     * @param clientconfig 
	     * @param propconfigs 
	     * @return
	     * @throws IOException 
	     * @throws DocumentException 
	     * @throws Exception 
	     */
	    public RpcResult propToPom(String project_code, String srcUrl, String profile, String parentVersion, ClientConfig clientconfig) throws DocumentException, IOException, Exception;
}
