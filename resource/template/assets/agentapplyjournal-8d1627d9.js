import{g as T,r as D,a as l,b as r,c as p,w as a,d as e,u as s,i as u,n as d,k as b}from"./index-fc9417a1.js";const N={__name:"agentapplyjournal",setup(v){T();let n=D({});return(m,_)=>{const w=l("base-input"),i=l("el-col"),f=l("base-select-no"),y=l("el-button"),h=l("el-row"),k=l("base-header"),o=l("el-table-column"),c=l("el-tag"),g=l("base-table-p"),C=l("base-content"),V=l("base-wrapper");return r(),p(V,null,{default:a(()=>[e(k,null,{default:a(()=>[e(h,null,{default:a(()=>[e(i,{span:4.5},{default:a(()=>[e(w,{modelValue:s(n).nikname,"onUpdate:modelValue":_[0]||(_[0]=t=>s(n).nikname=t),label:"充值金额",onClear:m.refreshTableData},null,8,["modelValue","onClear"])]),_:1}),e(i,{span:4.5},{default:a(()=>[e(f,{modelValue:s(n).status,"onUpdate:modelValue":_[1]||(_[1]=t=>s(n).status=t),label:"状态","tag-type":"success",onClear:m.refreshTableData,style:{"margin-right":"10px"},clearable:"","option-props":{label:"name",value:"value"},dataList:m.statusList},null,8,["modelValue","onClear","dataList"])]),_:1}),e(i,{span:4.5},{default:a(()=>[e(y,{type:"primary",onClick:m.refreshTableData},{default:a(()=>[u("查询")]),_:1},8,["onClick"])]),_:1})]),_:1})]),_:1}),e(C,null,{default:a(()=>[e(g,{ref:"baseTableRef",api:"agentaccountapplyjourna.page",params:s(n),indexCode:!0},{default:a(()=>[e(o,{label:"系统单号",prop:"ordernum",width:"180"}),e(o,{label:"类型",prop:"typename"}),e(o,{label:"变更前收入",prop:"pretotalincome"},{default:a(t=>[t.row.pretotalincome?(r(),p(c,{key:0},{default:a(()=>[u(d(t.row.pretotalincome),1)]),_:2},1024)):b("",!0)]),_:1}),e(o,{label:"变更前支出",prop:"prewithdrawamount"},{default:a(t=>[t.row.prewithdrawamount?(r(),p(c,{key:0},{default:a(()=>[u(d(t.row.prewithdrawamount),1)]),_:2},1024)):b("",!0)]),_:1}),e(o,{label:"变更前代确认充值",prop:"pretoincomeamount"}),e(o,{label:"变更前代确认提现",prop:"pretowithdrawamount"}),e(o,{label:"变更后收入",prop:"posttotalincome"},{default:a(t=>[t.row.posttotalincome?(r(),p(c,{key:0,type:"success"},{default:a(()=>[u(d(t.row.posttotalincome),1)]),_:2},1024)):b("",!0)]),_:1}),e(o,{label:"变更后支出",prop:"postwithdrawamount"},{default:a(t=>[t.row.postwithdrawamount?(r(),p(c,{key:0,type:"success"},{default:a(()=>[u(d(t.row.postwithdrawamount),1)]),_:2},1024)):b("",!0)]),_:1}),e(o,{label:"变更后已确认充值",prop:"posttoincomeamount"}),e(o,{label:"变更后已确认提现",prop:"posttowithdrawamount"}),e(o,{label:"创建时间",prop:"create_time"}),e(o,{label:"备注",prop:"remark"})]),_:1},8,["params"])]),_:1})]),_:1})}}};export{N as default};
