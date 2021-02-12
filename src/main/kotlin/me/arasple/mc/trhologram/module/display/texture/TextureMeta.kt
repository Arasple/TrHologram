package me.arasple.mc.trhologram.module.display.texture

/**
 * @author Arasple
 * @date 2021/1/24 12:15
 *
 */
enum class TextureMeta(val regex: Regex) {

    DATA_VALUE("(?i)[<{]data-?value[:=](\\d+?)[>}]"),

    MODEL_DATA("(?i)[<{]model-?data[:=](\\d+?)[>}]"),

    LEATHER_DYE("(?i)[<{]dye[:=](\\d{3},\\d{3},\\d{3})[>}]");

    constructor(regex: String) : this(regex.toRegex())

}