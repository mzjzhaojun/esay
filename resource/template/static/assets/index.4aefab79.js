import{s,a as E,u as L,r as v,o as O,e as i,c as U,d as z,j as o,f as t,t as d,k as p,v as D,w as _,x as M,y as P,m as x}from"./index.c25cc860.js";import{p as R}from"./payconfig.129f1315.js";const c="/rest/v1/merchantaccount";var T={page(e,l){return s({url:c+"/page",method:"post",data:e,headers:l})},list(e){return s({url:c+"/list",method:"post",data:e})},add(e){return s({url:c,method:"post",data:e})},update(e){return s({url:c,method:"put",data:e})},delete(e){return s({url:c+"/"+e,method:"delete"})},get(){return s({url:c+"/data",method:"get"})},bank(){return s({url:c+"/bank",method:"get"})},getbank(e){return s({url:c+"/bank/"+e,method:"get"})}};var $=(e,l)=>{const n=e.__vccOpts||e;for(const[r,u]of l)n[r]=u;return n};const h=e=>(M("data-v-5d14cc04"),e=e(),P(),e),q={class:"profile-container"},F={class:"profile-header"},G={class:"profile-header-inner"},H={class:"profile-info"},J={class:"profile-info-main"},K={class:"profile-info-main-nickname"},Q={class:"profile-number"},W={class:"profile-number-box"},X={class:"profile-number-box-num"},Y=h(()=>o("span",{class:"profile-number-box-text"},"\u5145\u503C\u91D1\u989D",-1)),Z={class:"profile-number-box"},ee={class:"profile-number-box-num"},oe=h(()=>o("span",{class:"profile-number-box-text"},"\u63D0\u6B3E\u91D1\u989D",-1)),te={class:"profile-number-box"},ne={class:"profile-number-box-num"},ae=h(()=>o("span",{class:"profile-number-box-text"},"\u4F59\u989D",-1)),se=x("\u6B27\u6613\u4E00\u6863\u4E70\u5165\u4EF7"),ce=h(()=>o("span",{class:"custom-title"},"$ 1",-1)),re={setup(e){const l=E(),n=L();let r=v({}),u=v({});O(async()=>{let m=await T.get();console.info(m),r.value=m.body;let f=await R.get();console.info(f),u.value=f.body});function b(){n.push({path:"/my/balance"})}function g(){n.push({path:"/my/incomerecord"})}function y(){n.push({path:"/my/withdraw"})}function k(){n.push({path:"/my/withdrawrecord"})}function w(){n.push({path:"/exchange/exchangerecord"})}function C(){n.push({path:"/my/aisle"})}function S(){n.push({path:"/my/applyjournal"})}function I(){l.LogOut(),n.push({path:"/#/login"})}return(m,f)=>{const A=i("van-image"),a=i("van-grid-item"),B=i("van-grid"),N=i("van-divider"),j=i("van-icon"),V=i("van-cell");return U(),z("div",q,[o("div",F,[o("div",G,[o("div",H,[t(A,{class:"profile-info-avatar",round:"",fit:"cover",src:"/favicon.ico"}),o("div",J,[o("span",K,d(p(D)()),1)])]),o("div",Q,[o("div",W,[o("span",X,"\uFFE5"+d(p(r).totalincome),1),Y]),o("div",Z,[o("span",ee,"\uFFE5"+d(p(r).withdrawamount),1),oe]),o("div",te,[o("span",ne,"\uFFE5"+d(p(r).balance),1),ae])])]),t(B,{clickable:"","column-num":4},{default:_(()=>[t(a,{icon:"balance-pay",text:"\u5145\u503C",onClick:b}),t(a,{icon:"refund-o",text:"\u5145\u503C\u8BB0\u5F55",onClick:g}),t(a,{icon:"cash-o",text:"\u63D0\u73B0",onClick:y}),t(a,{icon:"after-sale",text:"\u63D0\u73B0\u8BB0\u5F55",onClick:k}),t(a,{icon:"notes-o",text:"\u6362\u6C47\u8BB0\u5F55",onClick:w}),t(a,{icon:"service-o",text:"\u6211\u7684\u901A\u9053",onClick:C}),t(a,{icon:"comment-o",text:"\u8D44\u91D1\u660E\u7EC6",onClick:S}),t(a,{icon:"close",text:"\u9000\u51FA",onClick:I})]),_:1}),t(N,{style:{color:"#1989fa",borderColor:"#1989fa",padding:"0 5px"}},{default:_(()=>[se]),_:1}),t(V,null,{title:_(()=>[ce,t(j,{name:"exchange",size:"20px"})]),default:_(()=>[x("\uFFE5 "+d(p(u).exchange)+" ",1)]),_:1})])])}}};var de=$(re,[["__scopeId","data-v-5d14cc04"]]);export{de as default};
