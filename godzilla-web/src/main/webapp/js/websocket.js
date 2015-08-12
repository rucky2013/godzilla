var isEcho = false;
var server_host = "ws://10.100.142.65:9999/websocket"
var socket;
var showlength = 20;
if (isEcho &&!window.WebSocket) {
  window.WebSocket = window.MozWebSocket;
}
var lock = false;
if (isEcho &&window.WebSocket) {
  socket = new WebSocket(server_host);
  socket.onmessage = function(event) {
	//var length = $("#messagebox").find("div").length;
	//if(length>showlength) {
	//	$("#messagebox").empty();
	//}
	$("#messagebox").text($("#messagebox").text()+ ' \r\n' + event.data);
	//始终显示最后一行
	var psconsole = $('#messagebox');
    if(psconsole.length)
       psconsole.scrollTop(psconsole[0].scrollHeight - psconsole.height());
  };
  socket.onopen = function(event) {
    /*var ta = document.getElementById('messagebox');
    ta.value = "Web Socket opened!";*/
	// alert("open");
  };
  socket.onclose = function(event) {
    /*var ta = document.getElementById('messagebox');
    ta.value = ta.value + "Web Socket closed"; */
	 // alert("closed");
  };
} else if(isEcho){
  alert("Your browser does not support Web Socket.");
}

function send(message) {
  if (!isEcho&&!window.WebSocket) { return; }
  if (socket.readyState == WebSocket.OPEN) {
    socket.send(message);
  } else {
    alert("The socket is not open.");
  }
}