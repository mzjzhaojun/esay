import{u as S,r as s,o as D,g as y,e as d,c as j,d as z,j as b,f as o,w as i,k as t,F as E,N as k,l as c,m as g,n as M,p as P,q as $}from"./index.c25cc860.js";import{m as G}from"./merchant.4c2a4af0.js";import{m as H}from"./merchantaisle.6d4ef5d6.js";import{s as J}from"./sys_bank.911fb734.js";import{p as K}from"./payout.8317ad50.js";const L={style:{margin:"16px"}},Q={style:{margin:"16px"}},W=g(" \u63D0\u4EA4 "),X=g("\u52A0\u8F7D\u4E2D..."),oe={setup(Y){const V=S();let a=s({}),f=s({}),p=s(!1);s(!1);let u=s(!1);s(!1);let v=s([]),w=s([]),_=s(null);s(null),s(null);let q=s(null),h="http://116.212.152.243:18000/esay/rest/v2/file/upload";D(async()=>{let r={userid:y(),type:70},e=await H.list(r);v.value=e.body;let n=await G.getdata();console.info(n),a.value.totalincome=n.body.balance;let m=await J.list();w.value=m.body});function x(r){_=r.aisleid,a.value.aisleid=r.name,u.value=!1}async function U(){let r=parseInt(a.value.totalincome),e=parseInt(a.value.amount);if(r<e||e<=0)k({type:"danger",message:"\u8F93\u5165\u91D1\u989D\u8981\u5C0F\u4E8E\u53EF\u7528\u4F59\u989D"});else{p.value=!0,a.value.aisleid=_,a.value.bankcode=q;try{f={userid:y(),qrcode:a.qrcode,remark:a.value.remark,aisleid:a.value.aisleid,amount:a.value.amount,accname:a.value.accname,bankname:"\u652F\u4ED8\u5B9D",accnumer:a.value.accname},(await K.add(f)).code==200&&(k({type:"success",message:"\u63D0\u4EA4\u6210\u529F"}),setTimeout(()=>{p.value=!1,V.push({path:"/my"})},1e3))}catch{p.value=!1}}}function C(r){let e=new FormData,n={Satoken:M(),Tenant_Id:P()};e.append("file",r.file),$.post(h,e,{"content-type":"multipart/form-data",headers:n}).then(m=>{a.qrcode=m.data.body.url}).catch(m=>{console.info(m)})}return(r,e)=>{const n=d("van-field"),m=d("van-picker"),I=d("van-popup"),N=d("van-uploader"),T=d("van-cell-group"),A=d("van-button"),B=d("van-form"),F=d("van-loading"),R=d("van-overlay");return j(),z(E,null,[b("div",L,[o(B,{"label-width":"90",onSubmit:U},{default:i(()=>[o(T,{inset:""},{default:i(()=>[o(n,{modelValue:t(a).aisleid,"onUpdate:modelValue":e[0]||(e[0]=l=>t(a).aisleid=l),required:"","is-link":"",readonly:"",name:"picker",label:"\u9009\u62E9\u901A\u9053",placeholder:"\u70B9\u51FB\u9009\u62E9\u901A\u9053",onClick:e[1]||(e[1]=l=>c(u)?u.value=!0:u=!0)},null,8,["modelValue"]),o(I,{show:t(u),"onUpdate:show":e[3]||(e[3]=l=>c(u)?u.value=l:u=l),position:"bottom"},{default:i(()=>[o(m,{columns:t(v),"columns-field-names":{text:"name",value:"aisleid"},onConfirm:x,onCancel:e[2]||(e[2]=l=>c(u)?u.value=!1:u=!1)},null,8,["columns"])]),_:1},8,["show"]),o(n,{modelValue:t(a).amount,"onUpdate:modelValue":e[4]||(e[4]=l=>t(a).amount=l),name:"\u63D0\u6B3E\u91D1\u989D",label:"\u63D0\u6B3E\u91D1\u989D",placeholder:"\u63D0\u6B3E\u91D1\u989D",rules:[{required:!0,message:"\u8BF7\u586B\u5199\u63D0\u6B3E\u91D1\u989D"}],required:""},null,8,["modelValue"]),o(n,{modelValue:t(a).totalincome,"onUpdate:modelValue":e[5]||(e[5]=l=>t(a).totalincome=l),name:"\u53EF\u7528\u4F59\u989D",label:"\u53EF\u7528\u4F59\u989D",placeholder:"\u53EF\u7528\u4F59\u989D",rules:[{required:!0,message:"\u53EF\u7528\u4F59\u989D"}],readonly:"",required:""},null,8,["modelValue"]),o(n,{modelValue:t(a).accname,"onUpdate:modelValue":e[6]||(e[6]=l=>t(a).accname=l),name:"\u59D3\u540D",label:"\u59D3\u540D",placeholder:"\u59D3\u540D",rules:[{required:!0,message:"\u59D3\u540D"}],required:""},null,8,["modelValue"]),o(n,{name:"uploader",label:"\u4E8C\u7EF4\u7801",required:""},{input:i(()=>[o(N,{"max-count":1,"after-read":C,modelValue:t(a).qrcode,"onUpdate:modelValue":e[7]||(e[7]=l=>t(a).qrcode=l)},null,8,["modelValue"])]),_:1})]),_:1}),b("div",Q,[o(A,{block:"",type:"primary","native-type":"submit"},{default:i(()=>[W]),_:1})])]),_:1})]),o(R,{show:t(p)},{default:i(()=>[o(F,{size:"24px",vertical:""},{default:i(()=>[X]),_:1})]),_:1},8,["show"])],64)}}};export{oe as default};
