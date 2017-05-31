package com.pmf.kviz.servis

import java.io.File
import scala.collection.mutable.ListBuffer
import scala.xml.XML
import com.pmf.kviz.model.Pitanje
import scala.xml.Elem

object FajlServis {
  
  def dohvatiNazivePostojecihDokumenata(repozitorijumPitanja: String) = {
    (new File(repozitorijumPitanja))
        .listFiles
        .map(nazivFajla => nazivFajla.getName.substring(0, nazivFajla.getName.lastIndexOf(".xml")))
        .toList
  }
  
  def ucitajXMLDokument(fajl:String) = {
    val xml = XML.loadFile(fajl)
    parsirajXMLDokument(xml)
  }
  
  def ucitajXMLDokument(fajl:File) = {
    val xml = XML.loadFile(fajl)
    parsirajXMLDokument(xml)
  }
  
  private def parsirajXMLDokument(xml:Elem) = {
    val pitanja = (xml \ "pitanje").map(pitanje => {
      Pitanje.parsirajXML(pitanje)
    }).toSet
    collection.mutable.Set(pitanja.toArray:_*)
  }
  
  def ispisiXMLDokument(putanjaDoFajla:String, pitanja:List[Pitanje]):Unit = {
    val xml = <pitanja>
    { pitanja.map(_.generisiXML) }
    </pitanja>
    val prettyPrinter = new scala.xml.PrettyPrinter(80, 4)
    XML.save(putanjaDoFajla, XML.loadString(prettyPrinter.format(xml)), "UTF-8", true, null)
  }
}