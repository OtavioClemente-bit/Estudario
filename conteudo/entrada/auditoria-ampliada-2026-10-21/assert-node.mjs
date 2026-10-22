import nodeAssert from 'node:assert/strict';
export const assert=(value,message)=>nodeAssert.ok(value,message);
export const assertEquals=(actual,expected,message)=>nodeAssert.deepStrictEqual(actual,expected,message);
export function assertThrows(fn,ErrorClass,includes){
  let error;
  try{fn();}catch(e){error=e;}
  nodeAssert.ok(error,'Era esperado erro');
  if(ErrorClass)nodeAssert.ok(error instanceof ErrorClass);
  if(includes)nodeAssert.ok(error.message.includes(includes));
  return error;
}
