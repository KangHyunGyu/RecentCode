//v.3.0 build 110713

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
dhtmlXGridObject.prototype.hidePivot=function(){if(this._pgridCont){this._pgrid&&this._pgrid.destructor();var a=this._pgridCont.parentNode;a.innerHTML="";a.parentNode==this.entBox&&this.entBox.removeChild(a);this._pgrid=this._pgridSelect=this._pUNI=this._pgridCont=null}};
dhtmlXGridObject.prototype.makePivot=function(a,b){b=b||{};this.hidePivot();if(!a){a=document.createElement("DIV");a.style.cssText="position:absolute; top:0px; left:0px;background-color:white;";a.style.height=this.entBox.offsetHeight+"px";a.style.width=this.entBox.offsetWidth+"px";if(this.entBox.style.position!="absolute")this.entBox.style.position="relative";this.entBox.appendChild(a)}typeof a!="object"&&(a=document.getElementById(a));if(b.column_list)this._column_list=b.column_list;else{this._column_list=
[];for(var e=0;e<this.hdr.rows[1].cells.length;e++)this._column_list.push(this.hdr.rows[1].cells[e][_isIE?"innerText":"textContent"])}var c=this;a.innerHTML="<table cellspacing='0' cellpadding='0'><tr><td style='width:160px' align='center'></td><td>&nbsp;&nbsp;&nbsp;</td><td></td></tr></table><div></div>";var d=this.makePivotSelect(this._column_list);d.style.width="80px";d.onchange=function(){c._pivotS.value=this.value!=-1?this.value:"";c._reFillPivotLists();c._renderPivot2()};var f=this.makePivotSelect(this._column_list);
f.onchange=function(){c._pivotS.x=this.value!=-1?this.value:"";c._reFillPivotLists();c._renderPivot()};var g=this.makePivotSelect(this._column_list);g.onchange=function(){c._pivotS.y=this.value!=-1?this.value:"";c._reFillPivotLists();c._renderPivot()};var h=this.makePivotSelect(["Sum","Min","Max","Average","Count"],-1);h.style.width="70px";h.onchange=function(){c._pivotS.action=this.value!=-1?this.value:null;c._renderPivot2()};if(b.readonly)d.disabled=f.disabled=g.disabled=h.disabled=!0;a.firstChild.rows[0].cells[0].appendChild(h);
a.firstChild.rows[0].cells[0].appendChild(d);a.firstChild.rows[0].cells[2].appendChild(f);var j=a.childNodes[1];j.style.width=a.offsetWidth+"px";j.style.height=a.offsetHeight-20+"px";j.style.overflow="hidden";this._pgridCont=j;this._pgridSelect=[d,f,g,h];this._pData=this._fetchPivotData();this._pUNI=[];this._pivotS={action:b.action||"0",value:typeof b.value!="undefined"?b.value||"0":null,x:typeof b.x!="undefined"?b.x||"0":null,y:typeof b.y!="undefined"?b.y||"0":null};d.value=this._pivotS.value;f.value=
this._pivotS.x;g.value=this._pivotS.y;h.value=this._pivotS.action;c._reFillPivotLists();this._renderPivot()};dhtmlXGridObject.prototype._fetchPivotData=function(){for(var a=[],b=0;b<this._cCount;b++){for(var e=[],c=0;c<this.rowsCol.length;c++)this.rowsCol[c]._cntr||e.push(this.cells2(c,b).getValue());a.push(e)}return a};
dhtmlXGridObject.prototype._renderPivot=function(){_isIE&&this._pgridSelect[2].removeNode(!0);this._pgrid&&this._pgrid.destructor();this._pgrid=new dhtmlXGridObject(this._pgridCont);this._pgrid.attachEvent("onBeforeSelect",function(){return!1});if(this._pivotS.x){for(var a=this._getUniList(this._pivotS.x),b=[160],e=0;e<a.length;e++)b.push(100);a=[""].concat(a);this._pgrid.setHeader(a);this._pgrid.setInitWidths(b.join(","))}else this._pgrid.setHeader(""),this._pgrid.setInitWidths("160");this._pgrid.init();
this._pgrid.setEditable(!1);this._pgrid.setSkin(this.entBox.className.replace("gridbox gridbox_",""));var c=this._pgrid.hdr.rows[1].cells[0];if(c.firstChild&&c.firstChild.tagName=="DIV")c=c.firstChild;c.appendChild(this._pgridSelect[2]);this._pgrid.setSizes();if(this._pivotS.y){a=this._getUniList(this._pivotS.y);for(e=0;e<a.length;e++)this._pgrid.addRow(this._pgrid.uid(),[a[e]],-1)}else this._pgrid.addRow(1,"not ready",1);this._renderPivot2()};
dhtmlXGridObject.prototype._pivot_action_0=function(a,b,e,c,d,f){for(var g=0,h=f[a],j=f[b],i=f[e],l=h.length-1;l>=0;l--)h[l]==c&&j[l]==d&&(g+=parseFloat(i[l]));return g};dhtmlXGridObject.prototype._pivot_action_1=function(a,b,e,c,d,f){ret=9999999999;for(var g=f[a],h=f[b],j=f[e],i=g.length-1;i>=0;i--)g[i]==c&&h[i]==d&&(ret=Math.min(parseFloat(j[i]),ret));ret==9999999999&&(ret="");return ret};
dhtmlXGridObject.prototype._pivot_action_2=function(a,b,e,c,d,f){ret=-9999999999;for(var g=f[a],h=f[b],j=f[e],i=g.length-1;i>=0;i--)g[i]==c&&h[i]==d&&(ret=Math.max(parseFloat(j[i]),ret));ret==-9999999999&&(ret="");return ret};dhtmlXGridObject.prototype._pivot_action_3=function(a,b,e,c,d,f){for(var g=0,h=0,j=f[a],i=f[b],l=f[e],k=j.length-1;k>=0;k--)j[k]==c&&i[k]==d&&(g+=parseFloat(l[k]),h++);return h?g/h:""};
dhtmlXGridObject.prototype._pivot_action_4=function(a,b,e,c,d,f){for(var g=0,h=0,j=f[a],i=f[b],l=f[e],k=j.length-1;k>=0;k--)j[k]==c&&i[k]==d&&g++;return g};
dhtmlXGridObject.prototype._renderPivot2=function(){if(this._pivotS.x&&this._pivotS.y&&this._pivotS.value&&this._pivotS.action)for(var a=this["_pivot_action_"+this._pivotS.action],b=this._getUniList(this._pivotS.x),e=this._getUniList(this._pivotS.y),c=0;c<b.length;c++)for(var d=0;d<e.length;d++)this._pgrid.cells2(d,c+1).setValue(Math.round(a(this._pivotS.x,this._pivotS.y,this._pivotS.value,b[c],e[d],this._pData)*100)/100)};
dhtmlXGridObject.prototype._getUniList=function(a){if(!this._pUNI[a]){for(var b={},e=[],c=this._pData[a].length-1;c>=0;c--)b[this._pData[a][c]]=!0;for(var d in b)b[d]===!0&&e.push(d);this._pUNI[a]=e.sort()}return this._pUNI[a]};dhtmlXGridObject.prototype._fillPivotList=function(a,b,e,c){e||(e={},c=-1);a.innerHTML="";a.options[a.options.length]=new Option("-select-",-1);for(var d=0;d<b.length;d++)e[d]||b[d]===null||(a.options[a.options.length]=new Option(b[d],d));a.value=parseInt(c)};
dhtmlXGridObject.prototype._reFillPivotLists=function(){for(var a=[],b=[],e=0;e<3;e++)a.push(this._pgridSelect[e]),b.push(a[e].value);var c=this._reFfillPivotLists,d={};d[b[1]]=d[b[2]]=!0;this._fillPivotList(a[0],this._column_list,d,b[0]);d={};d[b[0]]=d[b[2]]=!0;this._fillPivotList(a[1],this._column_list,d,b[1]);d={};d[b[1]]=d[b[0]]=!0;this._fillPivotList(a[2],this._column_list,d,b[2]);this._reFfillPivotLists=c};
dhtmlXGridObject.prototype.makePivotSelect=function(a,b){var e=document.createElement("SELECT");this._fillPivotList(e,a,b);e.style.cssText="width:150px; height:20px; font-family:Tahoma; font-size:8pt; font-weight:normal;";return e};

//v.3.0 build 110713

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/