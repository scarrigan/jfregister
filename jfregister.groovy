 new URL("http://www.jforum.se").eachLine{
   (it =~ /.*meeting=(.*?)'/).each{
       println "Current meeting number is " + it[1]
   }
}
