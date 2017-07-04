$(document).ready(function(){
	var login = new Login();
	login.bindEvent();
});
function Login() {
	this.errDom = $("#errorMsgId");
	this.nameDom=$("#username");
	this.passDom=$("#password");
	this.rmbDom=$("#remember");
	
	this.signBtn=$("#signBtn");
}
Login.prototype = {
	bindEvent : function() {
		this.signBtn.unbind("click");
		this.signBtn.bind("click",$.hitch(this,this.login));
	},
	login : function () {
		var username =this.nameDom.val();
		var password = this.passDom.val();
		var remember = this.rmbDom.attr("checked")=="checked";
		//ds.info(this.errDom,"正在登陆请稍后..."+username+"-"+password+"-"+remember);
	}
}