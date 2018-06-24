/**
 * Created by Tiny Giant on 2018/6/2.
 */


var $latestDate = $('.latestDate');
var $latestTime = $('.latestTime');
var $clockRoot = $('.clocks');
var $addBtn = $('.addBtn');//屏幕上的添加按钮
var $addFrame = $('.addFrame');//添加框
var $addTime = $('.addTime');//添加框时间框
var $addDate = $('.addDate');//添加框日期框
var $add = $('.add');//添加框添加按钮
var $disabledWaller = $('.disabledWaller');//幕布

var time = null;
var date = null;

//刷新最近一次响铃
function flashLatestRing(){
	var clock = window.android.getLatestClock();
	clock = JSON.parse(clock);
	if(null == clock){
		$latestDate.html('');
		$latestTime.html('暂无');
	}else{
		var ringTime = clock.ringTime;
		var latest = ringTime.split(' ');
		$latestDate.html(latest[0]);
		$latestTime.html(latest[1]);
	}
}
//初始化闹钟列表
function initClocks(){
	flashLatestRing();
	clearClocks();
	var clocks = window.android.initList();
	clocks = JSON.parse(clocks);
	$.each(clocks,function(i,n){
		initClock(n);
	});
	clockOps();
}

function initClock(clock){
	var id = clock.id;
	var ringTime = clock.ringTime.split(' ');
	var freq = clock.freq;
	var vibrate = clock.vibrate;
	var sound = clock.sound;
	var introduction = clock.introduction;
	var state = clock.state;
	
	var on = '';
	var off = '';
	var isLight = '';
	if(0 == state){
		isLight = 'light';
		on = 'disPlayNone';
	}else if(1 == state){
		off = 'disPlayNone';
	}
	if(0 == vibrate){
		vibrate = 'OFF';
	}else if(1 == vibrate){
		vibrate = 'ON';
	}
	sound = sound.substring(0,sound.lastIndexOf('.'));
	if(null == introduction){
		introduction = ' ';
	}
	
	var content = "<li class='clock fullWidth "+isLight+"'>"
	                +"<input class='id' type='hidden' value='"+id+"'>"
	                +"<ul class='info fullWidth flexCS'>"
		                +"<li class='time fullHeight flexCS'>"+ringTime[1]+"</li>"
		                +"<li class='stateOp flexCE'>"
			                +"<div class='on flexCS "+on+"'>"
				                +"<div class='boll'></div>"
				                +"<span class='bollTip fullHeight flexCC'>on</span>"
			                +"</div>"
			                +"<div class='off flexCE "+off+"'>"
			                	+"<span class='bollTip fullHeight flexCC'>off</span>"
			                	+"<div class='boll'></div>"
			                +"</div>"
		                +"</li>"
	                +"</ul>"
	                +"<ul class='details disPlayNone'>"
		                +"<li class='dateFrame fullWidth flexCS'>"
			                +"<span>&nbsp;开始日期：</span>"
			                +"<span class='date'>"+ringTime[0]+"</span>"
		                +"</li>"
		                +"<li class='freqFrame fullWidth flexCS'>"
			                +"<span>&nbsp;响铃频率：</span>"
			                +"<span class='freq'>"+freq+"天/次</span>"
		                +"</li>"
		                +"<li class='vibrateFrame fullWidth flexCS'>"
			                +"<span>&nbsp;震动：</span>"
			                +"<span class='vibrate'>"+vibrate+"</span>"
		                +"</li>"
		                +"<li class='ringFrame fullWidth flexCS'>"
			                +"<label class='fullWidth fullHeight flexCS'>"
			                    +"<input class='sound fullWidth fullHeight disPlayNone' type='file' accept='audio/*' />"
			                    +"<span>&nbsp;铃声：</span>"
			                    +"<span class='ring'>"+sound+"</span>"
			                +"</label>"
		                +"</li>"
		                +"<li class='introductionFrame fullWidth flexCS'>"
			                +"<span>&nbsp;说明：</span>"
			                +"<span class='introduction'>"+introduction+"</span>"
		                +"</li>"
		                +"<li class='deleteFrame fullWidth flexCE'>"
		                	+"<span class='delete fullHeight flexCE'>删除</span>"
		                +"</li>"
	                +"</ul>"
                 +"</li>"
    $clockRoot.append(content);
}

