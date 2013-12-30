$(window).ready(function() {

    var MAX_SCROLL = 5;
    var CURRENT_SCROLL_LEVEL = 0;

    var updating = false;

    var appendComics = function() {

        $.get("/random", function(data) {

            newComics = $(data).children();

            $('.images ul').append(newComics);

            // Allow more updates!
            if(CURRENT_SCROLL_LEVEL < MAX_SCROLL) {
                $('.loading').addClass('hidden');
                updating = false;
            } else {
                $('.loading p').text("Want more? Click 'More Random!' below!");
                $('.loading img').addClass('hidden');
            }

        });

    }

    $(window).scroll(function() {

        var scrollDist = $(document).height() - $(window).scrollTop();
        var path = window.location.pathname;

        if(scrollDist < 1000 && updating == false && path == '/') {

            // Ensure not massive surge
            updating = true;
            CURRENT_SCROLL_LEVEL++;
            $('.loading').removeClass('hidden');

            // Loading comics
            appendComics();
        }

    });

});
