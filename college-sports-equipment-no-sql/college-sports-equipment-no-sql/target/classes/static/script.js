const api='/api';
const $=id=>document.getElementById(id);
async function call(path,options={}){const response=await fetch(api+path,{headers:{'Content-Type':'application/json'},...options});const body=await response.json().catch(()=>({}));if(!response.ok)throw new Error(body.message||'Something went wrong');return body;}
function error(e){alert(e.message);}
async function refresh(){try{
 const [items,records,groups]=await Promise.all([call('/equipment'),call('/history'),call('/summary')]);
 $('types').textContent=items.length;
 $('available').textContent=items.reduce((n,x)=>n+x.availableQuantity,0);
 $('issued').textContent=items.reduce((n,x)=>n+x.issuedQuantity,0);
 $('damaged').textContent=items.reduce((n,x)=>n+x.damagedQuantity,0);
 $('inventory').innerHTML=items.map(x=>`<tr><td>${x.id}</td><td>${x.name}</td><td>${x.sport}</td><td>${x.totalQuantity}</td><td>${x.availableQuantity}</td><td>${x.issuedQuantity}</td><td>${x.damagedQuantity}</td></tr>`).join('');
 $('equipmentId').innerHTML=items.map(x=>`<option value="${x.id}">${x.name} (${x.sport}) - available ${x.availableQuantity}</option>`).join('');
 $('issueId').innerHTML=records.filter(x=>x.issuedQuantity>x.returnedQuantity).map(x=>`<option value="${x.id}">#${x.id} ${x.studentName} - ${x.equipmentName}, pending ${x.issuedQuantity-x.returnedQuantity}</option>`).join('');
 $('history').innerHTML=records.map(x=>`<tr><td>${x.id}</td><td>${x.studentName}</td><td>${x.studentRollNo}</td><td>${x.equipmentName}</td><td>${x.issuedQuantity}</td><td>${x.returnedQuantity}</td><td>${x.damagedQuantity}</td><td>${x.issuedQuantity-x.returnedQuantity}</td></tr>`).join('');
 $('summary').innerHTML=groups.map(x=>`<tr><td>${x.sport}</td><td>${x.available}</td><td>${x.issued}</td><td>${x.damaged}</td></tr>`).join('');
 }catch(e){error(e)}}
$('addForm').addEventListener('submit',async ev=>{ev.preventDefault();try{await call('/equipment',{method:'POST',body:JSON.stringify({name:$('equipmentName').value,sport:$('sportName').value,quantity:Number($('totalQty').value)})});ev.target.reset();await refresh();}catch(e){error(e)}});
$('issueForm').addEventListener('submit',async ev=>{ev.preventDefault();try{await call('/issue',{method:'POST',body:JSON.stringify({equipmentId:Number($('equipmentId').value),studentName:$('studentName').value,studentRollNo:$('rollNo').value,quantity:Number($('issueQty').value)})});ev.target.reset();await refresh();}catch(e){error(e)}});
$('returnForm').addEventListener('submit',async ev=>{ev.preventDefault();try{await call('/return/'+$('issueId').value,{method:'POST',body:JSON.stringify({quantity:Number($('returnQty').value),damagedQuantity:Number($('damagedQty').value)})});ev.target.reset();await refresh();}catch(e){error(e)}});
refresh();