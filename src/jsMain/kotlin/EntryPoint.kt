import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {
    kotlinext.js.require("./app.css")
    
    val welcome = RevercoView.create()
    createRoot(document.body!!).render(welcome)
}