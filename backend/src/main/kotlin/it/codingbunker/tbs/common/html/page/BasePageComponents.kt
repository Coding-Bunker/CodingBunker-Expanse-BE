package it.codingbunker.tbs.common.html.page

import kotlinx.html.*

@HtmlTagMarker
inline fun HTML.bulmaHead(crossinline block: HEAD.() -> Unit) {
    head {
        styleLink("https://cdn.jsdelivr.net/npm/bulma@0.9.2/css/bulma.min.css")
        styleLink("https://cdn.jsdelivr.net/npm/bulma@0.9.2/css/bulma.min.css")
        meta(name = "viewport", content = "width=device-width, initial-scale=1")
        block()
    }
}

inline fun HTML.baseHtmlBody(crossinline block: BODY.() -> Unit) {
    body {
        block()
        script("module", "https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.esm.js") {}
        script(src = "https://ionic.io/ionicons/usage#:~:text=js%22%3E%3C/script%3E%0A%3Cscript-,nomodule,-src%3D%22https%3A//unpkg") {
            attributes.put("nomodule", "")
        }
    }
}

class IONIC_ICON(consumer: TagConsumer<*>) : HTMLTag(
    "ion-icon", consumer, emptyMap(),
    inlineTag = true,
    emptyTag = true
), HtmlInlineTag {
    fun name(iconName: String) {
        attributes["name"] = iconName
    }
}

inline fun HTMLTag.ionicIcon(crossinline block: IONIC_ICON.() -> Unit) {
    IONIC_ICON(consumer).visit(block)
}
