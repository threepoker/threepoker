var DeskUserData = function(){
	userId = 0;
	pos = 0;
	gold = 0;
	putInGold = 0;
	isPlaying = false;
	isSeeCard = false;
	isGiveUp = true;
};
var DeskUser = cc.Layer.extend({
	userData:null,	
	ctor:function (userId,pos) {
		//////////////////////////////
		// 1. super init first
		this._super();
		this.userData = new DeskUserData();
		this.userData.userId = userId;
		this.userData.pos = pos;
		var json = ccs.load(res.DESKPLAYERJSON);
		this.root = json.node;
		this.addChild(this.root);
		this.initBetChipPos();
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
		switch (this.pos) {
		case 0:
			betBg.setPosition(100, 100);
			break;
		case 1:
			betBg.setPosition(100, 100);
			break;
		case 2:
			betBg.setPosition(100, 100);
			break;
		case 3:
			betBg.setPosition(100, 100);
			break;
		case 4:
			betBg.setPosition(100, 100);
			break;
			betBg.setPosition(100, 100);
		default:
			break;
		}
	},
	
	
	setPutInGold:function(gold){
		this.userData.putInGold = gold;
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