import{g as se,t as ue,r as v,o as de,a as u,v as re,b as r,c as p,w as o,d as t,u as l,x as R,i as f,n as pe,k as g,e as S,j as U,m as ie,F as me,q as _e}from"./index-857f4c92.js";const be=["src"],Ve={__name:"n",setup(fe){const{proxy:m}=se();let x=m.$store.user.useUserStore(),{logout:L}=x,{userObj:T}=ue(x),s=v(!1),i=v({}),n=v({}),c=v(""),$=v({update:"编辑",add:"创建",updatePwd:"更新密码"}),w=v("123456"),j=v({username:[{required:!0,message:"请输入账号",trigger:"blur"}],password:[{required:!0,pattern:/^(\w){6,16}$/,message:"请设置6-16位字母、数字组合"}],nickname:[{required:!0,message:"请输入你昵称",trigger:"blur"}]}),N=v([]);de(()=>{q()});async function q(){let d=await m.$api.sys_dept.tree();N.value=d.body}function B(d){i.value.dept_id=d.id,y()}async function y(){m.$refs.baseTableRef.refresh()}function I(){n.value={sex:0,dept_id:i.value.dept_id},c.value="add",s.value=!0}function M(d){n.value=Object.assign({},d),n.value.password=null,c.value="update",s.value=!0}async function E(d){let a=await m.$api.sys_user.delete(d);m.submitOk(a.messge),y()}async function Q(d){n.value=Object.assign({},d),c.value="updatePwd",w.value="123456",s.value=!0}async function z(){n.value.password=w.value;let d=await m.$api.sys_user.update(n.value);m.submitOk(d.msg,()=>{G()}),s.value=!1}function A(){m.$refs.dataFormRef.validate(async d=>{if(d){let a=await m.$api.sys_user[n.value.id?"update":"add"](n.value);m.submitOk(a.msg),y(),s.value=!1}})}function G(){n.value.id===T.value.id&&L()}return(d,a)=>{const H=u("el-tree"),J=u("base-card"),h=u("base-cascader"),C=u("base-input"),V=u("el-button"),K=u("base-header"),b=u("el-table-column"),W=u("el-tag"),X=u("base-delete-btn"),Y=u("base-table-p"),Z=u("base-content"),ee=u("base-card-no"),k=u("base-input-no"),P=u("base-dialog"),_=u("el-form-item"),le=u("el-option"),ae=u("el-select"),te=u("base-upload-single"),O=u("base-select"),oe=u("el-form"),ne=u("base-wrapper"),D=re("has-perm");return r(),p(ne,{class:"flex"},{default:o(()=>[t(J,{title:"组织架构",style:{width:"200px"}},{default:o(()=>[t(H,{data:l(N),props:{children:"children",label:"name"},"highlight-current":"","default-expand-all":"","expand-on-click-node":!1,onNodeClick:B},null,8,["data"])]),_:1}),t(ee,{title:"用户管理",class:"flex-1"},{default:o(()=>[t(K,null,{right:o(()=>[R((r(),p(V,{type:"primary",onClick:I},{default:o(()=>[f("添加")]),_:1})),[[D,"sys:user:add"]])]),default:o(()=>[t(h,{modelValue:l(i).dept_id,"onUpdate:modelValue":a[0]||(a[0]=e=>l(i).dept_id=e),style:{"margin-right":"10px"},clearable:"",label:"企业",props:{value:"id",label:"name",children:"children",checkStrictly:!0,emitPath:!1},api:"sys_dept.tree"},null,8,["modelValue"]),t(C,{modelValue:l(i).username,"onUpdate:modelValue":a[1]||(a[1]=e=>l(i).username=e),label:"账号",onClear:y},null,8,["modelValue"]),t(C,{modelValue:l(i).nickname,"onUpdate:modelValue":a[2]||(a[2]=e=>l(i).nickname=e),label:"名称 ",onClear:y},null,8,["modelValue"]),t(C,{modelValue:l(i).phone,"onUpdate:modelValue":a[3]||(a[3]=e=>l(i).phone=e),label:"手机号",onClear:y},null,8,["modelValue"]),R((r(),p(V,{type:"primary",onClick:y},{default:o(()=>[f("查询")]),_:1})),[[D,"sys:user:page"]])]),_:1}),t(Z,null,{default:o(()=>[t(Y,{ref:"baseTableRef",api:"sys_user.listPage",params:l(i),indexCode:!0},{default:o(()=>[t(b,{"show-overflow-tooltip":!0,prop:"username",label:"用户账号"}),t(b,{prop:"nickname",label:"用户名称"}),t(b,{prop:"sexName",label:"性别"}),t(b,{prop:"phone",label:"手机号码"}),t(b,{prop:"email",label:"邮箱"}),t(b,{label:"角色","show-overflow-tooltip":!0},{default:o(e=>[e.row.roleNames?(r(),p(W,{key:0},{default:o(()=>[f(pe(e.row.roleNames),1)]),_:2},1024)):g("",!0)]),_:1}),t(b,{label:"头像",prop:"avatarUrl"},{default:o(e=>[S("span",null,[S("img",{src:e.row.avatar_url,alt:"",class:"img-sm"},null,8,be)])]),_:1}),t(b,{"show-overflow-tooltip":!0,prop:"deptName",label:"归属企业"}),t(b,{label:"操作",width:"230"},{default:o(e=>[t(V,{link:"",onClick:F=>M(e.row)},{default:o(()=>[f("编辑")]),_:2},1032,["onClick"]),e.row.isFixed?g("",!0):(r(),p(X,{key:0,onOk:F=>E(e.row.id)},null,8,["onOk"])),t(V,{link:"",onClick:F=>Q(e.row)},{default:o(()=>[f("更新密码")]),_:2},1032,["onClick"])]),_:1})]),_:1},8,["params"])]),_:1})]),_:1}),l(c)==="updatePwd"?(r(),p(P,{key:0,modelValue:l(s),"onUpdate:modelValue":a[7]||(a[7]=e=>U(s)?s.value=e:s=e),title:l($)[l(c)],width:"30%"},{footer:o(()=>[t(V,{onClick:a[5]||(a[5]=e=>U(s)?s.value=!1:s=!1)},{default:o(()=>[f("取消")]),_:1}),t(V,{type:"primary",onClick:a[6]||(a[6]=e=>z())},{default:o(()=>[f("确定")]),_:1})]),default:o(()=>[t(k,{modelValue:l(w),"onUpdate:modelValue":a[4]||(a[4]=e=>U(w)?w.value=e:w=e),placeholder:"请输入密码"},null,8,["modelValue"])]),_:1},8,["modelValue","title"])):(r(),p(P,{key:1,modelValue:l(s),"onUpdate:modelValue":a[19]||(a[19]=e=>U(s)?s.value=e:s=e),title:l($)[l(c)],width:"30%"},{footer:o(()=>[t(V,{onClick:a[18]||(a[18]=e=>U(s)?s.value=!1:s=!1)},{default:o(()=>[f("取消")]),_:1}),t(V,{type:"primary",onClick:A},{default:o(()=>[f("确定")]),_:1})]),default:o(()=>[t(oe,{ref:"dataFormRef",model:l(n),rules:l(j),"label-width":"80px"},{default:o(()=>[t(_,{label:"账号:",prop:"username"},{default:o(()=>[t(k,{modelValue:l(n).username,"onUpdate:modelValue":a[8]||(a[8]=e=>l(n).username=e),disabled:l(c)!="add"},null,8,["modelValue","disabled"])]),_:1}),l(c)==="add"?(r(),p(_,{key:0,label:"密码:",prop:"password"},{default:o(()=>[t(k,{modelValue:l(n).password,"onUpdate:modelValue":a[9]||(a[9]=e=>l(n).password=e)},null,8,["modelValue"])]),_:1})):g("",!0),t(_,{label:"昵称:",prop:"nickname"},{default:o(()=>[t(k,{modelValue:l(n).nickname,"onUpdate:modelValue":a[10]||(a[10]=e=>l(n).nickname=e)},null,8,["modelValue"])]),_:1}),t(_,{label:"性别:",prop:"sex"},{default:o(()=>[t(ae,{modelValue:l(n).sex,"onUpdate:modelValue":a[11]||(a[11]=e=>l(n).sex=e),placeholder:"请选择"},{default:o(()=>[(r(!0),ie(me,null,_e(d.sexList,e=>(r(),p(le,{key:e.value,label:e.name,value:e.value},null,8,["label","value"]))),128))]),_:1},8,["modelValue"])]),_:1}),t(_,{label:"手机号码:",prop:"phone"},{default:o(()=>[t(k,{modelValue:l(n).phone,"onUpdate:modelValue":a[12]||(a[12]=e=>l(n).phone=e)},null,8,["modelValue"])]),_:1}),t(_,{label:"邮箱:",prop:"email"},{default:o(()=>[t(k,{modelValue:l(n).email,"onUpdate:modelValue":a[13]||(a[13]=e=>l(n).email=e)},null,8,["modelValue"])]),_:1}),t(_,{label:"头像:",prop:"avatarUrl"},{default:o(()=>[t(te,{modelValue:l(n).avatar_url,"onUpdate:modelValue":a[14]||(a[14]=e=>l(n).avatar_url=e)},null,8,["modelValue"])]),_:1}),t(_,{label:"归属企业:"},{default:o(()=>[l(s)?(r(),p(h,{key:0,modelValue:l(n).dept_id,"onUpdate:modelValue":a[15]||(a[15]=e=>l(n).dept_id=e),clearable:"",style:{width:"100%"},placeholder:"请选择",props:{value:"id",label:"name",children:"children",checkStrictly:!0,emitPath:!1},api:"sys_dept.tree"},null,8,["modelValue"])):g("",!0)]),_:1}),t(_,{label:"岗位:",prop:"post_idlist"},{default:o(()=>[l(s)?(r(),p(O,{key:0,modelValue:l(n).post_idlist,"onUpdate:modelValue":a[16]||(a[16]=e=>l(n).post_idlist=e),"tag-type":"success",style:{width:"100%"},multiple:"",clearable:"","option-props":{label:"name",value:"id"},api:"sys_post.list"},null,8,["modelValue"])):g("",!0)]),_:1}),t(_,{label:"角色:"},{default:o(()=>[l(s)?(r(),p(O,{key:0,modelValue:l(n).roleIdList,"onUpdate:modelValue":a[17]||(a[17]=e=>l(n).roleIdList=e),"tag-type":"warning",multiple:"",style:{width:"100%"},"option-props":{label:"name",value:"id"},api:"sys_role.list"},null,8,["modelValue"])):g("",!0)]),_:1})]),_:1},8,["model","rules"])]),_:1},8,["modelValue","title"]))]),_:1})}}};export{Ve as default};
