import{g as H,r as f,a as r,b as w,c as C,w as n,d as e,u as a,i as V,n as J,k as L,j as A}from"./index-3e58188c.js";const W={__name:"merchantpayout",setup(K){const{proxy:b}=H(),{userObj:y}=b.$store.user.useUserStore();let s=f({userid:y.id}),o=f({}),i=f(!1),g=f(""),U=f({userid:y.id}),q=f({aisleid:[{required:!0,message:"请选择通道",trigger:"blur"}],accnumer:[{required:!0,message:"请输入卡号",trigger:"blur"}],accname:[{required:!0,message:"请输入账户名",trigger:"blur"}],bankname:[{required:!0,message:"请输入银行名称",trigger:"blur"}],amount:[{required:!0,trigger:"change",validator:h}]});function h(d,l,p){return!l||l>o.value.totalincome?p(new Error("请输入金额,并且不能大于可用余额")):p()}function m(){b.$refs.baseTableRef.refresh()}function R(){S(),o.value={},g.value="add",i.value=!0}async function S(){let d=await b.$api.merchantaccount.bank();o.value={totalincome:d.body.balance}}function T(){b.$refs.dataFormRef.validate(async d=>{if(d){o.value.provinceCityAreaList&&(o.value.bankaddress=o.value.provinceCityAreaList[0],o.value.provinceCityAreaList.length>2&&(o.value.bankaddress=o.value.bankaddress+""+o.value.provinceCityAreaList[1]+o.value.provinceCityAreaList[2]));let l=await b.$api.payout[o.value.id?"update":"add"](o.value);b.submitOk(l.message),m(),i.value=!1}})}function $(d){o.value.accname=d.accname,o.value.accnumer=d.accnumber,o.value.bankname=d.bankname}return(d,l)=>{const p=r("base-input"),_=r("el-col"),B=r("base-select-no"),v=r("el-button"),j=r("el-row"),F=r("base-header"),u=r("el-table-column"),N=r("el-tag"),O=r("el-image"),P=r("base-table-p"),D=r("base-content"),x=r("base-cascader-no"),c=r("el-form-item"),k=r("base-input-no"),z=r("base-select-query"),E=r("province-city-area"),I=r("el-form"),Q=r("base-dialog"),G=r("base-wrapper");return w(),C(G,null,{default:n(()=>[e(F,null,{default:n(()=>[e(j,null,{default:n(()=>[e(_,{span:4.5},{default:n(()=>[e(p,{modelValue:a(s).name,"onUpdate:modelValue":l[0]||(l[0]=t=>a(s).name=t),label:"商户名称",onClear:m},null,8,["modelValue"])]),_:1}),e(_,{span:4.5},{default:n(()=>[e(p,{modelValue:a(s).code,"onUpdate:modelValue":l[1]||(l[1]=t=>a(s).code=t),label:"商户编码",onClear:m},null,8,["modelValue"])]),_:1}),e(_,{span:4.5},{default:n(()=>[e(p,{modelValue:a(s).nikname,"onUpdate:modelValue":l[2]||(l[2]=t=>a(s).nikname=t),label:"商户标签",onClear:m},null,8,["modelValue"])]),_:1}),e(_,{span:4.5},{default:n(()=>[e(B,{modelValue:a(s).status,"onUpdate:modelValue":l[3]||(l[3]=t=>a(s).status=t),label:"状态","tag-type":"success",onClear:m,style:{"margin-right":"10px"},clearable:"","option-props":{label:"name",value:"value"},dataList:d.statusList},null,8,["modelValue","dataList"])]),_:1}),e(_,{span:4.5},{default:n(()=>[e(p,{modelValue:a(s).agentname,"onUpdate:modelValue":l[4]||(l[4]=t=>a(s).agentname=t),label:"代理名称",onClear:m},null,8,["modelValue"])]),_:1}),e(_,{span:4.5},{default:n(()=>[e(v,{type:"primary",onClick:m},{default:n(()=>[V("查询")]),_:1}),e(v,{type:"success",onClick:R},{default:n(()=>[V("发起代付")]),_:1})]),_:1})]),_:1})]),_:1}),e(D,null,{default:n(()=>[e(P,{ref:"baseTableRef",api:"payout.page",params:a(s),indexCode:!0,orderBy:"create_time",dir:"desc"},{default:n(()=>[e(u,{label:"系统订单号",prop:"ordernum"}),e(u,{label:"商户订单号",prop:"merchantordernum"}),e(u,{label:"通道",prop:"aislename"}),e(u,{label:"状态",prop:"statusname",width:"100"},{default:n(t=>[t.row.statusname?(w(),C(N,{key:0,type:"danger"},{default:n(()=>[V(J(t.row.statusname),1)]),_:2},1024)):L("",!0)]),_:1}),e(u,{label:"金额",prop:"amount",width:"80"}),e(u,{label:"账户名",prop:"accname",width:"80"}),e(u,{label:"卡号/地址",prop:"accnumer"}),e(u,{label:"银行名称",prop:"bankname",width:"120"}),e(u,{label:"创建时间",prop:"create_time"}),e(u,{label:"操作秒数",prop:"backlong",width:"80"}),e(u,{label:"完成时间",prop:"successtime"}),e(u,{label:"回执",width:"125"},{default:n(t=>[e(O,{style:{width:"50px",height:"50px"},src:t.row.imgurl,"zoom-rate":1.2,"max-scale":7,"min-scale":.2,"preview-src-list":[t.row.imgurl],"initial-index":4,"preview-teleported":"true",fit:"cover"},null,8,["src","preview-src-list"])]),_:1})]),_:1},8,["params"])]),_:1}),a(g)==="add"||a(g)==="update"?(w(),C(Q,{key:0,modelValue:a(i),"onUpdate:modelValue":l[13]||(l[13]=t=>A(i)?i.value=t:i=t),title:d.dialogTitleObj[a(g)],width:"23%"},{footer:n(()=>[e(v,{onClick:l[12]||(l[12]=t=>A(i)?i.value=!1:i=!1)},{default:n(()=>[V("取 消")]),_:1}),e(v,{type:"primary",onClick:T},{default:n(()=>[V("确 定")]),_:1})]),default:n(()=>[e(I,{inline:!0,ref:"dataFormRef",rules:a(q),model:a(o),"label-width":"100px"},{default:n(()=>[e(c,{label:"选择通道:",prop:"aisleid"},{default:n(()=>[e(x,{modelValue:a(o).aisleid,"onUpdate:modelValue":l[5]||(l[5]=t=>a(o).aisleid=t),style:{"margin-right":"10px"},clearable:"",label:"",params:a(U),props:{value:"aisleid",label:"name",checkStrictly:!0,emitPath:!1},api:"merchantaisle.page"},null,8,["modelValue","params"])]),_:1}),e(c,{label:"可用余额:",prop:"totalincome"},{default:n(()=>[e(k,{modelValue:a(o).totalincome,"onUpdate:modelValue":l[6]||(l[6]=t=>a(o).totalincome=t),disabled:a(g)=="add"},null,8,["modelValue","disabled"])]),_:1}),e(c,{label:"金额",prop:"amount"},{default:n(()=>[e(k,{modelValue:a(o).amount,"onUpdate:modelValue":l[7]||(l[7]=t=>a(o).amount=t),onkeyup:"value=value.replace(/[^\\d.]/g,'')"},null,8,["modelValue"])]),_:1}),e(c,{label:"姓名:",prop:"accname"},{default:n(()=>[e(z,{api:"merchantbanks.page",params:{userid:a(y).id},query:"accname",modelValue:a(o).accname,"onUpdate:modelValue":l[8]||(l[8]=t=>a(o).accname=t),onChoose:$},null,8,["params","modelValue"])]),_:1}),e(c,{label:"账号:",prop:"accnumer"},{default:n(()=>[e(k,{modelValue:a(o).accnumer,"onUpdate:modelValue":l[9]||(l[9]=t=>a(o).accnumer=t)},null,8,["modelValue"])]),_:1}),e(c,{label:"银行名称:",prop:"bankcode"},{default:n(()=>[e(x,{modelValue:a(o).bankcode,"onUpdate:modelValue":l[10]||(l[10]=t=>a(o).bankcode=t),style:{"margin-right":"10px"},clearable:"",label:"",params:a(U),props:{value:"code",label:"name",checkStrictly:!0,emitPath:!1},api:"sys_bank.list"},null,8,["modelValue","params"])]),_:1}),e(c,{label:"银行卡地址:"},{default:n(()=>[e(E,{modelValue:a(o).provinceCityAreaList,"onUpdate:modelValue":l[11]||(l[11]=t=>a(o).provinceCityAreaList=t)},null,8,["modelValue"])]),_:1})]),_:1},8,["rules","model"])]),_:1},8,["modelValue","title"])):L("",!0)]),_:1})}}};export{W as default};
