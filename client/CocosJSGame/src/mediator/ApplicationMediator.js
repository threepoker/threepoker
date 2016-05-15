var ApplicationMediator = {
		RegisterEvent:function(){
			NotificationCenter.addObserver(ApplicationMediator, ApplicationMediator.onNeWorkOpen, NOTIFY_ONOPEN, null);
			NotificationCenter.addObserver(ApplicationMediator, ApplicationMediator.onNeWorkClose, NOTIFY_ONCLOSE, null);
			NotificationCenter.addObserver(ApplicationMediator, ApplicationMediator.onNeWorkError, NOTIFY_ONERROR, null);
		},
		onNeWorkOpen:function(data){
			GameProxy.loginDevice();
		},
		onNeWorkClose:function(data){
			
		},
		onNeWorkError:function(data){
			
		}
}