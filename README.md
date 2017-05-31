# Kviz

Da bi se projekat izbildovao potrebno je instalirati sledece   
SBT(Simple build tool) verzija: 0.13.15 , Scala verzija: 2.12.1, Java verzija: 1.8.131
    
Program se bilduje komandom    
      
```
sbt clean compile    
```
     
Izvrsni .jar se pravi komandom    
sbt assembly    

Program se pokrece komandom    
sbt run   
      
Da bi se uvezao u eclipsu potrebno je izvrsiti komandu     
sbt eclipse       
        
Program moze da ucitava pitanja ili iz XML(.xml) fajla ili kroz formu. Ucitanja pitanja cuvaju se po oblastima u XML(.xml) formatu u folderu /repozitorijum_pitanja. Moguce je zadati konfiguraciju kviza kroz odgovarajucu formu. Na osnovu zadate konfiguracije, kao i ucitanih pitanja generise se kviz. Svi generisani kvizovi nalaze se u folderu /kviz. Kvizovi se generisu u Latex (.tex) formatu a zatim automatski prevode u PDF(.pdf) format.      
       
Razvoj je radjen na OS Linux distribucija Ubuntu 16.04.2 LTS.                 
Izvrsni alat je formata JavaARchive(.jar) i moze se izvrsiti ako na sistemu postoji instalirana java komandom       
java -jar kviz.jar         
         
Autori      
Marko Mladenovic - marko_ml@yahoo.com        
Andrijana Cakarevic - andrijanacakarevic95@gmail.com        
Nikola Rajkovic - rajkovic.nikola.995@gmail.com         

    

