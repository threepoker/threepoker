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
		NotificationCenter.addObserver(ProtoDesk, ProtoDesk.getDeskInfoRes, ProtoTag.GETDESKINFO, null);
		this._super();
	},

	onExit:function (){
		NotificationCenter.removeObserver(this);
		this._super();
	},
	
	btnBackTouch:function(sender,type){
		if(type==ccui.Widget.TOUCH_ENDED){
			ProtoDesk.exitDeskReq();
		}
	},
	getDeskInfoRes:function(data){
		
	},
});