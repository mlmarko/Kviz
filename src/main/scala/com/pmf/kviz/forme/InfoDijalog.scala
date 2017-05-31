package com.pmf.kviz.forme

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

object InfoDijalog extends Alert(AlertType.Information) {
  title = "Info"
  
  def prikaziInfo(infoPoruka:String) = {
    headerText = infoPoruka
    showAndWait
  }
  
}