function clockOps(){
	onClick();
	offClick();
	timeClick();
	infoClick();
	dateFrameClick();
	freqFrameClick();
	vibrateFrameClick();
	soundChange();
	introductionFrameClick();
	deleteClick();
}


//清空闹钟列表
function clearClocks(){
	var $clocks = $('.clock');
	$clocks.each(function(){
		$(this).remove();
	});
}

//添加按钮事件，弹出添加框
function addBtnClick() {
    $addBtn.click(function () {
        $addTime.html(null);
        $addDate.html(null);
        popDialog($addFrame);
    });
}

//幕布点击事件
function disabledWallerClick() {
    $disabledWaller.click(function () {
        closeDialog($addFrame);
    });
}

//选择时间按钮点击事件
function addTimeClick() {
    var $addTimeFrame = $('.addTimeFrame');
    $addTimeFrame.click(function () {
        popTime(null,$addTime,null);
    });
}
//选择日期按钮点击事件
function addDateClick() {
    var $addDateFrame = $('.addDateFrame');
    $addDateFrame.click(function () {
        popDate(null,$addDate,null);
    });
}

//添加框的添加按钮点击事件
function addClick() {
    $add.click(function () {
    	if(null == time || null == date){
    		showTip('请填写完整的信息');
    	}else{
			var now = new Date();
			var targetTime = date + " " + time;
			var targetDate = new Date(Date.parse(targetTime));
			if(now >= targetDate){
				getRightTime(now,targetDate,1,$addDate,null);
				plus.nativeUI.toast('已纠正开始日期,再次点击保存');
				return;
			}else{
				var ringTime = date + " " + time;
				window.android.add(ringTime);
				initClocks();
				closeDialog($addFrame);
			}
    	}
    });
}

//-------------------------------------------------------------------------------

//闹钟开关：开 点击事件,效果：关闭闹钟
function onClick() {
    var $ons = $('.on');
    $ons.each(function () {
        $(this).click(function (e) {
            var $clock = $(this).closest('.clock');
            var $off = $clock.find('.off');
            var id = $clock.find('.id').val();

            on($clock,$(this),$off);
            window.android.updateState(Number(id),0);
            flashLatestRing();
            e.stopPropagation();
        });
    });
}
//闹钟开关：关 点击事件，效果：打开闹钟
function offClick() {
    var $offs = $('.off');
    $offs.each(function () {
        $(this).click(function (e) {
            var $clock = $(this).closest('.clock');
            var $on = $clock.find('.on');
            var id = $clock.find('.id').val();

            off($clock,$on,$(this));
            window.android.updateState(Number(id),1);
            flashLatestRing();
            e.stopPropagation();
        });
    });
}
//时间点击事件
function timeClick() {
    var $times = $('.time');
    $times.each(function () {
        $(this).click(function (e) {
        	var $clock = $(this).closest('.clock');
        	var $date = $clock.find('.dateFrame').find('.date');
        	
            var id = $clock.find('.id').val();
            var itemTime = $(this).html().trim();
            var itemDate = $date.html().trim();
            var itemFreq = $clock.find('.freqFrame').find('.freq').html().trim();
            itemFreq = itemFreq.substring(0,itemFreq.indexOf('天'));
            
            popTime(itemTime,$(this),function(){
            	var now = new Date();
            	var targetTime = itemDate + " " + time;
           		var targetDate = new Date(Date.parse(targetTime));
            	getRightTime(now,targetDate,itemFreq,$date,function(){
            		var ringTime = date + ' ' + time;
            		window.android.updateRingTime(Number(id),ringTime);
            		flashLatestRing();
            	});
            });
            
            e.stopPropagation();
        });
    });
}

