(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-b4d9ffb6"],{"28a5":function(e,t,l){"use strict";var i=l("aae3"),a=l("cb7c"),n=l("ebd6"),r=l("0390"),c=l("9def"),s=l("5f1b"),o=l("520a"),u=l("79e5"),d=Math.min,h=[].push,m="split",p="length",f="lastIndex",v=4294967295,g=!u(function(){RegExp(v,"y")});l("214f")("split",2,function(e,t,l,u){var b;return b="c"=="abbc"[m](/(b)*/)[1]||4!="test"[m](/(?:)/,-1)[p]||2!="ab"[m](/(?:ab)*/)[p]||4!="."[m](/(.?)(.?)/)[p]||"."[m](/()()/)[p]>1||""[m](/.?/)[p]?function(e,t){var a=String(this);if(void 0===e&&0===t)return[];if(!i(e))return l.call(a,e,t);var n,r,c,s=[],u=(e.ignoreCase?"i":"")+(e.multiline?"m":"")+(e.unicode?"u":"")+(e.sticky?"y":""),d=0,m=void 0===t?v:t>>>0,g=new RegExp(e.source,u+"g");while(n=o.call(g,a)){if(r=g[f],r>d&&(s.push(a.slice(d,n.index)),n[p]>1&&n.index<a[p]&&h.apply(s,n.slice(1)),c=n[0][p],d=r,s[p]>=m))break;g[f]===n.index&&g[f]++}return d===a[p]?!c&&g.test("")||s.push(""):s.push(a.slice(d)),s[p]>m?s.slice(0,m):s}:"0"[m](void 0,0)[p]?function(e,t){return void 0===e&&0===t?[]:l.call(this,e,t)}:l,[function(l,i){var a=e(this),n=void 0==l?void 0:l[t];return void 0!==n?n.call(l,a,i):b.call(String(a),l,i)},function(e,t){var i=u(b,e,this,t,b!==l);if(i.done)return i.value;var o=a(e),h=String(this),m=n(o,RegExp),p=o.unicode,f=(o.ignoreCase?"i":"")+(o.multiline?"m":"")+(o.unicode?"u":"")+(g?"y":"g"),y=new m(g?o:"^(?:"+o.source+")",f),Y=void 0===t?v:t>>>0;if(0===Y)return[];if(0===h.length)return null===s(y,h)?[h]:[];var x=0,T=0,M=[];while(T<h.length){y.lastIndex=g?T:0;var k,S=s(y,g?h:h.slice(T));if(null===S||(k=d(c(y.lastIndex+(g?0:T)),h.length))===x)T=r(h,T,p);else{if(M.push(h.slice(x,T)),M.length===Y)return M;for(var _=1;_<=S.length-1;_++)if(M.push(S[_]),M.length===Y)return M;T=x=k}}return M.push(h.slice(x)),M}]})},aae3:function(e,t,l){var i=l("d3f4"),a=l("2d95"),n=l("2b4c")("match");e.exports=function(e){var t;return i(e)&&(void 0!==(t=e[n])?!!t:"RegExp"==a(e))}},e94c:function(e,t,l){"use strict";l.r(t);var i=function(){var e=this,t=e.$createElement,l=e._self._c||t;return l("t-query-panel",{attrs:{model:e.query,footer:"300"},scopedSlots:e._u([{key:"querybar",fn:function(){return[l("el-form-item",[l("el-date-picker",{attrs:{type:"datetime",placeholder:"开始时间"},model:{value:e.query.startTime,callback:function(t){e.$set(e.query,"startTime",t)},expression:"query.startTime"}})],1),l("el-form-item",[l("el-date-picker",{attrs:{type:"datetime",placeholder:"结束时间"},model:{value:e.query.endTime,callback:function(t){e.$set(e.query,"endTime",t)},expression:"query.endTime"}})],1),l("el-form-item",[l("el-select",{staticClass:"tw-select",staticStyle:{"margin-left":"20px"},attrs:{multiple:"",filterable:"",remote:"","reserve-keyword":"","remote-method":e.handleVehicleQuerySearch,"collapse-tags":"",placeholder:"车牌号码","no-data-text":e.select.noDataText,loading:e.select.loading},nativeOn:{click:function(t){e.select.selectedVehicle=[]}},model:{value:e.query.vehicle,callback:function(t){e.$set(e.query,"vehicle",t)},expression:"query.vehicle"}},e._l(e.select.selectedVehicle,function(e){return l("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}),1)],1),l("el-form-item",["111111"===e.loginType?l("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择车辆组"},model:{value:e.query.vehicleGroupId,callback:function(t){e.$set(e.query,"vehicleGroupId",t)},expression:"query.vehicleGroupId"}},e._l(e.select.vehicleGroup,function(e){return l("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}),1):e._e()],1),l("el-form-item",[l("el-button",{attrs:{type:"primary"},on:{click:e.handleQueryClick}},[e._v("查询")]),l("el-button",{attrs:{type:"primary"},on:{click:e.handleExportClick}},[e._v("导出")]),e.table.data.length>0?l("span",{staticStyle:{color:"blueviolet","margin-left":"15px"},on:{mouseover:function(t){e.table.dataDisabled=1},mouseleave:function(t){e.table.dataDisabled=0}}},[e._v("鼠标悬浮查看车辆里程")]):e._e()],1)]},proxy:!0}])},[1===e.table.dataDisabled?e._l(e.table.vehicle_mileage,function(t){return l("el-button",{attrs:{type:"info",size:"mini",disabled:""}},[e._v(e._s(t+"公里"))])}):e._e(),l("t-table-page",{attrs:{data:e.table.data,loading:e.table.display,"page-size":20}},[l("el-table-column",{attrs:{type:"index",label:"序号",align:"center",width:"50",resizable:!1}}),l("el-table-column",{attrs:{prop:"VEHICLE_NUM",label:"车牌号码",align:"center",width:"120"}}),l("el-table-column",{attrs:{prop:"PX",label:"经度",align:"center",width:"120"}}),l("el-table-column",{attrs:{prop:"PY",label:"纬度",align:"center",width:"120"}}),l("el-table-column",{attrs:{prop:"SPEED",label:"速度",align:"center",width:"120"}}),l("el-table-column",{attrs:{prop:"SPEED_TIME",label:"时间",align:"center",width:"240"}}),l("el-table-column",{attrs:{prop:"ADDRESS",label:"所在位置",align:"center","min-width":"240",resizable:!1}})],1)],2)},a=[],n=(l("28a5"),l("6b54"),l("c1df")),r=l.n(n),c=l("0e08"),s=l("bc3a"),o=l.n(s),u=l("17fb"),d=l.n(u),h={name:"CyclingMileageStatistics",data:function(){return{loginType:"",query:{startTime:"",endTime:"",vehicle:[],vehicleGroupId:""},inputVehicle:{loading:!1,option:[]},table:{display:!1,data:[],vehicle_mileage:[],dataDisabled:0},select:{vehicle:[],selectedVehicle:[],noDataText:"请输入三个字符以上",loading:!1,vehicleGroup:[]}}},mounted:function(){this.query.startTime=r()(r()().subtract(2,"hours")),this.query.endTime=r()(),this.loginType=this.$cookies.get("loginType"),this.getVehicleGroup(),this.getVehicle()},methods:{iconClassName:c["m"],handleVehicleQuerySearch:function(e){var t=this;this.select.selectedVehicle=[],this.select.noDataText="请输入三个字符以上",e.length>=3&&(this.select.loading=!0,setTimeout(function(){t.select.loading=!1,t.select.selectedVehicle=d.a.filter(t.select.vehicle,function(t){return t.value.indexOf(e)>-1})},200),this.select.noDataText="无数据")},getVehicleGroup:function(){var e=this;if("1"!==this.loginType)return null;o.a.get("common/vehicleGroup",{baseURL:c["a"],params:{userId:this.$cookies.get("userId")}}).then(function(t){e.select.vehicleGroup=d.a.map(t.data,function(e){return{value:e.CLZS,label:e.CLZ_NAME}})}).catch(function(e){})},getVehicle:function(){var e=this;o.a.get("common/vehicle",{baseURL:c["a"],params:{}}).then(function(t){e.select.vehicle=d.a.map(t.data,function(e){return{label:e.VEHICLE_NO,value:e.VEHICLE_NO}})}).catch(function(e){})},findCyclingMileageStatistics:function(){var e=this,t=this.query,l=t.startTime,i=t.endTime,a=t.vehicle,n=t.vehicleGroupId;return l&&i?(l&&r()(l).format("YYYY-MM"))!==(i&&r()(i).format("YYYY-MM"))?this.$message.error("无法跨月查询！"):0===a.length?this.$message.error("请选择车牌！"):(this.table.display=!0,this.table.data=[],void o.a.get("dataStatistics/findCyclingMileageStatistics",{baseURL:c["a"],params:{startTime:l&&r()(l).format("YYYY-MM-DD HH:mm:ss"),endTime:i&&r()(i).format("YYYY-MM-DD HH:mm:ss"),vehicle:a.toString(),vehicleGroupId:n}}).then(function(t){e.table.data=t.data.datas||[],e.table.vehicle_mileage=t.data.count.split(";"),e.table.display=!1}).catch(function(e){console.log(e)})):this.$message.error("请选择时间！")},handleQueryClick:function(){this.findCyclingMileageStatistics()},handleExportClick:function(){var e=this.query,t=e.startTime,l=e.endTime,i=e.vehicle,a=e.vehicleGroupId;return 0===i.length?this.$message.error("请选择车牌！"):t&&l?(t&&r()(t).format("YYYY-MM"))!==(l&&r()(l).format("YYYY-MM"))?this.$message.error("无法跨月查询！"):void this.$confirm("是否需要导出?","提示",{confirmButtonText:"是",cancelButtonText:"否",cancelButtonClass:"el-button--danger",closeOnClickModal:!1,type:"info",center:!0}).then(function(){window.open("".concat(c["a"],"dataStatistics/findCyclingMileageStatisticsExcel?startTime=").concat(t&&r()(t).format("YYYY-MM-DD HH:mm:ss"),"&endTime=").concat(l&&r()(l).format("YYYY-MM-DD HH:mm:ss"),"&vehicle=").concat(i.toString(),"&vehicleGroupId=").concat(a))}).catch(function(){}):this.$message.error("请选择时间！")}}},m=h,p=l("2877"),f=Object(p["a"])(m,i,a,!1,null,null,null);t["default"]=f.exports}}]);
//# sourceMappingURL=chunk-b4d9ffb6.f3cbefda.js.map