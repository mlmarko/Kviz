package com.pmf.kviz.servis

import com.pmf.kviz.model.Pitanje
import com.pmf.kviz.vo.Konstante
import org.slf4j.LoggerFactory
import java.io.File
import java.io.PrintWriter
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import com.pmf.kviz.forme.GreskaDijalog
import com.pmf.kviz.forme.InfoDijalog

import de.nixosoft.jlr.JLRGenerator

object PitanjeServis {
  
  val logger = LoggerFactory.getLogger(getClass)

  //Set se koristi da bi se eliminisali duplikati iz strukture (konkretno liste pitanja)
  //Set u pozadini koristi hashCode i equals metode da bi proverio jednakost elemenata (da li takav element vec postoji u setu)
  private var pitanjaPoOblastima = scala.collection.mutable.Map[String, 
    scala.collection.mutable.Set[Pitanje]]()
  
  def ucitajSvaPitanja() = {
    logger.debug("Ucitavam oblasti")
    val oblasti = FajlServis.dohvatiNazivePostojecihDokumenata(Konstante.repozitorijumPitanja)

    logger.debug("Ucitavam postojeca pitanja")
    pitanjaPoOblastima = collection.mutable.Map(oblasti.map(
        oblast => oblast -> FajlServis.ucitajXMLDokument(Konstante.repozitorijumPitanja.concat("/").concat(oblast).concat(".xml"))) : _*)
    pitanjaPoOblastima
  }
  
  def dohvatiSveOblasti() = {
    logger.debug("Dohvatam postojece oblasti")
    pitanjaPoOblastima.keySet
  }
  
  def ucitajNovaPitanjaIzFajla(fajl:File) = {
    logger.debug("Ucitavam nova pitanja")
    FajlServis.ucitajXMLDokument(fajl).toList
  }
  
  def dodajNovaPitanja(novaPitanja:List[Pitanje]) = {
    logger.debug("Rasporedjujem nova pitanja po oblastima")    
    novaPitanja.foreach(pitanje => {
      val oblast = pitanje.oblast
      val pitanja = pitanjaPoOblastima.getOrElse(oblast, scala.collection.mutable.Set())
      pitanja.add(pitanje)
      pitanjaPoOblastima.put(oblast, pitanja)
    })
  
    azurirajFajloveSaPitanjima  
  }
  
  private def azurirajFajloveSaPitanjima() = {
    logger.debug("Azuriram postojece i pravim nove fajlove po oblastima sa pitanjima")
    pitanjaPoOblastima.foreach(par => {
      FajlServis.ispisiXMLDokument(
          Konstante.repozitorijumPitanja.concat("/").concat(par._1).concat(".xml"), par._2.toList)
    })
  }
  
  def generisiSadrzajKviza(tezina:Option[Int],  konfiguracijaOblasti:Map[String, Int]) = {
    if (tezina.isEmpty && konfiguracijaOblasti.isEmpty) {
      logger.debug("Neuspesno generisanje kviza. Nije zadata ni tezina niti konfiguracija oblasti")
      List[Pitanje]()
    } else if (konfiguracijaOblasti.isEmpty) {
      logger.debug("Generisem kviz bez izabranih oblasti sa zadatom tezinom")
  	  var trenutnaTezina = 0;
      val svaPitanjaSortirano = pitanjaPoOblastima.map(par => par._2).toList.flatten.sorted
      val sadrzajKviza = svaPitanjaSortirano.map(pitanje => {
        if (trenutnaTezina + pitanje.tezina <= tezina.get) {
          trenutnaTezina += pitanje.tezina
          Some(pitanje)
        } else {
          None
        }
      }).filter(_.isDefined).flatten
      
      if (!sadrzajKviza.isEmpty) {
      	sadrzajKviza.foreach(pitanje => azurirajBrojPonavaljanja(pitanje))
      	azurirajFajloveSaPitanjima
      	logger.debug("Kviz bez izabranih oblasti sa zadatom tezinom uspesno generisan")
      	sadrzajKviza
      } else {
        logger.debug("Neuspesno generisanje kviza sa zadatom tezinom.")
        List[Pitanje]()
      }
    } else {
      logger.debug("Generisem kviz sa izabranim oblastima")
  	  var trenutnaTezina = 0;
    	val pitanjaPoOblastimaSortirano = pitanjaPoOblastima.map(par => par._1 -> par._2.toList.sorted).toMap
    	val sadrzajKviza = konfiguracijaOblasti.map(par => {
    	  val pitanjaPoIzabranojOblasti = pitanjaPoOblastimaSortirano.getOrElse(par._1, List[Pitanje]()).take(par._2)
    	  val tezinaPitanjaPoIzabranojOblasti = pitanjaPoIzabranojOblasti.map(pitanje => pitanje.tezina).sum
    	  trenutnaTezina += tezinaPitanjaPoIzabranojOblasti
    	  pitanjaPoIzabranojOblasti
    	}).toList.flatten
    	
    	if (!sadrzajKviza.isEmpty) {
    	  sadrzajKviza.foreach(pitanje => azurirajBrojPonavaljanja(pitanje))
    	  azurirajFajloveSaPitanjima
    	  logger.debug("Kviz uspesno generisan")
    	  sadrzajKviza
    	} else {
        logger.debug("Neuspesno generisanje kviza sa izabranim oblastima i zadatom tezinom.")
    	  List[Pitanje]()
    	}
    }
  }
  
  private def azurirajBrojPonavaljanja(pitanje:Pitanje) = {
    pitanjaPoOblastima.getOrElse(pitanje.oblast, scala.collection.mutable.Set())
        .foreach(pitanjeIzOblasti => if (pitanje == pitanjeIzOblasti) pitanjeIzOblasti.inkrementirajBrojPonavljanja)
  }
  