function infoClick() {
    var $infos = $('.info');
    $infos.each(function () {
        $(this).click(function () {
            var $details = $(this).closest('.clock').find('.details');
            if (-1 != $details[0].className.indexOf('disPlayNone')){
                $details.removeClass('disPlayNone');
            }else{
                $details.addClass('disPlayNone');
            }
        });
    });
}
//开始日期点击事件
function dateFrameClick() {
    var $dateFrames = $('.dateFrame');
    $dateFrames.each(function () {
        $(this).click(function () {
            var $clock = $(this).closest('.clock');
			var $date = $(this).find('.date');
			var id = $clock.find('.id').val();
			var itemDate = $date.html().trim();
			var itemTime = $clock.find('.time').html().trim();
			var itemFreq = $clock.find('.freqFrame').find('.freq').html().trim();
			itemFreq = itemFreq.substring(0,itemFreq.indexOf('天'));
			
			popDate(itemDate,$date,function(){
				var now = new Date();
				var targetTime = date + ' ' + itemTime;
				var targetDate = new Date(Date.parse(targetTime));
				getRightTime(now,targetDate,itemFreq,$date,function(){
					itemTime = $clock.find('.time').html().trim();
            		var ringTime = date + ' ' + itemTime;
            		window.android.updateRingTime(Number(id),ringTime);
            		flashLatestRing();
				});
			});
        });
    });
}
//响铃频率点击事件
function freqFrameClick(){
	var $freqFrames = $('.freqFrame'); 
	$freqFrames.each(function(){
		$(this).click(function(){
			var $clock = $(this).closest('.clock');
			var $time = $clock.find('.time');
			var $date = $clock.find('.dateFrame').find('.date');
			var $freq = $(this).find('.freq');
			
			var id = $clock.find('.id').val();
			var itemTime = $time.html().trim();
			var itemDate = $date.html().trim();
			
			popFreqSelector(1,7,$freq,function(){
				var now = new Date();
				var targetTime = itemDate + ' ' + itemTime;
				var targetDate = new Date(Date.parse(targetTime));
				var itemFreq = $freq.html().trim();
				itemFreq = itemFreq.substring(0,itemFreq.indexOf('天'));
				getRightTime(now,targetDate,itemFreq,$date,function(){
            		window.android.updateFreq(Number(id),Number(itemFreq));
				})
			})
		});
	});
}
//震动点击事件
function vibrateFrameClick(){
	var $vibrateFrames = $('.vibrateFrame');
	$vibrateFrames.each(function(){
		$(this).click(function(){
			var $clock = $(this).closest('.clock');
			var $vibrate = $(this).find('.vibrate');
			
			var id = $clock.find('.id').val();
			
			popVibrateSelector($vibrate,function(){
				var vibrate = $vibrate.html();
				if('ON' == vibrate){
					vibrate = 1;
				}else if('OFF' == vibrate){
					vibrate = 0;
				}
        		window.android.updateVibrate(Number(id),Number(vibrate));
			});
		});
	});
}
//铃声改变事件
function soundChange(){
	var $sounds = $('.sound');
	$sounds.each(function(){
		$(this).change(function(){
			var $clock = $(this).closest('.clock');
			var $ring = $(this).closest('.ringFrame').find('.ring');
			
			var id = $clock.find('.id').val();
			var sound = $(this)[0].files[0];
			var soundName = $(this).val().trim();
			soundName = soundName.substring(soundName.lastIndexOf('\\')+1);
			var type = null;
			
			var extra = soundName.substring(soundName.lastIndexOf('.'));
			for(var i = 0 ; i < soundBinaryType.length ; i++){
				if(-1 != extra.indexOf(soundType[i])){
					type = soundType[i];
					soundName = soundName.substring(soundName.lastIndexOf('\\')+1,soundName.lastIndexOf(type));
					break;
				}
			}
			
			var fileReader = new FileReader();
			fileReader.readAsBinaryString(sound);
			fileReader.onload = function(){
				var file = fileReader.result;
				var head = file.substring(0,10).toLowerCase();
				if(null == type){
					for(var i = 0 ; i < soundBinaryType.length ; i++){
						if(-1 != head.indexOf(soundBinaryType[i])){
							type = soundType[i];
							break;
						}
					}
				}
				
				if(-1 != soundName.lastIndexOf('-')){
					soundName = soundName.substring(soundName.lastIndexOf('-') + 2);
				}
				window.android.updateSound(Number(id),file,soundName+type);
				$ring.html(soundName);
			}
		});
	});
}
var soundBinaryType = ['id3','flac']
var soundType = ['.mp3','.flac'];

