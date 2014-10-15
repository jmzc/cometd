package com.prosodie.server.service;



import javax.inject.Inject;

import org.cometd.annotation.Listener;
import org.cometd.annotation.Service;
import org.cometd.annotation.Session;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.bayeux.server.LocalSession;
import org.cometd.bayeux.server.ServerChannel;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/*
 * 
 * A CometD service is a Java class that allow a developer to specify the code to run when Bayeux messages 
 * are received on Bayeux channels.
 *
 * While normally it is better to use a Bayeux service to implement your business logic, 
 * a MessageListener may be used to count the number of messages,
 * as well as to measure the latency between when a message was sent and when it was received, 
 * for example in collaboration with the timesync extension.
 */
@Service
public class ServerService
{
	
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Inject
    private BayeuxServer bayeuxServer;
	
    @Session
    private LocalSession sender;

	@Listener("/service/event")
    public void process(ServerSession session, ServerMessage message)
    {

		
		logger.info("Received greeting {}' from remote client {}  client: {}", message.getData(), session.getId() , message.getChannel());
                     
        String channelName = "/event/jmzc";
        
        // Initialize the channel, making it persistent and lazy
        bayeuxServer.createChannelIfAbsent(channelName, new ConfigurableServerChannel.Initializer()
        {
            public void configureChannel(ConfigurableServerChannel channel)
            {
                channel.setPersistent(true);
                channel.setLazy(true);
            }
        });

       
        
        // Convert the Update business object to a CometD-friendly format
        /*
        Map<String, Object> data = new HashMap<String, Object>(4);
        data.put("symbol", update.getSymbol());
        data.put("oldValue", update.getOldValue());
        data.put("newValue", update.getNewValue());
        */
        
        String data = "Hello client !!!" + System.currentTimeMillis();

        // Publish to all subscribers
        ServerChannel channel = bayeuxServer.getChannel(channelName);
        channel.publish(sender, data);
        
        
        /*
         * Applications that want to deliver messages to a specific client can do so by looking up its correspondent server session and 
         * delivering the message using ServerSession.deliver(...).
         */
        /*
        System.out.printf("Send %s to %s %n", data, channelName);
        session.deliver(session, channelName, data, null);
        */
        

    }
}