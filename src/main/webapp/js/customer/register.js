$(document).ready(function(){
	var register = new Register();
	register.bindEvent();
});
function Register() {
	this.errDom = $("#errorMsgId");
	this.nameDom=$("#username");//email
	this.nickDom=$("#nickname");
	this.passDom=$("#password");
	this.repassDom=$("#repassword");
	
	this.registerBtn=$("#registerBtn");
}
Register.prototype = {
	bindEvent : function() {
		this.registerBtn.unbind("click");
		this.registerBtn.bind("click",$.hitch(this,this.register));
	},
	register : function () {
		var email =this.nameDom.val();
		var nickname = this.nickDom.val();
		var password = this.passDom.val();
		var repassword = this.repassDom.val();
		if(ds.isEmpty(email)) {
			ds.error(this.errDom,"邮箱不能为空");
			return;
		}
		if(ds.isEmail(email)) {
			ds.error(this.errDom,"邮箱格式不正确");
			return;
		}
		ds.info(this.errDom,"正在登陆请稍后..."+email+"-"+password+"-"+nickname);
	}
}