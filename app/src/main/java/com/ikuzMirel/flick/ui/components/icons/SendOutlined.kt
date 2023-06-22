package com.ikuzMirel.flick.ui.components.icons

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Outlined.SendOutlined: ImageVector
    get() {
        if (_sendOutlined != null) {
            return _sendOutlined!!
        }
        _sendOutlined = ImageVector.Builder(
            name = "SendOutlined",
            defaultWidth = 48.0.dp,
            defaultHeight = 48.0.dp,
            viewportWidth = 48.0F,
            viewportHeight = 48.0F,
        ).path(
            fill = SolidColor(Color(0xFFFFFFFF)),
            fillAlpha = 1.0F,
            strokeAlpha = 1.0F,
            strokeLineWidth = 0.0F,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0F,
            pathFillType = PathFillType.NonZero,
        ) {
            moveTo(6.0F, 40.0F)
            verticalLineTo(8.0F)
            lineTo(44.0F, 24.0F)

            moveTo(9.0F, 35.35F)
            lineTo(36.2F, 24.0F)
            lineTo(9.0F, 12.5F)
            verticalLineTo(20.9F)
            lineTo(21.1F, 24.0F)
            lineTo(9.0F, 27.0F)

            moveTo(9.0F, 35.35F)
            verticalLineTo(24.0F)
            verticalLineTo(12.5F)
            verticalLineTo(20.9F)
            verticalLineTo(27.0F)
            close()
        }.build()
        return _sendOutlined!!
    }
private var _sendOutlined: ImageVector? = null