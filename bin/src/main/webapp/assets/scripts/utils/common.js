/*通用工具*/
define(function(require, exports, module) {
	require('jquery');
	function Common(){}
	module.exports=Common;
	Common.prototype._noEmpty=function(value){
		if (value==null||value==''||value==undefined) {
			return false;
		}
		return true;
	}
	Common.prototype._numBigZero=function(value){
		try {
			var num=parseInt(value);
			if (num<=0) {
				return false;
			}else{
				return true;
			}
		} catch (e) {
			return false;
		}
	}
	Common.prototype._paramsEmpty=function(data){
		var rul = data[0]["data"],result=true;
		if (typeof rul=="string") {
			if(this._noEmpty(rul)){
				if (rul.indexOf("&")>0) {
					var string = rul.split('&');
					var res = [];
					for(var i = 0;i<string.length;i++){
					    var str = string[i].split('=');
					    res.push(str[1]);
					}
					for (var int = 0; int < res.length; int++) {
						if (!this._noEmpty(res[int])) {
							result= false;
						}
					}
				}else{
					var str = rul.split('=');
				    if (!this._noEmpty(str[1])) {
				    	result=false;
					}
				}
			}
		}else if (typeof rul=="object") {
			for ( var key in rul) {
				if (!this._noEmpty(rul[key])) {
					result=false;
				}
			}
		}
		return result;
	}
	Common.prototype._paramsEmptyFill=function(self,array){
		var rul =self.data,result=true,temp={};
			if(this._noEmpty(rul)){
				if (rul.indexOf("&")>0) {
					var string = rul.split('&');
					var res = [],key=[];
					for(var i = 0;i<string.length;i++){
					    var str = string[i].split('=');
					    res.push(str[1]);
					    key.push(str[0]);
					}
					var data=[];
					for (var int = 0; int < res.length; int++) {
						if (!this._noEmpty(res[int])) {
							if (array&&array[key[int]]!=undefined) {
								res[int]=array[key[int]];
							}else{
								console.log(key[int]+"参数为空");
								result= false;
							}
						}
						data.push(key[int]+"="+res[int]);
					}
					temp.data=data.join("&");
				}else{
					var str = rul.split('=');
				    if (!this._noEmpty(str[1])) {
				    	if (array&&array[str[0]]!=undefined){
				    		str[1]=array[str[0]];
				    	}
				    	else{
				    		result=false;
				    	}
					}
				    temp.data=str[0]+"="+str[1];
				}
			}
			temp.r=result;
		return temp;
	}
	/*验证图片上传重复*/
	Common.prototype._imgUP=function(className,src){
		var appendOImagesFlag = true;
		$('.'+className).each(function(e){
			if($(this).data('value')==src){
			appendOImagesFlag= false;
			return;
			}
		});
		return appendOImagesFlag;
	}
	Common.prototype._showLoding=function(){
		$("body").append('<div id="LodingDiv"><div class="loding" style="display: block; margin-top: -124px;"><h2>Loading<div class="sk-spinner sk-spinner-three-bounce"style="display: inline-block;"><div class="sk-bounce1"></div><div class="sk-bounce2"></div><div class="sk-bounce3"></div></div></h2></div><div class="loding-backdrop"></div></div>');
		return;
	}
	Common.prototype._hideLoding=function(){
		$("#LodingDiv").remove();
		return;
	}
	
	
});
