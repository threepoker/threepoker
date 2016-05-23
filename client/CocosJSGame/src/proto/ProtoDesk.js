var ProtoDesk = {
		RegisterEvent:function(){
			NotificationCenter.addObserver(ProtoDesk, ProtoDesk.enterDeskRes, ProtoTag.ENTERDESK, null);
			NotificationCenter.addObserver(ProtoDesk, ProtoDesk.getDeskInfoRes, ProtoTag.GETDESKINFO, null);
			NotificationCenter.addObserver(ProtoDesk, ProtoDesk.exitDeskRes, ProtoTag.EXITDESK, null);
		},
		enterDeskReq:function(level){
			var json = {};
			json.tag = ProtoTag.ENTERDESK;
			json.userId = User.userId;
			json.level = 1;
			NetWork.sendMSG(JSON.stringify(json));
		}, 
		enterDeskRes:function(data){	
			cc.log("ProtoDesk loginRes data = "+data);
			if(1 == data.status){//进桌成功
				ProtoDesk.getDeskInfoReq();
				NotificationCenter.postNotification(NotificationTag.SHOWDESKSCENE, null);
			}else{//进桌失败
				cc.log("enter desk fail");
			}
		},
		exitDeskReq:function(){
			var json = {};
			json.tag = ProtoTag.EXITDESK;
			json.userId = User.userId;
			NetWork.sendMSG(JSON.stringify(json));
		},
		exitDeskRes:function(data){
			NotificationCenter.postNotification(NotificationTag.SHOWHALLSCENE, null);
		},
		getDeskInfoReq:function(){
			var json = {};
			json.tag = ProtoTag.GETDESKINFO;
			json.userId = User.userId;
			NetWork.sendMSG(JSON.stringify(json));
		},
		getDeskInfoRes:function(data){
			
		},
} 