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
		
		this.cardPointBg_ = new cc.Sprite();
		this.cardPointBg_.x = 18;
		this.cardPointBg_.y = 100; 
		this.cardPointBg_.setScale(0.7);
		this.cardFont_.addChild(this.cardPointBg_); 
		
		this.cardTypeBg_ = new cc.Sprite();
		this.cardTypeBg_.x = 18;
		this.cardTypeBg_.y = 80;
		this.cardTypeBg_.setScale(0.7);
		this.cardFont_.addChild(this.cardTypeBg_); 
		
		//this.setScale(1);
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
		var cardType = (value&0xF0)>>4;
		var cardPoint = value&0x0F;
		cc.log("cardType = "+cardType+" cardPont = "+cardPoint);
		switch(cardType){ 
		case 0:
		case 2:
			this.cardPointBg_.initWithSpriteFrameName("cardrnum_"+cardPoint+".png");
			break;
		case 1:
		case 3:
			this.cardPointBg_.initWithSpriteFrameName("cardbnum_"+cardPoint+".png");
		}
		this.cardTypeBg_.initWithSpriteFrameName("cardtype_"+cardType+".png");
		
	},
	seeCard:function(isAnimation,delayTime,revert){
		this.cardBack_.setVisible(false);
		this.cardFont_.setVisible(true);
	},
	slotSeeCard:function(){
		
	},
	slotHideCard:function(){
		
	},
	getCard:function(){
		return this.cardValue_;		
	}

});