modules = {
    application {
        resource url:'js/application.js'
    }

    'sticky-menu' {
        resource url: 'js/sticky-menu.js'
    }

    'weekdays' {
        resource url: 'js/jquery.weekdays.js'
    }

    'opentele-scroll' {
        resource url: 'js/opentele.scroll.js'
        dependsOn 'jquery-livequery'
    }

    'jquery-livequery' {
        resource url: 'js/jquery.livequery.min.js'
    }
}
