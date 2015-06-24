$(document).ready(function() {

    $('#carousel-areas').carousel({
        interval: 500000
    });

    $('#gs-carousel-home').carousel({
        interval: 5000
    });

    $('#gs-carousel-mobile').carousel({
        interval: 4000
    });

    //SIDEBAR RIGHT
    $('#nav-expander').on('click',function(e){
        e.preventDefault();
        $('body').toggleClass('nav-expanded');
    });

    $('#nav-expander2').on('click',function(e){
        e.preventDefault();
        $('body').toggleClass('nav-expanded2');
    });

    // dropdowns from forms
    $(".select2").select2({ allowClear: true});

    // panel left expand and collapse
    ExpandManger.initControls();

    // Metis MEnu
    $('#menu').metisMenu();
    $('#menu2').metisMenu();
    $('#menu3').metisMenu();

    // Login dropdown close
    $('.dropdown-toggle').dropdown();
    $('.dropdown-menu').find('form').click(function (e) {
        e.stopPropagation();
    });

    // select and diselect checkbox table
    $('#checkbox-all').click(function(){
        var $mainCheckbox = $(this);
        var mainIsChecked = $mainCheckbox.is(':checked');
        $('.checkbox-list').each(function(i, elem){
            var $childCheckbox = $(elem);
            if (mainIsChecked){
                if(!$childCheckbox.is(':checked')){
                    $childCheckbox.click();
                }
            }else{
                if($childCheckbox.is(':checked')){
                    $childCheckbox.click();
                }
            }
        });
    });
});

var ExpandManger = {

    isExpandedLeftPanel: function() {
        return $('body').hasClass('nav-expanded2');
    },
    showLeftPanel: function() {
        $('body').toggleClass('nav-expanded2');
    },
    hideLeftPanel: function() {
        $('body').removeClass('nav-expanded2');
    },
    initControls: function() {
        var self = this;
        $('ul.sideways li').click(function() {
            var shouldIClosePanel = $(this).hasClass('active');
            if (self.isExpandedLeftPanel()) {
                if (shouldIClosePanel) {
                    self.hideLeftPanel();
                }
            } else {
                self.showLeftPanel();
            }
        });
    }
};

var showUploadedFile = {

}



