
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
        
        var lxftest = new PokerCard();
        lxftest.x = cc.winSize.width/2;
        lxftest.y = cc.winSize.height/2;
        this.addChild(lxftest);
        return true;
    },
	   
});

var HelloWorldScene = cc.Scene.extend({
    onEnter:function () {
        this._super(); 
        var layer = new HelloWorldLayer();
        this.addChild(layer);
        
        var ws = new WebSocket("ws://localhost:8080/ddd/ws");
        ws.onopen = function(){ 
        	cc.log("onopen"); 
        };
         
        ws.onmessage = function(message){
        	cc.log("onmessage= "+message.data);
        	EventCenter.postNotification("onOpen",message.data);
        };
    }
});      
  
