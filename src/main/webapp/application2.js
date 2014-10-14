require(['dojox/cometd', 'dojo/dom', 'dojo/dom-construct', 'dojo/domReady!']
, function(cometd, dom, doc)
{
    cometd.configure({
        url: location.protocol + '//' + location.host + config.contextPath + '/cometd',
        logLevel: 'info'
    });

    cometd.addListener('/meta/handshake', function(message)
    {
        if (message.successful)
        {
            dom.byId('status').innerHTML += '<div>CometD handshake successful</div>';
            /*
            cometd.subscribe('/response', function(message)
    	     {

    	                // Find the div for the given stock symbol
    	                var id = 'response';
    	                var response = dom.byId(id);
    	                
    	                response.innerHTML = '<span>' + message.data + '</span>';
    	      });
    	      */
            cometd.subscribe('/hello', function(message)
           	     {

           	                // Find the div for the given stock symbol
           	                var id = 'response';
           	                var response = dom.byId(id);
           	                
           	                response.innerHTML = '<span>' + message.data + '</span>';
           	      });
            
        }
        else
        {
            dom.byId('status').innerHTML += '<div>CometD handshake failed</div>';
        }
    });
    
    cometd.addListener('/meta/connect', function(message)
    	    {
    	        if (message.advice.multiple-clients)
    	        {
    	            alert('multiple-clients');
    	        }
    	        
    	    });


    dom.byId('greeter').onclick = function()
    {
        cometd.publish('/hello', 'Hello, World');
    };

    //
    cometd.handshake();
});

/*

-- client half-object on the remote client side --
The client creates a client session to establish a Bayeux communication with the server, 
and this allows the client to publish and receive messages.

*/
