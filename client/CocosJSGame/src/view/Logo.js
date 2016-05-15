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
		
		this.addChild(json.node);
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