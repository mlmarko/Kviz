package com.pmf.kviz.model


class Pitanje (val tezina:Int, val oblast:String, var brojPonavljanja:Int, val postavka:String, val odgovor:String) extends Equals with Ordered[Pitanje] {
  def generisiXML = <pitanje>
      { <tezina>{tezina}</tezina> }
      { <oblast>{oblast}</oblast> }
      { <brojPonavljanja>{brojPonavljanja}</brojPonavljanja> }
      { <postavka>{postavka}</postavka> }
      { <odgovor>{odgovor}</odgovor> }
    </pitanje>
      
  def inkrementirajBrojPonavljanja() = {
    brojPonavljanja += 1
  }
  
  def validiraj() {
    if (tezina < 1 || tezina > 10) {
      throw new IllegalArgumentException("Tezina pitanja mora biti izmedju 1 i 10");
    }
    if (oblast == null || oblast.trim.isEmpty) {
      throw new IllegalArgumentException("Oblast pitanja nije definisana");
    }
    if (postavka == null || postavka.trim.isEmpty) {
      throw new IllegalArgumentException("Postavka pitanja nije definisana");
    }
    if (odgovor == null || odgovor.trim.isEmpty) {
      throw new IllegalArgumentException("Odgovor pitanja nije definisan");
    }
  } 

  def canEqual(other: Any) = {
    other.isInstanceOf[com.pmf.kviz.model.Pitanje]
  }

  override def equals(other: Any) = {
    other match {
      case that: com.pmf.kviz.model.Pitanje => that.canEqual(Pitanje.this) && postavka == that.postavka
      case _ => false
    }
  }

  override def hashCode() = {
    val prime = 41
    prime + postavka.hashCode
  }
  
  override def compare (that: Pitanje) = {
    this.brojPonavljanja - that.brojPonavljanja
  }
}

object Pitanje {
  def parsirajXML(node: scala.xml.NodeSeq): Pitanje =
    new Pitanje(
        (node \ "tezina").text.toInt, 
        (node \ "oblast").text, 
        if ((node \ "brojPonavljanja").text.isEmpty) 0 else (node \ "brojPonavljanja").text.toInt, 
        (node \ "postavka").text, 
        (node \ "odgovor").text);
}
  
