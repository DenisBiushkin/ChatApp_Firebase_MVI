package com.example.unmei.presentation.messanger_feature.util

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.Stroke

import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showBackground = true)
@Composable
fun showCanvas(){
   // Box(modifier = Modifier.fillMaxSize()){
   CanvasTraining()
   // MyCanvas()
    //}


}
@Composable
fun MyCanvas() {
    Canvas(
        modifier = Modifier
            .width(300.dp)
            .height(300.dp)
    ) {
        val path = Path().apply {
            moveTo(400f, 100f)

            drawPoints(
               points = listOf(
                  Offset(400f,100f)
               ),
                pointMode = PointMode.Points,
                color = Color.Blue,
                strokeWidth = 10f
            )
            lineTo(100f, 100f)
            lineTo(100f, 400f)
            lineTo(600f, 400f)

            arcTo(
                rect = Rect(
                    //получается прмоугольник 600 на 600 у.е
                    //а четвертинка будет 300 на 300
                    left = 400f,
                    top = -200f,//
                    right = 1000f,//правая
                    bottom = 400f//нижняя грань прямоуголника
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            )

        }
        drawPath(
            path = path,
            style = Stroke(width = 10f),
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.Red,
                    Color.Blue,
                )
            ),
        )
    }
}

@Composable
fun CanvasTraining(
    
){
    val textMeasurer = rememberTextMeasurer()
val longTextSample="С помощью Compose вы можете использовать TextMeasurer , чтобы получить доступ к измеренному размеру текста, в зависимости от вышеуказанных факторов. Если вы хотите нарисовать"
//    Column {
//        Canvas(
//            modifier = Modifier
//                .padding(30.dp)
//                .width(300.dp)
//                .height(150.dp)
//        ){
//            drawRoundRect(
//                color = Color.Red,
//                cornerRadius = CornerRadius(20.dp.toPx())
//            )
//        }
//        Box(
//            modifier = Modifier
//                .padding(start = 30.dp)
//                .clip(
//                    shape = CustomBubbleMessageShape(position = true)
//                ).background(brush = Brush.linearGradient(
//                    colors = listOf(
//                        Color.Red,
//                        Color.Blue,
//                    )
//                ))
//        ){
//            val otherPadding = 4.dp
//           Column(
//              modifier =  Modifier
//                  .padding(
//                      start = otherPadding,
//                      top=otherPadding,
//                      bottom = otherPadding,
//                     end = 15.dp
//                  )
//           ) {
//               val textWidth= 20.sp
//               Text(
//                   text = "Привет, это текст сообщения",
//                   fontSize = textWidth
//                   )
//               Text(text = "Время сообщения 18:30",fontSize = textWidth)
//           }
//        }
//        Spacer(modifier = Modifier.height(30.dp))
//        Box(
//            modifier = Modifier
//                .padding(start = 30.dp)
//                .clip(
//                    shape = CustomBubbleMessageShape()
//                ).background(brush = Brush.linearGradient(
//                    colors = listOf(
//                        Color.Red,
//                        Color.Blue,
//                    )
//                ))
//        ){
//            val otherPadding = 4.dp
//            Column(
//                modifier =  Modifier
//                    .padding(
//                        start = 15.dp,
//                        top=otherPadding,
//                        bottom = otherPadding,
//                        end = otherPadding
//                    )
//            ) {
//                val textWidth= 20.sp
//                Text(
//                    text = "Привет, это текст сообщения",
//                    fontSize = textWidth
//                )
//                Text(text = "Время сообщения 18:30",fontSize = textWidth)
//            }
//        }


//
//        Spacer(modifier = Modifier.height(30.dp))
//        Spacer(
//            modifier = Modifier
//                .padding(20.dp)
//                .drawWithCache {
//                    val measureText =
//                        textMeasurer.measure(
//                            text = longTextSample,
//                            //ограничения
//                            constraints = Constraints.fixedWidth(
//                                300.dp
//                                    .toPx()
//                                    .toInt()
//                            )
//                        )
//                    onDrawBehind {
//                        drawRoundRect(
//                            color = Color.Blue,
//                            size = measureText.size.toSize(),
//                            cornerRadius = CornerRadius(20.dp.toPx())
//                        )
//                        drawText(
//                            textLayoutResult = measureText,
//                            topLeft = Offset(20f, 30f)
//                        )
//                    }
//                }
//                .height(200.dp)
//
//        )
    }
//    Spacer(
//        modifier = Modifier
//            .padding(start = 20.dp)
//            .drawWithCache {
//                val measuredText =
//                    textMeasurer.measure(
//                        AnnotatedString(longTextSample),
//                        constraints = Constraints.fixedWidth((size.width * 2f / 3f).toInt()),
//                        style = TextStyle(fontSize = 18.sp)
//                    )
//
//                onDrawBehind {
//                    drawRect(Color.Blue, size = measuredText.size.toSize())
//                    drawText(measuredText)
//                }
//            }
//            .fillMaxSize()
//    )


//    Canvas(
//        modifier = Modifier
//            .padding(30.dp)
//            .size(300.dp)
//
//    ) {
//        val measuredText =
//            textMeasurer.measure(
//                AnnotatedString(longTextSample),
//                constraints = Constraints.fixedWidth((size.width * 2f / 3f).toInt()),
//                style = TextStyle(fontSize = 18.sp)
//            )
//        drawRect(Color.Blue, size = measuredText.size.toSize())
//        drawText(
//            measuredText
//        )
//        drawRect(
//            color= Color.Blue,
//            size = size
//        )
//        drawRect(
//            color=Color.Red,
//            topLeft = Offset(0f,0f),
//            style = Stroke(
//                width =2.dp.toPx()
//            )
//        )

  //  }
