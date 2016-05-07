var EventCenter = {
	events:[],
	addObserver:function(target,selector,name,sender){
		if(name != undefined && name != ""){
			for (var i = 0; i < this.events.length+1; i++) {
				if (this.events[i] == null) {
					this.events[i] = new Object();
					this.events[i].target = target;
					this.events[i].name = name;
					this.events[i].selector = selector;
					this.events[i].sender = sender;		
					break;
				}				
			}
		}
	},
	postNotification:function(name,data){
		for (var i = 0; i < this.events.length; i++) {
			if(this.events[i] != undefined && this.events[i].name == name){
				var mSelector = this.events[i].selector;
				if(data != null){
					mSelector.bind(this.events[i].target)(data);			
				}else{
					mSelector.bind(this.events[i].target)(this.events[i].sender,data);		
				}
			}
		}
	},
	removeObserver:function(name){
		for (var i = 0; i < this.events.length; i++) {
			if ((this.events[i] != null && typeof(name) == "object" && this.events[i].target == name)
					|| (this.events[i] != null && typeof(name) == "string" && this.events[i].name == name)
					) {
				this.events[i] = null;
			}
		}	
	},
}