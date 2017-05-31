package com.pmf.kviz.forme

import scalafx.Includes._
import scalafx.scene.image.Image
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.BackgroundImage
import scalafx.scene.layout.BackgroundRepeat
import scalafx.scene.layout.BackgroundSize
import scalafx.scene.layout.BackgroundPosition
import scalafx.scene.layout.Background


object PocetnaForma extends BorderPane {

  val img:Image = new Image(getClass.getResourceAsStream("/quiz.png"))
  val bckImage = new BackgroundImage(img, BackgroundRepeat.NoRepeat, BackgroundRepeat.NoRepeat, BackgroundPosition.Center, BackgroundSize.Default)
  background = new Background(Array(bckImage))
}