//说明框点击事件
function introductionFrameClick(){
	var $introductionFrames = $('.introductionFrame');
	$introductionFrames.each(function(){
		$(this).click(function(){
			var $clock = $(this).closest('.clock');
			var $introduction = $(this).find('.introduction');
			
			var id = $clock.find('.id').val();
			popIntroduction($introduction,function(){
				window.android.updateIntroduction(Number(id),$introduction.html().trim());
			})
		});
	});
}
//删除点击事件
function deleteClick(){
	var $deletes = $('.delete');
	$deletes.each(function(){
		$(this).click(function(){
			var $clock = $(this).closest('.clock');
			var id = $clock.find('.id').val();
			window.android.removeClock(Number(id));
			initClocks();
		});
	});
}






//----------------------------------------------------------------------------

//弹出时间选择框
function popTime(defTime,obj,callback){
	var time1 = new Date();
	if(null != defTime){
		time1.setHours(defTime.split(':')[0],defTime.split(':')[1]);
	}
	plus.nativeUI.pickTime(
    	function(e){
    		var t = e.date;
    		var h = t.getHours();
    		var ms = t.getMinutes();
    		h < 10 ? h = '0' + h : h;
    		ms < 10 ? ms = '0' + ms : ms;
    		time = h + ":" + ms;
    		obj.html(time);
    		if(null != callback){
    			callback();
    		}
    	},
    	function(e){
    		
    	},
    	{time:time1}
    );
}
//弹出日期选择框
function popDate(defDate,obj,callback){
	var date1 = new Date();
	if(null != defDate){
		date1.setFullYear(defDate.split('-')[0],defDate.split('-')[1]-1,defDate.split('-')[2]);
	}
	plus.nativeUI.pickDate(
		function(e){
			var d = e.date;
			var y = d.getFullYear();
			var m = d.getMonth() + 1;
			var day = d.getDate();
			m < 10 ? m = '0' + m : m;
			day <10 ? day = '0' + day : day;
			date = y + "-" + m + "-" + day;
			obj.html(date);
			if(null != callback){
    			callback();
    		}
		},
		function(e){
			
		},
		{date:date1}
	);
	
}
//校验日期
function getRightTime(now,targetDate,freq,obj,callback){
	freq = Number(freq);
	while(now >= targetDate){
		targetDate.setDate(targetDate.getDate()+freq);
	}
	var y = targetDate.getFullYear();
	var m = targetDate.getMonth() + 1;
	var day = targetDate.getDate();
	m < 10 ? m = '0' + m : m;
	day < 10 ? day = '0' + day : day;
	date = y + '-' + m + '-' + day;
	obj.html(date);
	if(null != callback){
		callback();
	}
}
//弹出频率选择框
function popFreqSelector(min,max,obj,callback){
	var buttons = new Array();
	while(max >= min){
		var title = '{"title":"' + min + '天/次"}';
		min ++;
		title = $.parseJSON(title);
		buttons.push(title);
	}
	
	plus.nativeUI.actionSheet(
		{
			title:'选择响铃频率',
			cancel:'取消',
			buttons:buttons
		},
		function(e){
			if(0 != e.index && -1 != e.index){
				obj.html(buttons[e.index-1].title);
				if(null != callback){
					callback();
				}
			}
		}
	);
}
//弹出震动选择框
function popVibrateSelector(obj,callback){
	var buttons = new Array();
	var on = $.parseJSON('{"title":"ON"}')
	var off = $.parseJSON('{"title":"OFF"}')
	buttons.push(on);
	buttons.push(off);
	
		plus.nativeUI.actionSheet(
		{
			title:'选择是否开启震动',
			cancel:'取消',
			buttons:buttons
		},
		function(e){
			if(0 != e.index && -1 != e.index){
				obj.html(buttons[e.index-1].title);
				if(null != callback){
					callback();
				}
			}
		}
	);
}
//弹出说明框
function popIntroduction(obj,callback){
	var defaultIn = obj.html();
	plus.nativeUI.prompt(
		'不超过20字',
		function(e){
			if(null == e.value || 0 == e.value.length){
				return;
			}else if(20 < e.value.length){
				plus.nativeUI.toast('超过了20字');
				return;
			}else{
				obj.html(e.value);
				if(null != callback){
					callback();
				}
			}
		},
		'闹钟说明',
		defaultIn,
		null,
	);
}

//初始化页面
function init() {
	initClocks();
    addTimeClick();
    addDateClick();
    addClick();
    disabledWallerClick();
    addBtnClick();
}

init();