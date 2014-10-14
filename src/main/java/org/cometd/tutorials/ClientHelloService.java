package org.cometd.tutorials;



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

/*
 * The org.cometd.Client class is the server-side representation of a remote Bayeux client, 
 * that is, a client that connected successfully to the server using the Bayeux protocol.
 * 
 * It is possible to add extensions to a Client instance and add/remove listeners on a Client instance
 *
 */

/*
 * http://docs.cometd.org/reference/java_server.html#java_server_services
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
public class ClientHelloService
{
	@Inject
    private BayeuxServer bayeuxServer;
	
    @Session
    private LocalSession sender;

	@Listener("/service/event")
    public void process(ServerSession session, ServerMessage message)
    {

		System.out.printf("Received greeting '%s' from remote client %s  client: %s %n", message.getData(), session.getId() , message.getChannel());
                     
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