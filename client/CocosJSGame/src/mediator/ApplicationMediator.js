var ApplicationMediator = {
		RegisterEvent:function(){
			NotificationCenter.addObserver(ApplicationMediator, ApplicationMediator.onNeWorkOpen, NotificationTag.NOTIFY_ONOPEN, null);
			NotificationCenter.addObserver(ApplicationMediator, ApplicationMediator.onNeWorkClose, NotificationTag.NOTIFY_ONCLOSE, null);
			NotificationCenter.addObserver(ApplicationMediator, ApplicationMediator.onNeWorkError, NotificationTag.NOTIFY_ONERROR, null);
			NotificationCenter.addObserver(ApplicationMediator, ApplicationMediator.onShowLogoScene, NotificationTag.SHOWLOGOSCENE, null);
			NotificationCenter.addObserver(ApplicationMediator, ApplicationMediator.onShowHallScene, NotificationTag.SHOWHALLSCENE, null);
			NotificationCenter.addObserver(ApplicationMediator, ApplicationMediator.onShowDeskScene, NotificationTag.SHOWDESKSCENE, null);
		},
		onNeWorkOpen:function(data){
			ProtoLogin.loginDevice();
		},
		onNeWorkClose:function(data){
			
		},
		onNeWorkError:function(data){
			
		},
		onShowLogoScene:function(data){
			
		},
		onShowHallScene:function(data){
			// 转场特效持续两秒
			var transitionTime = 0.5;
			// 创建下一个场景
			var hallScene = new HallScene();
			// 使用下一个场景创建转场特效场景
			var transitionScene = new cc.TransitionProgressInOut(transitionTime, hallScene);
			// 替换运行场景为转场特效场景
			cc.director.runScene(transitionScene);
		},
		onShowDeskScene:function(data){
			var transitionTime = 0.5;
			// 创建下一个场景
			var deskScene = new DeskScene();
			// 使用下一个场景创建转场特效场景
			var transitionScene = new cc.TransitionProgressInOut(transitionTime, deskScene);
			// 替换运行场景为转场特效场景
			cc.director.runScene(transitionScene);
		},
}