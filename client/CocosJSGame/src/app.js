
var HelloWorldLayer = cc.Layer.extend({
    sprite:null,
    ctor:function () {
        //////////////////////////////
        // 1. super init first
        this._super();
        cc.sys.dump();
        if(cc.sysbrowserType != cc.sys.BROWSER_TYPE_UNKNOWN ){//浏览器
        	cc.spriteFrameCache.addSpriteFrames(res.POKER_PLIST,res.POKER_PNG);
        }else{//客户端
        	cc.SpriteFrameCache.getInstance().addSpriteFrames(res.POKER_PLIST,res.POKER_PNG);        	
        }

        
        this.card1 = new PokerCard();
        this.card1.x = cc.winSize.width/2;
        this.card1.y = cc.winSize.height/2;
        this.addChild(this.card1);
        
        this.card2 = new PokerCard();
        this.card2.x = cc.winSize.width/2 + 30;
        this.card2.y = cc.winSize.height/2;
        this.addChild(this.card2);
        
        this.card3 = new PokerCard();
        this.card3.x = cc.winSize.width/2 + 60;
        this.card3.y = cc.winSize.height/2;
        this.addChild(this.card3);
        
        var touchBtn = new cc.ControlButton();
        

        NetWork.create();
        
        return true;
    },

    onGetCards:function(data){
    	cc.log("data = "+data);
    	var dataJson = eval('('+data+')');
    	cc.log("card0 = "+dataJson["card0"]);
    	this.card1.setCardValue(dataJson["card0"]);
    	this.card1.seeCard();
    	
    	this.card2.setCardValue(dataJson["card1"]);
    	this.card2.seeCard();
    	
    	this.card3.setCardValue(dataJson["card2"]);
    	this.card3.seeCard();
    },
    onEnter:function (){
    	EventCenter.addObserver(this,this.onGetCards, MSGTAG.GETCARDS, null);
    	this._super();
    },

    onExit:function (){
    	EventCenter.removeObserver(this);
    	this._super();
    },
	   
});

var HelloWorldScene = cc.Scene.extend({
    onEnter:function () {
        this._super(); 
        var layer = new HelloWorldLayer();
        this.addChild(layer);
        
    }
});      
  
