var DeskScene = cc.Scene.extend({
	onEnter:function () {
		this._super(); 
		var layer = new Desk();
		this.addChild(layer);
	}
});  
var Desk = cc.Layer.extend({
	sprite:null,
	ctor:function () {
		//////////////////////////////
		// 1. super init first
		this._super();
		var json = ccs.load(res.DESKJSON);
		var root = json.node;
		this.addChild(root)
		var btnBack = ccui.helper.seekWidgetByName(root, "Button_back");
		btnBack.addTouchEventListener(this.btnBackTouch, this);

		return true;
	},
	onEnter:function (){
		this._super();
	},

	onExit:function (){
		NotificationCenter.removeObserver(this);
		this._super();
	},
	btnBackTouch:function(sender,type){
		if(type==ccui.Widget.TOUCH_ENDED){
			// 转场特效持续两秒
			var transitionTime = 0.5;
			// 创建下一个场景
			var hallScene = new HallScene();
			// 使用下一个场景创建转场特效场景
			var transitionScene = new cc.TransitionProgressInOut(transitionTime, hallScene);
			// 替换运行场景为转场特效场景
			cc.director.runScene(transitionScene);
		}
	},

});