var ApplicationMediator = {
		RegisterEvent:function(){
			EventCenter.addObserver(ApplicationMediator, ApplicationMediator.onNeWorkOpen, NOTIFY_ONOPEN, null);
			EventCenter.addObserver(ApplicationMediator, ApplicationMediator.onNeWorkClose, NOTIFY_ONCLOSE, null);
			EventCenter.addObserver(ApplicationMediator, ApplicationMediator.onNeWorkError, NOTIFY_ONERROR, null);
		},
		onNeWorkOpen:function(data){
			GameProxy.loginReq("cocos", "js", "123456", "前端", 1, "微软", "1.0.1");
		},
		onNeWorkClose:function(data){
			
		},
		onNeWorkError:function(data){
			
		}
}