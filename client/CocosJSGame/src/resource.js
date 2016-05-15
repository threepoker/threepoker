var res = {
    GOLD : "res/ui/common/gold.png",
    DESKBG:"res/ui/desk/bg.png",
    HALLBG:"res/ui/hall/bg.png",
    HALLQUICKSTART:"res/ui/hall/quickStart.png",
    
    HALLJSON:"res/ui/hall.json",
    LOGOJSON:"res/ui/logo.json",
    DESKJSON:"res/ui/desk.json"
};

var textArr = [
    
];

var g_resources = g_resources || [];
for (var i in res) {
    g_resources.push(res[i]);
}