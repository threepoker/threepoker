var DeskScene = cc.Scene.extend({
	onEnter:function () {
		this._super(); 
		var layer = new Desk();
		this.addChild(layer);
	}
});  
var Desk = cc.Layer.extend({
	sprite:null,
	difPos:0,
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
		NotificationCenter.addObserver(this, this.onNotifyEnterDeskRes, ProtoTag.NOTIFYENTERDESK, null);
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
				this.difPos = i;
				break;
			}
		}
		//cc.log("selfPos = "+selfPos+"  difPos="+this.difPos+" userNum="+data.userNum);
		for(i=0; i<data.userNum; i++){
			var deskUserData = new DeskUserData();
			deskUserData.id = data[i+"_userId"];
			deskUserData.nickName = data[i+"_userNickName"];
			deskUserData.gold = data[i+"_userGold"];
			deskUserData.pos = data[i+"_userPos"];
			deskUserData.putInGold = data[i+"_userPutInGold"];
			deskUserData.isPlaying = data[i+"_userIsPlaying"];
			deskUserData.isSeeCard = data[i+"_userIsSeeCard"];
			deskUserData.isGiveUp = data[i+"_userIsGiveUp"];
			deskUserData.difPos = this.difPos;
			this.addUser(deskUserData);
		}
	},
	onNotifyEnterDeskRes:function(data){
			var deskUserData = new DeskUserData();
			deskUserData.id = data["userId"];
			deskUserData.pos = data["pos"];
			deskUserData.nickName = data["nickName"];
			deskUserData.gold = data["gold"];
			deskUserData.difPos = this.difPos;
			cc.log("id="+deskUserData.id+"  nickName="+deskUserData.nickName);
			this.addUser(deskUserData);
	},
	addUser:function(deskUserData){
		var deskUser = new DeskUser(deskUserData);
		deskUser.tag = deskUserData.id;
		this.addChild(deskUser);
	},
	removeUser:function(id){
		if(null != this.getChildByTag(id)){
			this.removeChildByTag(id);
		}
	}
});