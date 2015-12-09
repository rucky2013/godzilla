package com.rpcf.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.rpcf.spring.ReferenceBean;

public class RpcfNamespaceHandler extends NamespaceHandlerSupport {
	
	public void init() {
		registerBeanDefinitionParser("reference", new RpcfBeanDefinationParser(ReferenceBean.class, false));
	}
}
