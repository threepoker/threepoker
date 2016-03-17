var PokerCard = cc.Layer.extend({	
		//////////////////////////////
		// 1. super init first
	ctor:function () {
		this._super();
		this.cardFont_ = new cc.Sprite("#card_front.png");
		this.cardFont_.setVisible(true);
		this.addChild(this.cardFont_);
		
		this.cardBack_ = new cc.Sprite();
		this.cardBack_.setTexture("#card_back.png");
		this.addChild(this.cardBack_);
		
		this.setContentSize(this.cardFont_.getContentSize());
		
		this.setScale(0.5);
		return true;
	},
	onEnter:function (){
		this._super();

	},

	onExit:function (){
		this._super();
	},
	setCardValue:function(value){
		this.cardValue_ = value;
	},
	seeCard:function(isAnimation,delayTime,revert){

	},
	slotSeeCard:function(){
		
	},
	slotHideCard:function(){
		
	},
	getCard:function(){
		return this.cardValue_;		
	}

});