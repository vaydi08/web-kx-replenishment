function checkBrowser(){
	this.ver=navigator.appVersion
	this.dom=document.getElementById?1:0
	this.ie5=(this.ver.indexOf("MSIE 5")>-1 && this.dom)?1:0;
	this.ie4=(document.all && !this.dom)?1:0;
	this.ns5=(this.dom && parseInt(this.ver) >= 5) ?1:0;
	this.ns4=(document.layers && !this.dom)?1:0;
	this.bw=(this.ie5 || this.ie4 || this.ns4 || this.ns5)
	return this
}
bw=new checkBrowser()
var speed=50
var loop, timer
function makeObj(obj,nest){
    nest=(!nest) ? '':'document.'+nest+'.'
	this.el=bw.dom?document.getElementById(obj):bw.ie4?document.all[obj]:bw.ns4?eval(nest+'document.'+obj):0;
  	this.css=bw.dom?document.getElementById(obj).style:bw.ie4?document.all[obj].style:bw.ns4?eval(nest+'document.'+obj):0;
	this.scrollHeight=bw.ns4?this.css.document.height:this.el.offsetHeight
	this.clipHeight=bw.ns4?this.css.clip.height:this.el.offsetHeight
	this.scrollWidth=bw.ns4?this.css.document.width:this.el.offsetWidth
	this.clipWidth=bw.ns4?this.css.clip.width:this.el.offsetWidth
	this.up=goUp;this.down=goDown;
	this.left=goLeft;this.right=goRight;
	this.moveIt=moveIt; this.x; this.y;
    this.obj = obj + "Object"
    eval(this.obj + "=this")
    return this
}
function moveIt(x,y){
	this.x=x;this.y=y
	this.css.left=this.x
	this.css.top=this.y
}
function goDown(move){
	if(this.y>-this.scrollHeight+oCont.clipHeight){
		this.moveIt(0,this.y-move)
			if(loop) setTimeout(this.obj+".down("+move+")",speed)
	}
}
function goUp(move){
	if(this.y<0){
		this.moveIt(0,this.y-move)
		if(loop) setTimeout(this.obj+".up("+move+")",speed)
	}
}

function goLeft(move) {
	if(this.x<0){
		this.moveIt(this.x-move,0)
		if(loop) setTimeout(this.obj+".right("+move+")",speed)
	}
}

function goRight(move) {
	if(this.x>-this.scrollWidth+oCont.clipWidth){
		this.moveIt(this.x-move,0)
			if(loop) setTimeout(this.obj+".left("+move+")",speed)
	}
	
}

function scroll(speed){
	if(loaded){
		loop=true;
		if(speed>0) oScroll.down(speed)
		else oScroll.up(speed)
	}
}
function scrollV(speed) {
	if(loaded){
		loop=true;
		if(speed>0) oScroll.right(speed)
		else oScroll.left(speed)
	}
}
function noScroll(){
	loop=false
	if(timer) clearTimeout(timer)
}
var loaded;
function scrollInit(){
	oCont=new makeObj('divCont')
	oScroll=new makeObj('divText','divCont')
	oScroll.moveIt(0,0)
	oCont.css.visibility='visible'
	loaded=true;
}