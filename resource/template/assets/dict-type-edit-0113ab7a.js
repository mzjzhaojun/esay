import{g as C,r as i,a as n,b as U,c as j,w as d,d as u,j as c,i as b,u as a}from"./index-857f4c92.js";const $={__name:"dict-type-edit",setup(O,{expose:g}){const{proxy:r}=C();let t=i(!1),l=i({}),p=i(""),V=i({name:[{required:!0,message:"类型不得为空",trigger:"blur"}],code:[{required:!0,message:"编码不得为空",trigger:"blur"}]});g({open:v});function v(s,e){p.value=s,s==="add"?(l.value={},l.value.status=!0):s==="update"&&(l.value=Object.assign({},e)),t.value=!0}async function x(){r.$refs.dataFormRef.validate(async s=>{if(s){let e=await r.$api.sys_dict_type[l.value.id?"update":"add"](l.value);r.$emit("save-succ"),t.value=!1,r.submitOk(e.msg)}})}return(s,e)=>{const f=n("el-input"),m=n("el-form-item"),y=n("base-radio-group"),k=n("el-form"),_=n("el-button"),w=n("base-dialog");return U(),j(w,{modelValue:a(t),"onUpdate:modelValue":e[4]||(e[4]=o=>c(t)?t.value=o:t=o),title:s.dialogTitleObj[a(p)],width:"25%"},{footer:d(()=>[u(_,{onClick:e[3]||(e[3]=o=>c(t)?t.value=!1:t=!1)},{default:d(()=>[b("取 消")]),_:1}),u(_,{type:"primary",onClick:x},{default:d(()=>[b("确 定")]),_:1})]),default:d(()=>[u(k,{ref:"dataFormRef",model:a(l),rules:a(V),"label-width":"125px"},{default:d(()=>[u(m,{label:"字典类型名称：",prop:"name"},{default:d(()=>[u(f,{modelValue:a(l).name,"onUpdate:modelValue":e[0]||(e[0]=o=>a(l).name=o),placeholder:"输入字典类型名称"},null,8,["modelValue"])]),_:1}),u(m,{label:"字典类型编码：",prop:"code"},{default:d(()=>[u(f,{modelValue:a(l).code,"onUpdate:modelValue":e[1]||(e[1]=o=>a(l).code=o),disabled:a(l).id,placeholder:"输入字典类型编码"},null,8,["modelValue","disabled"])]),_:1}),u(m,{label:"状态："},{default:d(()=>[u(y,{modelValue:a(l).status,"onUpdate:modelValue":e[2]||(e[2]=o=>a(l).status=o)},null,8,["modelValue"])]),_:1})]),_:1},8,["model","rules"])]),_:1},8,["modelValue","title"])}}};export{$ as default};
