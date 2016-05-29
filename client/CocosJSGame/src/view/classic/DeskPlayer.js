var DeskPlayer = cc.Layer.extend({
	sprite:null,
	ctor:function () {
		//////////////////////////////
		// 1. super init first
		this._super();
		var json = ccs.load(res.DESKPLAYERJSON);
		var root = json.node;
		this.addChild(root)
		var btnBack = ccui.helper.seekWidgetByName(root, "Button_back");

		return true;
	},
	onEnter:function (){
		this._super();
	},

	onExit:function (){
		this._super();
	},
});