//v.3.0 build 110713

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
function dhtmlXContainer(j){var e=this;this.obj=j;this.dhxcont=null;this.st=document.createElement("DIV");this.st.style.position="absolute";this.st.style.left="-200px";this.st.style.top="0px";this.st.style.width="100px";this.st.style.height="1px";this.st.style.visibility="hidden";this.st.style.overflow="hidden";document.body.insertBefore(this.st,document.body.childNodes[0]);this.obj._getSt=function(){return e.st};this.obj.dv="def";this.obj.av=this.obj.dv;this.obj.cv=this.obj.av;this.obj.vs={};this.obj.vs[this.obj.av]=
{};this.obj.view=function(a){if(!this.vs[a]){this.vs[a]={};this.vs[a].dhxcont=this.vs[this.dv].dhxcont;var b=document.createElement("DIV");b.style.position="relative";b.style.left="0px";b.style.width="200px";b.style.height="200px";b.style.overflow="hidden";b.style.visibility="";e.st.appendChild(b);this.vs[a].dhxcont.mainCont[a]=b}this.avt=this.av;this.av=a;return this};this.obj.setActive=function(){if(this.vs[this.av])this.cv=this.av,this.vs[this.avt].dhxcont==this.vs[this.avt].dhxcont.mainCont[this.avt].parentNode&&
(e.st.appendChild(this.vs[this.avt].dhxcont.mainCont[this.avt]),this.vs[this.avt].menu&&e.st.appendChild(document.getElementById(this.vs[this.avt].menuId)),this.vs[this.avt].toolbar&&e.st.appendChild(document.getElementById(this.vs[this.avt].toolbarId)),this.vs[this.avt].sb&&e.st.appendChild(document.getElementById(this.vs[this.avt].sbId))),this.vs[this.av].dhxcont!=this.vs[this.av].dhxcont.mainCont[this.av].parentNode&&(this.vs[this.av].dhxcont.insertBefore(this.vs[this.av].dhxcont.mainCont[this.av],
this.vs[this.av].dhxcont.childNodes[this.vs[this.av].dhxcont.childNodes.length-1]),this.vs[this.av].menu&&this.vs[this.av].dhxcont.insertBefore(document.getElementById(this.vs[this.av].menuId),this.vs[this.av].dhxcont.childNodes[0]),this.vs[this.av].toolbar&&this.vs[this.av].dhxcont.insertBefore(document.getElementById(this.vs[this.av].toolbarId),this.vs[this.av].dhxcont.childNodes[this.vs[this.av].menu?1:0]),this.vs[this.av].sb&&this.vs[this.av].dhxcont.insertBefore(document.getElementById(this.vs[this.av].sbId),
this.vs[this.av].dhxcont.childNodes[this.vs[this.av].dhxcont.childNodes.length-1])),this._doOnResize&&this._doOnResize(),this.avt=null};this.obj._viewRestore=function(){var a=this.av;if(this.avt)this.av=this.avt,this.avt=null;return a};this.setContent=function(a){this.obj.vs[this.obj.av].dhxcont=a;this.obj._init()};this.obj._init=function(){this.vs[this.av].dhxcont.innerHTML="<div ida='dhxMainCont' style='position: relative; left: 0px; top: 0px; overflow: hidden;'></div><div ida='dhxContBlocker' class='dhxcont_content_blocker' style='display: none;'></div>";
this.vs[this.av].dhxcont.mainCont={};this.vs[this.av].dhxcont.mainCont[this.av]=this.vs[this.av].dhxcont.childNodes[0]};this.obj._genStr=function(a){for(var b="",c="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",d=0;d<a;d++)b+=c.charAt(Math.round(Math.random()*(c.length-1)));return b};this.obj.setMinContentSize=function(a,b){this.vs[this.av]._minDataSizeW=a;this.vs[this.av]._minDataSizeH=b};this.obj._setPadding=function(a,b){typeof a=="object"?(this._offsetTop=a[0],this._offsetLeft=
a[1],this._offsetWidth=a[2],this._offsetHeight=a[3]):(this._offsetLeft=this._offsetTop=a,this._offsetWidth=-a*2,this._offsetHeight=-a*2);this.vs[this.av].dhxcont.className="dhxcont_global_content_area "+(b||"")};this.obj.moveContentTo=function(a){for(var b in this.vs){a.view(b).setActive();var c=null;this.vs[b].grid&&(c="grid");this.vs[b].tree&&(c="tree");this.vs[b].tabbar&&(c="tabbar");this.vs[b].folders&&(c="folders");this.vs[b].layout&&(c="layout");c!=null&&(a.view(b).attachObject(this.vs[b][c+
"Id"]),a.vs[b][c]=this.vs[b][c],a.vs[b][c+"Id"]=this.vs[b][c+"Id"],a.vs[b][c+"Obj"]=this.vs[b][c+"Obj"],this.vs[b][c]=null,this.vs[b][c+"Id"]=null,this.vs[b][c+"Obj"]=null);if(this.vs[b]._frame)a.vs[b]._frame=this.vs[b]._frame,this.vs[b]._frame=null;if(this.vs[b].menu!=null)a.cv==a.av?a.vs[a.av].dhxcont.insertBefore(document.getElementById(this.vs[b].menuId),a.vs[a.av].dhxcont.childNodes[0]):a._getSt().appendChild(document.getElementById(this.vs[b].menuId)),a.vs[b].menu=this.vs[b].menu,a.vs[b].menuId=
this.vs[b].menuId,a.vs[b].menuHeight=this.vs[b].menuHeight,this.vs[b].menu=null,this.vs[b].menuId=null,this.vs[b].menuHeight=null,this.cv==this.av&&this._doOnAttachMenu&&this._doOnAttachMenu("unload"),a.cv==a.av&&a._doOnAttachMenu&&a._doOnAttachMenu("move");if(this.vs[b].toolbar!=null)a.cv==a.av?a.vs[a.av].dhxcont.insertBefore(document.getElementById(this.vs[b].toolbarId),a.vs[a.av].dhxcont.childNodes[a.vs[a.av].menu!=null?1:0]):a._getSt().appendChild(document.getElementById(this.vs[b].toolbarId)),
a.vs[b].toolbar=this.vs[b].toolbar,a.vs[b].toolbarId=this.vs[b].toolbarId,a.vs[b].toolbarHeight=this.vs[b].toolbarHeight,this.vs[b].toolbar=null,this.vs[b].toolbarId=null,this.vs[b].toolbarHeight=null,this.cv==this.av&&this._doOnAttachToolbar&&this._doOnAttachToolbar("unload"),a.cv==a.av&&a._doOnAttachToolbar&&a._doOnAttachToolbar("move");if(this.vs[b].sb!=null)a.cv==a.av?a.vs[a.av].dhxcont.insertBefore(document.getElementById(this.vs[b].sbId),a.vs[a.av].dhxcont.childNodes[a.vs[a.av].dhxcont.childNodes.length-
1]):a._getSt().appendChild(document.getElementById(this.vs[b].sbId)),a.vs[b].sb=this.vs[b].sb,a.vs[b].sbId=this.vs[b].sbId,a.vs[b].sbHeight=this.vs[b].sbHeight,this.vs[b].sb=null,this.vs[b].sbId=null,this.vs[b].sbHeight=null,this.cv==this.av&&this._doOnAttachStatusBar&&this._doOnAttachStatusBar("unload"),a.cv==a.av&&a._doOnAttachStatusBar&&a._doOnAttachStatusBar("move");for(var d=this.vs[b].dhxcont.mainCont[b],f=a.vs[b].dhxcont.mainCont[b];d.childNodes.length>0;)f.appendChild(d.childNodes[0])}a.view(this.av).setActive()};
this.obj.adjustContent=function(a,b,c,d,f){this.vs[this.av].dhxcont.style.left=(this._offsetLeft||0)+"px";this.vs[this.av].dhxcont.style.top=(this._offsetTop||0)+b+"px";var e=a.clientWidth+(this._offsetWidth||0);if(d!==!0)this.vs[this.av].dhxcont.style.width=Math.max(0,e)+"px";if(d!==!0&&this.vs[this.av].dhxcont.offsetWidth>e)this.vs[this.av].dhxcont.style.width=Math.max(0,e*2-this.vs[this.av].dhxcont.offsetWidth)+"px";var g=a.clientHeight+(this._offsetHeight||0);this.vs[this.av].dhxcont.style.height=
Math.max(0,g-b)+(c!=null?c:0)+"px";if(this.vs[this.av].dhxcont.offsetHeight>g-b)this.vs[this.av].dhxcont.style.height=Math.max(0,(g-b)*2-this.vs[this.av].dhxcont.offsetHeight)+"px";if(f&&!isNaN(f))this.vs[this.av].dhxcont.style.height=Math.max(0,parseInt(this.vs[this.av].dhxcont.style.height)-f)+"px";if(this.vs[this.av]._minDataSizeH!=null&&parseInt(this.vs[this.av].dhxcont.style.height)<this.vs[this.av]._minDataSizeH)this.vs[this.av].dhxcont.style.height=this.vs[this.av]._minDataSizeH+"px";if(this.vs[this.av]._minDataSizeW!=
null&&parseInt(this.vs[this.av].dhxcont.style.width)<this.vs[this.av]._minDataSizeW)this.vs[this.av].dhxcont.style.width=this.vs[this.av]._minDataSizeW+"px";if(d!==!0&&(this.vs[this.av].dhxcont.mainCont[this.av].style.width=this.vs[this.av].dhxcont.clientWidth+"px",this.vs[this.av].dhxcont.mainCont[this.av].offsetWidth>this.vs[this.av].dhxcont.clientWidth))this.vs[this.av].dhxcont.mainCont[this.av].style.width=Math.max(0,this.vs[this.av].dhxcont.clientWidth*2-this.vs[this.av].dhxcont.mainCont[this.av].offsetWidth)+
"px";var h=this.vs[this.av].menu!=null?!this.vs[this.av].menuHidden?this.vs[this.av].menuHeight:0:0,k=this.vs[this.av].toolbar!=null?!this.vs[this.av].toolbarHidden?this.vs[this.av].toolbarHeight:0:0,i=this.vs[this.av].sb!=null?!this.vs[this.av].sbHidden?this.vs[this.av].sbHeight:0:0;this.vs[this.av].dhxcont.mainCont[this.av].style.height=this.vs[this.av].dhxcont.clientHeight+"px";if(this.vs[this.av].dhxcont.mainCont[this.av].offsetHeight>this.vs[this.av].dhxcont.clientHeight)this.vs[this.av].dhxcont.mainCont[this.av].style.height=
Math.max(0,this.vs[this.av].dhxcont.clientHeight*2-this.vs[this.av].dhxcont.mainCont[this.av].offsetHeight)+"px";this.vs[this.av].dhxcont.mainCont[this.av].style.height=Math.max(0,parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.height)-h-k-i)+"px"};this.obj.coverBlocker=function(){return this.vs[this.av].dhxcont.childNodes[this.vs[this.av].dhxcont.childNodes.length-1]};this.obj.showCoverBlocker=function(){this.coverBlocker().style.display=""};this.obj.hideCoverBlocker=function(){this.coverBlocker().style.display=
"none"};this.obj.updateNestedObjects=function(){this.vs[this.av].grid&&this.vs[this.av].grid.setSizes();this.vs[this.av].sched&&this.vs[this.av].sched.setSizes();this.vs[this.av].tabbar&&this.vs[this.av].tabbar.adjustOuterSize();this.vs[this.av].folders&&this.vs[this.av].folders.setSizes();this.vs[this.av].editor&&(_isIE||this.vs[this.av].editor._prepareContent(!0),this.vs[this.av].editor.setSizes());if(this.vs[this.av].layout)(this.vs[this.av].dhxcont._isAcc||this.vs[this.av].dhxcont._isTabbarCell)&&
this.vs[this.av].dhxcont.skin=="dhx_skyblue"?(this.vs[this.av].layoutObj.style.width=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.width)+2+"px",this.vs[this.av].layoutObj.style.height=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.height)+2+"px"):(this.vs[this.av].layoutObj.style.width=this.vs[this.av].dhxcont.mainCont[this.av].style.width,this.vs[this.av].layoutObj.style.height=this.vs[this.av].dhxcont.mainCont[this.av].style.height),this.vs[this.av].layout.setSizes();if(this.vs[this.av].accordion!=
null)this.skin=="dhx_web"?(this.vs[this.av].accordionObj.style.width=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.width)+"px",this.vs[this.av].accordionObj.style.height=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.height)+"px"):(this.vs[this.av].accordionObj.style.width=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.width)+2+"px",this.vs[this.av].accordionObj.style.height=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.height)+2+"px"),this.vs[this.av].accordion.setSizes();
this.vs[this.av].dockedCell&&this.vs[this.av].dockedCell.updateNestedObjects();this.vs[this.av].form&&this.vs[this.av].form.setSizes()};this.obj.attachStatusBar=function(){if(!this.vs[this.av].sb){var a=document.createElement("DIV");a.className=this._isCell?"dhxcont_sb_container_layoutcell":"dhxcont_sb_container";a.id="sbobj_"+this._genStr(12);a.innerHTML="<div class='dhxcont_statusbar'></div>";this.cv==this.av?this.vs[this.av].dhxcont.insertBefore(a,this.vs[this.av].dhxcont.childNodes[this.vs[this.av].dhxcont.childNodes.length-
1]):e.st.appendChild(a);a.setText=function(a){this.childNodes[0].innerHTML=a};a.getText=function(){return this.childNodes[0].innerHTML};a.onselectstart=function(a){a=a||event;return a.returnValue=!1};this.vs[this.av].sb=a;this.vs[this.av].sbHeight=this.skin=="dhx_web"?41:this.skin=="dhx_skyblue"?23:a.offsetHeight;this.vs[this.av].sbId=a.id;this._doOnAttachStatusBar&&this._doOnAttachStatusBar("init");this.adjust();return this.vs[this._viewRestore()].sb}};this.obj.detachStatusBar=function(){if(this.vs[this.av].sb)this.vs[this.av].sb.setText=
null,this.vs[this.av].sb.getText=null,this.vs[this.av].sb.onselectstart=null,this.vs[this.av].sb.parentNode.removeChild(this.vs[this.av].sb),this.vs[this.av].sb=null,this.vs[this.av].sbHeight=null,this.vs[this.av].sbId=null,this._viewRestore(),this._doOnAttachStatusBar&&this._doOnAttachStatusBar("unload")};this.obj.getFrame=function(){return this.getView()._frame};this.obj.getView=function(a){return this.vs[a||this.av]};this.obj.attachMenu=function(a){if(!this.vs[this.av].menu){var b=document.createElement("DIV");
b.style.position="relative";b.style.overflow="hidden";b.id="dhxmenu_"+this._genStr(12);this.cv==this.av?this.vs[this.av].dhxcont.insertBefore(b,this.vs[this.av].dhxcont.childNodes[0]):e.st.appendChild(b);typeof a!="object"?this.vs[this.av].menu=new dhtmlXMenuObject(b.id,a||this.skin):(a.parent=b.id,this.vs[this.av].menu=new dhtmlXMenuObject(a));this.vs[this.av].menuHeight=this.skin=="dhx_web"?29:b.offsetHeight;this.vs[this.av].menuId=b.id;this._doOnAttachMenu&&this._doOnAttachMenu("init");this.adjust();
return this.vs[this._viewRestore()].menu}};this.obj.detachMenu=function(){if(this.vs[this.av].menu){var a=document.getElementById(this.vs[this.av].menuId);this.vs[this.av].menu.unload();this.vs[this.av].menu=null;this.vs[this.av].menuId=null;this.vs[this.av].menuHeight=null;a&&a.parentNode.removeChild(a);a=null;this._viewRestore();this._doOnAttachMenu&&this._doOnAttachMenu("unload")}};this.obj.attachToolbar=function(a){if(!this.vs[this.av].toolbar){var b=document.createElement("DIV");b.style.position=
"relative";b.style.overflow="hidden";b.id="dhxtoolbar_"+this._genStr(12);this.cv==this.av?this.vs[this.av].dhxcont.insertBefore(b,this.vs[this.av].dhxcont.childNodes[this.vs[this.av].menu!=null?1:0]):e.st.appendChild(b);typeof a!="object"?this.vs[this.av].toolbar=new dhtmlXToolbarObject(b.id,a||this.skin):(a.parent=b.id,this.vs[this.av].toolbar=new dhtmlXToolbarObject(a));this.vs[this.av].toolbarHeight=this.skin=="dhx_web"?41:b.offsetHeight+(this._isLayout&&this.skin=="dhx_skyblue"?2:0);this.vs[this.av].toolbarId=
b.id;this._doOnAttachToolbar&&this._doOnAttachToolbar("init");this.adjust();return this.vs[this._viewRestore()].toolbar}};this.obj.detachToolbar=function(){if(this.vs[this.av].toolbar){var a=document.getElementById(this.vs[this.av].toolbarId);this.vs[this.av].toolbar.unload();this.vs[this.av].toolbar=null;this.vs[this.av].toolbarId=null;this.vs[this.av].toolbarHeight=null;a&&a.parentNode.removeChild(a);a=null;this._viewRestore();this._doOnAttachToolbar&&this._doOnAttachToolbar("unload")}};this.obj.attachGrid=
function(){if(this._isWindow&&this.skin=="dhx_skyblue")this.vs[this.av].dhxcont.mainCont[this.av].style.border="#a4bed4 1px solid",this._redraw();var a=document.createElement("DIV");a.id="dhxGridObj_"+this._genStr(12);a.style.width="100%";a.style.height="100%";a.cmp="grid";document.body.appendChild(a);this.attachObject(a.id,!1,!0);this.vs[this.av].grid=new dhtmlXGridObject(a.id);this.vs[this.av].grid.setSkin(this.skin);if(this.skin!="dhx_web")this.vs[this.av].grid.entBox.style.border="0px solid white",
this.vs[this.av].grid._sizeFix=0;this.vs[this.av].gridId=a.id;this.vs[this.av].gridObj=a;return this.vs[this._viewRestore()].grid};this.obj.attachScheduler=function(a,b){var c=document.createElement("DIV");c.id="dhxSchedObj_"+this._genStr(12);c.innerHTML='<div id="'+c.id+'" class="dhx_cal_container" style="width:100%; height:100%;"><div class="dhx_cal_navline"><div class="dhx_cal_prev_button">&nbsp;</div><div class="dhx_cal_next_button">&nbsp;</div><div class="dhx_cal_today_button"></div><div class="dhx_cal_date"></div><div class="dhx_cal_tab" name="day_tab" style="right:204px;"></div><div class="dhx_cal_tab" name="week_tab" style="right:140px;"></div><div class="dhx_cal_tab" name="month_tab" style="right:76px;"></div></div><div class="dhx_cal_header"></div><div class="dhx_cal_data"></div></div>';
document.body.appendChild(c.firstChild);this.attachObject(c.id,!1,!0);this.vs[this.av].sched=scheduler;this.vs[this.av].schedId=c.id;scheduler.setSizes=scheduler.update_view;scheduler.destructor=function(){};scheduler.init(c.id,a,b);return this.vs[this._viewRestore()].sched};this.obj.attachTree=function(a){if(this._isWindow&&this.skin=="dhx_skyblue")this.vs[this.av].dhxcont.mainCont[this.av].style.border="#a4bed4 1px solid",this._redraw();var b=document.createElement("DIV");b.id="dhxTreeObj_"+this._genStr(12);
b.style.width="100%";b.style.height="100%";b.cmp="tree";document.body.appendChild(b);this.attachObject(b.id,!1,!0);this.vs[this.av].tree=new dhtmlXTreeObject(b.id,"100%","100%",a||0);this.vs[this.av].tree.setSkin(this.skin);this.vs[this.av].tree.allTree.childNodes[0].style.marginTop="2px";this.vs[this.av].tree.allTree.childNodes[0].style.marginBottom="2px";this.vs[this.av].treeId=b.id;this.vs[this.av].treeObj=b;return this.vs[this._viewRestore()].tree};this.obj.attachTabbar=function(a){if(this._isWindow&&
this.skin=="dhx_skyblue")this.vs[this.av].dhxcont.style.border="none",this.setDimension(this.w,this.h);var b=document.createElement("DIV");b.id="dhxTabbarObj_"+this._genStr(12);b.style.width="100%";b.style.height="100%";b.style.overflow="hidden";b.cmp="tabbar";document.body.appendChild(b);this.attachObject(b.id,!1,!0);this.className=="dhtmlxLayoutSinglePoly"&&this.hideHeader();this.vs[this.av].tabbar=new dhtmlXTabBar(b.id,a||"top",20);if(!this._isWindow)this.vs[this.av].tabbar._s.expand=!0;this.vs[this.av].tabbar.setSkin(this.skin);
this.vs[this.av].tabbar.adjustOuterSize();this.vs[this.av].tabbarId=b.id;this.vs[this.av].tabbarObj=b;return this.vs[this._viewRestore()].tabbar};this.obj.attachFolders=function(){if(this._isWindow&&this.skin=="dhx_skyblue")this.vs[this.av].dhxcont.mainCont[this.av].style.border="#a4bed4 1px solid",this._redraw();var a=document.createElement("DIV");a.id="dhxFoldersObj_"+this._genStr(12);a.style.width="100%";a.style.height="100%";a.style.overflow="hidden";a.cmp="folders";document.body.appendChild(a);
this.attachObject(a.id,!1,!0);this.vs[this.av].folders=new dhtmlxFolders(a.id);this.vs[this.av].folders.setSizes();this.vs[this.av].foldersId=a.id;this.vs[this.av].foldersObj=a;return this.vs[this._viewRestore()].folders};this.obj.attachAccordion=function(){if(this._isWindow&&this.skin=="dhx_skyblue")this.vs[this.av].dhxcont.mainCont[this.av].style.border="#a4bed4 1px solid",this._redraw();var a=document.createElement("DIV");a.id="dhxAccordionObj_"+this._genStr(12);this.skin=="dhx_web"?(a.style.left=
"0px",a.style.top="0px",a.style.width=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.width)+"px",a.style.height=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.height)+"px"):(a.style.left="-1px",a.style.top="-1px",a.style.width=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.width)+2+"px",a.style.height=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.height)+2+"px");a.style.position="relative";a.cmp="accordion";document.body.appendChild(a);this.attachObject(a.id,
!1,!0);this.vs[this.av].accordion=new dhtmlXAccordion(a.id,this.skin);this.vs[this.av].accordion.setSizes();this.vs[this.av].accordionId=a.id;this.vs[this.av].accordionObj=a;return this.vs[this._viewRestore()].accordion};this.obj.attachLayout=function(a,b){if(this._isCell&&this.skin=="dhx_skyblue")this.hideHeader(),this.vs[this.av].dhxcont.style.border="0px solid white",this.adjustContent(this.childNodes[0],0);this._isCell&&this.skin=="dhx_web"&&this.hideHeader();var c=document.createElement("DIV");
c.id="dhxLayoutObj_"+this._genStr(12);c.style.overflow="hidden";c.style.position="absolute";c.style.left="0px";c.style.top="0px";c.style.width=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.width)+"px";c.style.height=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.height)+"px";if((this._isTabbarCell||this._isAcc)&&this.skin=="dhx_skyblue")c.style.left="-1px",c.style.top="-1px",c.style.width=parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.width)+2+"px",c.style.height=
parseInt(this.vs[this.av].dhxcont.mainCont[this.av].style.height)+2+"px";c.dhxContExists=!0;c.cmp="layout";document.body.appendChild(c);this.attachObject(c.id,!1,!0);this.vs[this.av].layout=new dhtmlXLayoutObject(c,a,b||this.skin);this._isWindow&&this.attachEvent("_onBeforeTryResize",this.vs[this.av].layout._defineWindowMinDimension);this.vs[this.av].layoutId=c.id;this.vs[this.av].layoutObj=c;return this.vs[this._viewRestore()].layout};this.obj.attachEditor=function(){if(this._isWindow&&this.skin==
"dhx_skyblue")this.vs[this.av].dhxcont.mainCont[this.av].style.border="#a4bed4 1px solid",this._redraw();var a=document.createElement("DIV");a.id="dhxEditorObj_"+this._genStr(12);a.style.position="relative";a.style.display="none";a.style.overflow="hidden";a.style.width="100%";a.style.height="100%";a.cmp="editor";document.body.appendChild(a);this.attachObject(a.id,!1,!0);this.vs[this.av].editor=new dhtmlXEditor(a.id,this.skin);this.vs[this.av].editorId=a.id;this.vs[this.av].editorObj=a;return this.vs[this._viewRestore()].editor};
this.obj.attachMap=function(a){var b=document.createElement("DIV");b.id="GMapsObj_"+this._genStr(12);b.style.position="relative";b.style.display="none";b.style.overflow="hidden";b.style.width="100%";b.style.height="100%";b.cmp="gmaps";document.body.appendChild(b);this.attachObject(b.id,!1,!0);a||(a={center:new google.maps.LatLng(40.719837,-73.992348),zoom:11,mapTypeId:google.maps.MapTypeId.ROADMAP});this.vs[this.av].gmaps=new google.maps.Map(b,a);return this.vs[this.av].gmaps};this.obj.attachObject=
function(a,b,c){typeof a=="string"&&(a=document.getElementById(a));if(b){a.style.visibility="hidden";a.style.display="";var d=a.offsetWidth,f=a.offsetHeight}this._attachContent("obj",a);if(b&&this._isWindow)a.style.visibility="",this._adjustToContent(d,f);c||this._viewRestore()};this.obj.detachObject=function(a,b){for(var c=null,d=null,f="tree,grid,layout,tabbar,accordion,folders".split(","),e=0;e<f.length;e++)if(this.vs[this.av][f[e]]){c=this.vs[this.av][f[e]];d=this.vs[this.av][f[e]+"Obj"];if(a){c.unload&&
c.unload();for(c.destructor&&c.destructor();d.childNodes.length>0;)d.removeChild(d.childNodes[0]);d.parentNode.removeChild(d);c=d=null}else document.body.appendChild(d),d.style.display="none";this.vs[this.av][f[e]]=null;this.vs[this.av][f[e]+"Id"]=null;this.vs[this.av][f[e]+"Obj"]=null}if(c!=null&&d!=null)return[c,d];if(a&&this.vs[this.av]._frame)this._detachURLEvents(),this.vs[this.av]._frame=null;for(var g=this.vs[this.av].dhxcont.mainCont[this.av];g.childNodes.length>0;)if(a==!0)g.removeChild(g.childNodes[0]);
else{var h=g.childNodes[0];b!=null?(typeof b!="object"&&(b=document.getElementById(b)),b.appendChild(h)):document.body.appendChild(h);h.style.display="none"}};this.obj.appendObject=function(a){typeof a=="string"&&(a=document.getElementById(a));this._attachContent("obj",a,!0)};this.obj.attachHTMLString=function(a){this._attachContent("str",a);for(var b=a.match(/<script[^>]*>[^\f]*?<\/script>/g)||[],c=0;c<b.length;c++){var d=b[c].replace(/<([\/]{0,1})script[^>]*>/g,"");window.execScript?window.execScript(d):
window.eval(d)}};this.obj.attachURL=function(a,b){this._attachContent(b==!0?"urlajax":"url",a,!1);this._viewRestore()};this.obj.adjust=function(){if(this.skin=="dhx_skyblue"){if(this.vs[this.av].menu){if(this._isWindow||this._isLayout)this.vs[this.av].menu._topLevelOffsetLeft=0,document.getElementById(this.vs[this.av].menuId).style.height="26px",this.vs[this.av].menuHeight=document.getElementById(this.vs[this.av].menuId).offsetHeight,this._doOnAttachMenu&&this._doOnAttachMenu("show");if(this._isCell)document.getElementById(this.vs[this.av].menuId).className+=
" in_layoutcell",this.vs[this.av].menuHeight=25;if(this._isAcc)document.getElementById(this.vs[this.av].menuId).className+=" in_acccell",this.vs[this.av].menuHeight=25;this._doOnAttachMenu&&this._doOnAttachMenu("adjust")}if(this.vs[this.av].toolbar){if(this._isWindow||this._isLayout)document.getElementById(this.vs[this.av].toolbarId).style.height="29px",this.vs[this.av].toolbarHeight=document.getElementById(this.vs[this.av].toolbarId).offsetHeight,this._doOnAttachToolbar&&this._doOnAttachToolbar("show");
this._isCell&&(document.getElementById(this.vs[this.av].toolbarId).className+=" in_layoutcell");this._isAcc&&(document.getElementById(this.vs[this.av].toolbarId).className+=" in_acccell")}}};this.obj._attachContent=function(a,b,c){if(c!==!0){if(this.vs[this.av]._frame)this._detachURLEvents(),this.vs[this.av]._frame=null;for(;this.vs[this.av].dhxcont.mainCont[this.av].childNodes.length>0;)this.vs[this.av].dhxcont.mainCont[this.av].removeChild(this.vs[this.av].dhxcont.mainCont[this.av].childNodes[0])}if(a==
"url"){if(this._isWindow&&b.cmp==null&&this.skin=="dhx_skyblue")this.vs[this.av].dhxcont.mainCont[this.av].style.border="#a4bed4 1px solid",this._redraw();var d=document.createElement("IFRAME");d.frameBorder=0;d.border=0;d.style.width="100%";d.style.height="100%";d.setAttribute("src","javascript:false;");this.vs[this.av].dhxcont.mainCont[this.av].appendChild(d);d.src=b;this.vs[this.av]._frame=d;this._attachURLEvents()}else if(a=="urlajax"){if(this._isWindow&&b.cmp==null&&this.skin=="dhx_skyblue")this.vs[this.av].dhxcont.mainCont[this.av].style.border=
"#a4bed4 1px solid",this.vs[this.av].dhxcont.mainCont[this.av].style.backgroundColor="#FFFFFF",this._redraw();var e=this,i=String(this.av).valueOf(),g=function(){var a=e.av;e.av=i;e.attachHTMLString(this.xmlDoc.responseText,this);e.av=a;e._doOnFrameContentLoaded&&e._doOnFrameContentLoaded();this.destructor()},h=new dtmlXMLLoaderObject(g,window);h.dhxWindowObject=this;h.loadXML(b)}else if(a=="obj"){if(this._isWindow&&b.cmp==null&&this.skin=="dhx_skyblue")this.vs[this.av].dhxcont.mainCont[this.av].style.border=
"#a4bed4 1px solid",this.vs[this.av].dhxcont.mainCont[this.av].style.backgroundColor="#FFFFFF",this._redraw();this.vs[this.av].dhxcont._frame=null;this.vs[this.av].dhxcont.mainCont[this.av].appendChild(b);this.vs[this.av].dhxcont.mainCont[this.av].style.overflow=c===!0?"auto":"hidden";b.style.display=""}else if(a=="str"){if(this._isWindow&&b.cmp==null&&this.skin=="dhx_skyblue")this.vs[this.av].dhxcont.mainCont[this.av].style.border="#a4bed4 1px solid",this.vs[this.av].dhxcont.mainCont[this.av].style.backgroundColor=
"#FFFFFF",this._redraw();this.vs[this.av].dhxcont._frame=null;this.vs[this.av].dhxcont.mainCont[this.av].innerHTML=b}};this.obj._attachURLEvents=function(){var a=this,b=this.vs[this.av]._frame;_isIE?b.onreadystatechange=function(){if(b.readyState=="complete"){try{b.contentWindow.document.body.onmousedown=function(){a._doOnFrameMouseDown&&a._doOnFrameMouseDown()}}catch(c){}try{a._doOnFrameContentLoaded&&a._doOnFrameContentLoaded()}catch(d){}}}:b.onload=function(){try{b.contentWindow.onmousedown=function(){a._doOnFrameMouseDown&&
a._doOnFrameMouseDown()}}catch(c){}try{a._doOnFrameContentLoaded&&a._doOnFrameContentLoaded()}catch(d){}}};this.obj._detachURLEvents=function(){if(_isIE)try{this.vs[this.av]._frame.onreadystatechange=null,this.vs[this.av]._frame.contentWindow.document.body.onmousedown=null,this.vs[this.av]._frame.onload=null}catch(a){}else try{this.vs[this.av]._frame.contentWindow.onmousedown=null,this.vs[this.av]._frame.onload=null}catch(b){}};this.obj.showMenu=function(){if(this.vs[this.av].menu&&this.vs[this.av].menuId&&
document.getElementById(this.vs[this.av].menuId).style.display=="none")this.vs[this.av].menuHidden=!1,this._doOnAttachMenu&&this._doOnAttachMenu("show"),document.getElementById(this.vs[this.av].menuId).style.display="",this._viewRestore()};this.obj.hideMenu=function(){if(this.vs[this.av].menu&&this.vs[this.av].menuId&&document.getElementById(this.vs[this.av].menuId).style.display!="none")document.getElementById(this.vs[this.av].menuId).style.display="none",this.vs[this.av].menuHidden=!0,this._doOnAttachMenu&&
this._doOnAttachMenu("hide"),this._viewRestore()};this.obj.showToolbar=function(){if(this.vs[this.av].toolbar&&this.vs[this.av].toolbarId&&document.getElementById(this.vs[this.av].toolbarId).style.display=="none")this.vs[this.av].toolbarHidden=!1,this._doOnAttachToolbar&&this._doOnAttachToolbar("show"),document.getElementById(this.vs[this.av].toolbarId).style.display="",this._viewRestore()};this.obj.hideToolbar=function(){if(this.vs[this.av].toolbar&&this.vs[this.av].toolbarId&&document.getElementById(this.vs[this.av].toolbarId).style.display!=
"none")this.vs[this.av].toolbarHidden=!0,document.getElementById(this.vs[this.av].toolbarId).style.display="none",this._doOnAttachToolbar&&this._doOnAttachToolbar("hide"),this._viewRestore()};this.obj.showStatusBar=function(){if(this.vs[this.av].sb&&this.vs[this.av].sbId&&document.getElementById(this.vs[this.av].sbId).style.display=="none")this.vs[this.av].sbHidden=!1,this._doOnAttachStatusBar&&this._doOnAttachStatusBar("show"),document.getElementById(this.vs[this.av].sbId).style.display="",this._viewRestore()};
this.obj.hideStatusBar=function(){if(this.vs[this.av].sb&&this.vs[this.av].sbId&&document.getElementById(this.vs[this.av].sbId).style.display!="none")this.vs[this.av].sbHidden=!0,document.getElementById(this.vs[this.av].sbId).style.display="none",this._doOnAttachStatusBar&&this._doOnAttachStatusBar("hide"),this._viewRestore()};this.obj._dhxContDestruct=function(){var a=this.av,b;for(b in this.vs)this.av=b,this.detachMenu(),this.detachToolbar(),this.detachStatusBar(),this.detachObject(!0),this.vs[b].dhxcont.mainCont[b].parentNode.removeChild(this.vs[b].dhxcont.mainCont[b]),
this.vs[b].dhxcont.mainCont[b]=null;this.vs[this.dv].dhxcont.mainCont=null;this.vs[this.dv].dhxcont.parentNode.removeChild(this.vs[this.dv].dhxcont);for(b in this.vs)this.vs[b].dhxcont=null;this._dhxContDestruct=this._genStr=this._init=this._setPadding=this._viewRestore=this._detachURLEvents=this._attachURLEvents=this._attachContent=this.updateNestedObjects=this.hideCoverBlocker=this.showCoverBlocker=this.coverBlocker=this.adjustContent=this.moveContentTo=this.setMinContentSize=this.adjust=this.show=
this.view=this.attachURL=this.attachHTMLString=this.appendObject=this.detachObject=this.attachObject=this.attachEditor=this.attachLayout=this.attachAccordion=this.attachFolders=this.attachTabbar=this.attachTree=this.attachScheduler=this.attachGrid=this.hideStatusBar=this.hideToolbar=this.hideMenu=this.showStatusBar=this.showToolbar=this.showMenu=this.detachStatusBar=this.detachToolbar=this.detachMenu=this.attachStatusBar=this.attachToolbar=this.attachMenu=this.vs=null;e.st.parentNode.removeChild(e.st);
e.st=null;e.setContent=null;e.dhxcont=null;e=e.obj=null;if(dhtmlx.detaches)for(b in dhtmlx.detaches)dhtmlx.detaches[b](this)};if(dhtmlx.attaches)for(var i in dhtmlx.attaches)this.obj[i]=dhtmlx.attaches[i]};

//v.3.0 build 110713

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/