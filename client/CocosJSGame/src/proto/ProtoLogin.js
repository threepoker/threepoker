var ProtoLogin = {
		RegisterEvent:function(){
			NotificationCenter.addObserver(ProtoLogin, ProtoLogin.loginRes, ProtoTag.LOGIN, null);
			NotificationCenter.addObserver(ProtoLogin, ProtoLogin.getUserInfoRes, ProtoTag.GETUSERINFO, null);
		},
		loginDevice:function(){
			cc.sys.dump();
			ProtoLogin.loginReq("", "", cc.sys.os+cc.sys.browserType, "前端", 1, "微软", "1.0.1");
		},
		loginReq:function(userName,password,deviceId,nickName,channelId,model,version){
			var json = {};
			json.tag = ProtoTag.LOGIN;
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
			cc.log("ProtoLogin loginRes data = "+data);
			if(1 == data.status){//登录成功
				User.userId = data.userId;
				ProtoLogin.getUserInfoReq();
			}else{//登录失败
				ProtoLogin.loginDevice();
			}
		},
		getUserInfoReq:function(){
			var json = {};
			json.tag = ProtoTag.GETUSERINFO;
			json.userId = User.userId;
			NetWork.sendMSG(JSON.stringify(json));
		},
		getUserInfoRes:function(){
			NotificationCenter.postNotification(NotificationTag.SHOWHALLSCENE, null);
		}
		
} 