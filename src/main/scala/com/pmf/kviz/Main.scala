package com.pmf.kviz

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import org.slf4j.LoggerFactory

import com.pmf.kviz.forme.GlavnaForma
import com.pmf.kviz.forme.GreskaDijalog
import com.pmf.kviz.servis.PitanjeServis

import scalafx.application.JFXApp
import scalafx.application.Platform
import scalafx.scene.Scene
import java.io.File
import com.pmf.kviz.vo.Konstante
import com.pmf.kviz.forme.InfoDijalog
import javafx.collections.ObservableList


object Main extends JFXApp {
  
  val logger = LoggerFactory.getLogger(getClass)
  
  stage = new JFXApp.PrimaryStage {
    title.value = "Kviz"
    width = 1200
    height = 900
    
    scene = new Scene {
      root = GlavnaForma
      podesiCSS(stylesheets)
    }
    
    show
    
    Try {
      val repozitorijumPitanjaFolder = new File(Konstante.repozitorijumPitanja)
      repozitorijumPitanjaFolder.mkdir
      val kvizoviFolder = new File(Konstante.kvizovi)
      kvizoviFolder.mkdir
      PitanjeServis.ucitajSvaPitanja 
    } match {
      case Success(_) => logger.debug("Postojeca pitanja uspesno ucitana")
      case Failure(ex) => {
        ex.printStackTrace
        GreskaDijalog.prikaziGresku("Greska prilikom ucitavanja svih pitanja.", ex.getMessage)
        Platform.exit
      }
    }
  }
  
  private def podesiCSS(stylesheets:ObservableList[String]):Unit = {
    GreskaDijalog.dialogPane.value.getStylesheets.addAll(getClass.getResource("/modena.css").toExternalForm)
    InfoDijalog.dialogPane.value.getStylesheets.addAll(getClass.getResource("/modena.css").toExternalForm)
    stylesheets.add(getClass.getResource("/modena.css").toExternalForm)
    stylesheets.add(getClass.getResource("/application.css").toExternalForm)
  }
}