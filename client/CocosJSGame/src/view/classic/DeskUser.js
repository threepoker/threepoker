var DeskUserData = function(){
	this.userId = 0;
	this.nickName = "";
	this.pos = 0;
	this.difPos = 0;
	this.gold = 0;
	this.putInGold = 0;
	this.isPlaying = false;
	this.isSeeCard = false;
	this.isGiveUp = true;
};
var DeskUser = cc.Layer.extend({
	userData:null,	
	ctor:function (deskUserData) {
		//////////////////////////////
		// 1. super init first
		this._super();
		this.userData = deskUserData;
		var json = ccs.load(res.DESKPLAYERJSON);
		this.root = json.node;
		this.addChild(this.root);
		this.initBetChipPos();
		this.labelNickName = ccui.helper.seekWidgetByName(this.root, "Label_nickName");
		this.setNickName(deskUserData.nickName);
		this.labelTotalGold = ccui.helper.seekWidgetByName(this.root, "Label_gold");
		this.setGold(deskUserData.gold);
		this.labelPutInGold = ccui.helper.seekWidgetByName(this.root, "Label_bet");
		this.setPutInGold(deskUserData.putInGold);
		return true;
	},
	onEnter:function (){
		this._super();
	},

	onExit:function (){
		this._super();
	},
	initBetChipPos:function(){
		var betBg = ccui.helper.seekWidgetByName(this.root, "Image_bet_bg");	
		switch ((this.userData.pos+this.userData.difPos)%5) {
		case 0:
			betBg.setPosition(105,447);
			this.root.setPosition(236, 128);
			break;
		case 1:
			betBg.setPosition(219,169);
			this.root.setPosition(29, 218);
			break;
		case 2:
			betBg.setPosition(91,190);
			this.root.setPosition(548, 106);
			break;
		case 3:
			betBg.setPosition(-50,187);
			this.root.setPosition(108, 206);
			break;
		case 4:
			betBg.setPosition(-81,101);
			this.root.setPosition(104, 476);
			break;
		default:
			break;
		}
	},
	
	setNickName:function(name){
		this.labelNickName.setString(name);
	},
	setGold:function(gold){
		this.userData.gold = gold;
		this.labelTotalGold.setString(gold+"");
	},
	setPutInGold:function(gold){
		this.userData.putInGold = gold;
		this.labelPutInGold.setString(gold+"");
	},
	setIsPlaying:function(status){
		this.userData.isPlaying = status;
	},
	setIsSeeCard:function(status){
		this.userData.isSeeCard = status;
	},
	setIsGiveUp:function(status){
		this.userData.isGiveUp = status;
	}
});