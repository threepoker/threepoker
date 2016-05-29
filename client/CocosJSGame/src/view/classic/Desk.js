var DeskScene = cc.Scene.extend({
	onEnter:function () {
		this._super(); 
		var layer = new Desk();
		this.addChild(layer);
	}
});  
var Desk = cc.Layer.extend({
	sprite:null,
	posDiff:0,
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
		ProtoDesk.getDeskInfoReq();
		NotificationCenter.addObserver(this, this.getDeskInfoRes, ProtoTag.GETDESKINFO, null);
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
		var selfPos = 0;
		var i = 0;
		for(i=0; i<data.userNum; i++){
			if (User.userId == data[i+"_userId"]) {
				selfPos = data[i+"_userPos"];
				break;
			} 
		}
		for(i=0; i<5; i++){
			if((i+selfPos)%5==2){
				this.posDiff = i;
				break;
			}
		}
	},
});