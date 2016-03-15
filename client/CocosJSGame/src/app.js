
var HelloWorldLayer = cc.Layer.extend({
    sprite:null,
    ctor:function () {
        //////////////////////////////
        // 1. super init first
        this._super();

        /////////////////////////////
        // 2. add a menu item with "X" image, which is clicked to quit the program
        //    you may modify it.
        // ask the window size
        var size = cc.winSize;

        // add a "close" icon to exit the progress. it's an autorelease object
        var closeItem = new cc.MenuItemImage(
            res.CloseNormal_png,
            res.CloseSelected_png,
            function () {
                cc.log("Menu is clicked!");
            }, this);
        closeItem.attr({
            x: size.width - 20,
            y: 20,
            anchorX: 0.5,
            anchorY: 0.5
        });

        var menu = new cc.Menu(closeItem);
        menu.x = 0;
        menu.y = 0;
        this.addChild(menu, 1);

        var screenBg = new cc.Sprite("res/CloseSelected.png");
        screenBg.y = cc.winSize.height/2;
        this.addChild(screenBg);
        /////////////////////////////
        // 3. add your codes below...
        // add a label shows "Hello World"
        // create and initialize a label
        var helloLabel = new cc.LabelTTF("Hello 祁晓芳", "Arial", 38);
        // position the label on the center of the screen
        helloLabel.x = size.width / 2;
        helloLabel.y = 0;
        // add the label as a child to this layer
        this.addChild(helloLabel, 5);
        helloLabel.runAction(
            cc.spawn(
                cc.moveBy(1.5, cc.p(0, size.height/2)),
                cc.fadeOut(2.5)
            )
        );
        var lable;
        for (var i=0;i<textArr.length;i++)
        {
        	lable = new cc.LabelTTF(textArr[i],"Arial",38);
        	this.addChild(lable);
        	this.runCommonAction(lable);
        }
        
        return true;
    },
	callBack:function(node){
		this.runCommonAction(node);
	},
	runCommonAction:function(node){
		node.anchorX = 0;
		node.anchorY = 0;
		node.x = cc.winSize.width;
		var a = cc.random0To1()*10;
		a = a > 9 ? 9 : a;
		a = a < 1 ? 1 : a;
		node.y = a*cc.winSize.height/10; 
		var that = this;
		node.runAction(
				cc.sequence(
						cc.delayTime(cc.random0To1()*10),
						cc.moveBy(3,cc.p(-cc.winSize.width-node.getContentSize().width,0)),
						cc.callFunc(that.callBack,that)
				)
		);
	}
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
        };
    }
});

