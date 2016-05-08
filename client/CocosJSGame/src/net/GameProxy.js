var GameProxy = {
		RegisterEvent:function(){
			NotificationCenter.addObserver(GameProxy, GameProxy.loginRes, MSGTAG.LOGIN, null);
		},
		loginReq:function(userName,password,deviceId,nickName,channelId,model,version){
			var json = {};
			json.tag = MSGTAG.LOGIN;
			json.userName = userName;
			json.password = password;
			json.deviceId = deviceId;
			json.nickName = nickName;
			json.channelId = channelId;
			json.model = model;
			json.version = version;
			NetWork.sendMSG(JSON.stringify(json));
		}, 
		loginRes:function(data){			
			if(1 == data.tag){//登录成功
				
			}else{//登录失败
				
			}
		},
} 