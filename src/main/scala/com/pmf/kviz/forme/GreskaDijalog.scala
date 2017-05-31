package com.pmf.kviz.forme

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

object GreskaDijalog extends Alert(AlertType.Error) {
  title = "Greska"
  
  def prikaziGresku(infoGreske:String, tekstGreske:String) = {
    headerText = infoGreske
    contentText = tekstGreske
    showAndWait
  }
  
}