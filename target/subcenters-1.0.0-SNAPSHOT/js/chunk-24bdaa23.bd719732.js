(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-24bdaa23"],{"0241":function(t,e,n){},"28a5":function(t,e,n){"use strict";var i=n("aae3"),s=n("cb7c"),a=n("ebd6"),l=n("0390"),o=n("9def"),r=n("5f1b"),c=n("520a"),u=n("79e5"),g=Math.min,d=[].push,f="split",x="length",m="lastIndex",v=4294967295,p=!u(function(){RegExp(v,"y")});n("214f")("split",2,function(t,e,n,u){var b;return b="c"=="abbc"[f](/(b)*/)[1]||4!="test"[f](/(?:)/,-1)[x]||2!="ab"[f](/(?:ab)*/)[x]||4!="."[f](/(.?)(.?)/)[x]||"."[f](/()()/)[x]>1||""[f](/.?/)[x]?function(t,e){var s=String(this);if(void 0===t&&0===e)return[];if(!i(t))return n.call(s,t,e);var a,l,o,r=[],u=(t.ignoreCase?"i":"")+(t.multiline?"m":"")+(t.unicode?"u":"")+(t.sticky?"y":""),g=0,f=void 0===e?v:e>>>0,p=new RegExp(t.source,u+"g");while(a=c.call(p,s)){if(l=p[m],l>g&&(r.push(s.slice(g,a.index)),a[x]>1&&a.index<s[x]&&d.apply(r,a.slice(1)),o=a[0][x],g=l,r[x]>=f))break;p[m]===a.index&&p[m]++}return g===s[x]?!o&&p.test("")||r.push(""):r.push(s.slice(g)),r[x]>f?r.slice(0,f):r}:"0"[f](void 0,0)[x]?function(t,e){return void 0===t&&0===e?[]:n.call(this,t,e)}:n,[function(n,i){var s=t(this),a=void 0==n?void 0:n[e];return void 0!==a?a.call(n,s,i):b.call(String(s),n,i)},function(t,e){var i=u(b,t,this,e,b!==n);if(i.done)return i.value;var c=s(t),d=String(this),f=a(c,RegExp),x=c.unicode,m=(c.ignoreCase?"i":"")+(c.multiline?"m":"")+(c.unicode?"u":"")+(p?"y":"g"),h=new f(p?c:"^(?:"+c.source+")",m),C=void 0===e?v:e>>>0;if(0===C)return[];if(0===d.length)return null===r(h,d)?[d]:[];var _=0,k=0,$=[];while(k<d.length){h.lastIndex=p?k:0;var w,E=r(h,p?d:d.slice(k));if(null===E||(w=g(o(h.lastIndex+(p?0:k)),d.length))===_)k=l(d,k,x);else{if($.push(d.slice(_,k)),$.length===C)return $;for(var S=1;S<=E.length-1;S++)if($.push(E[S]),$.length===C)return $;k=_=w}}return $.push(d.slice(_)),$}]})},"36eb":function(t,e,n){},4882:function(t,e,n){"use strict";var i=n("55b7"),s=n.n(i);s.a},"55b7":function(t,e,n){},"5de7":function(t,e,n){"use strict";var i=n("36eb"),s=n.n(i);s.a},"6e6d":function(t,e,n){"use strict";n.r(e);var i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"tw-setting-template"},[n("div",{ref:"setting",staticClass:"tw-setting"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("终端选择设置")]),n("div",{staticClass:"tw-setting-form"},[n("el-form",{attrs:{inline:!0,"label-width":"80px"}},[n("el-form-item",{attrs:{label:"终端选择"}},[n("el-autocomplete",{staticStyle:{width:"280px"},attrs:{"fetch-suggestions":t.querySearchAsync,placeholder:t.queryPlaceholder,"popper-class":"vehicleWidth"},on:{select:t.handleSelect},model:{value:t.vehicle,callback:function(e){t.vehicle=e},expression:"vehicle"}})],1),n("el-form-item",[n("el-button",{attrs:{size:"mini",type:"primary"},on:{click:t.getTerminal}},[t._v("查询")])],1),n("el-form-item",[n("el-button",{attrs:{size:"mini",type:"primary"},on:{click:t.getSetupState}},[t._v("终端设置")])],1)],1)],1)]),n("div",{directives:[{name:"loading",rawName:"v-loading",value:t.loading,expression:"loading"}],ref:"tabContent",staticClass:"tw-tab-content"},[n("el-tabs",{attrs:{type:"card"},on:{"tab-click":t.handleClick},model:{value:t.activeName,callback:function(e){t.activeName=e},expression:"activeName"}},[n("el-tab-pane",{attrs:{label:"基本信息",name:"first"}},[n("basic-info",{attrs:{setting:t.data},on:{test:t.getEditInfo}})],1),n("el-tab-pane",{attrs:{label:"连接参数",name:"second"}},[n("connection-parameter",{attrs:{setting:t.data},on:{test:t.getEditInfo}})],1),n("el-tab-pane",{attrs:{label:"汇报参数",name:"third"}},[n("reporting-parameters",{attrs:{setting:t.data},on:{test:t.getEditInfo}})],1),n("el-tab-pane",{attrs:{label:"通话参数",name:"fourth"}},[n("call-parameter",{attrs:{setting:t.data},on:{test:t.getEditInfo}})],1)],1)],1)])},s=[],a=(n("28a5"),function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"tw-setting-template"},[n("div",{staticStyle:{float:"left",width:"50%"}},[n("div",{ref:"setting",staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("终端发送设置")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",{attrs:{"label-width":"180px"}},[n("el-form-item",{attrs:{label:"终端心跳发送间隔（秒）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0001")}},model:{value:t.setting["0x0001"],callback:function(e){t.$set(t.setting,"0x0001",e)},expression:"setting['0x0001']"}})],1)],1)],1)]),n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("位置汇报位置")]),n("div",{staticClass:"tw-setting-inside"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("位置汇报策略")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",{attrs:{"label-width":"10px"}},[n("el-form-item",[n("el-radio-group",{on:{change:function(e){return t.editEvent("0x0020")}},model:{value:t.setting["0x0020"],callback:function(e){t.$set(t.setting,"0x0020",e)},expression:"setting['0x0020']"}},[n("el-radio",{attrs:{label:0}},[t._v("定时汇报")]),n("el-radio",{attrs:{label:1}},[t._v("定距汇报")]),n("el-radio",{attrs:{label:2}},[t._v("定时和定距")])],1)],1)],1)],1)]),n("div",{staticClass:"tw-setting-inside"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("位置汇报方案")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",[n("el-form-item",{attrs:{"label-width":"30px"}},[n("el-radio-group",{on:{change:function(e){return t.editEvent("0x0021")}},model:{value:t.setting["0x0021"],callback:function(e){t.$set(t.setting,"0x0021",e)},expression:"setting['0x0021']"}},[n("el-radio",{attrs:{label:0}},[t._v("根据ACC状态")]),n("el-radio",{attrs:{label:1}},[t._v("根据空重车状态")]),n("el-radio",{attrs:{label:3}},[t._v("根据登录状态和空重车状态")]),n("el-radio",{attrs:{label:2}},[t._v("根据登录状态和ACC状态")])],1)],1)],1)],1)])]),n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("终端发送设置")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",{attrs:{"label-width":"230px","label-position":"left"}},[n("el-form-item",{attrs:{label:"驾驶员未登录汇报时间间隔（秒）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0022")}},model:{value:t.setting["0x0022"],callback:function(e){t.$set(t.setting,"0x0022",e)},expression:"setting['0x0022']"}})],1),n("el-form-item",{attrs:{label:"ACC OFF汇报时间间隔（秒）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0023")}},model:{value:t.setting["0x0023"],callback:function(e){t.$set(t.setting,"0x0023",e)},expression:"setting['0x0023']"}})],1),n("el-form-item",{attrs:{label:"ACC ON时间汇报时间间隔（秒）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0024")}},model:{value:t.setting["0x0024"],callback:function(e){t.$set(t.setting,"0x0024",e)},expression:"setting['0x0024']"}})],1),n("el-form-item",{attrs:{label:"空车时间汇报时间间隔（秒）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0025")}},model:{value:t.setting["0x0025"],callback:function(e){t.$set(t.setting,"0x0025",e)},expression:"setting['0x0025']"}})],1),n("el-form-item",{attrs:{label:"重车时间汇报时间间隔（秒）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0026")}},model:{value:t.setting["0x0026"],callback:function(e){t.$set(t.setting,"0x0026",e)},expression:"setting['0x0026']"}})],1),n("el-form-item",{attrs:{label:"休眠时汇报时间间隔（秒）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0027")}},model:{value:t.setting["0x0027"],callback:function(e){t.$set(t.setting,"0x0027",e)},expression:"setting['0x0027']"}})],1),n("el-form-item",{attrs:{label:"紧急报警时汇报时间间隔（秒）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0028")}},model:{value:t.setting["0x0028"],callback:function(e){t.$set(t.setting,"0x0028",e)},expression:"setting['0x0028']"}})],1)],1)],1)])]),n("div",{staticStyle:{float:"left",width:"50%"}},[n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("其他设置")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",{attrs:{"label-width":"120px"}},[n("el-form-item",{attrs:{label:"拐点补传角度"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0030")}},model:{value:t.setting["0x0030"],callback:function(e){t.$set(t.setting,"0x0030",e)},expression:"setting['0x0030']"}})],1)],1)],1)]),n("div",{staticClass:"tw-setting tab",staticStyle:{"margin-bottom":"68px"}},[n("div",{staticClass:"el-divider__text is-left"},[t._v("SMS设置")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",{attrs:{"label-width":"180px","label-position":"left"}},[n("el-form-item",{attrs:{label:"消息应答超时时间（秒）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0004")}},model:{value:t.setting["0x0004"],callback:function(e){t.$set(t.setting,"0x0004",e)},expression:"setting['0x0004']"}})],1),n("el-form-item",{attrs:{label:"消息重传次数"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0005")}},model:{value:t.setting["0x0005"],callback:function(e){t.$set(t.setting,"0x0005",e)},expression:"setting['0x0005']"}})],1)],1)],1)]),n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("距离间隔汇报设置")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",{attrs:{"label-width":"230px","label-position":"left"}},[n("el-form-item",{attrs:{label:"驾驶员未登录汇报时间间隔（米）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0029")}},model:{value:t.setting["0x0029"],callback:function(e){t.$set(t.setting,"0x0029",e)},expression:"setting['0x0029']"}})],1),n("el-form-item",{attrs:{label:"ACC OFF汇报时间间隔（米）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x002a")}},model:{value:t.setting["0x002a"],callback:function(e){t.$set(t.setting,"0x002a",e)},expression:"setting['0x002a']"}})],1),n("el-form-item",{attrs:{label:"ACC ON时间汇报时间间隔（米）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x002b")}},model:{value:t.setting["0x002b"],callback:function(e){t.$set(t.setting,"0x002b",e)},expression:"setting['0x002b']"}})],1),n("el-form-item",{attrs:{label:"空车时间汇报时间间隔（米）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x002c")}},model:{value:t.setting["0x002c"],callback:function(e){t.$set(t.setting,"0x002c",e)},expression:"setting['0x002c']"}})],1),n("el-form-item",{attrs:{label:"重车时间汇报时间间隔（米）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x002d")}},model:{value:t.setting["0x002d"],callback:function(e){t.$set(t.setting,"0x002d",e)},expression:"setting['0x002d']"}})],1),n("el-form-item",{attrs:{label:"休眠时汇报时间间隔（米）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x002e")}},model:{value:t.setting["0x002e"],callback:function(e){t.$set(t.setting,"0x002e",e)},expression:"setting['0x002e']"}})],1),n("el-form-item",{attrs:{label:"紧急报警时汇报时间间隔（米）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x002f")}},model:{value:t.setting["0x002f"],callback:function(e){t.$set(t.setting,"0x002f",e)},expression:"setting['0x002f']"}})],1)],1)],1)])])])}),l=[],o={name:"ReportingParameters",data:function(){return{radio:3}},methods:{editEvent:function(t){this.$emit("test",t)}},props:["setting"]},r=o,c=(n("af9b"),n("2877")),u=Object(c["a"])(r,a,l,!1,null,"6a30afe6",null),g=u.exports,d=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"tw-setting-template"},[n("div",{staticStyle:{float:"left",width:"33.333%"}},[n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("车辆信息设置")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",[n("span",{staticClass:"setting-label"},[t._v("车辆里程表读数，1/10km")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0080")}},model:{value:t.setting["0x0080"],callback:function(e){t.$set(t.setting,"0x0080",e)},expression:"setting['0x0080']"}})],1),n("span",{staticClass:"setting-label"},[t._v("车辆所在的省域ID")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0081")}},model:{value:t.setting["0x0081"],callback:function(e){t.$set(t.setting,"0x0081",e)},expression:"setting['0x0081']"}})],1),n("span",{staticClass:"setting-label"},[t._v("车辆所在的市域ID")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0082")}},model:{value:t.setting["0x0082"],callback:function(e){t.$set(t.setting,"0x0082",e)},expression:"setting['0x0082']"}})],1),n("span",{staticClass:"setting-label"},[t._v("出租车企业营运许可证号")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x00892")}},model:{value:t.setting["0x0092"],callback:function(e){t.$set(t.setting,"0x0092",e)},expression:"setting['0x0092']"}})],1),n("span",{staticClass:"setting-label"},[t._v("出租车企业简称")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0093")}},model:{value:t.setting["0x0093"],callback:function(e){t.$set(t.setting,"0x0093",e)},expression:"setting['0x0093']"}})],1),n("span",{staticClass:"setting-label"},[t._v("出租车车牌号码（不包含汉字）")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0094")}},model:{value:t.setting["0x0094"],callback:function(e){t.$set(t.setting,"0x0094",e)},expression:"setting['0x0094']"}})],1)],1)],1)]),n("div",{staticClass:"tw-setting-form tab",staticStyle:{"padding-top":"0px"}},[n("el-form",[n("span",{staticClass:"setting-label"},[t._v("终端设备维护密码")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x004a")}},model:{value:t.setting["0x004a"],callback:function(e){t.$set(t.setting,"0x004a",e)},expression:"setting['0x004a']"}})],1)],1)],1)]),n("div",{staticStyle:{float:"left",width:"33.333%",height:"100%"}},[n("div",{staticClass:"tw-setting tab",staticStyle:{height:"100%"}},[n("div",{staticClass:"el-divider__text is-left"},[t._v("驾驶设置")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",[n("span",{staticClass:"setting-label"},[t._v("最高速度（km/h）")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0055")}},model:{value:t.setting["0x0055"],callback:function(e){t.$set(t.setting,"0x0055",e)},expression:"setting['0x0055']"}})],1),n("span",{staticClass:"setting-label"},[t._v("超速持续时间（秒）")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0056")}},model:{value:t.setting["0x0056"],callback:function(e){t.$set(t.setting,"0x0056",e)},expression:"setting['0x0056']"}})],1),n("span",{staticClass:"setting-label"},[t._v("连续驾驶时间门限（秒）")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0057")}},model:{value:t.setting["0x0057"],callback:function(e){t.$set(t.setting,"0x0057",e)},expression:"setting['0x0057']"}})],1),n("span",{staticClass:"setting-label"},[t._v("当天累计驾驶时间门限（秒）")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x005a")}},model:{value:t.setting["0x005a"],callback:function(e){t.$set(t.setting,"0x005a",e)},expression:"setting['0x005a']"}})],1),n("span",{staticClass:"setting-label"},[t._v("最小休息时间（秒）")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0058")}},model:{value:t.setting["0x0058"],callback:function(e){t.$set(t.setting,"0x0058",e)},expression:"setting['0x0058']"}})],1),n("span",{staticClass:"setting-label"},[t._v("最长停车时间（秒）")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0059")}},model:{value:t.setting["0x0059"],callback:function(e){t.$set(t.setting,"0x0059",e)},expression:"setting['0x0059']"}})],1)],1)],1)])]),n("div",{staticStyle:{float:"left",width:"33.333%"}},[n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("计价器设置")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",[n("span",{staticClass:"setting-label"},[t._v("营运次数限制：0-9999；0：不限制")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0090")}},model:{value:t.setting["0x0090"],callback:function(e){t.$set(t.setting,"0x0090",e)},expression:"setting['0x0090']"}})],1),n("span",{staticClass:"setting-label"},[t._v("营运时间限制(yyyyMMDDhh);0000000000：不限制")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0091")}},model:{value:t.setting["0x0091"],callback:function(e){t.$set(t.setting,"0x0091",e)},expression:"setting['0x0091']"}})],1)],1)],1)]),n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("智能终端录音模式")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",[n("el-radio-group",{on:{change:function(e){return t.editEvent("0x00a0")}},model:{value:t.setting["0x00a0"],callback:function(e){t.$set(t.setting,"0x00a0",e)},expression:"setting['0x00a0']"}},[n("el-radio",{attrs:{label:1}},[t._v("全程录音")]),n("el-radio",{attrs:{label:2}},[t._v("翻盘录音")])],1)],1)],1)]),n("div",{staticClass:"tw-setting-form tab",staticStyle:{"padding-top":"0px"}},[n("el-form",[n("span",{staticClass:"setting-label"},[t._v("智能终端录音文件最大时长（0-255）分钟")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x00a1")}},model:{value:t.setting["0x00a1"],callback:function(e){t.$set(t.setting,"0x00a1",e)},expression:"setting['0x00a1']"}})],1),n("span",{staticClass:"setting-label"},[t._v("液晶（LCD）心跳间隔（单位S 1-60）")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x00a2")}},model:{value:t.setting["0x00a2"],callback:function(e){t.$set(t.setting,"0x00a2",e)},expression:"setting['0x00a2']"}})],1),n("span",{staticClass:"setting-label"},[t._v("LED心跳时间间隔（单位S 1-60）")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x00a3")}},model:{value:t.setting["0x00a3"],callback:function(e){t.$set(t.setting,"0x00a3",e)},expression:"setting['0x00a3']"}})],1),n("span",{staticClass:"setting-label"},[t._v("ACC OFF后进入休眠模式时间间隔， 单位S")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x00af")}},model:{value:t.setting["0x00af"],callback:function(e){t.$set(t.setting,"0x00af",e)},expression:"setting['0x00af']"}})],1)],1)],1)])])},f=[],x={name:"BasicInfo",data:function(){return{radio:1}},methods:{editEvent:function(t){this.$emit("test",t)}},props:["setting"]},m=x,v=(n("5de7"),Object(c["a"])(m,d,f,!1,null,"ee0b0e0a",null)),p=v.exports,b=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"tw-setting-template"},[n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("主服务器设置")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",{attrs:{"label-width":"180px","label-position":"left"}},[n("el-form-item",{attrs:{label:"无线通信拨号访问点"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0010")}},model:{value:t.setting["0x0010"],callback:function(e){t.$set(t.setting,"0x0010",e)},expression:"setting['0x0010']"}})],1),n("el-form-item",{attrs:{label:"无线通信拨号用户名"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0011")}},model:{value:t.setting["0x0011"],callback:function(e){t.$set(t.setting,"0x0011",e)},expression:"setting['0x0011']"}})],1),n("el-form-item",{attrs:{label:"无线通信拨号密码"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0012")}},model:{value:t.setting["0x0012"],callback:function(e){t.$set(t.setting,"0x0012",e)},expression:"setting['0x0012']"}})],1),n("el-form-item",{attrs:{label:"地址、IP或域名"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0013")}},model:{value:t.setting["0x0013"],callback:function(e){t.$set(t.setting,"0x0013",e)},expression:"setting['0x0013']"}})],1)],1)],1)]),n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("备份服务器设置")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",{attrs:{"label-width":"180px"}},[n("el-form-item",{attrs:{label:"无线通信拨号访问点"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0014")}},model:{value:t.setting["0x0014"],callback:function(e){t.$set(t.setting,"0x0014",e)},expression:"setting['0x0014']"}})],1),n("el-form-item",{attrs:{label:"无线通信拨号用户名"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0015")}},model:{value:t.setting["0x0015"],callback:function(e){t.$set(t.setting,"0x0015",e)},expression:"setting['0x0015']"}})],1),n("el-form-item",{attrs:{label:"无线通信拨号密码"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0016")}},model:{value:t.setting["0x0016"],callback:function(e){t.$set(t.setting,"0x0016",e)},expression:"setting['0x0016']"}})],1),n("el-form-item",{attrs:{label:"地址、IP或域名"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0017")}},model:{value:t.setting["0x0017"],callback:function(e){t.$set(t.setting,"0x0017",e)},expression:"setting['0x0017']"}})],1)],1)],1)]),n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("TCP设置")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",{attrs:{"label-width":"180px"}},[n("el-form-item",{attrs:{label:"消息应答超时时间（S）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0002")}},model:{value:t.setting["0x0002"],callback:function(e){t.$set(t.setting,"0x0002",e)},expression:"setting['0x0002']"}})],1),n("el-form-item",{attrs:{label:"消息重传次数"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0003")}},model:{value:t.setting["0x0003"],callback:function(e){t.$set(t.setting,"0x0003",e)},expression:"setting['0x0003']"}})],1),n("el-form-item",{attrs:{label:"主服务器TCP端口"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0018")}},model:{value:t.setting["0x0018"],callback:function(e){t.$set(t.setting,"0x0018",e)},expression:"setting['0x0018']"}})],1),n("el-form-item",{attrs:{label:"备份服务器TCP端口"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0019")}},model:{value:t.setting["0x0019"],callback:function(e){t.$set(t.setting,"0x0019",e)},expression:"setting['0x0019']"}})],1)],1)],1)]),n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("一卡通或支付平台服务器")]),n("div",{staticClass:"tw-setting-form tab"},[n("el-form",{attrs:{"label-width":"180px"}},[n("el-form-item",{attrs:{label:"消息应答超时时间（S）"}},[n("el-input")],1),n("el-form-item",{attrs:{label:"消息重传次数"}},[n("el-input")],1),n("el-form-item",{attrs:{label:"主服务器TCP端口"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x001b")}},model:{value:t.setting["0x001b"],callback:function(e){t.$set(t.setting,"0x001b",e)},expression:"setting['0x001b']"}})],1),n("el-form-item",{attrs:{label:"备份服务器TCP端口"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x001d")}},model:{value:t.setting["0x001d"],callback:function(e){t.$set(t.setting,"0x001d",e)},expression:"setting['0x001d']"}})],1)],1)],1)])])},h=[],C={name:"ConnectionParameter",methods:{editEvent:function(t){this.$emit("test",t)}},props:["setting"]},_=C,k=(n("4882"),Object(c["a"])(_,b,h,!1,null,"58eadfd1",null)),$=k.exports,w=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"tw-setting-template"},[n("div",{staticClass:"tw-setting tab",staticStyle:{height:"100%"}},[n("div",{staticClass:"el-divider__text is-left"},[t._v("通信电话设置")]),n("div",{staticClass:"tw-setting-form long"},[n("el-form",{attrs:{"label-width":"200px","label-position":"left"}},[n("el-form-item",{attrs:{label:"监控平台电话号码"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0040")}},model:{value:t.setting["0x0040"],callback:function(e){t.$set(t.setting,"0x0040",e)},expression:"setting['0x0040']"}})],1),n("el-form-item",{attrs:{label:"复位电话号码"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0041")}},model:{value:t.setting["0x0041"],callback:function(e){t.$set(t.setting,"0x0041",e)},expression:"setting['0x0041']"}})],1),n("el-form-item",{attrs:{label:"恢复出厂设置电话"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0012")}},model:{value:t.setting["0x0012"],callback:function(e){t.$set(t.setting,"0x0012",e)},expression:"setting['0x0012']"}})],1),n("el-form-item",{attrs:{label:"监控平台SMS电话"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0013")}},model:{value:t.setting["0x0013"],callback:function(e){t.$set(t.setting,"0x0013",e)},expression:"setting['0x0013']"}})],1),n("el-form-item",{attrs:{label:"接收终端SMS文本报警号码"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0014")}},model:{value:t.setting["0x0014"],callback:function(e){t.$set(t.setting,"0x0014",e)},expression:"setting['0x0014']"}})],1)],1),n("el-form",[n("span",{staticClass:"setting-label"},[t._v("每次最长通话时间（秒）")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0046")}},model:{value:t.setting["0x0046"],callback:function(e){t.$set(t.setting,"0x0046",e)},expression:"setting['0x0046']"}})],1),n("span",{staticClass:"setting-label"},[t._v("当月最长通话时间（秒）")]),n("el-form-item",[n("el-input",{on:{change:function(e){return t.editEvent("0x0047")}},model:{value:t.setting["0x0047"],callback:function(e){t.$set(t.setting,"0x0047",e)},expression:"setting['0x0047']"}})],1)],1),n("el-form",{attrs:{"label-width":"150px","label-position":"left"}},[n("el-form-item",{attrs:{label:"电话短号长度"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0048")}},model:{value:t.setting["0x0048"],callback:function(e){t.$set(t.setting,"0x0048",e)},expression:"setting['0x0048']"}})],1),n("el-form-item",{attrs:{label:"监听电话号码"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x0049")}},model:{value:t.setting["0x0049"],callback:function(e){t.$set(t.setting,"0x0049",e)},expression:"setting['0x0049']"}})],1),n("el-form-item",{attrs:{label:"语音播报音量（0-9）"}},[n("el-input",{on:{change:function(e){return t.editEvent("0x004b")}},model:{value:t.setting["0x004b"],callback:function(e){t.$set(t.setting,"0x004b",e)},expression:"setting['0x004b']"}})],1)],1)],1)]),n("div",{staticClass:"tw-setting tab"},[n("div",{staticClass:"el-divider__text is-left"},[t._v("终端电话接听策略")]),n("div",{staticClass:"tw-setting-form radio"},[n("el-form",[n("el-form-item",[n("el-radio-group",{on:{change:function(e){return t.editEvent("0x0045")}},model:{value:t.setting["0x0045"],callback:function(e){t.$set(t.setting,"0x0045",e)},expression:"setting['0x0045']"}},[n("el-radio",{attrs:{label:0}},[t._v("自动接听")]),n("el-radio",{attrs:{label:1}},[t._v("ACC ON时自动接听；OFF时手动接听")])],1)],1)],1)],1)])])},E=[],S={name:"CallParameters",data:function(){return{radio:3}},methods:{editEvent:function(t){this.$emit("test",t)}},props:["setting"]},y=S,T=(n("fe01"),Object(c["a"])(y,w,E,!1,null,"276adc8d",null)),I=T.exports,R=n("0e08"),N=n("bc3a"),O=n.n(N),P=n("17fb"),L=n.n(P),M={name:"ParameterSetting",data:function(){return{activeName:"first",queryPlaceholder:"请输入车牌号码、终端编号、SIM卡号",placeholderIndex:0,vehicle:"",checkCode:0,goon:0,isgoon:0,time:0,istime:0,data:[],datas:[],loading:!1,params:{},sendCheckCode:0}},mounted:function(){var t=this;this.$nextTick(function(){console.log(t.$refs.setting);var e=parseInt(t.$refs.setting.offsetHeight);t.$refs.tabContent.style.height="calc(100% - ".concat(e,"px - 20px)"),console.info(t.$refs.tabContent.style.height)})},methods:{handleClick:function(t,e){console.log(t,e)},querySearchAsync:function(t,e){if(t.length<3)return e([]);this.getVehiList(t).then(function(t){console.log(t.data),e(t.data&&L.a.map(t.data,function(t){return{value:t.INFO,label:t.VEHI_NO}})||[])})},handleSelect:function(t){var e=this,n=new URLSearchParams;n.append("type","1"),n.append("vhic",t.label),O.a.post("map/getOneVhic",n,{baseURL:R["a"]}).then(function(t){e.vehicleList=t.data})},getVehiList:function(t){var e=new URLSearchParams;return e.append("vhic",t),O.a.post("map/getVhic",e,{baseURL:R["a"]})},getTerminalInfo:function(){console.log(2),this.goon=1,this.checkCode=(new Date).getTime();var t=new URLSearchParams;t.append("type",this.checkCode),t.append("isu",this.vehicle.split("(")[1].split(",")[0]),O.a.post("map/SendMessageMQ",t,{baseURL:R["a"]}).then(function(t){console.log(t.data)})},getTerminal:function(){var t=this;this.vehicle.length<10?this.$message("终端选择错误！"):(this.loading=!0,console.log(1),1!=this.goon&&setTimeout(this.getTerminalInfo,1e3),O.a.get("map/ReceiveMessageMQ",{baseURL:R["a"]}).then(function(e){if(console.log(e.data.serialNumber),t.loading=!1,console.log(e.data.serialNumber,t.checkCode),e.data.serialNumber==t.checkCode){if(t.goon=0,"设备不在线！！！"==e.data.error3)return void t.$message("该设备不在线！");var n=e.data.paramBody;t.data=t.datas=n,console.log(n)}else{1!=t.goon&&(t.time=(new Date).getTime());var i=(new Date).getTime();if(console.log(i,t.time),console.log(1,(i-t.time)/1e3),i-t.time>=15e3)return t.goon=0,void t.$message("查询失败，请重试！");t.goon=1,t.getTerminal()}}).catch(function(t){this.goon,console.log(t),this.loading=!1,this.$message("查询失败，请重试！")}))},getEditInfo:function(t){this.params[t]=this.data[t];var e=JSON.stringify(this.params);console.log(e)},setTerminal:function(){this.sendCheckCode=(new Date).getTime();var t=new URLSearchParams;t.append("type",this.sendCheckCode),t.append("isu",this.vehicle.split("(")[1].split(",")[0]),t.append("param",JSON.stringify(this.params)),O.a.post("map/setTerminal",t,{baseURL:R["a"]}).then(function(t){})},getSetupState:function(){var t=this;JSON.stringify(this.params).length<5?this.$message("请修改需要设置的参数后，再进行重点设置！"):(this.loading=!0,1!=this.isgoon&&setTimeout(this.setTerminal,1e3),O.a.get("map/ReceiveMessageMQ",{baseURL:R["a"]}).then(function(e){if(t.loading=!1,console.log(e.data.serialNumber,t.sendCheckCode,e.data),e.data.serialNumber==t.sendCheckCode)return t.isgoon=0,"0"==e.data.Result?void t.$message("设置成功！"):"2"==e.data.Result?void t.$message("参数不正确！"):void t.$message("设置失败，请重新进行终端设置！");1!=t.isgoon&&(t.istime=(new Date).getTime());var n=(new Date).getTime();if(console.log(n,t.istime),console.log(1,(n-t.istime)/1e3),n-t.istime>=15e3)return t.isgoon=0,void t.$message("设置超时，请重新进行终端设置！！");t.isgoon=1,t.getSetupState()}))}},components:{"reporting-parameters":g,"basic-info":p,"connection-parameter":$,"call-parameter":I}},D=M,A=(n("c39c"),Object(c["a"])(D,i,s,!1,null,"497acec6",null));e["default"]=A.exports},"8f15":function(t,e,n){},aae3:function(t,e,n){var i=n("d3f4"),s=n("2d95"),a=n("2b4c")("match");t.exports=function(t){var e;return i(t)&&(void 0!==(e=t[a])?!!e:"RegExp"==s(t))}},ab9f:function(t,e,n){},af9b:function(t,e,n){"use strict";var i=n("ab9f"),s=n.n(i);s.a},c39c:function(t,e,n){"use strict";var i=n("0241"),s=n.n(i);s.a},fe01:function(t,e,n){"use strict";var i=n("8f15"),s=n.n(i);s.a}}]);
//# sourceMappingURL=chunk-24bdaa23.bd719732.js.map