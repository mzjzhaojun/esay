import{g as W,r as U,a as r,b as k,c as y,w as a,d as e,u as o,i as p,n as h,k as x,j as q}from"./index-fc37d5dd.js";const Z={__name:"agent",setup(X){const{proxy:b}=W();let w=U({}),m=U({}),u=U({}),d=U(!1),f=U(""),Q=U({name:[{required:!0,message:"请输入商户名称",trigger:"blur"}],code:[{required:!0,pattern:/^(\w){6,16}$/,message:"请设置6-16位字母、数字组合数字编码",trigger:"blur"}],exchange:[{required:!0,message:"请输入汇率",trigger:"blur"}],onecost:[{required:!0,message:"请输入单笔手续费",trigger:"blur"}],nkname:[{required:!0,message:"请输入商户标签",trigger:"blur"}],username:[{required:!0,message:"请输入设置商户登录账号",trigger:"blur"}],password:[{required:!0,message:"请输入设置商户密码",trigger:"blur"}],downpoint:[{required:!0,message:"请输入设置商户密码",trigger:"blur"}]});function v(){b.$refs.baseTableRef.refresh()}function S(){u.value={status:!0,downpoint:0},f.value="add",d.value=!0}function T(s){u.value=Object.assign({},s),f.value="update",d.value=!0}async function z(s){let t=await b.$api.agent.delete(s.id);v(),b.submitOk(t.message)}function A(){b.$refs.dataFormRef.validate(async s=>{if(s){let t=await b.$api.agent[u.value.id?"update":"add"](u.value);b.submitOk(t.message),v(),d.value=!1}})}function I(s){m.value={agentid:s.id},f.value="merchantrecord",d.value=!0}async function E(s){let t=await b.$api.merchant.removeagent(s.id);C(),b.submitOk(t.message)}function C(){b.$refs.basemerchantTableRef.refresh()}function G(){f.value="add",d.value=!1}return(s,t)=>{const $=r("base-input"),_=r("el-col"),R=r("base-select-no"),g=r("el-button"),D=r("el-row"),L=r("base-header"),n=r("el-table-column"),j=r("base-switch"),O=r("el-tag"),F=r("base-delete-btn"),N=r("base-table-p"),B=r("base-content"),V=r("base-input-no"),i=r("el-form-item"),H=r("base-radio-group"),J=r("el-input-number"),K=r("el-form"),M=r("base-dialog"),P=r("base-wrapper");return k(),y(P,null,{default:a(()=>[e(L,null,{default:a(()=>[e(D,null,{default:a(()=>[e(_,{span:4.5},{default:a(()=>[e($,{modelValue:o(w).name,"onUpdate:modelValue":t[0]||(t[0]=l=>o(w).name=l),label:"代理名称",onClear:v},null,8,["modelValue"])]),_:1}),e(_,{span:4.5},{default:a(()=>[e($,{modelValue:o(w).nkname,"onUpdate:modelValue":t[1]||(t[1]=l=>o(w).nkname=l),label:"代理标签",onClear:v},null,8,["modelValue"])]),_:1}),e(R,{modelValue:o(w).status,"onUpdate:modelValue":t[2]||(t[2]=l=>o(w).status=l),label:"状态","tag-type":"success",onClear:v,style:{"margin-right":"10px"},clearable:"","option-props":{label:"name",value:"value"},dataList:s.statusList},null,8,["modelValue","dataList"]),e(_,{span:4.5},{default:a(()=>[e(g,{type:"primary",onClick:v},{default:a(()=>[p("查询")]),_:1}),e(g,{type:"success",onClick:S},{default:a(()=>[p("添加")]),_:1})]),_:1})]),_:1})]),_:1}),e(B,null,{default:a(()=>[e(N,{ref:"baseTableRef",api:"agent.page",params:o(w),indexCode:!0},{default:a(()=>[e(n,{label:"代理名称",prop:"name"}),e(n,{label:"标签",prop:"nkname"}),e(n,{label:"余额",prop:"balance"}),e(n,{label:"状态"},{default:a(l=>[e(j,{modelValue:l.row.status,"onUpdate:modelValue":c=>l.row.status=c,api:"agent.update",params:{id:l.row.id,status:!l.row.status}},null,8,["modelValue","onUpdate:modelValue","params"])]),_:1}),e(n,{label:"交易费率%",prop:"exchange"},{default:a(l=>[l.row.exchange?(k(),y(O,{key:0,type:"success"},{default:a(()=>[p(h(l.row.exchange)+"%",1)]),_:2},1024)):x("",!0)]),_:1}),e(n,{label:"单笔手续费",prop:"onecost"}),e(n,{label:"提现汇率浮动",prop:"downpoint"}),e(n,{label:"下级商户数量",prop:"downmerchantcount"},{default:a(l=>[l.row.downmerchantcount?(k(),y(O,{key:0},{default:a(()=>[p(" 商户"+h(l.row.downmerchantcount)+"个",1)]),_:2},1024)):x("",!0)]),_:1}),e(n,{label:"创建时间",prop:"create_time"}),e(n,{label:"备注",prop:"remark"}),e(n,{label:"操作"},{default:a(l=>[e(g,{type:"primary",link:"",onClick:c=>T(l.row)},{default:a(()=>[p("编辑")]),_:2},1032,["onClick"]),e(g,{type:"success",link:"",onClick:c=>I(l.row)},{default:a(()=>[p("商户管理")]),_:2},1032,["onClick"]),e(g,{type:"warning",link:"",onClick:c=>T(l.row)},{default:a(()=>[p("资金明细")]),_:2},1032,["onClick"]),e(F,{onOk:c=>z(l.row)},null,8,["onOk"])]),_:1})]),_:1},8,["params"])]),_:1}),o(f)==="add"||o(f)==="update"?(k(),y(M,{key:0,modelValue:o(d),"onUpdate:modelValue":t[14]||(t[14]=l=>q(d)?d.value=l:d=l),title:s.dialogTitleObj[o(f)],width:"50%"},{footer:a(()=>[e(g,{onClick:t[13]||(t[13]=l=>q(d)?d.value=!1:d=!1)},{default:a(()=>[p("取 消")]),_:1}),e(g,{type:"primary",onClick:A},{default:a(()=>[p("确 定")]),_:1})]),default:a(()=>[e(K,{inline:!0,ref:"dataFormRef",rules:o(Q),model:o(u),"label-width":"110px"},{default:a(()=>[e(i,{label:"代理名称:",prop:"name"},{default:a(()=>[e(V,{modelValue:o(u).name,"onUpdate:modelValue":t[3]||(t[3]=l=>o(u).name=l)},null,8,["modelValue"])]),_:1}),e(i,{label:"登录账号:",prop:"username"},{default:a(()=>[e(V,{modelValue:o(u).username,"onUpdate:modelValue":t[4]||(t[4]=l=>o(u).username=l),disabled:o(f)!="add"},null,8,["modelValue","disabled"])]),_:1}),e(i,{label:"代理标签:",prop:"nkname"},{default:a(()=>[e(V,{modelValue:o(u).nkname,"onUpdate:modelValue":t[5]||(t[5]=l=>o(u).nkname=l)},null,8,["modelValue"])]),_:1}),e(i,{label:"登录密码:",prop:"password"},{default:a(()=>[e(V,{modelValue:o(u).password,"onUpdate:modelValue":t[6]||(t[6]=l=>o(u).password=l)},null,8,["modelValue"])]),_:1}),e(i,{label:"单笔手续费:",prop:"onecost"},{default:a(()=>[e(V,{modelValue:o(u).onecost,"onUpdate:modelValue":t[7]||(t[7]=l=>o(u).onecost=l),onkeyup:"value=value.replace(/[^\\d.]/g,'')"},null,8,["modelValue"])]),_:1}),e(i,{label:"状态:",prop:"status"},{default:a(()=>[e(H,{modelValue:o(u).status,"onUpdate:modelValue":t[8]||(t[8]=l=>o(u).status=l)},null,8,["modelValue"])]),_:1}),e(i,{label:"交易费率:",prop:"exchange"},{default:a(()=>[e(V,{modelValue:o(u).exchange,"onUpdate:modelValue":t[9]||(t[9]=l=>o(u).exchange=l),tip:"%",label:"百分比1-100整数",onkeyup:"value=value.replace(/[^\\d.]/g,'')"},null,8,["modelValue"])]),_:1}),e(i,{label:"提现汇率浮动:",prop:"downpoint"},{default:a(()=>[e(J,{modelValue:o(u).downpoint,"onUpdate:modelValue":t[10]||(t[10]=l=>o(u).downpoint=l),precision:2,min:-.3,max:.3,step:.01,"controls-position":"right",size:"default"},null,8,["modelValue"])]),_:1}),e(i,{label:"备注:"},{default:a(()=>[e(V,{modelValue:o(u).remark,"onUpdate:modelValue":t[11]||(t[11]=l=>o(u).remark=l),type:"textarea",style:{width:"300px"}},null,8,["modelValue"])]),_:1}),e(i,{label:"登录ip:"},{default:a(()=>[e(V,{modelValue:o(u).ipaddress,"onUpdate:modelValue":t[12]||(t[12]=l=>o(u).ipaddress=l),type:"textarea",style:{width:"300px"}},null,8,["modelValue"])]),_:1})]),_:1},8,["rules","model"])]),_:1},8,["modelValue","title"])):x("",!0),o(f)==="merchantrecord"?(k(),y(M,{key:1,modelValue:o(d),"onUpdate:modelValue":t[19]||(t[19]=l=>q(d)?d.value=l:d=l),title:s.商户管理,width:"70%","destroy-on-close":!0,onClose:G},{default:a(()=>[e(L,null,{default:a(()=>[e(D,null,{default:a(()=>[e(_,{span:4.5},{default:a(()=>[e($,{modelValue:o(m).name,"onUpdate:modelValue":t[15]||(t[15]=l=>o(m).name=l),label:"商户名称",onClear:C},null,8,["modelValue"])]),_:1}),e(_,{span:4.5},{default:a(()=>[e($,{modelValue:o(m).code,"onUpdate:modelValue":t[16]||(t[16]=l=>o(m).code=l),label:"商户编码",onClear:C},null,8,["modelValue"])]),_:1}),e(_,{span:4.5},{default:a(()=>[e($,{modelValue:o(m).nikname,"onUpdate:modelValue":t[17]||(t[17]=l=>o(m).nikname=l),label:"商户标签",onClear:C},null,8,["modelValue"])]),_:1}),e(_,{span:4.5},{default:a(()=>[e(R,{modelValue:o(m).status,"onUpdate:modelValue":t[18]||(t[18]=l=>o(m).status=l),label:"状态","tag-type":"success",onClear:C,style:{"margin-right":"10px"},clearable:"","option-props":{label:"name",value:"value"},dataList:s.statusList},null,8,["modelValue","dataList"])]),_:1}),e(_,{span:4.5},{default:a(()=>[e(g,{type:"primary",onClick:C},{default:a(()=>[p("查询")]),_:1})]),_:1})]),_:1})]),_:1}),e(B,null,{default:a(()=>[e(N,{ref:"basemerchantTableRef",api:"merchant.page",params:o(m)},{default:a(()=>[e(n,{label:"商户名称",prop:"name"}),e(n,{label:"商户编码",prop:"code"}),e(n,{label:"标签",prop:"nikname"}),e(n,{label:"备注",prop:"remark"}),e(n,{label:"余额",prop:"balance"}),e(n,{label:"状态"},{default:a(l=>[e(j,{modelValue:l.row.status,"onUpdate:modelValue":c=>l.row.status=c,api:"merchant.update",params:{id:l.row.id,status:!l.row.status}},null,8,["modelValue","onUpdate:modelValue","params"])]),_:1}),e(n,{label:"当日手续费",prop:"todaycost"}),e(n,{label:"今日量",prop:"todaycount"}),e(n,{label:"总量",prop:"count"}),e(n,{label:"汇率",prop:"exchange"},{default:a(l=>[l.row.exchange?(k(),y(O,{key:0,type:"success"},{default:a(()=>[p(h(l.row.exchange)+"%",1)]),_:2},1024)):x("",!0)]),_:1}),e(n,{label:"单笔手续费",prop:"onecost"}),e(n,{label:"下浮点数",prop:"downpoint"}),e(n,{label:"代理名称",prop:"agentname"},{default:a(l=>[l.row.agentname?(k(),y(O,{key:0},{default:a(()=>[p(h(l.row.agentname),1)]),_:2},1024)):x("",!0)]),_:1}),e(n,{label:"创建时间",prop:"create_time"}),e(n,{label:"操作",width:"200"},{default:a(l=>[e(F,{onOk:c=>E(l.row)},null,8,["onOk"])]),_:1})]),_:1},8,["params"])]),_:1})]),_:1},8,["modelValue","title"])):x("",!0)]),_:1})}}};export{Z as default};
