(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-ec4a6ef0"],{"052f":function(e,t,a){},3372:function(e,t,a){"use strict";var l=a("052f"),i=a.n(l);i.a},"45fd":function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"template-wrapper"},[a("div",{ref:"queryBar"},[a("el-form",{staticClass:"tw-query-bar",attrs:{inline:!0,model:e.query,size:"small"}},[a("el-form-item",[a("el-date-picker",{attrs:{type:"datetime",placeholder:"开始日期"},model:{value:e.query.stime,callback:function(t){e.$set(e.query,"stime",t)},expression:"query.stime"}})],1),a("el-form-item",[a("el-date-picker",{attrs:{type:"datetime",placeholder:"结束日期"},model:{value:e.query.etime,callback:function(t){e.$set(e.query,"etime",t)},expression:"query.etime"}})],1),a("el-form-item",[a("el-select",{attrs:{filterable:"",placeholder:"工作组名称"},model:{value:e.query.wgName,callback:function(t){e.$set(e.query,"wgName",t)},expression:"query.wgName"}},[a("i",{staticClass:"tw-icon iconfont icon-group",attrs:{slot:"prefix"},slot:"prefix"}),a("el-option",{attrs:{label:"全部",value:""}}),e._l(e.workGroupList,function(e){return a("el-option",{key:e.id,attrs:{label:e.username,value:e.username}})})],2)],1),a("el-form-item",[a("el-select",{attrs:{placeholder:"设备类型"},model:{value:e.query.dType,callback:function(t){e.$set(e.query,"dType",t)},expression:"query.dType"}},[a("i",{staticClass:"tw-icon iconfont icon-type",attrs:{slot:"prefix"},slot:"prefix"}),a("el-option",{attrs:{label:"全部",value:""}}),a("el-option",{attrs:{label:"工作井",value:"工作井"}}),a("el-option",{attrs:{label:"杆塔",value:"杆塔"}}),a("el-option",{attrs:{label:"配电站",value:"配电站"}}),a("el-option",{attrs:{label:"其他设备",value:"其他设备"}})],1)],1),a("el-form-item",[a("el-button",{attrs:{type:"primary"},on:{click:e.handleQueryClick}},[e._v("查询")])],1)],1)],1),a("div",{staticClass:"tw-panel-table",style:{height:e.height}},[a("el-table",{staticStyle:{"margin-bottom":"10px"},attrs:{data:e.tableList,border:"",size:"small",height:"calc(100% - 50px)",width:"100%"}},[a("el-table-column",{attrs:{type:"index",label:"序号",width:"60"}}),a("el-table-column",{attrs:{prop:"GZZ_name",label:"工作组名称",width:"200"}}),a("el-table-column",{attrs:{prop:"SBLX",label:"设备类型",width:"100"}}),a("el-table-column",{attrs:{prop:"sbmc",label:"设备名称",width:"200"}}),a("el-table-column",{attrs:{prop:"WorksTime",label:"检查时间",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",{domProps:{textContent:e._s(e.fomarttime(t.row.WorksTime))}})]}}])}),a("el-table-column",{attrs:{prop:"phone",label:"联系电话",width:"100"}}),a("el-table-column",{attrs:{prop:"company",label:"公司名称",width:"200"}}),a("el-table-column",{attrs:{prop:"Remarks",label:"备注"}})],1),a("el-pagination",{attrs:{background:"",layout:"prev, pager, next","page-size":50,total:e.num},on:{"current-change":e.handleQueryNext}})],1)])},i=[],r=(a("cadf"),a("551c"),a("097d"),a("17fb"),a("bc3a")),o=a.n(r),n=a("c1df"),s=a.n(n),u={name:"ComprehensiveStatistics",data:function(){return{query:{stime:"",etime:"",wgName:"",dType:"",page:1},workGroupList:[],tableList:[],height:0,num:50}},mounted:function(){var e=this;this.$nextTick(function(){e.query.stime=e.fomarttime(new Date((new Date).getTime()-864e5)),e.query.etime=e.fomarttime(new Date),e.getWorkGroupList(),e.getJSGD(),window.onresize=function(){e.getJSGD()}})},computed:{},methods:{fomarttime:function(e){return s()(e).format("YYYY-MM-DD HH:mm:ss")},handleQueryNext:function(e){console.log(e),this.query.page=e,this.handleQueryClick()},getWorkGroupList:function(){var e=this,t=this.baseURL;o.a.get("company/getgzz",{baseURL:t}).then(function(t){e.workGroupList=t.data})},handleQueryClick:function(){var e=this,t=this.baseURL,a=this.query,l=a.stime,i=a.etime,r=a.wgName,n=a.dType,s=a.page;console.log(this.query),o.a.get("company/getzhtj",{baseURL:t,params:{stime:l,etime:i,wgName:r,dType:n,page:s}}).then(function(t){e.tableList=t.data.data,e.num=t.data.count,console.log(t.data.data)})},getJSGD:function(){var e="calc(100% - ".concat(this.$refs.queryBar.offsetHeight,"px - 20px)");this.height=e}}},c=u,p=(a("3372"),a("2877")),m=Object(p["a"])(c,l,i,!1,null,"f0570022",null);m.options.__file="ComprehensiveStatistics.vue";t["default"]=m.exports}}]);
//# sourceMappingURL=chunk-ec4a6ef0.9042780d.js.map