import csstype.*
import kotlinx.browser.window
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import kotlinx.serialization.json.encodeToDynamic
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.w3c.fetch.RequestInit
import org.w3c.files.Blob
import org.w3c.files.FileReader
import org.w3c.files.get
import react.FC
import react.Props
import react.dom.aria.ariaHidden
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.p
import react.dom.svg.ReactSVG
import react.dom.svg.ReactSVG.svg
import react.dom.svg.StrokeLinecap
import react.dom.svg.StrokeLinejoin
import react.useState
import kotlin.js.Promise
import kotlin.js.json

val RevercoView = FC<Props> { _ ->
    var bytecode by useState(listOf<Byte>())
    var classStructure by useState<ClassStructure?>(null)

    div {
        className = ClassName("flex dark:bg-slate-800 h-screen")

        div {
            className = ClassName("flex-1")

            for (row in bytecode.chunked(16)) {
                div {
                    className = ClassName("flex")

                    for (byte in row) {
                        div {
                            className = ClassName("flex-1 dark:text-white")

                            +byte.toInt().and(0xFF).toString(16).padStart(2, '0')
                        }
                    }

                    if (row.size != 16) {
                        for (i in 0 until 16 - row.size) {
                            div {
                                className = ClassName("flex-1 dark:text-white")

                                +"--"
                            }
                        }
                    }
                }
            }
        }

        div {
            className = ClassName("flex-1")

            ClassFileInput {
                onSubmit = { resolvedBytecode, resolvedStructure ->
                    bytecode = resolvedBytecode
                    classStructure = resolvedStructure
                }
            }
        }
    }
}

external interface ClassFileInputProps : Props {
    var onSubmit: (List<Byte>, ClassStructure) -> Unit
}

val ClassFileInput = FC<ClassFileInputProps> { props ->
    div {
        div {
            className = ClassName("flex items-center justify-center w-full")

            label {
                className =
                    ClassName("flex flex-col items-center justify-center w-full h-64 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 dark:hover:bg-bray-800 dark:bg-gray-700 hover:bg-gray-100 dark:border-gray-600 dark:hover:border-gray-500 dark:hover:bg-gray-600")

                div {
                    className = ClassName("flex flex-col items-center justify-center pt-5 pb-6")

                    svg {
                        ariaHidden = true
                        className = ClassName("w-10 h-10 mb-3 text-gray-400")
                        fill = "none"
                        stroke = "currentColor"
                        viewBox = "0 0 24 24"
                        xmlns = "http://www.w3.org/2000/svg"

                        ReactSVG.path {
                            strokeLinecap = StrokeLinecap.round
                            strokeLinejoin = StrokeLinejoin.round
                            strokeWidth = 2.0
                            d = "M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"
                        }
                    }

                    p {
                        className = ClassName("mb-2 text-sm text-gray-500 dark:text-gray-400")

                        ReactHTML.span {
                            className = ClassName("font-semibold")

                            +"Click to upload"
                        }

                        +" or drag and drop"
                    }

                    p {
                        className = ClassName("text-xs text-gray-500 dark:text-gray-400")

                        +"SVG, PNG, JPG or GIF (MAX. 800x400px)"
                    }
                }

                input {
                    className = ClassName("hidden")
                    id = "dropzone-file"
                    type = InputType.file

                    onChange = { event ->
                        val fr = FileReader()
                        val file = event.target.files?.get(0)

                        fr.onload = {
                            Promise { resolve, _ ->
                                resolve(Uint8Array(fr.result as ArrayBuffer))
                            }.then {
                                val name = file!!.name
                                val bytes = it.unsafeCast<ByteArray>()
                                val data = ClassFileData(name ,bytes)

                                window.fetch(
                                    "submit", RequestInit(
                                        method = "POST",
                                        headers = json(
                                            "Content-Type" to "application/octet-stream"
                                        ),
                                        body = Json.encodeToDynamic(data)
                                    )
                                ).then { res ->
                                    val classStructure = Json.decodeFromDynamic<ClassStructure>(res.body)

                                    println(res.status)
                                    println(classStructure)

                                    props.onSubmit(it.unsafeCast<ByteArray>().toList(), classStructure)
                                }.catch { err ->
                                    err.printStackTrace()
                                }
                            }.catch { err ->
                                err.printStackTrace()
                            }
                        }
                        fr.readAsArrayBuffer(file as Blob)
                    }
                }
            }
        }
    }
}