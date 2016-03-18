var PokerCard = cc.Layer.extend({	
		//////////////////////////////
		// 1. super init first
	ctor:function () {
		this._super();
		
		this.cardFont_ = new cc.Sprite("#card_front.png");
		this.cardFont_.setVisible(false);
		this.cardFont_.x = this.cardFont_.getContentSize().width/2;
		this.cardFont_.y = this.cardFont_.getContentSize().height/2;
		this.addChild(this.cardFont_);
		
		this.cardBack_ = new cc.Sprite("#card_back.png");
		this.cardBack_.x = this.cardBack_.getContentSize().width/2;
		this.cardBack_.y = this.cardBack_.getContentSize().height/2;
		this.addChild(this.cardBack_);
		cc.log("add pokercard");
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