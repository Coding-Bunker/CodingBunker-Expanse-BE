package it.codingbunker.tbs

import react.dom.render


fun main() {
    kotlinx.browser.document.getElementById("root")?.let {
        render(it) {
            child(app)
        }
    }
}