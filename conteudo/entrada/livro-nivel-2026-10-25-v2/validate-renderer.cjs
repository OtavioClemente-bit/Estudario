const fs=require('node:fs'),vm=require('node:vm'),assert=require('node:assert/strict');
const reduced={matches:false};let component;let texts=[],draws=0;
function finite(args){for(const value of args)if(typeof value==='number')assert.ok(Number.isFinite(value));}
class Path2D{constructor(value){if(value!=null)assert.equal(typeof value,'string');}moveTo(...a){finite(a)}lineTo(...a){finite(a)}quadraticCurveTo(...a){finite(a)}bezierCurveTo(...a){finite(a)}closePath(){}}
const customElements={get:()=>undefined,define:(name,type)=>{component=type;}};
const sandbox={window:{customElements,matchMedia:()=>reduced},customElements,HTMLElement:class{},Path2D,Math,Map,Set,console};
vm.createContext(sandbox);
let source=fs.readFileSync('web/src/jsMain/resources/profile-art.js','utf8').replace('  customElements.define("estudario-profile-art",ProfileArt);','  globalThis.bookQA={draw:book,paths}; customElements.define("estudario-profile-art",ProfileArt);');
vm.runInContext(source,sandbox);
const canvas=new Proxy({globalAlpha:1,fillText(text,...a){assert.match(text,/^\d+$/);texts.push(text);finite(a);},save(){},restore(){}},{get(target,key){if(key in target)return target[key];return (...a)=>{finite(a);draws++;};}});
const levels=[1,4,5,7,10,20,30,40,50,99,100,150,200];
for(const level of levels){texts=[];sandbox.bookQA.draw(canvas,level,1.2,1);assert.deepEqual(texts,[String(level)]);}
const paths=sandbox.bookQA.paths.size;
for(let frame=0;frame<4000;frame++)sandbox.bookQA.draw(canvas,levels[frame%levels.length],frame*.08,(frame%100)/100);
assert.equal(sandbox.bookQA.paths.size,paths,'animation must not grow the static path cache');
reduced.matches=true;
assert.equal(component.prototype._needsAnimation.call({_kind:'level'}),false);
assert.equal(component.prototype._tap.call({}),1);
console.log(JSON.stringify({levels:levels.length,frames:4000,staticPaths:paths,numberOnly:true,reducedMotion:true,finiteCoordinates:true}));
