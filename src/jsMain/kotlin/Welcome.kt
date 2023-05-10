import csstype.px
import emotion.react.css
import kotlinx.browser.window
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.w3c.fetch.RequestInit
import org.w3c.files.Blob
import org.w3c.files.FileReader
import org.w3c.files.get
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.input
import react.useState
import kotlin.js.json

external interface BytecodeView : Props {
    var bytecode: MutableList<UByte>
}

val Welcome = FC<BytecodeView> { props ->
    val bytecode by useState(props.bytecode)

    input {
        css {
            marginTop = 5.px
            marginBottom = 5.px
            fontSize = 14.px
        }
        type = InputType.file
        onChange = { event ->
            val fr = FileReader()

            fr.onload = {
                val array = Uint8Array(fr.result as ArrayBuffer)

                window.fetch(
                    "submit", RequestInit(
                        method = "POST",
                        headers = json(
                            "Content-Type" to "application/octet-stream"
                        ),
                        body = array
                    )
                ).then {
                    println(array.unsafeCast<ByteArray>())
                    window.location.href = it.url
                }
            }
            fr.readAsArrayBuffer(event.target.files?.get(0) as Blob)
        }
    }
}