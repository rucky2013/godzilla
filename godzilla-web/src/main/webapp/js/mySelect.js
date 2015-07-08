// JavaScript Document
/*Edit by zhaona6 2015-06-29*/
(function(){
	var bAdd = false;
	window.mySelect =function (sName){
		var oAge = document.getElementsByName(sName)[0];
		var oDiv = document.createElement('div');
		
		
		oDiv.className='mySelect';
		var oH2 = document.createElement('h2');
		
				
		oH2.innerHTML=oAge.options[oAge.selectedIndex].text;
		oDiv.appendChild(oH2);
		
		var oA = document.createElement('a');				
		oDiv.appendChild(oA);
		
		var oUl = document.createElement('ul');
		oA.onclick=function(ev){
			
			var oEvent = ev||event;
			if(oUl.style.display=='block'){
				oUl.style.display='none';
				this.style.backgroundPosition='0px 0px';
			}else{
				oUl.style.display='block';
				this.style.backgroundPosition='0px -26px';
			}
			oEvent.cancelBubble=true;
		};
		
		document.onclick=function(){
			oUl.style.display='none';
			oA.style.backgroundPosition='0px 0px';
		};
		
		for(var i=0;i<oAge.options.length;i++){
			var oLi = document.createElement('li');
			oLi.innerHTML=oAge.options[i].text;
			oLi.onmouseover=function(){
				this.style.background='#f7f8fa';
			};
			oLi.onmouseout=function(){
				this.style.background='';
			};
			(function(index){
				oLi.onclick=function(){
					oH2.innerHTML=this.innerHTML;
					oUl.style.display='none';
					oAge.selectedIndex=index;
				};
			})(i);
			oUl.appendChild(oLi);
		}
		oDiv.appendChild(oUl);
		
		oAge.parentNode.insertBefore(oDiv,oAge);
		oAge.style.display='none';
		if(bAdd)return;
		bAdd=true;
		var oLink = document.createElement('link');
		oLink.rel='stylesheet';
		oLink.href='css/mySelect.css';
		var oHead = document.getElementsByTagName('head')[0];
		oHead.appendChild(oLink);
	}
})();