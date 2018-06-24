/**
 * Created by Tiny Giant on 2018/5/23.
 */

var ws = null
function plusReady(){
	ws=plus.webview.currentWebview();
}

if(window.plus){
	plusReady();
}else{
	document.addEventListener('plusready',plusReady,false);
}

function showTip(message) {
    var $tip = $('.tip');
    $tip.removeClass('disPlayNone');
    $tip.html(message);
    $tip.fadeOut(1500,function () {
        $tip.removeAttr('style');
        $tip.addClass('disPlayNone');
        $tip.html(null);
    });
}

function popDialog(obj) {
    var $disabledWaller = $('.disabledWaller');
    $disabledWaller.removeClass('disPlayNone');
    obj.removeClass('disPlayNone');
}

function closeDialog(obj) {
    var $disabledWaller = $('.disabledWaller');
    $disabledWaller.addClass('disPlayNone');
    obj.addClass('disPlayNone');
}

function on(clock,on,off) {
    clock.addClass('light');
    on.addClass('disPlayNone');
    off.removeClass('disPlayNone');
}

function off(clock,on,off) {
    clock.removeClass('light');
    on.removeClass('disPlayNone');
    off.addClass('disPlayNone');
}

function setAddFrameFontSize(){
	var $addFrame = $('.addFrame');
	if($(window).width()<380){
		$addFrame.addClass('affFrameFontSizeSmall');
	}
}
setAddFrameFontSize();
