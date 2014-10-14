	

	/*
	 * https://github.com/cometd/cometd/blob/master/cometd-javascript/examples-jquery/src/main/webapp/jquery-examples/chat/chat.js
	 */

	 

      $(document).ready(function()
      {

    	
    	  $('#username').prop('disabled', false);
     	  $('#login').prop('disabled', false);
     	  $('#logout').prop('disabled', true);
     	 	
     	  var client = new Client();

     	  $('#push').click(function()    { client.send(); }); 
     	  $('#login').click(function()   { client.login($('#username').val()); }); 
    	  $('#logout').click(function()  { client.logout(); }); 
    	  
    	  
    	  
    	  $(window).unload(function()  { client.logout(); }); 
    	  
 
     });

       
     
   
	/**
	 * Communication between client (**page scope**) and server 
	 * 
	 * 
	 * 
	 */
    function Client()
    {
        var _self 			= this;
        
        var _status 		= false;
        var _subscription	= null;

        var _username 		= null; 
        
        
        
        var url = location.protocol + "//" + location.host + config.contextPath + "/cometd";
        $.cometd.configure
        (
        {
             url: url,
             logLevel: 'debug'
        });
        
        
        $.cometd.addListener('/meta/connect',   _connect);
        $.cometd.addListener('/meta/handshake', _handshake);
       
  
        this.login = function(username)
        {
        	if (_status)
        	{
        		status('you already are logged !!');
        	 
        	}
        	else
        	{
        		  _username = username; 
        		  
        		  $.cometd.handshake();
        	}
        	
        	

        };
        
        this.logout = function()
        {
        	if (_status)
        	{
        		 $.cometd.disconnect();
        	 
        	}

        };
        
        
        this.send = function()
        {
        	if (!_status)
        	{
        		status('cannot send because you are disconnected !!');
        	 
        	}
        	else
        	{
        		$.cometd.publish('/service/event','Pushed');
        	}
         
        };

        this.receive = function(message)
        {
        	$('#response').text(message.data);
        };
        
        
        function status(message)
        {
        	$('#status').append('<div>' + message + '</div>');
        };

       
        function _unsubscribe()
        {
        	if (_subscription)
            {
                $.cometd.unsubscribe(_subscription);
            }
        	
        	_subscription = null;
            
        }

        function _subscribe(channel)
        {
            _subscription = $.cometd.subscribe(channel, _self.receive);
       }

        function _handshaked()
        {
        	
        	 status('handshake successful');
        	 
        	 $('#username').prop('disabled', true);
        	 $('#login').prop('disabled', true);
        	 $('#logout').prop('disabled', false);
        	 
        	 
        	 _subscribe('/event/' + _username);
        }

        function _connected()
        {
        	
        	  status('connected');
        	  _status 	= true;
           
        }

        function _disconnected()
        {
            status('disconnected');
            _status = false;
            
            $('#username').prop('disabled', false);
       	 	$('#login').prop('disabled', false);
       	 	$('#logout').prop('disabled', true);
          
        }

        function _closed()
        {
        	 status('closed');
        	 _status = false;
           
        }

        /*
         * Be warned that the use of the _connect() along with the _connected status variable can result in your code 
         * to be called more than once if, for example, you experience temporary network failures or if the server restarts.
         */
        function _connect(message)
        {
        		/*
        		 * One small caveat with the /meta/connect channel is that /meta/connect is also used for polling the server. 
        		 * Therefore, if a disconnect is issued during an active poll, the server returns the active poll and this triggers the /meta/connect listener
        		 */
        		if ($.cometd.isDisconnected())
        		{
        			 _disconnected();
        		}
        		else
        		{
	                var t = _status;
	                _status = message.successful === true;
	                if (!t && _status)
	                {
	                    _connected();
	                }
	                else if (t && !_status)
	                {
	                    _disconnected();
	                }
        		}
        }

        function _handshake(message)
        {
            if (message.successful)
            {
                _handshaked();
            }
        }

        
      
        
        
    }
    

