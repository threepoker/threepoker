var GameProxy = {
		RegisterEvent:function(){
			NotificationCenter.addObserver(GameProxy, GameProxy.loginRes, MSGTAG.LOGIN, null);
			NotificationCenter.addObserver(GameProxy, GameProxy.getUserInfoRes, MSGTAG.GETUSERINFO, null);
		},
		loginDevice:function(){
			GameProxy.loginReq("", "", "123456", "前端", 1, "微软", "1.0.1");
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
			cc.log("GameProxy loginRes data = "+data);
			if(MSGTAG.LOGIN == data.tag){//登录成功
				User.userId = data.userId;
				GameProxy.getUserInfoReq();
			}else{//登录失败
				GameProxy.loginDevice();
			}
		},
		getUserInfoReq:function(){
			var json = {};
			json.tag = MSGTAG.GETUSERINFO;
			json.userId = "";
			NetWork.sendMSG(JSON.stringify(json));
		},
		getUserInfoRes:function(){
			// 转场特效持续两秒
			var transitionTime = 1;
			// 创建下一个场景
			var hallScene = new HallScene();
			// 使用下一个场景创建转场特效场景
			var transitionScene = new cc.TransitionProgressInOut(transitionTime, hallScene);
			// 替换运行场景为转场特效场景
			cc.director.runScene(transitionScene);
		}
		
} 