package com.pmf.kviz.forme

import scalafx.Includes._
import scalafx.scene.layout.BorderPane
import scalafx.scene.control.RadioButton
import scalafx.scene.control.ToggleGroup
import scalafx.scene.control.ToolBar
import scalafx.geometry.Orientation
import scalafx.scene.control.Separator
import scalafx.event.ActionEvent
import scalafx.scene.layout.BackgroundFill
import scalafx.scene.paint.Color
import scalafx.scene.layout.Background

object UnosPitanjaForma extends BorderPane {
  
  val bckFill = new BackgroundFill(Color.White, null, null)
  background = new Background(Array(bckFill))
  
  val rdoPojedinacno = new RadioButton("Pojedinacno")
	val rdoKrozFajl = new RadioButton("Kroz fajl")
	val tgUnosPitanja = new ToggleGroup
	tgUnosPitanja.toggles.addAll(rdoPojedinacno, rdoKrozFajl)
  val tbUnosPitanja = new ToolBar
  tbUnosPitanja.setOrientation(Orientation.Vertical)
  tbUnosPitanja.items.addAll(new Separator, rdoPojedinacno, rdoKrozFajl, new Separator)
  left = tbUnosPitanja
  
  rdoPojedinacno.selected = true
  rdoKrozFajl.selected = false
  center = UnosPitanjaPojedinacnoForma
  
  rdoPojedinacno.onAction = (event: ActionEvent) => {
    rdoPojedinacno.selected = true
    rdoKrozFajl.selected = false
    center = UnosPitanjaPojedinacnoForma
  }
  
  rdoKrozFajl.onAction = (event: ActionEvent) => {
    rdoPojedinacno.selected = false
    rdoKrozFajl.selected = true
    center = UnosPitanjaKrozFajlForma
  }
}