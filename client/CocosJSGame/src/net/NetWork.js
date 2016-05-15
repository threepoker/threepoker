
var NetWork = {
		ws:null,
		that:null,
		create:function(){ 
			if(null != this.ws) return;
			this.ws = new WebSocket("ws://localhost:8080/ws");//("ws://120.25.196.22:8080/ddd/ws");                
			
			this.ws.onopen = function(e){ 
				cc.log("onopen = "+e);
				NotificationCenter.postNotification(NOTIFY_ONOPEN);
			}; 

			this.ws.onmessage = function(message){ 
				cc.log("onmessage= "+message.data);
				
				var object = eval('('+message.data+')');
				var tag = object["tag"];
				cc.log("onmessage tag="+tag);
				NotificationCenter.postNotification(tag,object);
			};	
			this.ws.onclose = function(e){ 
				cc.log("onclose = "+e);
				NotificationCenter.postNotification(NOTIFY_ONCLOSE);
				this.ws = null;
			};
			this.ws.onerror = function (e) {   
				cc.log("onerror = "+e);
				NotificationCenter.postNotification(NOTIFY_ONERROR);
			};
		},
		sendMSG:function(msg){ 
			cc.log("sendMsg="+msg);
			if(null!=NetWork.ws){ 
				this.ws.send(msg);
			}else{
				NetWork.openNet();
			}
		},
		openNet:function(){
			NetWork.create();			
		}
}