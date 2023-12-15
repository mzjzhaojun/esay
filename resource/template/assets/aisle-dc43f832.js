import{g as Z,r as w,a as n,b as y,c as k,w as a,d as e,u as o,i as m,n as R,k as U,j as $}from"./index-65a05d71.js";const ae={__name:"aisle",setup(ee){const{proxy:p}=Z();let _=w({}),u=w({}),i=w({}),s=w(!1),c=w(!1),b=w(""),Q=w({name:[{required:!0,message:"请输入通道名称",trigger:"blur"}],code:[{required:!0,pattern:/^(\w){6,16}$/,message:"请设置6-16位字母、数字组合数字编码",trigger:"blur"}],exchange:[{required:!0,message:"请输入汇率",trigger:"blur"}],onecost:[{required:!0,message:"请输入单笔手续费",trigger:"blur"}]});function V(){p.$refs.baseTableRef.refresh()}function S(){u.value={status:!0},b.value="add",s.value=!0}function I(r){u.value=Object.assign({},r),b.value="update",s.value=!0}async function z(r){let t=await p.$api.aisle.delete(r.id);V(),p.submitOk(t.message)}function E(){p.$refs.dataFormRef.validate(async r=>{if(r){let t=await p.$api.aisle[u.value.id?"update":"add"](u.value);p.submitOk(t.message),V(),s.value=!1}})}function G(r){i.value={aisleid:r.id},b.value="channelrecord",s.value=!0}function H(){c.value=!0,s.value=!0}async function J(r){let t=await p.$api.aislechannel.add({channelid:r,aisleid:i.value.aisleid});p.submitOk(t.message),c.value=!1,v()}async function K(r){let t=await p.$api.aislechannel.delete(r.id);v(),p.submitOk(t.message)}function v(){V(),p.$refs.baseChannelTableRef.refresh()}function M(){b.value="add",s.value=!1}return(r,t)=>{const O=n("base-input"),C=n("el-col"),D=n("base-select-no"),f=n("el-button"),L=n("el-row"),q=n("base-header"),d=n("el-table-column"),j=n("base-switch"),T=n("el-tag"),F=n("base-delete-btn"),N=n("base-table-p"),A=n("base-content"),x=n("base-input-no"),h=n("el-form-item"),P=n("base-radio-group"),W=n("el-form"),B=n("base-dialog"),X=n("choose-channel-dialog"),Y=n("base-wrapper");return y(),k(Y,null,{default:a(()=>[e(q,null,{default:a(()=>[e(L,null,{default:a(()=>[e(C,{span:4.5},{default:a(()=>[e(O,{modelValue:o(_).name,"onUpdate:modelValue":t[0]||(t[0]=l=>o(_).name=l),label:"通道名称",onClear:V},null,8,["modelValue"])]),_:1}),e(C,{span:4.5},{default:a(()=>[e(O,{modelValue:o(_).code,"onUpdate:modelValue":t[1]||(t[1]=l=>o(_).code=l),label:"通道编码",onClear:V},null,8,["modelValue"])]),_:1}),e(D,{modelValue:o(_).status,"onUpdate:modelValue":t[2]||(t[2]=l=>o(_).status=l),label:"状态","tag-type":"success",onClear:V,style:{"margin-right":"10px"},clearable:"","option-props":{label:"name",value:"value"},dataList:r.statusList},null,8,["modelValue","dataList"]),e(C,{span:4.5},{default:a(()=>[e(f,{type:"primary",onClick:V},{default:a(()=>[m("查询")]),_:1}),e(f,{type:"success",onClick:S},{default:a(()=>[m("添加")]),_:1})]),_:1})]),_:1})]),_:1}),e(A,null,{default:a(()=>[e(N,{ref:"baseTableRef",api:"aisle.page",params:o(_),indexCode:!0},{default:a(()=>[e(d,{label:"通道名称",prop:"name"}),e(d,{label:"编码",prop:"code"}),e(d,{label:"状态"},{default:a(l=>[e(j,{modelValue:l.row.status,"onUpdate:modelValue":g=>l.row.status=g,api:"aisle.update",params:{id:l.row.id,status:!l.row.status}},null,8,["modelValue","onUpdate:modelValue","params"])]),_:1}),e(d,{label:"交易费率‰",prop:"exchange"},{default:a(l=>[l.row.exchange?(y(),k(T,{key:0,type:"success"},{default:a(()=>[m(R(l.row.exchange)+"‰",1)]),_:2},1024)):U("",!0)]),_:1}),e(d,{label:"单笔手续费",prop:"onecost"}),e(d,{label:"绑定渠道数",prop:"channelcount"},{default:a(l=>[l.row.channelcount?(y(),k(T,{key:0},{default:a(()=>[m(" 已绑定"+R(l.row.channelcount)+"个",1)]),_:2},1024)):U("",!0)]),_:1}),e(d,{label:"备注",prop:"remark"}),e(d,{label:"创建时间",prop:"create_time"}),e(d,{label:"操作"},{default:a(l=>[e(f,{type:"primary",link:"",onClick:g=>I(l.row)},{default:a(()=>[m("编辑")]),_:2},1032,["onClick"]),e(f,{type:"success",link:"",onClick:g=>G(l.row)},{default:a(()=>[m("渠道管理")]),_:2},1032,["onClick"]),e(F,{onOk:g=>z(l.row)},null,8,["onOk"])]),_:1})]),_:1},8,["params"])]),_:1}),o(b)==="add"||o(b)==="update"?(y(),k(B,{key:0,modelValue:o(s),"onUpdate:modelValue":t[10]||(t[10]=l=>$(s)?s.value=l:s=l),title:r.dialogTitleObj[o(b)],width:"23%"},{footer:a(()=>[e(f,{onClick:t[9]||(t[9]=l=>$(s)?s.value=!1:s=!1)},{default:a(()=>[m("取 消")]),_:1}),e(f,{type:"primary",onClick:E},{default:a(()=>[m("确 定")]),_:1})]),default:a(()=>[e(W,{ref:"dataFormRef",rules:o(Q),model:o(u),"label-width":"100px"},{default:a(()=>[e(h,{label:"通道名称:",prop:"name"},{default:a(()=>[e(x,{modelValue:o(u).name,"onUpdate:modelValue":t[3]||(t[3]=l=>o(u).name=l)},null,8,["modelValue"])]),_:1}),e(h,{label:"通道编码:",prop:"code"},{default:a(()=>[e(x,{modelValue:o(u).code,"onUpdate:modelValue":t[4]||(t[4]=l=>o(u).code=l)},null,8,["modelValue"])]),_:1}),e(h,{label:"交易费率:",prop:"exchange"},{default:a(()=>[e(x,{modelValue:o(u).exchange,"onUpdate:modelValue":t[5]||(t[5]=l=>o(u).exchange=l),tip:"‰",label:"千分比 1-100整数",onkeyup:"value=value.replace(/[^\\d.]/g,'')"},null,8,["modelValue"])]),_:1}),e(h,{label:"单笔手续费:",prop:"onecost"},{default:a(()=>[e(x,{modelValue:o(u).onecost,"onUpdate:modelValue":t[6]||(t[6]=l=>o(u).onecost=l),onkeyup:"value=value.replace(/[^\\d.]/g,'')"},null,8,["modelValue"])]),_:1}),e(h,{label:"状态:"},{default:a(()=>[e(P,{modelValue:o(u).status,"onUpdate:modelValue":t[7]||(t[7]=l=>o(u).status=l)},null,8,["modelValue"])]),_:1}),e(h,{label:"备注:"},{default:a(()=>[e(x,{modelValue:o(u).remark,"onUpdate:modelValue":t[8]||(t[8]=l=>o(u).remark=l),rows:2,type:"textarea",style:{width:"300px"}},null,8,["modelValue"])]),_:1})]),_:1},8,["rules","model"])]),_:1},8,["modelValue","title"])):U("",!0),o(b)==="channelrecord"?(y(),k(B,{key:1,modelValue:o(s),"onUpdate:modelValue":t[15]||(t[15]=l=>$(s)?s.value=l:s=l),title:"渠道管理",width:"60%","destroy-on-close":!0,onClose:M},{default:a(()=>[o(c)?(y(),k(X,{key:0,modelValue:o(c),"onUpdate:modelValue":t[11]||(t[11]=l=>$(c)?c.value=l:c=l),"append-to-body":"",title:"添加通道",width:"50%",onChoose:J},null,8,["modelValue"])):U("",!0),e(q,null,{default:a(()=>[e(L,null,{default:a(()=>[e(C,{span:4.5},{default:a(()=>[e(O,{modelValue:o(i).name,"onUpdate:modelValue":t[12]||(t[12]=l=>o(i).name=l),label:"渠道名称",onClear:v},null,8,["modelValue"])]),_:1}),e(C,{span:4.5},{default:a(()=>[e(O,{modelValue:o(i).code,"onUpdate:modelValue":t[13]||(t[13]=l=>o(i).code=l),label:"渠道编码",onClear:v},null,8,["modelValue"])]),_:1}),e(D,{modelValue:o(i).status,"onUpdate:modelValue":t[14]||(t[14]=l=>o(i).status=l),label:"状态","tag-type":"success",onClear:v,style:{"margin-right":"10px"},clearable:"","option-props":{label:"name",value:"value"},dataList:r.statusList},null,8,["modelValue","dataList"]),e(C,{span:4.5},{default:a(()=>[e(f,{type:"primary",onClick:v},{default:a(()=>[m("查询")]),_:1}),e(f,{type:"success",onClick:H},{default:a(()=>[m("添加通道")]),_:1})]),_:1})]),_:1})]),_:1}),e(A,null,{default:a(()=>[e(N,{ref:"baseChannelTableRef",api:"aislechannel.page",params:o(i)},{default:a(()=>[e(d,{label:"渠道名称",prop:"name"}),e(d,{label:"备注",prop:"remark"}),e(d,{label:"编码",prop:"code"}),e(d,{label:"状态"},{default:a(l=>[e(j,{modelValue:l.row.status,"onUpdate:modelValue":g=>l.row.status=g,api:"channel.update",params:{id:l.row.channelid,status:!l.row.status}},null,8,["modelValue","onUpdate:modelValue","params"])]),_:1}),e(d,{label:"汇率",prop:"exchange"},{default:a(l=>[l.row.exchange?(y(),k(T,{key:0,type:"success"},{default:a(()=>[m(R(l.row.exchange)+"%",1)]),_:2},1024)):U("",!0)]),_:1}),e(d,{label:"单笔手续费",prop:"onecost"}),e(d,{label:"操作"},{default:a(l=>[e(F,{onOk:g=>K(l.row)},null,8,["onOk"])]),_:1})]),_:1},8,["params"])]),_:1})]),_:1},8,["modelValue"])):U("",!0)]),_:1})}}};export{ae as default};
