import{g as H,r as g,a as o,b as i,c as p,w as t,d as a,u as n,i as c,n as h,k as b,j as C}from"./index-fc9417a1.js";const W={__name:"ordermerchant",setup(J){const{proxy:m}=H();let u=g({}),r=g(!1),_=g(""),O=g({}),y=g({type:20}),R=g({merchantid:[{required:!0,message:"请选择商户",trigger:"blur"}],accnumber:[{required:!0,message:"请选择卡或地址",trigger:"blur"}],merchantexchange:[{required:!0,message:"请输入汇率",trigger:"blur"}],amount:[{required:!0,message:"请输入金额",trigger:"blur"}]});function f(){m.$refs.baseTableRef.refresh()}function T(){u.value={},U(),_.value="add",r.value=!0}async function U(){let s=await m.$api.payconfig.get();u.value=s.body,m.submitOk(s.message)}function q(s){O.value=s,_.value="confirm",r.value=!0}async function B(){_.value="add",r.value=!1;let s=await m.$api.merchantaccountorder.pass(O.value.id);m.submitOk(s.message),f()}async function F(s){let l=await m.$api.merchantaccountorder.turndown(s.id);f(),m.submitOk(l.message)}function L(){m.$refs.dataFormRef.validate(async s=>{if(s){u.value.id=null;let l=await m.$api.merchantaccountorder[u.value.id?"update":"add"](u.value);m.submitOk(l.message),f(),r.value=!1}})}return(s,l)=>{const N=o("base-input"),v=o("el-col"),P=o("base-select-no"),k=o("el-button"),S=o("el-row"),j=o("base-header"),d=o("el-table-column"),w=o("el-tag"),$=o("base-confirm-btn"),D=o("base-table-p"),A=o("base-content"),E=o("base-cascader-no"),V=o("el-form-item"),x=o("base-input-no"),I=o("el-form"),M=o("base-dialog"),Q=o("authenticator-dialog"),z=o("base-wrapper");return i(),p(z,null,{default:t(()=>[a(j,null,{default:t(()=>[a(S,null,{default:t(()=>[a(v,{span:4.5},{default:t(()=>[a(N,{modelValue:n(y).nikname,"onUpdate:modelValue":l[0]||(l[0]=e=>n(y).nikname=e),label:"充值金额",onClear:f},null,8,["modelValue"])]),_:1}),a(v,{span:4.5},{default:t(()=>[a(P,{modelValue:n(y).status,"onUpdate:modelValue":l[1]||(l[1]=e=>n(y).status=e),label:"状态","tag-type":"success",onClear:f,style:{"margin-right":"10px"},clearable:"","option-props":{label:"name",value:"value"},dataList:s.statusList},null,8,["modelValue","dataList"])]),_:1}),a(v,{span:4.5},{default:t(()=>[a(k,{type:"primary",onClick:f},{default:t(()=>[c("查询")]),_:1}),a(k,{type:"success",onClick:T},{default:t(()=>[c("创建充值")]),_:1})]),_:1})]),_:1})]),_:1}),a(A,null,{default:t(()=>[a(D,{ref:"baseTableRef",api:"merchantaccountorder.page",params:n(y),indexCode:!0},{default:t(()=>[a(d,{label:"商户名称",prop:"username"}),a(d,{label:"商户编码",prop:"merchantcode"}),a(d,{label:"标签",prop:"nkname"}),a(d,{label:"充值U金额",prop:"amount"}),a(d,{label:"汇率",prop:"exchange"},{default:t(e=>[e.row.exchange?(i(),p(w,{key:0,type:"success"},{default:t(()=>[c(h(e.row.exchange)+"%",1)]),_:2},1024)):b("",!0)]),_:1}),a(d,{label:"结算汇率",prop:"merchantexchange"},{default:t(e=>[e.row.merchantexchange?(i(),p(w,{key:0,type:"success"},{default:t(()=>[c(h(e.row.exchange)+"%",1)]),_:2},1024)):b("",!0)]),_:1}),a(d,{label:"到账RMB金额",prop:"amountreceived"},{default:t(e=>[e.row.amountreceived?(i(),p(w,{key:0,type:"success"},{default:t(()=>[c(h(e.row.amountreceived),1)]),_:2},1024)):b("",!0)]),_:1}),a(d,{label:"状态"},{default:t(e=>[e.row.statusname?(i(),p(w,{key:0},{default:t(()=>[c(h(e.row.statusname),1)]),_:2},1024)):b("",!0)]),_:1}),a(d,{label:"创建时间",prop:"create_time"}),a(d,{label:"备注",prop:"remark"}),a(d,{label:"操作",width:"100"},{default:t(e=>[e.row.status==10?(i(),p($,{key:0,type:"success",onOk:G=>q(e.row),label:"确认"},null,8,["onOk"])):b("",!0),e.row.status==10?(i(),p($,{key:1,type:"warning",onOk:G=>F(e.row),label:"拒绝"},null,8,["onOk"])):b("",!0)]),_:1})]),_:1},8,["params"])]),_:1}),n(_)==="add"||n(_)==="update"?(i(),p(M,{key:0,modelValue:n(r),"onUpdate:modelValue":l[7]||(l[7]=e=>C(r)?r.value=e:r=e),title:s.dialogTitleObj[n(_)],width:"23%"},{footer:t(()=>[a(k,{onClick:l[6]||(l[6]=e=>C(r)?r.value=!1:r=!1)},{default:t(()=>[c("取 消")]),_:1}),a(k,{type:"primary",onClick:L},{default:t(()=>[c("确 定")]),_:1})]),default:t(()=>[a(I,{ref:"dataFormRef",rules:n(R),model:n(u),"label-width":"100px"},{default:t(()=>[a(V,{label:"选择商户:",prop:"merchantid"},{default:t(()=>[a(E,{modelValue:n(u).merchantid,"onUpdate:modelValue":l[2]||(l[2]=e=>n(u).merchantid=e),style:{"margin-right":"10px"},clearable:"",label:"",props:{value:"id",label:"name",checkStrictly:!0,emitPath:!1},api:"merchant.list"},null,8,["modelValue"])]),_:1}),a(V,{label:"充值金额:",prop:"amount"},{default:t(()=>[a(x,{modelValue:n(u).amount,"onUpdate:modelValue":l[3]||(l[3]=e=>n(u).amount=e)},null,8,["modelValue"])]),_:1}),a(V,{label:"汇率:"},{default:t(()=>[a(x,{modelValue:n(u).exchange,"onUpdate:modelValue":l[4]||(l[4]=e=>n(u).exchange=e),disabled:n(_)=="add"},null,8,["modelValue","disabled"]),a(k,{style:{"margin-left":"10px"},type:"warning",onClick:U},{default:t(()=>[c("刷新")]),_:1})]),_:1}),a(V,{label:"备注:",prop:"remark"},{default:t(()=>[a(x,{modelValue:n(u).remark,"onUpdate:modelValue":l[5]||(l[5]=e=>n(u).remark=e),type:"textarea",style:{width:"215px"}},null,8,["modelValue"])]),_:1})]),_:1},8,["rules","model"])]),_:1},8,["modelValue","title"])):b("",!0),n(_)=="confirm"?(i(),p(Q,{key:1,modelValue:n(r),"onUpdate:modelValue":l[8]||(l[8]=e=>C(r)?r.value=e:r=e),"append-to-body":"",title:"安全验证",width:"15%",onConfirms:B},null,8,["modelValue"])):b("",!0)]),_:1})}}};export{W as default};
