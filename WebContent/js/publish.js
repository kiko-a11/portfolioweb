$(function(){

    function setAge() {
        if($('#birthday').length) {
            const today = new Date();
            const y1 = today.getFullYear().toString().padStart(4, '0');
            const m1 = (today.getMonth() + 1).toString().padStart(2, '0');
            const d1 = today.getDate().toString().padStart(2, '0');

            const birthday = $('#birthday').html()
            const birthday_s = birthday.split('-');
            var age = Math.floor((Number(y1 + m1 + d1) - Number(birthday_s[0] + birthday_s[1] + birthday_s[2].replace('生',''))) / 10000);
            $('#age').html("(" + age + "歳)");
        }
    }

    $('button.copy-clipboard').click(function() {
        $('input.copy-clipboard').select();
        document.execCommand('copy');
    });

//    $('#header-publish').addClass('active');
    setAge();

    $('input.copy-clipboard').val(location.href);

});