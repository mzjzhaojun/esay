import{m as k}from"./merchantaccountorder.0fadc5e2.js";import{r as l,e as d,c as f,d as _,f as p,w as y,g as B,F as U,h as w,i as L}from"./index.c25cc860.js";const F={setup(S){const n=l([]),t=l(!1),s=l(!1),u=l(!1),r=l(1),v=l("");function m(){setTimeout(()=>{u.value&&(n.value=[],u.value=!1),g()},1e3)}function V(){n.value=[],r.value=1,g()}async function g(){let h={pageNum:r.value,pageSize:10,orderBy:"create_time",dir:"desc"},a={accname:v.value,userid:B(),type:21},o=await k.page(a,h);if(console.info(o),o.body.records.length>0){let c=o.body.records;c.length>0?(c.forEach(i=>{n.value.push(i)}),t.value=!1,r.value++):s.value=!0}else s.value=!0,t.value=!1}function x(){s.value=!1,t.value=!0,r.value=1,n.value=[],m()}return(h,a)=>{const o=d("van-search"),c=d("van-cell"),i=d("van-list"),b=d("van-pull-refresh");return f(),_("div",null,[p(o,{modelValue:v.value,"onUpdate:modelValue":a[0]||(a[0]=e=>v.value=e),"input-align":"center",placeholder:"\u8BF7\u8F93\u5165\u641C\u7D22\u540D\u79F0",onSearch:V},null,8,["modelValue"]),p(b,{modelValue:u.value,"onUpdate:modelValue":a[2]||(a[2]=e=>u.value=e),onRefresh:x},{default:y(()=>[p(i,{loading:t.value,"onUpdate:loading":a[1]||(a[1]=e=>t.value=e),finished:s.value,"finished-text":"\u6CA1\u6709\u66F4\u591A\u4E86",onLoad:m},{default:y(()=>[(f(!0),_(U,null,w(n.value,e=>(f(),L(c,{key:e,title:"\uFFE5"+e.amount+"   \u6C47\u7387:"+e.merchantexchange,value:e.statusname,label:"   $:"+e.usdtval},null,8,["title","value","label"]))),128))]),_:1},8,["loading","finished"])]),_:1},8,["modelValue"])])}}};export{F as default};
