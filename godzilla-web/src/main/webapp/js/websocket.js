var server_host = "ws://localhost:9999/websocket"
var socket;
var showlength = 10;
if (!window.WebSocket) {
  window.WebSocket = window.MozWebSocket;
}
if (window.WebSocket) {
  socket = new WebSocket(server_host);
  socket.onmessage = function(event) {
	var length = $("#messagebox").find("div").length;
	while(length>showlength) {
		$("#messagebox").find("div：eq(0)").remove();
	}
	$("#messagebox").append('<div>' + event.data + '</div>');
  };
  socket.onopen = function(event) {
    /*var ta = document.getElementById('messagebox');
    ta.value = "Web Socket opened!";*/
	 alert("open");
  };
  socket.onclose = function(event) {
    /*var ta = document.getElementById('messagebox');
    ta.value = ta.value + "Web Socket closed"; */
	  alert("closed");
  };
} else {
  alert("Your browser does not support Web Socket.");
}

function send(message) {
  if (!window.WebSocket) { return; }
  if (socket.readyState == WebSocket.OPEN) {
    socket.send(message);
  } else {
    alert("The socket is not open.");
  }
}