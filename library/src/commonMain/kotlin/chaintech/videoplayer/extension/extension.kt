package chaintech.videoplayer.extension

import chaintech.videoplayer.util.formatInterval
import chaintech.videoplayer.util.formatMinSec

internal fun Int.formatMinSec(): String {
    return formatMinSec(this)
}

internal fun Int.formattedInterval(): Int {
    return  formatInterval(this)
}