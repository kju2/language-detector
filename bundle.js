var app=function(){"use strict";function t(){}function e(t,e){for(var n in e)t[n]=e[n];return t}function n(t,e){for(var n in e)t[n]=1;return t}function a(t){t()}function s(t,e){t.appendChild(e)}function i(t,e,n){t.insertBefore(e,n)}function r(t){t.parentNode.removeChild(t)}function o(t){return document.createElement(t)}function c(t){return document.createTextNode(t)}function l(t,e,n,a){t.addEventListener(e,n,a)}function u(t,e,n,a){t.removeEventListener(e,n,a)}function d(t,e,n){t.classList[n?"add":"remove"](e)}function h(){return Object.create(null)}function g(t){t._lock=!0,f(t._beforecreate),f(t._oncreate),f(t._aftercreate),t._lock=!1}function f(t){for(;t&&t.length;)t.shift()()}var m={destroy:function(e){this.destroy=t,this.fire("destroy"),this.set=t,this._fragment.d(!1!==e),this._fragment=null,this._state={}},get:function(){return this._state},fire:function(t,e){var n=t in this._handlers&&this._handlers[t].slice();if(n)for(var a=0;a<n.length;a+=1){var s=n[a];if(!s.__calling)try{s.__calling=!0,s.call(this,e)}finally{s.__calling=!1}}},on:function(t,e){var n=this._handlers[t]||(this._handlers[t]=[]);return n.push(e),{cancel:function(){var t=n.indexOf(e);~t&&n.splice(t,1)}}},set:function(t){this._set(e({},t)),this.root._lock||g(this.root)},_recompute:t,_set:function(t){var n=this._state,a={},s=!1;for(var i in t=e(this._staged,t),this._staged={},t)this._differs(t[i],n[i])&&(a[i]=s=!0);s&&(this._state=e(e({},n),t),this._recompute(a,this._state),this._bind&&this._bind(a,this._state),this._fragment&&(this.fire("state",{changed:a,current:this._state,previous:n}),this._fragment.p(a,this._state),this.fire("update",{changed:a,current:this._state,previous:n})))},_stage:function(t){e(this._staged,t)},_mount:function(t,e){this._fragment[this._fragment.i?"i":"m"](t,e||null)},_differs:function(t,e){return t!=t?e==e:t!==e||t&&"object"==typeof t||"function"==typeof t}};const _=["Now, here, you see, it takes all the running you can do, to keep in the same place. If you want to get somewhere else, you must run at least twice as fast as that!","Borra con el codo lo que escribe con la mano.","Damit das Mögliche entsteht, muß immer wieder das Unmögliche versucht werden.","Det spiller ingen rolle hvor sakte du går så lenge du ikke stopper.","Aatelles ei mää aika hukkaa.","À chaque fou plaît sa marotte.","Za dziękuję nic się nie kupuje.","Die middel kan wel erger as die kwaad wees.","Asnjëri nuk mund ti shërbejë dy zotërinj.","حبل الكذب قصير","Արևն ամպի տակ չի մնայ։","Aeg parandab haavad, aga jätab armid.","Загляне сонца i ў наша ваконца.","Wie a zegt moet ook b zeggen.","Al du sinjoroj samtempe oni servi ne povas.","Alles zu seiner Zeit.","אין הנחתום מעיד על עיסתו","जान है तो जहान है","Addig nyújtózkodj, amíg a takaród ér.","Allir vilja herrann vera, en enginn sekkinn bera.","Ada asap ada api.","A cane scottato l 'acqua fredda pare calda.","猿も木から落ちる","A colt you may break but an old horse you never can.","กระต่ายหมายจันทร์","เพื่อนกินหาง่าย เพื่อนตายหายาก",".از این گوش میگیرد, از ان گوش در میکند",".אַ קלוגער פֿאַרשטייט פֿון איין וואָרט צוויי","Алты́нного во́ра ве́шают, а полти́нного че́ствуют.","Alla känner apan, men apan känner ingen.","Altın kılıç her kapıyı açar.","В каламутній воді легко рибу ловити.","Adar o'r unlliw, ehedant i'r unlle.","An cleas a bhíos ag an deaid, bíonn sé ag an mac.","Δώσε τόπο στην οργή."],p=function(t,e,n){if(!(n>2))return fetch("https://k18vr57k5l.execute-api.eu-central-1.amazonaws.com/default/languageDetectorV1",{body:JSON.stringify(e),headers:{"Content-Type":"application/json","x-api-key":"eFU71njyBpYOE0O6NUqW5mk7ZtmpYYO5fCe7s8u3"},method:"POST",mode:"cors"}).then(t=>t.json()).then(a=>{"string"==typeof a?t.set({language:a,isInProgress:!1}):(console.log(JSON.stringify(a)),p(t,e,n+1))}).catch(a=>{console.log(a),p(t,e,n+1)});t.set({language:"",isInProgress:!1})};var v={detectLanguage(t){this.set({isInProgress:!0}),p(this,t,0)},randomSample(){this.set({text:_[Math.floor(Math.random()*Math.floor(_.length))]})},reset(){this.set({text:"",language:"",isInProgress:!1})}};function k(t,e){var n,h,g,f,m,_,p,v,k,b,x,w,N,j,A,I,P,L,z=!1;function O(){z=!0,t.set({text:g.value}),z=!1}function S(n){t.detectLanguage(e.text)}function C(e){t.randomSample()}function R(e){t.reset()}var q=e.showResult&&y(t,e);return{c(){n=o("div"),h=o("div"),g=o("textarea"),f=c("\n"),m=o("div"),_=o("div"),p=o("button"),v=c("Detect\n      Language"),b=c("\n  "),x=o("div"),(w=o("button")).textContent="Random Text Sample",N=c("\n  "),j=o("div"),(A=o("button")).textContent="Reset",I=c("\n"),q&&q.c(),P=document.createComment(""),l(g,"input",O),g.className="textarea",g.placeholder="Please enter a text, e.g. Hello world",g.rows="6",h.className="control",n.className="field",l(p,"click",S),p.className="button is-info",p.disabled=k=0==e.text.trim().length,d(p,"is-loading",e.isInProgress),_.className="control",l(w,"click",C),w.className="button is-grey",x.className="control",l(A,"click",R),A.className="button is-text",j.className="control",m.className="field is-grouped"},m(t,a){i(t,n,a),s(n,h),s(h,g),g.value=e.text,i(t,f,a),i(t,m,a),s(m,_),s(_,p),s(p,v),s(m,b),s(m,x),s(x,w),s(m,N),s(m,j),s(j,A),i(t,I,a),q&&q.m(t,a),i(t,P,a),L=!0},p(n,a){e=a,!z&&n.text&&(g.value=e.text),n.text&&k!==(k=0==e.text.trim().length)&&(p.disabled=k),n.isInProgress&&d(p,"is-loading",e.isInProgress),e.showResult?q?q.p(n,e):((q=y(t,e)).c(),q.m(P.parentNode,P)):q&&(q.d(1),q=null)},i(t,e){L||this.m(t,e)},o:a,d(t){t&&r(n),u(g,"input",O),t&&(r(f),r(m)),u(p,"click",S),u(w,"click",C),u(A,"click",R),t&&r(I),q&&q.d(t),t&&r(P)}}}function y(t,e){var n,a,l,u,d;return{c(){n=o("div"),a=c("This text is in "),l=o("strong"),u=c(e.language),d=c("."),n.className="has-text-left is-size-4"},m(t,e){i(t,n,e),s(n,a),s(n,l),s(l,u),s(n,d)},p(t,e){var n,a;t.language&&(n=u,a=e.language,n.data=""+a)},d(t){t&&r(n)}}}function b(t){!function(t,e){t._handlers=h(),t._slots=h(),t._bind=e._bind,t._staged={},t.options=e,t.root=e.root||t,t.store=e.store||t.root.store,e.root||(t._beforecreate=[],t._oncreate=[],t._aftercreate=[])}(this,t),this._state=e({isInProgress:!1,language:"",text:"Hello World"},t.data),this._recompute({language:1},this._state),this._intro=!!t.intro,this._fragment=k(this,this._state),this.root._oncreate.push(()=>{(function(){const t=document.querySelector(".burger"),e=document.querySelector("#"+t.dataset.target);t.addEventListener("click",function(){t.classList.toggle("is-active"),e.classList.toggle("is-active")})}).call(this),this.fire("update",{changed:n({},this._state),current:this._state})}),t.target&&(this._fragment.c(),this._mount(t.target,t.anchor),g(this)),this._intro=!0}return e(b.prototype,m),e(b.prototype,v),b.prototype._recompute=function(t,e){t.language&&this._differs(e.showResult,e.showResult=function({language:t}){return!!t}(e))&&(t.showResult=!0)},new b({target:document.getElementById("detector"),data:{name:"world"}})}();
