import{s as e}from"./index.5ac32ff6.js";const r="/rest/v1/payconfig";var d={page(t,a){return e({url:r+"/page",method:"post",data:t,headers:a})},list(t){return e({url:r+"/list",method:"post",data:t})},add(t){return e({url:r,method:"post",data:t})},update(t){return e({url:r,method:"put",data:t})},delete(t){return e({url:r+"/"+t,method:"delete"})},get(){return e({url:r+"/data",method:"get"})}};export{d as p};