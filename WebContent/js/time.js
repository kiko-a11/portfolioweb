function pad(c) {
    var ret = "";
    if (c < 10) {
        ret = '0' + c;
    }
    else {
        ret = c;
    }
    return ret;
}
window.addEventListener('load', function clock() {
    var now = new Date();

    var y = now.getFullYear();
    var m = pad(now.getMonth() + 1);
    var d = pad(now.getDate());
    var h = pad(now.getHours());
    var mi = pad(now.getMinutes());
    var timeElement = document.getElementById("time");
    if (timeElement) {
        timeElement.innerHTML = y + "/" + m + "/" + d + " " + h + ":" + mi
        setTimeout(clock, 1000)
    }
});
