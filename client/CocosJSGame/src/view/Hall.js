var HallScene = cc.Scene.extend({
	onEnter:function () {
		this._super(); 
		var layer = new Hall();
		this.addChild(layer);
	}
});  
var Hall = cc.Layer.extend({
	sprite:null,
	ctor:function () {
		//////////////////////////////
		// 1. super init first
		this._super();
		var root = ccs.uiReader.widgetFromJsonFile(res.HALLJSON);
		this.addChild(root);

		return true;
	},
	onEnter:function (){
		NotificationCenter.addObserver(this,this.onGetCards, MSGTAG.GETCARD, null);
		NetWork.sendMSG("");
		this._super();
	},

	onExit:function (){
		NotificationCenter.removeObserver(this);
		this._super();
	},

});