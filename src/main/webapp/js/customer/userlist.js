$(function(){
	var userlist= new UserList();
	userlist.load();
});
function UserList() {
	this.domId="userlist";
	this.readUserListUrl="user/getUsers";
	
	this.tablejs = new TableJs(this.domId);
	this.loadingHelper = new LoadingHelper();
	this.initTable();
	this.roles =[{value:0,name:"超级管理员"},{value:1,name:"管理员"},{value:2,name:"普通用户"}];
}
UserList.prototype = {
		initTable : function () {
			this.tablejs.pushHead("ID","id");
			this.tablejs.pushHead("账号","account");
			this.tablejs.pushHead("昵称","nickname");
			this.tablejs.pushHead("角色","roleId");
			this.tablejs.pushHead("日期","date");
			var configHelper = new TableJsConfigHelper();
			configHelper.setFenYe(true);
			configHelper.setCheckBox(false,"checkbox", true);// 设置启用 checkbox
			this.tablejs.setConfig( configHelper.getConfig());
		},
		load : function () {
			this.getUsers();
		},
		getUsers:function() {
			var _this= this;
			console.info("in");
			ds.post(this.readUserListUrl,{},function(data){
				if(data.s==1) {
					console.info("success");
					_this.tablejs.setData(data.data);
					_this.tablejs.show();
				}
				ds.log(data.i);
			},function(error){
				ds.log(error);
			});
		}
}