  def generisiKvizFajlove(pitanja:List[Pitanje], ukljuciOdgovore:Boolean) {
    val generisanoImeKviza = "kviz_".concat(System.currentTimeMillis.toString)  
    if (generisiFajlove(generisanoImeKviza, pitanja, false)) {
    	if (!ukljuciOdgovore) {
    		InfoDijalog.prikaziInfo("Uspesno generisani kviz fajlovi sa nazivom: ".concat(generisanoImeKviza))
    	} else if (generisiFajlove(generisanoImeKviza, pitanja, true)) {
    	  InfoDijalog.prikaziInfo("Uspesno generisani kviz i odgovor fajlovi sa nazivom: ".concat(generisanoImeKviza))
    	}
    }    
  }
  
  private def generisiFajlove(generisanoImeKviza:String, pitanja:List[Pitanje], ukljuciOdgovore:Boolean):Boolean = {
    val imeKviza = if (ukljuciOdgovore) generisanoImeKviza.concat("_odgovori") else generisanoImeKviza
    val putanjaDirektorijuma = napraviDirektorijum(imeKviza)
    if (putanjaDirektorijuma.isDefined) {
      val latexSadrzaj = generisiLatexSadrzaj(pitanja, ukljuciOdgovore)
      if (generisiLatexKvizFajl(putanjaDirektorijuma.get, imeKviza, latexSadrzaj)) {
        if (generisiPdfKvizFajl(putanjaDirektorijuma.get, imeKviza)) {
          true
        } else false
      } else false
    } else false
  }
  
  private def napraviDirektorijum(imeKviza:String) = {
    Try {
      val putanjaDirektorijuma = Konstante.kvizovi.concat("/").concat(imeKviza).concat(".pdf")
      val direktorijum = new File(putanjaDirektorijuma)
      direktorijum.mkdir
      putanjaDirektorijuma
    } match {
      case Success(putanjaDirektorijuma) => {
        logger.debug("Direktorijum kviza uspesno generisan. Putanja do direktorijuma: {}", putanjaDirektorijuma)
        Some(putanjaDirektorijuma)
      }
      case Failure(ex) => {
        ex.printStackTrace
        GreskaDijalog.prikaziGresku("Greska prilikom generisanja direktorijuma za kviz.", ex.getMessage)
        None
      }
    }
  }
  
  private def generisiLatexSadrzaj(pitanja:List[Pitanje], ukljuciOdgovore:Boolean) = {
    val latexSadrzaj = new StringBuilder
    latexSadrzaj ++= "\\documentclass[12pt]{article}\n"
    latexSadrzaj ++= "\\usepackage{amsmath}\n"
    latexSadrzaj ++= "\\title{\\vspace{-4cm}Kviz}\n"
    latexSadrzaj ++= "\\date{}"
    latexSadrzaj ++= "\\usepackage[parfill]{parskip}"
    latexSadrzaj ++= "\\begin{document}\n"
    latexSadrzaj ++= "\\maketitle\n"
    var i = 1;
    pitanja.foreach(pitanje => {
      latexSadrzaj ++= i.toString.concat(".").concat("[").concat(pitanje.tezina.toString).concat("] - ")
      latexSadrzaj ++= (pitanje.postavka + "\n")
      latexSadrzaj ++= "\\newline\n"
      if (ukljuciOdgovore) {
        latexSadrzaj ++= (pitanje.odgovor + "\n\n")
      }
      latexSadrzaj ++= "\\newline\n"
      i+=1
    })
    latexSadrzaj ++= "\\end{document}\n"
    latexSadrzaj.toString
  }
  
  private def generisiLatexKvizFajl(putanjaDirektorijuma:String, imeKviza:String, latexSadrzaj:String) = {
    val imeLatexKviza = imeKviza.concat(".tex")
    var pw:PrintWriter = null
    val generisanjeLatexa = Try {
      pw = new PrintWriter(new File(putanjaDirektorijuma.concat("/").concat(imeLatexKviza)))
      pw.write(latexSadrzaj)
    }
    if (pw != null) {
    	pw.close
    }
    
    generisanjeLatexa match {
      case Success(_) => {
        logger.debug("Latex kviz uspesno generisan. Ime fajla: {}", imeLatexKviza)
        true
      }
      case Failure(ex) => {
        ex.printStackTrace
        GreskaDijalog.prikaziGresku("Greska prilikom generisanja kviza u latex formatu.", ex.getMessage)
        false
      }
    }
  }
  
  private def generisiPdfKvizFajl(putanjaDirektorijuma:String, imeKviza:String) = {
		val imePdfKviza = imeKviza.concat(".pdf")
		val imeLatexKviza = imeKviza.concat(".tex")
		val pdfGenerator = new JLRGenerator
    Try {
      val latexFajl = new File(putanjaDirektorijuma.concat("/").concat(imeLatexKviza))
      val pdfFajl = new File(Konstante.kvizovi.concat("/").concat(imePdfKviza))
      val privremeniDirektorijum = new File(Konstante.kvizovi);
      pdfGenerator.generate(latexFajl, pdfFajl, privremeniDirektorijum)
      if (pdfGenerator.getPDF == null) {
        throw new RuntimeException(pdfGenerator.getErrorMessage)
      }
    } match {
      case Success(_) => {
        logger.debug("PDF kviz uspesno generisan. Ime fajla: {}", imePdfKviza)
        true
      }
      case Failure(ex) => {
        ex.printStackTrace
        GreskaDijalog.prikaziGresku("Greska prilikom generisanja kviza u PDF formatu.", ex.getMessage)
        false
      }
    }
  }
}