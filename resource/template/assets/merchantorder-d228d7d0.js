import{g as K,r as V,a as o,b as c,c as _,w as l,d as e,u as t,i,n as h,k as f,j as U,e as $}from"./index-55143ade.js";const P={style:{"text-align":"center"}},Z={__name:"merchantorder",setup(W){const{proxy:d}=K();let O=d.$store.user.useUserStore();const{userObj:R}=O;let r=V({}),m=V(!1),b=V(""),g=V({userid:R.id,type:20}),T=V({imgurl:[{required:!0,message:"请上传支付凭证",trigger:"blur"}],amount:[{required:!0,message:"请输入金额",trigger:"blur"}]});function w(){d.$refs.baseTableRef.refresh()}function q(){C(),b.value="add",m.value=!0}async function C(){let s=await d.$api.payconfig.get();r.value=s.body,d.submitOk(s.message),S()}async function B(s){let n=await d.$api.merchantaccountorder.cancle(s.id);d.submitOk(n.message),w()}function N(){d.$refs.dataFormRef.validate(async s=>{if(s){r.value.id=null;let n=await d.$api.merchantaccountorder.add(r.value);d.submitOk(n.message),w(),m.value=!1}})}async function S(){let s=await d.$api.merchant.getdata();r.value.merchantexchange=s.body.downpoint}return(s,n)=>{const j=o("base-input"),v=o("el-col"),D=o("base-select-no"),y=o("el-button"),F=o("el-row"),L=o("base-header"),u=o("el-table-column"),x=o("el-tag"),z=o("el-image"),A=o("base-confirm-btn"),E=o("base-table-p"),I=o("base-content"),k=o("base-input-no"),p=o("el-form-item"),M=o("base-qrcode"),Q=o("base-upload-single"),G=o("el-form"),H=o("base-dialog"),J=o("base-wrapper");return c(),_(J,null,{default:l(()=>[e(L,null,{default:l(()=>[e(F,null,{default:l(()=>[e(v,{span:4.5},{default:l(()=>[e(j,{modelValue:t(g).nikname,"onUpdate:modelValue":n[0]||(n[0]=a=>t(g).nikname=a),label:"充值金额",onClear:w},null,8,["modelValue"])]),_:1}),e(v,{span:4.5},{default:l(()=>[e(D,{modelValue:t(g).status,"onUpdate:modelValue":n[1]||(n[1]=a=>t(g).status=a),label:"状态","tag-type":"success",onClear:w,style:{"margin-right":"10px"},clearable:"","option-props":{label:"name",value:"value"},dataList:s.statusList},null,8,["modelValue","dataList"])]),_:1}),e(v,{span:4.5},{default:l(()=>[e(y,{type:"primary",onClick:w},{default:l(()=>[i("查询")]),_:1}),e(y,{type:"success",onClick:q},{default:l(()=>[i("充值")]),_:1})]),_:1})]),_:1})]),_:1}),e(I,null,{default:l(()=>[e(E,{ref:"baseTableRef",api:"merchantaccountorder.page",params:t(g),indexCode:!0},{default:l(()=>[e(u,{label:"充值单号",prop:"ordernum",width:"180px;"}),e(u,{label:"商户名称",prop:"username"}),e(u,{label:"商户编码",prop:"merchantcode"}),e(u,{label:"标签",prop:"nkname"}),e(u,{label:"充值U金额",prop:"amount"}),e(u,{label:"充值汇率",prop:"exchange"},{default:l(a=>[a.row.exchange?(c(),_(x,{key:0,type:"danger"},{default:l(()=>[i(h(a.row.exchange),1)]),_:2},1024)):f("",!0)]),_:1}),e(u,{label:"充值汇率浮动",prop:"merchantexchange"},{default:l(a=>[a.row.merchantexchange?(c(),_(x,{key:0,type:"danger"},{default:l(()=>[i(h(a.row.merchantexchange),1)]),_:2},1024)):f("",!0)]),_:1}),e(u,{label:"到账RMB金额",prop:"amountreceived"},{default:l(a=>[a.row.amountreceived?(c(),_(x,{key:0,type:"success"},{default:l(()=>[i(h(a.row.amountreceived),1)]),_:2},1024)):f("",!0)]),_:1}),e(u,{label:"状态"},{default:l(a=>[a.row.statusname?(c(),_(x,{key:0},{default:l(()=>[i(h(a.row.statusname),1)]),_:2},1024)):f("",!0)]),_:1}),e(u,{label:"支付凭证",prop:"imgurl"},{default:l(a=>[e(z,{style:{width:"50px",height:"50px"},src:a.row.imgurl,"zoom-rate":1.2,"max-scale":7,"min-scale":.2,"preview-src-list":[a.row.imgurl],"initial-index":4,"preview-teleported":"true",fit:"cover"},null,8,["src","preview-src-list"])]),_:1}),e(u,{label:"创建时间",prop:"create_time"}),e(u,{label:"备注",prop:"remark",width:"200"}),e(u,{label:"操作",width:"100"},{default:l(a=>[a.row.status==10?(c(),_(A,{key:0,type:"success",onOk:X=>B(a.row),label:"取消"},null,8,["onOk"])):f("",!0)]),_:1})]),_:1},8,["params"])]),_:1}),t(b)==="add"||t(b)==="update"?(c(),_(H,{key:0,modelValue:t(m),"onUpdate:modelValue":n[8]||(n[8]=a=>U(m)?m.value=a:m=a),title:s.dialogTitleObj[t(b)],width:"30%"},{footer:l(()=>[e(y,{onClick:n[7]||(n[7]=a=>U(m)?m.value=!1:m=!1)},{default:l(()=>[i("取 消")]),_:1}),e(y,{type:"primary",onClick:N},{default:l(()=>[i("确 定")]),_:1})]),default:l(()=>[e(G,{ref:"dataFormRef",rules:t(T),model:t(r),"label-width":"150px"},{default:l(()=>[e(p,{label:"充值金额:",prop:"amount"},{default:l(()=>[e(k,{modelValue:t(r).amount,"onUpdate:modelValue":n[2]||(n[2]=a=>t(r).amount=a)},null,8,["modelValue"])]),_:1}),e(p,{label:"充值汇率:"},{default:l(()=>[e(k,{modelValue:t(r).exchange,"onUpdate:modelValue":n[3]||(n[3]=a=>t(r).exchange=a),disabled:t(b)=="add"},null,8,["modelValue","disabled"]),e(y,{style:{"margin-left":"10px"},type:"warning",onClick:C},{default:l(()=>[i("刷新")]),_:1})]),_:1}),e(p,{label:"充值汇率浮动:",prop:"merchantexchange"},{default:l(()=>[e(k,{modelValue:t(r).merchantexchange,"onUpdate:modelValue":n[4]||(n[4]=a=>t(r).merchantexchange=a),disabled:t(b)=="add"},null,8,["modelValue","disabled"])]),_:1}),e(p,{label:"扫码支付:"},{default:l(()=>[$("div",P,[e(M,{value:t(r).usdt,size:"200"},null,8,["value"])])]),_:1}),e(p,{label:"网络TRC20:"},{default:l(()=>[$("span",null,h(t(r).usdt),1)]),_:1}),e(p,{label:"上传凭证:",prop:"imgurl"},{default:l(()=>[e(Q,{modelValue:t(r).imgurl,"onUpdate:modelValue":n[5]||(n[5]=a=>t(r).imgurl=a)},null,8,["modelValue"])]),_:1}),e(p,{label:"备注:",prop:"remark"},{default:l(()=>[e(k,{modelValue:t(r).remark,"onUpdate:modelValue":n[6]||(n[6]=a=>t(r).remark=a),type:"textarea",style:{width:"300px"}},null,8,["modelValue"])]),_:1})]),_:1},8,["rules","model"])]),_:1},8,["modelValue","title"])):f("",!0)]),_:1})}}};export{Z as default};