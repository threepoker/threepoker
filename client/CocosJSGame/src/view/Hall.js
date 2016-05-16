var HallScene = cc.Scene.extend({
	onEnter:function () {
		this._super(); 
		var layer = new Hall();
		this.addChild(layer);
	}
});  
var Hall = cc.Layer.extend({
	sprite:null,
	ctor:function () {
		//////////////////////////////
		// 1. super init first
		this._super();
		var json = ccs.load(res.HALLJSON);
		var root = json.node;
		this.addChild(root);
		var btnQuick = ccui.helper.seekWidgetByName(root, "Button_quickStar");
		btnQuick.addTouchEventListener(this.btnQuickTouch, this);
		return true;
	},
	onEnter:function (){
		this._super();
	},

	onExit:function (){
		NotificationCenter.removeObserver(this);
		this._super();
	},
	btnQuickTouch:function(sender,type){
		if(type==ccui.Widget.TOUCH_ENDED){
			ProtoDesk.enterDeskReq(0);
		}
	},

});