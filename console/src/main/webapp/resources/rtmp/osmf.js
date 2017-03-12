function osmf_play(url, domId, width, height, swfUrl, swfInstallUrl) {

	var flashvars = {};
	flashvars.src = url;
	flashvars.streamType = "live"; // live or recorded
	flashvars.autoPlay = true;
	flashvars.controlBarAutoHide = false;
	flashvars.scaleMode = "stretch";
	flashvars.bufferTime = 0.8;

	var params = {};
	params.allowFullScreen = true;

	var attributes = {};

	swfobject.embedSWF(swfUrl, domId,
			width, height, "11.1",
			swfInstallUrl, flashvars, params, attributes);
}