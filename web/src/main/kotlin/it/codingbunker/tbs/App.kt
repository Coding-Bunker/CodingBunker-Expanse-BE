package it.codingbunker.tbs

import csstype.*
import kotlinx.browser.document
import mui.icons.material.Image
import mui.material.*
import mui.system.BoxProps
import mui.system.SxProps
import mui.system.Theme
import react.Fragment
import react.Props
import react.dom.b
import react.dom.form
import react.dom.img
import react.dom.onChange
import react.fc

val app = fc<Props> {
    Container {
        attrs{
            maxWidth = "xs"
        }

        Box {


            attrs {

                sx?.let {
                    it.marginTop = 16.px
                    it.display = Display.flex
                    it.flexDirection = FlexDirection.column
                }
            }

            AppBar{
                img {
                    attrs.src = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"
                    attrs.width = 200.px.toString()
                }
            }

            TextField {
                attrs.onChange = {
                    println("onChange")
                    println(it.target.asDynamic().value)
                }
            }
        }
    }
}