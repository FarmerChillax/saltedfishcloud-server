(function(e,n){typeof exports=="object"&&typeof module!="undefined"?n(require("vue"),require("sfc-common")):typeof define=="function"&&define.amd?define(["vue","sfc-common"],n):(e=typeof globalThis!="undefined"?globalThis:e||self,n(e.Vue,e.SfcCommon))})(this,function(e,n){"use strict";var ee=Object.defineProperty,te=Object.defineProperties;var ne=Object.getOwnPropertyDescriptors;var h=Object.getOwnPropertySymbols;var oe=Object.prototype.hasOwnProperty,ae=Object.prototype.propertyIsEnumerable;var b=(e,n,i)=>n in e?ee(e,n,{enumerable:!0,configurable:!0,writable:!0,value:i}):e[n]=i,V=(e,n)=>{for(var i in n||(n={}))oe.call(n,i)&&b(e,i,n[i]);if(h)for(var i of h(n))ae.call(n,i)&&b(e,i,n[i]);return e},g=(e,n)=>te(e,ne(n));var i;(t=>{(a=>{function l(s,o){return{url:"/nwt/findWolByUid",params:{uid:s,checkOnline:!!o}}}a.findByUid=l;function u(s){return{url:"/nwt/saveWolDevice",method:"post",headers:{"Content-Type":"application/json"},data:s}}a.saveWolDevice=u;function r(s){return{url:"/nwt/wakeWolDevice",params:{id:s}}}a.wakeWolDevice=r})(t.Wol||(t.Wol={}));function c(){return{url:"/nwt/getAllInterface"}}t.getAllInterface=c})(i||(i={}));var le="",x=(t,c)=>{const a=t.__vccOpts||t;for(const[l,u]of c)a[l]=u;return a};const L={class:"tip"},A=e.createTextVNode(" IP\u5730\u5740: "),$={style:{"padding-left":"24px"}},M=e.createTextVNode(" \u5E7F\u64AD\u5730\u5740: "),O={style:{"padding-left":"24px"}},W=e.defineComponent({name:"NetworkInterfaceInfoCard"}),U=e.defineComponent(g(V({},W),{props:{networkInterface:{type:Object,default:()=>({})}},setup(t){return(c,a)=>{const l=e.resolveComponent("VCardContent"),u=e.resolveComponent("VCard");return e.openBlock(),e.createBlock(u,{class:"network-interface-info-card",title:t.networkInterface.name},{default:e.withCtx(()=>[e.createVNode(l,null,{default:e.withCtx(()=>[e.createElementVNode("div",L,[e.createElementVNode("div",null,"\u63A5\u53E3: "+e.toDisplayString(t.networkInterface.name),1),e.createElementVNode("div",null,"\u540D\u79F0: "+e.toDisplayString(t.networkInterface.displayName),1),e.createElementVNode("div",null,"MAC: "+e.toDisplayString(t.networkInterface.mac),1),e.createElementVNode("div",null,[A,e.createElementVNode("ul",$,[(e.openBlock(!0),e.createElementBlock(e.Fragment,null,e.renderList(t.networkInterface.ipList,r=>(e.openBlock(),e.createElementBlock("li",{key:r},e.toDisplayString(r),1))),128))])]),e.createElementVNode("div",null,[M,e.createElementVNode("ul",O,[(e.openBlock(!0),e.createElementBlock(e.Fragment,null,e.renderList(t.networkInterface.broadcastAddressList,r=>(e.openBlock(),e.createElementBlock("li",{key:r},e.toDisplayString(r),1))),128))])]),e.createElementVNode("div",null,"MTU: "+e.toDisplayString(t.networkInterface.mtu),1)])]),_:1})]),_:1},8,["title"])}}}));var I=x(U,[["__scopeId","data-v-55ec9a7c"]]);const S={style:{display:"flex","flex-wrap":"wrap"}},R=e.defineComponent({name:"NetworkInterfaceList",components:{NetworkInterfaceInfoCard:I}}),T=e.defineComponent(g(V({},R),{setup(t){const c=window.SfcUtils,a=new n.LoadingManager,l=a.getLoadingRef(),u=e.ref([]);return n.MethodInterceptor.createAsyncActionProxy({async loadData(){u.value=(await c.request(i.getAllInterface())).data.data}},!1,a).loadData(),(s,o)=>{const _=e.resolveComponent("LoadingMask");return e.openBlock(),e.createElementBlock("div",null,[e.createVNode(_,{loading:e.unref(l)},null,8,["loading"]),e.createElementVNode("div",S,[(e.openBlock(!0),e.createElementBlock(e.Fragment,null,e.renderList(e.unref(u),m=>(e.openBlock(),e.createBlock(I,{key:m.name,style:{animation:"up-in .2s"},"network-interface":m},null,8,["network-interface"]))),128))])])}}}));var re="";const j={class:"d-flex justify-space-between"},P={class:"tip",style:{width:"calc(100% - 12px)","margin-left":"12px"}},q={class:"d-flex align-center"},z=e.defineComponent({name:"WolDeviceCard"}),G=e.defineComponent(g(V({},z),{props:{wolDevice:{type:Object,default:()=>({})},loading:{type:Boolean,default:!1}},emits:["wake","edit"],setup(t,{emit:c}){return(a,l)=>{const u=e.resolveComponent("LoadingMask"),r=e.resolveComponent("CommonIcon"),s=e.resolveComponent("VBtn"),o=e.resolveComponent("VCardContent"),_=e.resolveComponent("VCard");return e.openBlock(),e.createBlock(_,{class:"wol-device-card"},{default:e.withCtx(()=>[e.createVNode(u,{loading:t.loading,type:"circular"},null,8,["loading"]),e.createVNode(o,null,{default:e.withCtx(()=>[e.createElementVNode("div",j,[e.createElementVNode("div",null,[e.createVNode(r,{icon:"mdi-laptop",color:t.wolDevice.isOnline?"primary":"",style:{"font-size":"32px"}},null,8,["color"])]),e.createElementVNode("div",P,[e.createElementVNode("div",q,[e.createTextVNode(" \u8BBE\u5907\u540D: "+e.toDisplayString(t.wolDevice.name)+" ",1),e.createVNode(r,{class:"link d-flex align-center",style:{"font-size":"10px","margin-left":"6px"},icon:"mdi-pencil",onClick:l[0]||(l[0]=m=>c("edit",t.wolDevice))})]),e.createElementVNode("div",null,"MAC: "+e.toDisplayString(t.wolDevice.mac),1),e.createElementVNode("div",null,"IP: "+e.toDisplayString(t.wolDevice.ip),1)]),e.createElementVNode("div",null,[t.wolDevice.isOnline?e.createCommentVNode("",!0):(e.openBlock(),e.createBlock(s,{key:0,style:{"margin-top":"6px"},icon:"mdi-power",color:"primary",onClick:l[1]||(l[1]=m=>c("wake",t.wolDevice))}))])])]),_:1})]),_:1})}}}));var F=x(G,[["__scopeId","data-v-8c0c3d60"]]);const H=e.defineComponent({name:"WolDeviceForm",components:{FormRow:n.Components.FormRow,FormCol:n.Components.FormCol,TextInput:n.Components.TextInput}}),J=e.defineComponent(g(V({},H),{props:{initObject:{type:Object,default:void 0},readOnly:{type:Boolean,default:!1}},emits:["submit"],setup(t,{expose:c,emit:a}){const l=t,u=window.SfcUtils,r=e.ref(),s=n.defineForm({actions:{async submit(){return await u.request(i.Wol.saveWolDevice(o))}},formData:{name:"",port:9,ip:"",mac:"",sendIp:"255.255.255.255"},formRef:r,validators:{name:[n.Validators.notNull(),n.Validators.maxLen("\u4E0D\u80FD\u5927\u4E8E255\u4E2A\u5B57\u7B26",255)],mac:[n.Validators.isMatchRegex("^([abcdefABCDEF\\d]{2}[:\\-]?){6}$","\u4E0D\u662F\u6709\u6548\u7684mac\u5730\u5740")],ip:[n.Validators.notNull(),n.Validators.isMatchRegex("^((2((5[0-5])|([0-4]\\d)))|([0-1]?\\d{1,2}))(\\.((2((5[0-5])|([0-4]\\d)))|([0-1]?\\d{1,2}))){3}$","\u4E0D\u662F\u6709\u6548\u7684ipv4\u5730\u5740")],port:[n.Validators.minNum(1),n.Validators.maxNum(65535)]},throwError:!0}),{formData:o,actions:_,validators:m,loadingRef:E,loadingManager:C}=s,B=C.getLoadingRef();return l.initObject&&Object.assign(o,l.initObject),c(s),(f,d)=>{const D=e.resolveComponent("LoadingMask"),w=e.resolveComponent("TextInput"),k=e.resolveComponent("FormCol"),y=e.resolveComponent("FormRow"),N=e.resolveComponent("base-form");return e.openBlock(),e.createBlock(N,{ref_key:"formRef",ref:r,"model-value":e.unref(o),"submit-action":e.unref(_).submit},{default:e.withCtx(()=>[e.createVNode(D,{loading:e.unref(B)},null,8,["loading"]),e.createVNode(y,null,{default:e.withCtx(()=>[e.createVNode(k,{label:"\u8BBE\u5907\u540D\u79F0","top-label":""},{default:e.withCtx(()=>[e.createVNode(w,{modelValue:e.unref(o).name,"onUpdate:modelValue":d[0]||(d[0]=p=>e.unref(o).name=p),rules:e.unref(m).name,placeholder:"\u8D77\u4E2A\u540D\u5B57\u5427~",readonly:t.readOnly},null,8,["modelValue","rules","readonly"])]),_:1}),e.createVNode(k,{label:"MAC\u5730\u5740","top-label":""},{default:e.withCtx(()=>[e.createVNode(w,{modelValue:e.unref(o).mac,"onUpdate:modelValue":d[1]||(d[1]=p=>e.unref(o).mac=p),rules:e.unref(m).mac,placeholder:"\u5173\u952E\u53C2\u6570",readonly:t.readOnly},null,8,["modelValue","rules","readonly"])]),_:1}),e.createVNode(k,{label:"\u7AEF\u53E3(udp)","top-label":""},{default:e.withCtx(()=>[e.createVNode(w,{modelValue:e.unref(o).port,"onUpdate:modelValue":d[2]||(d[2]=p=>e.unref(o).port=p),rules:e.unref(m).port,placeholder:"\u9ED8\u8BA4UDP\u7AEF\u53E3-9",readonly:t.readOnly},null,8,["modelValue","rules","readonly"])]),_:1}),e.createVNode(k,{label:"IP\u5730\u5740(ipv4)","top-label":""},{default:e.withCtx(()=>[e.createVNode(w,{modelValue:e.unref(o).ip,"onUpdate:modelValue":d[3]||(d[3]=p=>e.unref(o).ip=p),rules:e.unref(m).ip,placeholder:"\u7528\u6765\u68C0\u6D4B\u662F\u5426\u5728\u7EBF",readonly:t.readOnly},null,8,["modelValue","rules","readonly"])]),_:1}),e.createVNode(k,{label:"\u53D1\u9001/\u5E7F\u64AD\u5730\u5740","top-label":"",class:"mw-50"},{default:e.withCtx(()=>[e.createVNode(w,{modelValue:e.unref(o).sendIp,"onUpdate:modelValue":d[4]||(d[4]=p=>e.unref(o).sendIp=p),rules:e.unref(m).ip,placeholder:"255.255.255.255",readonly:t.readOnly},null,8,["modelValue","rules","readonly"])]),_:1})]),_:1})]),_:1},8,["model-value","submit-action"])}}})),K={class:"WolDeviceList"},Q=e.createTextVNode(" \u6DFB\u52A0\u8BBE\u5907 "),X=e.createTextVNode(" \u5237\u65B0 "),Y={style:{"margin-top":"12px"}},Z=e.defineComponent({name:"WolDeviceList",components:{WolDeviceCard:F,LoadingMask:n.Components.LoadingMask}}),v=e.defineComponent(g(V({},Z),{props:{uid:{type:[String,Number],default:0}},setup(t){const c=t,a=window.SfcUtils,l=new n.LoadingManager,u=l.getLoadingRef(),r=e.ref([]),s=e.reactive({}),o=async()=>{r.value=(await a.request(i.Wol.findByUid(c.uid,!0))).data.data||[],r.value.forEach(f=>{f.isOnline&&(s[f.id]=!1)})},_=async f=>{await a.confirm(`\u786E\u5B9A\u8981\u5524\u9192\u8BBE\u5907${f.name}\u5417\uFF1F`,"\u5524\u9192\u786E\u8BA4").then(()=>m.wake(f))},m=n.MethodInterceptor.createAsyncActionProxy({async loadData(){await o()},async wake(f){await a.request(i.Wol.wakeWolDevice(f.id)),s[f.id]=!0}},!1,l),E=f=>{const d=a.openComponentDialog(J,{title:f?"\u7F16\u8F91\u8BBE\u5907":"\u6DFB\u52A0\u8BBE\u5907",props:{initObject:f||{uid:c.uid}},extraDialogOptions:{persistent:!0},async onConfirm(){return(await d.getInstAsForm().submit()).success?(a.snackbar(f?"\u4FDD\u5B58\u6210\u529F":"\u6DFB\u52A0\u6210\u529F"),m.loadData(),!0):!1}})};m.loadData();let C=!1,B=setInterval(async()=>{if(!C)try{C=!0,await o()}finally{C=!1}},5e3);return e.onUnmounted(()=>{B&&clearInterval(B)}),(f,d)=>{const D=e.resolveComponent("VBtn"),w=e.resolveComponent("CommonIcon"),k=e.resolveComponent("LoadingMask");return e.openBlock(),e.createElementBlock("div",K,[e.createVNode(D,{color:"primary",onClick:d[0]||(d[0]=y=>E())},{default:e.withCtx(()=>[Q]),_:1}),e.createVNode(D,{style:{"margin-left":"12px"},onClick:e.unref(m).loadData},{default:e.withCtx(()=>[e.createVNode(w,{icon:"mdi-refresh"}),X]),_:1},8,["onClick"]),e.createVNode(k,{loading:e.unref(u)},null,8,["loading"]),e.createElementVNode("div",Y,[(e.openBlock(!0),e.createElementBlock(e.Fragment,null,e.renderList(e.unref(r),y=>(e.openBlock(),e.createBlock(F,{key:y.id,style:{animation:"up-in .2s"},"wol-device":y,loading:e.unref(s)[y.id],onWake:_,onEdit:d[1]||(d[1]=N=>E(N))},null,8,["wol-device","loading"]))),128))])])}}}));window.bootContext.addProcessor({taskName:"\u6CE8\u518C\u7F51\u7EDC\u5DE5\u5177",execute(t,c){t.component("WolDeviceList",v),t.component("NetworkInterfaceList",T)}})});
