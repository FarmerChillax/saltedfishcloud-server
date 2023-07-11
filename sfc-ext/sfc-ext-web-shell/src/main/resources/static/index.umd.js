(function(e,a){typeof exports=="object"&&typeof module!="undefined"?a(require("vue"),require("sfc-common")):typeof define=="function"&&define.amd?define(["vue","sfc-common"],a):(e=typeof globalThis!="undefined"?globalThis:e||self,a(e.Vue,e.SfcCommon))})(this,function(e,a){"use strict";var Ee=Object.defineProperty,ke=Object.defineProperties;var Se=Object.getOwnPropertyDescriptors;var O=Object.getOwnPropertySymbols;var Ne=Object.prototype.hasOwnProperty,De=Object.prototype.propertyIsEnumerable;var j=(e,a,x)=>a in e?Ee(e,a,{enumerable:!0,configurable:!0,writable:!0,value:x}):e[a]=x,k=(e,a)=>{for(var x in a||(a={}))Ne.call(a,x)&&j(e,x,a[x]);if(O)for(var x of O(a))De.call(a,x)&&j(e,x,a[x]);return e},S=(e,a)=>ke(e,Se(a));var x="",D=(t,r)=>{const C=t.__vccOpts||t;for(const[p,V]of r)C[p]=V;return C};const z={class:"simple-text-console"},H=e.defineComponent({name:"WsSimpleTextConsole"}),P=e.defineComponent(S(k({},H),{props:{output:{type:String,default:""},showInput:{type:Boolean,default:!1}},emits:["input"],setup(t,{emit:r}){const C=t,p=e.ref(""),V=e.ref(),f=async()=>{await e.nextTick();const c=V.value.$el;c.scrollTop=c.scrollHeight};return e.watch(()=>C.output,f),e.onMounted(f),(c,l)=>{const i=e.resolveComponent("SimpleTextarea"),n=e.resolveComponent("TextInput");return e.openBlock(),e.createElementBlock("div",z,[e.createVNode(i,{ref_key:"outputRef",ref:V,"model-value":t.output,style:e.normalizeStyle({height:t.showInput?"calc(100% - 64px)":"100%"}),readonly:""},null,8,["model-value","style"]),t.showInput?(e.openBlock(),e.createBlock(n,{key:0,modelValue:p.value,"onUpdate:modelValue":l[0]||(l[0]=s=>p.value=s),label:"\u547D\u4EE4",onKeypress:l[1]||(l[1]=e.withKeys(s=>{r("input",p.value),p.value=""},["enter"]))},null,8,["modelValue"])):e.createCommentVNode("",!0)])}}}));var $=D(P,[["__scopeId","data-v-242bd2d9"]]);const g="/webShell",G=window.SfcUtils;var y;(t=>{function r(o){return a.StringUtils.appendPath(G.axios.defaults.baseURL||"",`/webshell/${o}`)}t.getShellWebSocketUrl=r;function C(o){return`${g}/${o}`}t.getShellWsUrl=C;function p(o,_){return a.useJsonBody({url:`${g}/executeCommand`,method:"post",params:{nodeId:_},data:o})}t.sendSimpleCommand=p;function V(o,_){return a.useJsonBody({url:`${g}/createSession`,params:{nodeId:_},data:o,method:"post"})}t.createSession=V;function f(o){return{url:`${g}/restart`,params:{sessionId:o}}}t.restart=f;function c(o){return{url:`${g}/remove`,params:{sessionId:o}}}t.remove=c;function l(o){return{url:`${g}/kill`,params:{sessionId:o}}}t.kill=l;function i(o,_){return{url:`${g}/rename`,params:{name:_,sessionId:o}}}t.rename=i;function n(){return{url:`${g}/listSession`}}t.listSession=n;function s(o){return{url:`${g}/getLog`,params:{sessionId:o}}}t.getLog=s})(y||(y={}));var be="",Ae="",$e="";const b=t=>(e.pushScopeId("data-v-a44f36ca"),t=t(),e.popScopeId(),t),J={class:"d-flex align-center"},Q=b(()=>e.createElementVNode("br",null,null,-1)),X=b(()=>e.createElementVNode("p",{class:"text-header text-primary"}," \u9644\u52A0\u53C2\u6570 ",-1)),Y={class:"other-param"},Z=b(()=>e.createElementVNode("p",{class:"text-header text-primary",style:{"margin-bottom":"18px"}}," \u73AF\u5883\u53D8\u91CF ",-1)),v=b(()=>e.createElementVNode("thead",null,[e.createElementVNode("tr",null,[e.createElementVNode("th",null," Name "),e.createElementVNode("th",{style:{"min-width":"120px"}}," Value "),e.createElementVNode("th",{style:{width:"81px"}})])],-1)),ee={style:{"max-width":"92px"}},te=["onUpdate:modelValue"],oe=["onUpdate:modelValue"],ne=e.defineComponent({name:"WsSimpleExecView",components:{FormSelect:a.Components.FormSelect,ClusterSelector:a.Components.ClusterSelector,WsSimpleTextConsole:$}}),le=e.defineComponent(S(k({},ne),{setup(t){const r=[{title:"UTF-8",value:"utf8"},{title:"GBK",value:"gbk"}],C=window.SfcUtils,p=new a.LoadingManager,V=p.getLoadingRef(),f=e.ref(""),c=e.ref(""),l=e.ref(!1),i=e.ref([{name:"",value:""}]),n=e.reactive({charset:"utf8",cmd:"",env:{},timeout:10}),s=[];let o=0;const _=a.MethodInterceptor.createAsyncActionProxy({async send(){if(!V.value){if(n.cmd.length==0)return Promise.reject("\u547D\u4EE4\u4E0D\u80FD\u4E3A\u7A7A");try{s.unshift(n.cmd),s.length>=500&&s.pop(),f.value+=`SHELL> ${n.cmd}
`,n.env={},i.value.filter(d=>d.name.length>0).forEach(d=>{n.env[d.name]=d.value});const h=await C.request(y.sendSimpleCommand(n,l.value?c.value:null));f.value+=`${h.data.data.output}
\u6267\u884C\u8017\u65F6: ${h.data.data.time}ms \u7A0B\u5E8F\u9000\u51FA\u7801: ${h.data.data.exitCode}
`,n.cmd=""}catch(h){return f.value+=`
\u6267\u884C\u51FA\u9519: `+h+`
`,Promise.reject(h)}}}},!1,p),w=h=>{const d=h.code=="ArrowUp",m=h.code=="ArrowDown";if(!d&&!m){o=0;return}if(!!s.length){if(m&&o--,o>=s.length||o<0){m&&(n.cmd="",o=0);return}n.cmd=s[o],d&&o++}};return(h,d)=>{const m=e.resolveComponent("LoadingMask"),F=e.resolveComponent("TextInput"),B=e.resolveComponent("CommonIcon"),E=e.resolveComponent("VCheckbox"),A=e.resolveComponent("ClusterSelector"),U=e.resolveComponent("FormSelect"),I=e.resolveComponent("VTextField"),T=e.resolveComponent("VTable"),L=e.resolveDirective("ripple");return e.openBlock(),e.createElementBlock("div",null,[e.createVNode(m,{loading:e.unref(V)},null,8,["loading"]),e.createVNode($,{output:f.value,style:{height:"50vh"}},null,8,["output"]),e.createElementVNode("div",J,[e.createVNode(F,{modelValue:e.unref(n).cmd,"onUpdate:modelValue":d[0]||(d[0]=u=>e.unref(n).cmd=u),label:"\u8F93\u5165\u547D\u4EE4",placeholder:"\u8F93\u5165\u547D\u4EE4\uFF0C\u56DE\u8F66\u76F4\u63A5\u6267\u884C",style:{"margin-right":"6px"},"hide-details":"",onKeydown:w,onEnter:e.unref(_).send},null,8,["modelValue","onEnter"]),e.withDirectives(e.createVNode(B,{color:"primary",style:{padding:"12px","border-radius":"50%",cursor:"pointer"},icon:"mdi-send",flat:"",onClick:e.unref(_).send},null,8,["onClick"]),[[L]])]),Q,X,e.createElementVNode("div",Y,[e.createVNode(E,{modelValue:l.value,"onUpdate:modelValue":d[1]||(d[1]=u=>l.value=u),color:"primary",label:"\u6307\u5B9A\u8282\u70B9","hide-details":""},null,8,["modelValue"]),l.value?(e.openBlock(),e.createBlock(A,{key:0,modelValue:c.value,"onUpdate:modelValue":d[2]||(d[2]=u=>c.value=u),style:{"max-width":"240px"}},null,8,["modelValue"])):e.createCommentVNode("",!0),e.createVNode(U,{modelValue:e.unref(n).charset,"onUpdate:modelValue":d[3]||(d[3]=u=>e.unref(n).charset=u),placeholder:"\u7A0B\u5E8F\u8F93\u51FA\u7F16\u7801",style:{"max-width":"92px"},"hide-details":"",items:r},null,8,["modelValue"]),e.createVNode(I,{modelValue:e.unref(n).timeout,"onUpdate:modelValue":d[4]||(d[4]=u=>e.unref(n).timeout=u),class:"float-label-t6",style:{"margin-bottom":"6px"},color:"primary",variant:"underlined","hide-details":"",label:"\u6267\u884C\u8D85\u65F6(s)"},null,8,["modelValue"])]),Z,e.createElementVNode("div",null,[e.createVNode(T,{style:{width:"100%","max-width":"480px"},class:"elevation-1"},{default:e.withCtx(()=>[v,e.createElementVNode("tbody",null,[(e.openBlock(!0),e.createElementBlock(e.Fragment,null,e.renderList(e.unref(i),(u,K)=>(e.openBlock(),e.createElementBlock("tr",{key:K},[e.createElementVNode("td",ee,[e.withDirectives(e.createElementVNode("input",{"onUpdate:modelValue":N=>u.name=N,class:"plain-input"},null,8,te),[[e.vModelText,u.name]])]),e.createElementVNode("td",null,[e.withDirectives(e.createElementVNode("input",{"onUpdate:modelValue":N=>u.value=N,class:"plain-input"},null,8,oe),[[e.vModelText,u.value]])]),e.createElementVNode("td",null,[e.createVNode(B,{style:{cursor:"pointer"},icon:"mdi-plus",onClick:d[5]||(d[5]=N=>e.unref(i).push({name:"",value:""}))}),e.unref(i).length!=1?(e.openBlock(),e.createBlock(B,{key:0,style:{cursor:"pointer"},icon:"mdi-delete",onClick:N=>e.unref(i).splice(K,1)},null,8,["onClick"])):e.createCommentVNode("",!0)])]))),128))])]),_:1})])])}}}));var M=D(le,[["__scopeId","data-v-a44f36ca"]]),Ue="";const R=t=>(e.pushScopeId("data-v-5b7d4b38"),t=t(),e.popScopeId(),t),se={class:"d-flex align-center text-primary",style:{width:"100%"}},ae={style:{width:"40px"}},re={class:"d-flex align-center"},ie={class:"d-flex"},ce={class:"tip"},de={class:"d-flex"},ue=R(()=>e.createElementVNode("div",{style:{width:"81px","text-align":"right"}}," \u521B\u5EFA\u4E8E\uFF1A ",-1)),me={class:"d-flex"},pe=R(()=>e.createElementVNode("div",{style:{width:"81px","text-align":"right"}}," \u6267\u884C\u4E3B\u673A\uFF1A ",-1)),_e=e.defineComponent({name:"WsCard"}),fe=e.defineComponent(S(k({},_e),{props:{session:{type:Object,default:void 0}},emits:["rename","openConsole","restart","remove","kill"],setup(t,{emit:r}){const C=t,p=window.SfcUtils,V=async()=>{var n,s;const i=await p.prompt({title:"\u4F1A\u8BDD\u91CD\u547D\u540D",label:"\u4F1A\u8BDD\u540D\u79F0",defaultValue:(n=C.session)==null?void 0:n.name});i!=((s=C.session)==null?void 0:s.name)&&r("rename",i)},f=async()=>{await p.confirm("\u662F\u5426\u786E\u8BA4\u91CD\u542F\u8BE5Shell\u4F1A\u8BDD\uFF1F","\u91CD\u542F\u4F1A\u8BDD"),r("restart")},c=async()=>{var i;await p.confirm("\u662F\u5426\u786E\u8BA4\u79FB\u9664\u8BE5Shell\u4F1A\u8BDD\uFF1F"+((i=C.session)!=null&&i.running?"\u8FDB\u7A0B\u4ECD\u5728\u8FD0\u884C\u4E2D\uFF0C\u79FB\u9664\u540E\u5C06\u5F3A\u5236\u7ED3\u675F\u8FDB\u7A0B":""),"\u79FB\u9664\u4F1A\u8BDD"),r("remove")},l=async()=>{await p.confirm("\u662F\u5426\u786E\u8BA4\u7EC8\u6B62Shell\u4F1A\u8BDD\u8FDB\u7A0B\uFF1F","\u7EC8\u6B62\u8FDB\u7A0B"),r("kill")};return(i,n)=>{const s=e.resolveComponent("CommonIcon"),o=e.resolveComponent("VBtn"),_=e.resolveComponent("VCardTitle"),w=e.resolveComponent("VCardHeader"),h=e.resolveComponent("VCardContent"),d=e.resolveComponent("VCard");return e.openBlock(),e.createBlock(d,{class:"ws-card"},{default:e.withCtx(()=>[e.createVNode(w,null,{default:e.withCtx(()=>[e.createVNode(_,{class:"d-flex align-center justify-between",style:{width:"100%"}},{default:e.withCtx(()=>{var m,F,B;return[e.createElementVNode("div",{class:e.normalizeClass(["status-icon elevation-2",{active:(m=t.session)==null?void 0:m.running}])},null,2),e.createElementVNode("div",se,[e.createElementVNode("div",ae,[e.createVNode(s,{icon:"mdi-console"})]),e.createElementVNode("div",null,e.toDisplayString((F=t.session)==null?void 0:F.name),1),e.createVNode(s,{class:"edit-icon",icon:"mdi-pencil",onClick:V})]),e.createElementVNode("div",re,[(B=t.session)!=null&&B.running?(e.openBlock(),e.createBlock(o,{key:1,style:{"margin-right":"6px"},icon:"mdi-stop",title:"\u7EC8\u6B62\u8FDB\u7A0B",size:"small",flat:"",onClick:l})):(e.openBlock(),e.createBlock(o,{key:0,style:{"margin-right":"6px"},icon:"mdi-restart",title:"\u91CD\u542F\u4F1A\u8BDD",size:"small",flat:"",onClick:f})),e.createVNode(o,{icon:"mdi-open-in-new",style:{"margin-right":"6px"},size:"small",title:"\u6253\u5F00\u63A7\u5236\u53F0",flat:"",onClick:n[0]||(n[0]=E=>r("openConsole"))}),e.createVNode(o,{icon:"mdi-close",size:"small",title:"\u5220\u9664\u4F1A\u8BDD",flat:"",onClick:c})])]}),_:1})]),_:1}),e.createVNode(h,null,{default:e.withCtx(()=>{var m,F;return[e.createElementVNode("div",ie,[e.createElementVNode("div",ce,[e.createElementVNode("div",de,[ue,e.createElementVNode("div",null,e.toDisplayString(e.unref(a.StringFormatter).toDate(Number((m=t.session)==null?void 0:m.createAt))),1)]),e.createElementVNode("div",me,[pe,e.createElementVNode("div",null,e.toDisplayString((F=t.session)==null?void 0:F.host),1)])])])]}),_:1})]),_:1})}}}));var W=D(fe,[["__scopeId","data-v-5b7d4b38"]]);const Ce=e.defineComponent({name:"WsCreateSessionForm"}),Ve=e.defineComponent(S(k({},Ce),{emits:["submit"],setup(t,{expose:r,emit:C}){const p=window.SfcUtils,V=e.ref(),f=[{title:"/bin/bash",value:"/bin/bash"},{title:"powershell",value:"powershell"}],c=e.ref(!1),l=e.ref(""),i=[{title:"UTF-8",value:"UTF-8"},{title:"GBK",value:"GBK"}],n=a.defineForm({actions:{async submit(){h.beginLoading();try{const d=await p.request(y.createSession(s,c.value?l.value:void 0));return C("submit",s),d}finally{h.closeLoading()}}},formData:{name:"\u65B0\u5EFA\u4F1A\u8BDD",shell:"/bin/bash",charset:"UTF-8",env:{}},formRef:V,validators:{},throwError:!0}),{formData:s,actions:o,validators:_,loadingRef:w,loadingManager:h}=n;return r(n),(d,m)=>{const F=e.resolveComponent("LoadingMask"),B=e.resolveComponent("TextInput"),E=e.resolveComponent("form-col"),A=e.resolveComponent("FormSelect"),U=e.resolveComponent("VCheckboxBtn"),I=e.resolveComponent("ClusterSelector"),T=e.resolveComponent("form-row"),L=e.resolveComponent("base-form");return e.openBlock(),e.createBlock(L,{ref_key:"formRef",ref:V,"row-height":"81px","model-value":e.unref(s),"submit-action":e.unref(o).submit},{default:e.withCtx(()=>[e.createVNode(F,{loading:e.unref(w)},null,8,["loading"]),e.createVNode(T,{style:{"margin-top":"12px"}},{default:e.withCtx(()=>[e.createVNode(E,{"top-label":"",label:"\u4F1A\u8BDD\u540D\u79F0"},{default:e.withCtx(()=>[e.createVNode(B,{modelValue:e.unref(s).name,"onUpdate:modelValue":m[0]||(m[0]=u=>e.unref(s).name=u)},null,8,["modelValue"])]),_:1}),e.createVNode(E,{"top-label":"",label:"shell"},{default:e.withCtx(()=>[e.createVNode(A,{modelValue:e.unref(s).shell,"onUpdate:modelValue":m[1]||(m[1]=u=>e.unref(s).shell=u),items:e.unref(f)},null,8,["modelValue","items"])]),_:1}),e.createVNode(E,{"top-label":"",label:"\u7A0B\u5E8F\u8F93\u51FA\u7F16\u7801"},{default:e.withCtx(()=>[e.createVNode(A,{modelValue:e.unref(s).charset,"onUpdate:modelValue":m[2]||(m[2]=u=>e.unref(s).charset=u),items:e.unref(i)},null,8,["modelValue","items"])]),_:1}),e.createVNode(E,{"top-label":"",label:"\u6267\u884C\u8282\u70B9"},{default:e.withCtx(()=>[e.createVNode(U,{modelValue:c.value,"onUpdate:modelValue":m[3]||(m[3]=u=>c.value=u),color:"primary"},null,8,["modelValue"]),c.value?(e.openBlock(),e.createBlock(I,{key:0,modelValue:l.value,"onUpdate:modelValue":m[4]||(m[4]=u=>l.value=u),placeholder:""},null,8,["modelValue"])):(e.openBlock(),e.createElementBlock("span",{key:1,class:"tip",style:{"font-size":"16px",cursor:"pointer"},onClick:m[5]||(m[5]=u=>c.value=!0)},"\u6307\u5B9A\u8282\u70B9"))]),_:1})]),_:1})]),_:1},8,["model-value","submit-action"])}}}));var Ie="";const he={class:"d-flex align-center justify-center"},xe=e.createTextVNode(" \u521B\u5EFA\u4F1A\u8BDD "),ye={class:"d-flex align-center justify-center"},we=e.createTextVNode(" \u5237\u65B0 "),ge={class:"session-list"},Fe=e.defineComponent({name:"WsInteractiveExecView",components:{WsCard:W}}),Be=e.defineComponent(S(k({},Fe),{setup(t){const r=window.SfcUtils,C=new a.LoadingManager,p=C.getLoadingRef(),V=e.ref([]),f={async listSession(){V.value=((await r.request(y.listSession())).data.data||[]).sort((l,i)=>Number(i.createAt)-Number(l.createAt))},async rename(l,i){await r.request(y.rename(l.id,i)),await this.listSession()},async openConsole(l){const i=(await r.request(y.getLog(l.id))).data.data,n=await a.WebSocketService.connect({url:y.getShellWebSocketUrl(l.id)}),s=e.reactive({output:i,showInput:!0,style:{height:"calc(100vh - 180px)"},onInput(o){n.send(o+`
`)}});n.onmessage=o=>{s.output+=o.data},r.openComponentDialog($,{title:l.name,props:s,fullscreen:!0,onCancel(){return n.close(),f.listSession(),!0},showConfirm:!1})},createSession(){const l=r.openComponentDialog(Ve,{title:"\u521B\u5EFA\u4EA4\u4E92\u5F0Fshell\u4F1A\u8BDD",async onConfirm(){const i=await l.getInstAsForm().submit();return i.success?(c.listSession().then(async()=>{var n;await r.sleep(200),f.openConsole((n=i.data)==null?void 0:n.data.data)}),!0):(console.error(i.err),!1)}})},async restart(l){await r.request(y.restart(l.id)),await r.sleep(200),await this.listSession()},async remove(l){await r.request(y.remove(l.id)),await r.sleep(200),await this.listSession()},async kill(l){await r.request(y.kill(l.id)),await r.sleep(200),await this.listSession()}},c=a.MethodInterceptor.createAsyncActionProxy(f,!1,C);return e.onMounted(c.listSession),(l,i)=>{const n=e.resolveComponent("LoadingMask"),s=e.resolveComponent("CommonIcon"),o=e.resolveComponent("VBtn");return e.openBlock(),e.createElementBlock("div",null,[e.createVNode(n,{loading:e.unref(p)},null,8,["loading"]),e.createVNode(o,{color:"primary",onClick:f.createSession},{default:e.withCtx(()=>[e.createElementVNode("div",he,[e.createVNode(s,{icon:"mdi-plus"}),xe])]),_:1},8,["onClick"]),e.createVNode(o,{style:{"margin-left":"6px"},onClick:e.unref(c).listSession},{default:e.withCtx(()=>[e.createElementVNode("div",ye,[e.createVNode(s,{icon:"mdi-refresh"}),we])]),_:1},8,["onClick"]),e.createElementVNode("div",ge,[(e.openBlock(!0),e.createElementBlock(e.Fragment,null,e.renderList(e.unref(V),_=>(e.openBlock(),e.createBlock(W,{key:_.id,session:_,style:{animation:"up-in .2s"},onRename:w=>e.unref(c).rename(_,w),onOpenConsole:w=>e.unref(c).openConsole(_),onRestart:w=>e.unref(c).restart(_),onRemove:w=>e.unref(c).remove(_),onKill:w=>e.unref(c).kill(_)},null,8,["session","onRename","onOpenConsole","onRestart","onRemove","onKill"]))),128))])])}}}));var q=D(Be,[["__scopeId","data-v-3c071e61"]]);window.bootContext.addProcessor({execute(t,r){t.component(M.name,M),t.component(q.name,q)},taskName:"\u6CE8\u518CWebShell\u7EC4\u4EF6"})});
