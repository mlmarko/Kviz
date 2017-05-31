package com.pmf.kviz.forme

import scalafx.Includes._
import scalafx.scene.layout.BackgroundFill
import scalafx.scene.paint.Color
import scalafx.scene.layout.Region
import scalafx.scene.layout.Background
import scalafx.scene.layout.GridPane
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.control.TextField
import com.pmf.kviz.servis.PitanjeServis
import scalafx.scene.control.CheckBox
import scalafx.geometry.HPos
import scalafx.scene.control.ScrollPane
import scalafx.scene.control.Button
import scalafx.event.ActionEvent
import com.pmf.kviz.model.KonfiguracijaKviza
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import org.slf4j.LoggerFactory


object KonfiguracijaScrollForma extends ScrollPane {  
  content = KonfiguracionaForma  
  fitToHeight = true
  fitToWidth = true
}

object KonfiguracionaForma extends GridPane {
  
  val logger = LoggerFactory.getLogger(getClass)

  background = new Background(Array(new BackgroundFill(Color.White, null, null)))
  
  hgap = 50
  vgap = 10
  padding = Insets(50)
  
  val btnResetujPostavke = new Button("Resetuj postavke")
  val btnZapamtiPostavke = new Button("Zapamti postavke")
  add(btnResetujPostavke, 0, 0)
  add(btnZapamtiPostavke, 1, 0)
  
  //prazna labela kao novi red
  add(new Label, 0, 1)
  
  val lblTezina = new Label("Tezina kviza:")
  val tbTezina = new TextField
  
  add(lblTezina, 0, 2)
  add(tbTezina, 1, 2)
  
  val lblUkljuciOdgovore = new Label("Generisi kviz sa odgovorima: ")
  val cbUkljuciOdgovore = new CheckBox
  add(lblUkljuciOdgovore, 0, 3)
  add(cbUkljuciOdgovore, 1, 3)
  GridPane.setHalignment(cbUkljuciOdgovore, HPos.Center)
  
  //prazna labela kao novi red
  add(new Label, 0, 4)
  
  val lblKonfiguracija = new Label("Konfiguracija oblasti:")
  add(lblKonfiguracija, 0, 5)
  
  var grafickaKonfiguracijaOblasti = generisiNovuGrafickuKonfiguracijuOblasti(
      scala.collection.mutable.Map[String, TextField]())
      
  btnResetujPostavke.onAction = (event: ActionEvent) => {
    tbTezina.clear
    cbUkljuciOdgovore.selected = false
    grafickaKonfiguracijaOblasti.foreach(par => par._2.clear)
    KonfiguracijaKviza.tezina = None
    KonfiguracijaKviza.ukljuciOdgovore = false
    KonfiguracijaKviza.konfiguracijaOblasti.empty
    logger.debug("Konfiguracija resetovana")
  }
  
  btnZapamtiPostavke.onAction = (event: ActionEvent) => {
    Try {
      KonfiguracijaKviza.tezina = if (!tbTezina.text.value.isEmpty) Some(tbTezina.text.value.toInt) else None
      KonfiguracijaKviza.ukljuciOdgovore = cbUkljuciOdgovore.selected.value
      KonfiguracijaKviza.konfiguracijaOblasti = grafickaKonfiguracijaOblasti.map(par => 
          if (!par._2.text.value.isEmpty) Some(par._1 -> par._2.text.value.toInt) else None).flatten.toMap
      logger.debug("Konfiguracija uspesno setovana")
    } match {
      case Success(_) => InfoDijalog.prikaziInfo("Konfiguracija uspesno setovana.")
      case Failure(ex) => GreskaDijalog.prikaziGresku("Greska u konfiguraciji.", ex.getMessage)      
    }
  }
      
  def osveziGrafickuKonfiguracijuOblasti() = {
    grafickaKonfiguracijaOblasti = generisiNovuGrafickuKonfiguracijuOblasti(grafickaKonfiguracijaOblasti)
  }
  
  private def generisiNovuGrafickuKonfiguracijuOblasti(staraKonfiguracija:scala.collection.mutable.Map[String, TextField]) = {
		var i = 6 + staraKonfiguracija.size
		val konfiguracijaOblasti = staraKonfiguracija
		PitanjeServis.dohvatiSveOblasti.foreach(oblast => {
			if (!staraKonfiguracija.contains(oblast)) {
				val lblKonfiguracijaOblasti = new Label(oblast.concat(":"))
					val tbKonfiguracijaOblasti = new TextField
					konfiguracijaOblasti.put(oblast, tbKonfiguracijaOblasti)
					add(lblKonfiguracijaOblasti, 0, i)
					add(tbKonfiguracijaOblasti, 1, i)
					i+=1
			}
		})
		logger.debug("Osvezena konfiguraciona lista oblasti")
		konfiguracijaOblasti
  }
}
