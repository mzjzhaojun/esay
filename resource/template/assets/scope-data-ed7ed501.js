import{g as Q,r as m,o as q,a as s,b as v,c as k,w as o,d as e,u as l,i as f,n as G,k as w,m as H,j as N,z as J}from"./index-3e58188c.js";const K={key:0},Y={__name:"scope-data",setup(W){const{proxy:p}=Q();let i=m({}),n=m({}),u=m(!1),y=m(""),g=m([{label:"全部可见",value:1},{label:"本人可见",value:2},{label:"所在部门可见",value:3},{label:"所在部门及子级可见",value:4},{label:"所在角色",value:5},{label:"所在角色以及下级角色",value:6},{label:"自定义",value:10}]),U=m([]),C=m([]);q(()=>{c(),F()});async function c(){let d=await p.$api.sys_scope_data.tree(i.value);U.value=d.body}async function F(){let d=await p.$api.sys_menu.tree({type:1});C.value=d.body}function $(){n.value={scopeType:1,scopeVisibleField:"*"},y.value="add",u.value=!0}function I(d){n.value=Object.assign({},d),y.value="update",u.value=!0}async function O(d){let t=await p.$api.sys_scope_data.delete({id:d.id});c(),p.submitOk(t.message)}function D(){p.$refs.dataFormRef.validate(async d=>{if(d){let t=await p.$api.sys_scope_data[n.value.id?"update":"add"](n.value);p.submitOk(t.message),c(),u.value=!1}})}function R({row:d,rowIndex:t}){return d.children!=null&&d.children.length===0?"success-row":""}return(d,t)=>{const h=s("base-cascader"),S=s("base-input"),T=s("base-select"),V=s("el-button"),j=s("base-header"),b=s("el-table-column"),B=s("el-tag"),L=s("base-delete-btn"),z=s("el-table"),P=s("base-content"),r=s("el-form-item"),_=s("base-input-no"),A=s("el-form"),E=s("base-dialog"),M=s("base-wrapper");return v(),k(M,null,{default:o(()=>[e(j,null,{default:o(()=>[e(h,{modelValue:l(i).menuId,"onUpdate:modelValue":t[0]||(t[0]=a=>l(i).menuId=a),style:{"margin-right":"10px"},clearable:"",label:"菜单",props:{value:"id",label:"name",children:"children",checkStrictly:!0,emitPath:!1},"data-list":l(C)},null,8,["modelValue","data-list"]),e(S,{modelValue:l(i).scopeName,"onUpdate:modelValue":t[1]||(t[1]=a=>l(i).scopeName=a),label:"权限名称",onClear:c},null,8,["modelValue"]),e(T,{modelValue:l(i).scopeType,"onUpdate:modelValue":t[2]||(t[2]=a=>l(i).scopeType=a),style:{"margin-right":"10px"},clearable:"",label:"规则类型","option-props":{label:"label",value:"value"},"data-list":l(g),onClear:c},null,8,["modelValue","data-list"]),e(V,{type:"primary",onClick:c},{default:o(()=>[f("查询")]),_:1}),e(V,{type:"success",onClick:$},{default:o(()=>[f("添加")]),_:1})]),_:1}),e(P,null,{default:o(()=>[e(z,{ref:"baseTableRef",border:"",size:"small","header-cell-style":{background:"#13C3C3",color:"#fff"},"row-key":"custom_id","row-class-name":R,"tree-props":{children:"children",hasChildren:"hasChildren"},data:l(U),"default-expand-all":""},{default:o(()=>[e(b,{label:"菜单",align:"left",prop:"menuFullName",width:"300px"}),e(b,{label:"权限名称",prop:"scope_name"}),e(b,{label:"权限字段",prop:"scope_column"}),e(b,{label:"规则类型"},{default:o(a=>[a.row.scope_type?(v(),k(B,{key:0},{default:o(()=>[f(G(l(g).find(x=>x.value==a.row.scope_type).label),1)]),_:2},1024)):w("",!0)]),_:1}),e(b,{label:"创建时间",prop:"create_time"}),e(b,{label:"操作"},{default:o(a=>[a.row.children==null?(v(),H("div",K,[e(V,{link:"",onClick:x=>I(a.row)},{default:o(()=>[f("编辑")]),_:2},1032,["onClick"]),e(L,{onOk:x=>O(a.row)},null,8,["onOk"])])):w("",!0)]),_:1})]),_:1},8,["data"])]),_:1}),e(E,{modelValue:l(u),"onUpdate:modelValue":t[12]||(t[12]=a=>N(u)?u.value=a:u=a),title:d.dialogTitleObj[l(y)],width:"23%"},J({default:o(()=>[l(y)!=="detail"?(v(),k(A,{key:0,ref:"dataFormRef",model:l(n),"label-width":"90px"},{default:o(()=>[e(r,{label:"菜单:"},{default:o(()=>[e(h,{modelValue:l(n).menuId,"onUpdate:modelValue":t[3]||(t[3]=a=>l(n).menuId=a),style:{width:"100%"},clearable:"",props:{value:"id",label:"name",children:"children",checkStrictly:!0,emitPath:!1},"data-list":l(C)},null,8,["modelValue","data-list"])]),_:1}),e(r,{label:"名称:"},{default:o(()=>[e(_,{modelValue:l(n).scopeName,"onUpdate:modelValue":t[4]||(t[4]=a=>l(n).scopeName=a)},null,8,["modelValue"])]),_:1}),e(r,{label:"规则类型:"},{default:o(()=>[e(T,{modelValue:l(n).scopeType,"onUpdate:modelValue":t[5]||(t[5]=a=>l(n).scopeType=a),style:{width:"100%"},clearable:"","option-props":{label:"label",value:"value"},"data-list":l(g)},null,8,["modelValue","data-list"])]),_:1}),e(r,{label:"权限字段:"},{default:o(()=>[e(_,{modelValue:l(n).scopeColumn,"onUpdate:modelValue":t[6]||(t[6]=a=>l(n).scopeColumn=a),placeholder:"eg: dept_id"},null,8,["modelValue"])]),_:1}),e(r,{label:"可见字段:"},{default:o(()=>[e(_,{modelValue:l(n).scopeVisibleField,"onUpdate:modelValue":t[7]||(t[7]=a=>l(n).scopeVisibleField=a),placeholder:"* 标识所有"},null,8,["modelValue"])]),_:1}),e(r,{label:"全权限类名:"},{default:o(()=>[e(_,{modelValue:l(n).scopeClass,"onUpdate:modelValue":t[8]||(t[8]=a=>l(n).scopeClass=a),rows:5,type:"textarea",placeholder:"mapper class 全限定类名 （多个用英文逗号分隔）"},null,8,["modelValue"])]),_:1}),l(n).scopeType==10?(v(),k(r,{key:0,label:"规则值:"},{default:o(()=>[e(_,{modelValue:l(n).scopeValue,"onUpdate:modelValue":t[9]||(t[9]=a=>l(n).scopeValue=a),rows:2,type:"textarea"},null,8,["modelValue"])]),_:1})):w("",!0),e(r,{label:"备注:"},{default:o(()=>[e(_,{modelValue:l(n).remark,"onUpdate:modelValue":t[10]||(t[10]=a=>l(n).remark=a),rows:2,type:"textarea"},null,8,["modelValue"])]),_:1})]),_:1},8,["model"])):w("",!0)]),_:2},[l(y)!=="detail"?{name:"footer",fn:o(()=>[e(V,{onClick:t[11]||(t[11]=a=>N(u)?u.value=!1:u=!1)},{default:o(()=>[f("取 消")]),_:1}),e(V,{type:"primary",onClick:D},{default:o(()=>[f("确 定")]),_:1})]),key:"0"}:void 0]),1032,["modelValue","title"])]),_:1})}}};export{Y as default};