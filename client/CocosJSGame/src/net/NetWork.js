var MSGTAG = {
		GETCARDS:1,
}
var NetWork = {
		ws:null,
		create:function(){
			if(null != this.ws) return;
			this.ws = new WebSocket("ws://127.0.0.1:8080/ws");//("ws://120.25.196.22:8080/ddd/ws");  
			this.ws.onopen = function(e){ 
				cc.log("onopen = "+e);
				EventCenter.postNotification(NOTIFY_ONOPEN);
			}; 

			this.ws.onmessage = function(message){
				cc.log("onmessage= "+message.data);
				cc.log("tag = "+eval('('+data+')')["tag"]);
				EventCenter.postNotification(eval('('+data+')')["tag"],message.data);
			};	
			this.ws.onclose = function(e){
				cc.log("onclose = "+e);
				EventCenter.postNotification(NOTIFY_ONCLOSE);
				this.ws = null;
			};
			this.ws.onerror = function (e) {   
				cc.log("onerror = "+e);
				EventCenter.postNotification(NOTIFY_ONERROR);
			};
		},
		sendMSG:function(msg){
			if(null != this.ws){
				this.ws.send(msg);
			}
		}
}