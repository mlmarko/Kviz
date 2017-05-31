package com.pmf.kviz.forme

import scalafx.Includes._
import scalafx.scene.layout.GridPane
import scalafx.scene.control.Label
import scalafx.geometry.Insets
import scalafx.scene.control.TextField
import scalafx.scene.control.Button
import scalafx.geometry.HPos
import scalafx.scene.control.Slider
import scalafx.event.ActionEvent
import com.pmf.kviz.model.Pitanje
import com.pmf.kviz.servis.PitanjeServis
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import org.slf4j.LoggerFactory
import org.controlsfx.control.textfield.TextFields
import scalafx.scene.control.ComboBox
import collection.JavaConverters._
import scalafx.collections.ObservableBuffer

object UnosPitanjaPojedinacnoForma extends GridPane {
  
  val logger = LoggerFactory.getLogger(getClass)
  
  hgap = 50
  vgap = 10
  padding = Insets(10)
  
  val lblTezina = new Label("Tezina:")
  val lblOblast = new Label("Oblast:")
  val lblPitanje = new Label("Pitanje:")
  val lblOdgovor = new Label("Odgovor:")
  
  val slTezina = new Slider {
    min = 1
    max = 10
    value = 1
    showTickLabels = true
    showTickMarks = true
    majorTickUnit = 1
    minorTickCount = 0
    snapToTicks = true
  }
  val cbOblast = new ComboBox(PitanjeServis.dohvatiSveOblasti.toSeq)
  cbOblast.editable = true
  val tbPitanje = new TextField
  val tbOdgovor = new TextField
  
  
  TextFields.bindAutoCompletion(cbOblast.editor.value, cbOblast.items.value.toArray.toList.asJava)
  
  val btnUnesi = new Button("Unesi")
  GridPane.setHalignment(btnUnesi, HPos.Right)
  
  add(lblTezina, 0, 0)
  add(slTezina, 1, 0)
  add(lblOblast, 0, 1)
  add(cbOblast, 1, 1)
  add(lblPitanje, 0, 2)
  add(tbPitanje, 1, 2)
  add(lblOdgovor, 0, 3)
  add(tbOdgovor, 1, 3)
  add(btnUnesi, 1,4)
  
  btnUnesi.onAction = (event: ActionEvent) => {
    val novoPitanje = new Pitanje(slTezina.value.toInt, cbOblast.value.value, 0, tbPitanje.text.value, tbOdgovor.text.value)
    Try {
      novoPitanje.validiraj
      PitanjeServis.dodajNovaPitanja(List(novoPitanje)) 
    } match {
      case Success(_) => {
        logger.debug("Novo pitanje uspesno dodato")
        KonfiguracionaForma.osveziGrafickuKonfiguracijuOblasti
        cbOblast.items = ObservableBuffer(PitanjeServis.dohvatiSveOblasti.toSeq)
        TextFields.bindAutoCompletion(cbOblast.editor.value, cbOblast.items.value.toArray.toList.asJava)
        slTezina.value = 1
        cbOblast.selectionModel.value.clearSelection
        tbPitanje.clear
        tbOdgovor.clear    
      }
      case Failure(ex) => {
    	  ex.printStackTrace
    	  GreskaDijalog.prikaziGresku("Greska prilikom unosa novog pitanja.", ex.getMessage)
      }
    }
  }
  
}