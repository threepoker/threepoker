var LogoScene = cc.Scene.extend({
	onEnter:function () {
		this._super(); 
		var layer = new Logo();
		this.addChild(layer);
	}
});  
var Logo = cc.Layer.extend({
	sprite:null,
	ctor:function () {
		//////////////////////////////
		// 1. super init first
		this._super();
		var json = ccs.load(res.LOGOJSON);
		
		var btn = ccui.helper.seekWidgetByName(json.node, "Button_7");
		cc.log("img type = "+typeof(img));
		
		btn.loadTextureNormal(res.HALLQUICKSTART);
		
		cc.log("load logoScene");
		cc.log("json.name.name = "+json.node.name);
		this.addChild(json.node);
		
		var sprite = new cc.Sprite(res.GOLD);
		this.addChild(sprite);

		return true;
	},
	onEnter:function (){
		NetWork.openNet();
		this._super();
	},

	onExit:function (){
		this._super();
	},

});