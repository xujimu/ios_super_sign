//-------------------------------------
var designWidth=document.getElementsByTagName("head")[0].getAttribute("design-width");
font_size(designWidth);
function font_size(devwidth){
function _size(){
	var deviceWidth = document.documentElement.clientWidth;
	if(deviceWidth>=devwidth) deviceWidth=devwidth;
	document.documentElement.style.fontSize = deviceWidth/(devwidth/100) + 'px';
}
_size();
window.onresize=function(){
	_size();
};
}
var media = document.createElement('style');
    media.innerHTML = "@media screen and (min-width:" + designWidth + "px){.center{width:"+designWidth+"px;margin-left:-"+designWidth/2+"px;left:50%;}}";
  document.getElementsByTagName('head')[0].appendChild(media);
//-------------------------------------