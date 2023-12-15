import{g as z,r as c,a as u,b as r,c as i,w as o,d as e,u as a,i as p,n as C,k as g,j as Y,e as E}from"./index-fc9417a1.js";const G={class:"flex"},K={__name:"tenant",setup(H){const{proxy:k}=z();let y=c({}),n=c({}),d=c(!1),_=c(""),D=c({});function w(){k.$refs.baseTableRef.refresh()}function R(){n.value={sort:100,status:!0},_.value="add",d.value=!0}function T(b){n.value=Object.assign({},b),_.value="update",d.value=!0}function M(){k.$refs.dataFormRef.validate(async b=>{if(b){let t=await k.$api.sys_tenant[n.value.id?"update":"add"](n.value);k.submitOk(t.message),w(),d.value=!1}})}return(b,t)=>{const N=u("base-input"),f=u("el-button"),$=u("base-header"),s=u("el-table-column"),j=u("base-tag"),v=u("el-tag"),B=u("base-table-p"),F=u("base-content"),V=u("base-input-no"),m=u("el-form-item"),O=u("base-radio-group"),I=u("el-date-picker"),U=u("el-input-number"),S=u("base-select"),A=u("el-form"),Q=u("base-dialog"),q=u("base-wrapper");return r(),i(q,null,{default:o(()=>[e($,null,{default:o(()=>[e(N,{modelValue:a(y).name,"onUpdate:modelValue":t[0]||(t[0]=l=>a(y).name=l),label:"租户名",onClear:w},null,8,["modelValue"]),e(f,{type:"primary",onClick:w},{default:o(()=>[p("查询")]),_:1}),e(f,{type:"success",onClick:R},{default:o(()=>[p("添加")]),_:1})]),_:1}),e(F,null,{default:o(()=>[e(B,{ref:"baseTableRef",api:"sys_tenant.page",params:a(y),indexCode:!0},{default:o(()=>[e(s,{label:"租户ID",prop:"id"}),e(s,{label:"租户名",prop:"name"}),e(s,{label:"管理员",prop:"admin_name"}),e(s,{label:"管理员手机号",prop:"admin_phone"}),e(s,{label:"状态",prop:"status"},{default:o(l=>[e(j,{modelValue:l.row.status,"onUpdate:modelValue":x=>l.row.status=x},null,8,["modelValue","onUpdate:modelValue"])]),_:1}),e(s,{label:"过期时间",prop:"expire_time"},{default:o(l=>[l.row.expire_time?(r(),i(v,{key:0,type:"warning"},{default:o(()=>[p(C(l.row.expire_time),1)]),_:2},1024)):g("",!0)]),_:1}),e(s,{label:"账号数量",prop:"account_count"},{default:o(l=>[l.row.account_count?(r(),i(v,{key:0,type:"success"},{default:o(()=>[p(C(l.row.account_count),1)]),_:2},1024)):g("",!0)]),_:1}),e(s,{label:"租户套餐",prop:"package_id"}),e(s,{label:"创建时间",prop:"expire_time"}),e(s,{width:"150px",label:"操作"},{default:o(l=>[e(f,{link:"",onClick:x=>T(l.row)},{default:o(()=>[p("编辑")]),_:2},1032,["onClick"])]),_:1})]),_:1},8,["params"])]),_:1}),e(Q,{modelValue:a(d),"onUpdate:modelValue":t[12]||(t[12]=l=>Y(d)?d.value=l:d=l),title:b.dialogTitleObj[a(_)],width:"23%"},{footer:o(()=>[e(f,{onClick:t[11]||(t[11]=l=>Y(d)?d.value=!1:d=!1)},{default:o(()=>[p("取 消")]),_:1}),e(f,{type:"primary",onClick:M},{default:o(()=>[p("确 定")]),_:1})]),default:o(()=>[E("div",G,[e(A,{ref:"dataFormRef",model:a(n),rules:a(D),"label-width":"130px"},{default:o(()=>[e(m,{label:"租户名:"},{default:o(()=>[e(V,{modelValue:a(n).name,"onUpdate:modelValue":t[1]||(t[1]=l=>a(n).name=l)},null,8,["modelValue"])]),_:1}),e(m,{label:"租户状态:"},{default:o(()=>[e(O,{modelValue:a(n).status,"onUpdate:modelValue":t[2]||(t[2]=l=>a(n).status=l)},null,8,["modelValue"])]),_:1}),e(m,{label:"过期时间:"},{default:o(()=>[e(I,{modelValue:a(n).expire_time,"onUpdate:modelValue":t[3]||(t[3]=l=>a(n).expire_time=l),type:"datetime",placeholder:"请选择",format:"YYYY-MM-DD hh:mm:ss","value-format":"YYYY-MM-DD hh:mm:ss"},null,8,["modelValue"])]),_:1}),e(m,{label:"账号数量:"},{default:o(()=>[e(U,{modelValue:a(n).account_count,"onUpdate:modelValue":t[4]||(t[4]=l=>a(n).account_count=l),min:1,"controls-position":"right",placeholder:"请输入"},null,8,["modelValue"])]),_:1}),e(m,{label:"排序:"},{default:o(()=>[e(U,{modelValue:a(n).sort,"onUpdate:modelValue":t[5]||(t[5]=l=>a(n).sort=l),min:1,"controls-position":"right",placeholder:"请输入"},null,8,["modelValue"])]),_:1}),e(m,{label:"租户套餐:"},{default:o(()=>[a(d)?(r(),i(S,{key:0,modelValue:a(n).package_id,"onUpdate:modelValue":t[6]||(t[6]=l=>a(n).package_id=l),"option-props":{label:"name",value:"id"},api:"sys_tenant_package.list"},null,8,["modelValue"])):g("",!0)]),_:1}),e(m,{label:"管理员名称:"},{default:o(()=>[e(V,{modelValue:a(n).admin_name,"onUpdate:modelValue":t[7]||(t[7]=l=>a(n).admin_name=l)},null,8,["modelValue"])]),_:1}),e(m,{label:"管理员手机号:"},{default:o(()=>[e(V,{modelValue:a(n).admin_phone,"onUpdate:modelValue":t[8]||(t[8]=l=>a(n).admin_phone=l)},null,8,["modelValue"])]),_:1}),a(_)=="add"?(r(),i(m,{key:0,label:"账号:"},{default:o(()=>[e(V,{modelValue:a(n).username,"onUpdate:modelValue":t[9]||(t[9]=l=>a(n).username=l)},null,8,["modelValue"])]),_:1})):g("",!0),a(_)=="add"?(r(),i(m,{key:1,label:"密码:"},{default:o(()=>[e(V,{modelValue:a(n).password,"onUpdate:modelValue":t[10]||(t[10]=l=>a(n).password=l)},null,8,["modelValue"])]),_:1})):g("",!0)]),_:1},8,["model","rules"])])]),_:1},8,["modelValue","title"])]),_:1})}}};export{K as default};
