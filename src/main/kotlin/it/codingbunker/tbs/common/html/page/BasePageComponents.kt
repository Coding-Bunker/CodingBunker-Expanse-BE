package it.codingbunker.tbs.common.html.page

import kotlinx.html.*

@HtmlTagMarker
inline fun HTML.bulmaHead(crossinline block: HEAD.() -> Unit = {}) {
    head {
        styleLink("https://cdn.jsdelivr.net/npm/bulma@0.9.2/css/bulma.min.css")
        meta(name = "viewport", content = "width=device-width, initial-scale=1")
        block()
    }
}
