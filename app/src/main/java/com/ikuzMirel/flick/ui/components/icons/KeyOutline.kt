package com.ikuzMirel.flick.ui.components.icons

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Icons.Outlined.KeyOutline: ImageVector
    get() {
        if (_keyOutline != null) {
            return _keyOutline!!
        }
        _keyOutline = ImageVector.Builder(
            name = "Outlined.KeyOutline",
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
            moveTo(14.0F, 27.4F)
            quadTo(12.6F, 27.4F, 11.6F, 26.4F)
            quadTo(10.6F, 25.4F, 10.6F, 24.0F)
            quadTo(10.6F, 22.6F, 11.6F, 21.6F)
            quadTo(12.6F, 20.6F, 14.0F, 20.6F)
            quadTo(15.4F, 20.6F, 16.4F, 21.6F)
            quadTo(17.4F, 22.6F, 17.4F, 24.0F)
            quadTo(17.4F, 25.4F, 16.4F, 26.4F)
            quadTo(15.4F, 27.4F, 14.0F, 27.4F)

            moveTo(14.0F, 36.0F)
            quadTo(9.0F, 36.0F, 5.5F, 32.5F)
            quadTo(2.0F, 29.0F, 2.0F, 24.0F)
            quadTo(2.0F, 19.0F, 5.5F, 15.5F)
            quadTo(9.0F, 12.0F, 14.0F, 12.0F)
            quadTo(17.6F, 12.0F, 20.3F, 13.7F)
            quadTo(23.0F, 15.4F, 24.55F, 18.85F)
            horizontalLineTo(42.35F)
            lineTo(48.0F, 24.5F)
            lineTo(39.65F, 32.15F)
            lineTo(35.25F, 28.95F)
            lineTo(30.85F, 32.15F)
            lineTo(27.1F, 29.15F)
            horizontalLineTo(24.55F)
            quadTo(23.3F, 32.15F, 20.625F, 34.075F)
            quadTo(17.95F, 36.0F, 14.0F, 36.0F)

            moveTo(14.0F, 33.0F)
            quadTo(16.9F, 33.0F, 19.35F, 31.075F)
            quadTo(21.8F, 29.15F, 22.5F, 26.15F)
            horizontalLineTo(28.2F)
            lineTo(30.9F, 28.4F)
            lineTo(35.3F, 25.25F)
            lineTo(39.4F, 28.35F)
            lineTo(43.65F, 24.4F)
            lineTo(41.1F, 21.85F)
            horizontalLineTo(22.5F)
            quadTo(21.9F, 19.05F, 19.5F, 17.025F)
            quadTo(17.1F, 15.0F, 14.0F, 15.0F)
            quadTo(10.25F, 15.0F, 7.625F, 17.625F)
            quadTo(5.0F, 20.25F, 5.0F, 24.0F)
            quadTo(5.0F, 27.75F, 7.625F, 30.375F)
            quadTo(10.25F, 33.0F, 14.0F, 33.0F)
            close()
        }.build()
        return _keyOutline!!
    }
private var _keyOutline: ImageVector? = null