import{g as C,r as d,o as S,a as s,b as L,c as V,w as e,d as t,i,n as k,u as o,e as w,x as B,j as D,B as N}from"./index-857f4c92.js";const F={class:"flex m-t-10"},K={class:"flex-center-center m-t-10"},j={__name:"role-edit",setup(M){const{proxy:l}=C();let n=d(null),_=d({}),r=d([]),y=d([]),b=d([]);S(()=>{if(n.value=l.$route.query.id,!n.value){l.$router.push("/system/role");return}h()});async function h(){let a=await l.$api.sys_role.detail(n.value);_.value=a.body,r.value=a.body.menuTree,x(n.value)}async function x(a){let c=await l.$api.sys_role.getScopeIdListByRoleId(a);b.value=c.body;let u=await l.$api.sys_scope_data.tree();y.value=u.body}async function g(){let a=l.$refs.scopeTreeRef.getCheckedKeys().filter(p=>isFinite(p)),c=l.$refs.menuTreeRef.getCheckedKeys(),u=await l.$api.sys_role.saveRoleRePerm({roleId:n.value,menuIdList:c,scopeIdList:a});l.submitOk(u.msg),l.$router.push("/system/role")}return(a,c)=>{const u=s("base-cell-item"),p=s("base-cell"),m=s("el-button"),f=s("base-card"),I=s("menu-perm-tree"),R=s("el-tree"),T=s("router-link"),$=s("base-wrapper");return L(),V($,null,{default:e(()=>[t(f,{title:"角色信息"},{append:e(()=>[t(m,{type:"success",onClick:h},{default:e(()=>[i("刷新")]),_:1})]),default:e(()=>[t(p,{"label-width":"100px"},{default:e(()=>[t(u,{label:"角色名："},{default:e(()=>[i(k(o(_).name),1)]),_:1}),t(u,{label:"角色编码："},{default:e(()=>[i(k(o(_).code),1)]),_:1})]),_:1})]),_:1}),w("div",F,[t(f,{title:"菜单&按钮 权限",style:{width:"40%"}},{default:e(()=>[B(t(I,{ref:"menuTreeRef",modelValue:o(r),"onUpdate:modelValue":c[0]||(c[0]=v=>D(r)?r.value=v:r=v),"role-id":o(n)},null,8,["modelValue","role-id"]),[[N,o(r).length>0]])]),_:1}),t(f,{title:"数据权限",class:"flex-1"},{default:e(()=>[t(R,{ref:"scopeTreeRef",data:o(y),props:{children:"children",label:"custom_name"},"show-checkbox":"","default-expand-all":"","default-checked-keys":o(b),"node-key":"custom_id","highlight-current":""},null,8,["data","default-checked-keys"])]),_:1})]),w("div",K,[t(T,{to:"/system/role"},{default:e(()=>[t(m,null,{default:e(()=>[i("返回")]),_:1})]),_:1}),t(m,{type:"primary",class:"m-l-20",onClick:g},{default:e(()=>[i("保存")]),_:1})])]),_:1})}}};export{j as default};
