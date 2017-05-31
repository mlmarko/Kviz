package com.pmf.kviz.forme

import scalafx.Includes._
import scalafx.scene.layout.Pane
import org.slf4j.LoggerFactory
import scalafx.scene.layout.GridPane
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.stage.FileChooser
import scalafx.scene.control.Button
import scalafx.event.ActionEvent
import com.pmf.kviz.servis.PitanjeServis
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import collection.JavaConverters._
import scalafx.collections.ObservableBuffer
import org.controlsfx.control.textfield.TextFields

object UnosPitanjaKrozFajlForma extends GridPane {
  
  val logger = LoggerFactory.getLogger(getClass)
  
  hgap = 50
  vgap = 10
  padding = Insets(10)
  
  val btnIzaberiteFajl = new Button("Izaberite fajl")
  add(btnIzaberiteFajl, 0, 0)
  
  btnIzaberiteFajl.onAction = (event: ActionEvent) => {
    Try {
  	  val fcIzaberiFajl = new FileChooser
  	  fcIzaberiFajl.setTitle("Izaberite fajl sa pitanjima");
  	  val fajl = fcIzaberiFajl.showOpenDialog(null);
  	  logger.debug("Ucitani fajl: {}", fajl)
  	  if (fajl != null) {
  	    val novaPitanja = PitanjeServis.ucitajNovaPitanjaIzFajla(fajl)
  	    val validnaPitanja = novaPitanja.map(pitanje => {
  	      Try(pitanje.validiraj) match {
  	        case Success(_) => Some(pitanje)
  	        case Failure(ex) => {
  	          logger.debug("Validacija pitanja neuspesna. Pitanje {}, poruka {}", Array(pitanje.postavka, ex.getMessage))
  	          None
  	        }
  	      }
  	    }).flatten
  	    if (!validnaPitanja.isEmpty) {
  	      PitanjeServis.dodajNovaPitanja(validnaPitanja)
  	    }
  	  }
    } match {
      case Success(_) => {
    	  KonfiguracionaForma.osveziGrafickuKonfiguracijuOblasti
    	  UnosPitanjaPojedinacnoForma.cbOblast.items = ObservableBuffer(PitanjeServis.dohvatiSveOblasti.toSeq)
        TextFields.bindAutoCompletion(UnosPitanjaPojedinacnoForma.cbOblast.editor.value, UnosPitanjaPojedinacnoForma.cbOblast.items.value.toArray.toList.asJava)
        logger.debug("Nova pitanja uspesno ucitana iz fajla i sacuvana u sistem")
        InfoDijalog.prikaziInfo("Nova pitanja uspesno ucitana iz izabranog fajla.")
      }
      case Failure(ex) => {
        ex.printStackTrace
        GreskaDijalog.prikaziGresku("Greska prilikom unosa novih pitanja iz fajla.", ex.getMessage)
      }
    }
  }
}