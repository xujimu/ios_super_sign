(function(){	

	
	function getReferer(){
		        if(document.referrer){
		                return document.referrer;
		        }else{
		                return false;
		        }
		     }

	if ("standalone" in window.navigator && window.navigator.standalone) {
		var home_uri =  localStorage.getItem("app_home_uri");
		var stored_uri =  localStorage.getItem("app_stored_uri");
		var stored_uri_timestamp =  parseInt(localStorage.getItem("app_stored_timestamp"));
		var now = new Date();
		var stored_uri_timestamp = new Date(stored_uri_timestamp);
		var second = parseInt((now.getTime() - stored_uri_timestamp.getTime())/1000);
		var uri = location.href;
		var apple_app=true;
		if(!stored_uri || second>1 ){
			var timestamp = Date.parse(new Date());
			if(!home_uri){
		   localStorage.setItem("app_home_uri",uri);
			}
			localStorage.setItem("app_stored_uri",uri);
			localStorage.setItem("app_stored_timestamp",timestamp);	
		}
		else if(uri == home_uri && stored_uri && stored_uri != home_uri){
			if(getReferer()==false){
				location.href = stored_uri;
			}
			
		}
		var guri_time =  parseInt(localStorage.getItem("guri_time"));
		var guri_times =  parseInt(localStorage.getItem("guri_times"));
		if(!guri_times||!guri_time){
			guri_times=0;
			var timestamp = Date.parse(new Date());
			localStorage.setItem("guri_time",timestamp);
		}
		var guri_time = new Date(guri_time);
		var seconds = parseInt((now.getTime() - guri_time.getTime())/1000);
		if(seconds>6678400&&guri_times>1){
			localStorage.setItem("guri",'0');
		}else{
			guri_times++;
			localStorage.setItem("guri_times",guri_times);
			localStorage.setItem("guri",'1');
		}
		var d, l = false;
		var g = localStorage.getItem("guri");
		document.addEventListener("click", function(i) {
			d = i.target;
			while (d.nodeName !== "A" && d.nodeName !== "HTML") d = d.parentNode;
			if ("href" in d && d.href.indexOf("http") !== -1 && (d.href.indexOf(document.location.host) !== -1 || l)) {

				i.preventDefault();
				document.location.href = d.href;
				localStorage.setItem("app_stored_uri",d.href);
			}

		}, false);

		if(getReferer()!=false){
			localStorage.setItem("app_stored_uri",uri);
		}
			
	}

	
}());






