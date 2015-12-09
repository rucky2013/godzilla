package com.rpcf.spring.schema;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class RpcfBeanDefinationParser implements BeanDefinitionParser {

	private final Class<?> beanClass;
	
	private final boolean required;
	
	public RpcfBeanDefinationParser(Class<?> beanClass, boolean required) {
		this.beanClass = beanClass;
		this.required = required;
	}
	
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		return parse(element, parserContext, beanClass, required);
	}

	private static BeanDefinition parse(Element element, ParserContext parserContext, Class<?> beanClass, boolean required) {
		
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String id = element.getAttribute("id");
		
		if(id != null && id.length() >0) {
			if(parserContext.getRegistry().containsBeanDefinition(id)) {
				throw new IllegalStateException("Duplicate spring bean id " + id);
			}
			parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
			beanDefinition.getPropertyValues().addPropertyValue("id", id);
		}
		
		String interfaceName = element.getAttribute("interface");
		if(interfaceName !=null && interfaceName.length() >0) {
			beanDefinition.getPropertyValues().add("interfaceName", interfaceName);
		}
		
		return beanDefinition;
	}
}
