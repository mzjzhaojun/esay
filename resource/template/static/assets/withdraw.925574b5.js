import{u as y,r as i,o as x,e as r,c as w,d as q,j as c,f as l,w as u,k as t,F as k,N as p,m as _}from"./index.5ac32ff6.js";import{m as N}from"./merchant.520e94d6.js";import{m as U}from"./merchantaccountorder.ec25300b.js";const C={style:{margin:"16px"}},T={style:{margin:"16px"}},B=_(" \u63D0\u4EA4 "),I=_("\u52A0\u8F7D\u4E2D..."),j={setup(R){const f=y();let e=i({}),m=i(!1);x(async()=>{let s=await N.getdata();console.info(s),e.value.totalincome=s.body.balance});async function v(){let s=parseInt(e.value.totalincome),a=parseInt(e.value.amount),n=parseInt(e.merchantexchange);s<a||a<=0||n<=0?p({type:"danger",message:"\u8F93\u5165\u63D0\u73B0\u6216\u8005\u6C47\u7387\u6570\u503C\u9519\u8BEF"}):(m.value=!0,(await U.withdraw(e.value)).code==200?(p({type:"success",message:"\u63D0\u4EA4\u6210\u529F"}),setTimeout(()=>{m.value=!1,f.push({path:"/my"})},1e3)):m.value=!1)}return(s,a)=>{const n=r("van-field"),d=r("van-cell-group"),g=r("van-button"),h=r("van-form"),V=r("van-loading"),b=r("van-overlay");return w(),q(k,null,[c("div",C,[l(h,{"label-width":"70",onSubmit:v},{default:u(()=>[l(d,{inset:""},{default:u(()=>[l(n,{modelValue:t(e).accnumber,"onUpdate:modelValue":a[0]||(a[0]=o=>t(e).accnumber=o),label:"TRC20",placeholder:"\u7F51\u7EDCTRC20",rows:"2",type:"textarea",required:""},null,8,["modelValue"]),l(n,{modelValue:t(e).amount,"onUpdate:modelValue":a[1]||(a[1]=o=>t(e).amount=o),name:"\u63D0\u73B0\u91D1\u989D",label:"\u63D0\u73B0\u91D1\u989D",placeholder:"\u63D0\u73B0\u91D1\u989D",rules:[{required:!0,message:"\u8BF7\u586B\u5199\u63D0\u73B0\u91D1\u989D"}],required:""},null,8,["modelValue"]),l(n,{modelValue:t(e).totalincome,"onUpdate:modelValue":a[2]||(a[2]=o=>t(e).totalincome=o),name:"\u53EF\u7528\u4F59\u989D",label:"\u53EF\u7528\u4F59\u989D",placeholder:"\u53EF\u7528\u4F59\u989D",rules:[{required:!0,message:"\u53EF\u7528\u4F59\u989D"}],readonly:"",required:""},null,8,["modelValue"]),l(n,{modelValue:t(e).merchantexchange,"onUpdate:modelValue":a[3]||(a[3]=o=>t(e).merchantexchange=o),name:"\u63D0\u73B0\u6C47\u7387",label:"\u63D0\u73B0\u6C47\u7387",placeholder:"\u63D0\u73B0\u6C47\u7387",rules:[{required:!0,message:"\u63D0\u73B0\u6C47\u7387"}],required:""},null,8,["modelValue"]),l(n,{modelValue:t(e).remark,"onUpdate:modelValue":a[4]||(a[4]=o=>t(e).remark=o),name:"\u5907\u6CE8",label:"\u5907\u6CE8",placeholder:"\u5907\u6CE8"},null,8,["modelValue"])]),_:1}),c("div",T,[l(g,{block:"",type:"primary","native-type":"submit"},{default:u(()=>[B]),_:1})])]),_:1})]),l(b,{show:t(m)},{default:u(()=>[l(V,{size:"24px",vertical:""},{default:u(()=>[I]),_:1})]),_:1},8,["show"])],64)}}};export{j as default};