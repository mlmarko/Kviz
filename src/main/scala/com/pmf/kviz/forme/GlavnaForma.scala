package com.pmf.kviz.forme

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.Button
import scalafx.scene.control.Separator
import scalafx.scene.control.ToolBar
import scalafx.scene.layout.BorderPane
import scalafx.application.Platform
import com.pmf.kviz.servis.PitanjeServis
import com.pmf.kviz.model.KonfiguracijaKviza

object GlavnaForma extends BorderPane {
  
  val btnUnesiPitanja = new Button("Unos pitanja")
  val btnPodesavanjeKviza = new Button("Podesavanje kviza")
  val btnGenerisiKviz = new Button("Generisanje kviza")
  val btnKrajPrograma = new Button("Izlaz")
  val tbAkcije = new ToolBar
  tbAkcije.items.addAll(new Separator, btnUnesiPitanja, btnPodesavanjeKviza, btnGenerisiKviz, btnKrajPrograma, new Separator);
  
  top = tbAkcije
  center = PocetnaForma
  
  btnUnesiPitanja.onAction = (event: ActionEvent) => {
    center = UnosPitanjaForma
  }
  
  btnPodesavanjeKviza.onAction = (event: ActionEvent) => {
    center = KonfiguracijaScrollForma
  }
  
  btnGenerisiKviz.onAction = (event: ActionEvent) => {
	  val pitanja = PitanjeServis.generisiSadrzajKviza(KonfiguracijaKviza.tezina, KonfiguracijaKviza.konfiguracijaOblasti)
	  if (!pitanja.isEmpty) PitanjeServis.generisiKvizFajlove(pitanja, KonfiguracijaKviza.ukljuciOdgovore)
		else GreskaDijalog.prikaziGresku("Greska prilikom generisanja kviza.", "Proveriti podesavanje kviza.") 
  }
  
  btnKrajPrograma.onAction = (event: ActionEvent) => {
    Platform.exit
  }  
}