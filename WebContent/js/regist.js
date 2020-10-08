$(function(){

    function checkChange(){
        var token = "";
        var l = 20;
        var c = "abcdefghijklmnopqrstuvwxyz0123456789";
        var cl = c.length;
        for(var i=0; i<l; i++){
          token += c[Math.floor(Math.random()*cl)];
        }

        $('#token').val(token);
//        console.log($('#token').val());
    }

    function setEvents(){
        $('input').off('change');
        $('input').on('change', checkChange);
        $('textarea').off('change');
        $('textarea').on('change', checkChange);
    }

    setEvents();

});