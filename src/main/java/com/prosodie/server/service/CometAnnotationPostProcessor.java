package com.prosodie.server.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;


import org.cometd.annotation.ServerAnnotationProcessor;
import org.cometd.bayeux.server.BayeuxServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.stereotype.Component;


@Component
public class CometAnnotationPostProcessor implements DestructionAwareBeanPostProcessor
{
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Inject
    private BayeuxServer bayeuxServer;
	
    private ServerAnnotationProcessor processor;

    /*
     * The lifecycle callback init() creates CometD's ServerAnnotationProcessor, which is then used during Spring's bean post processing phases.
     */
  
    @PostConstruct
    private void init()
    {
    	logger.info("init configurer");
        this.processor = new ServerAnnotationProcessor(bayeuxServer);
    }

    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException
    {
    	
        processor.processDependencies(bean);
        processor.processConfigurations(bean);
        processor.processCallbacks(bean);
        return bean;
    }
    

    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException
    {
        return bean;
    }

    public void postProcessBeforeDestruction(Object bean, String name) throws BeansException
    {
        processor.deprocessCallbacks(bean);
    }

    
}