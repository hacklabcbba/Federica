$(document).ready(function(){
    var $loginBox = $("#login-box");
    $("#login a").click(function(e){
        e.stopPropagation();
        $loginBox.slideDown();
    });

    $("html").click(function(e){
        var clickOver_loginBox = $(e.target).attr("id") == $loginBox.attr("id");
        var clickOverChild_LoginBox =  $(e.target).parents("#login-box").length > 0;
        if (clickOver_loginBox || clickOverChild_LoginBox ){
            e.stopPropagation();
        }
        else {
            $loginBox.slideUp();
        }
    });
    $(document).keydown(function(e){
        if ( e.keyCode == "27" ) {
            $loginBox.slideUp();
        }
    });
});