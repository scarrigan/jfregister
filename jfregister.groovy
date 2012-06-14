

JForum jforum = new JForum()
Attendee attendee = new Attendee()

Integer latestMeetingNumber = jforum.latestMeetingNumber
if (attendee.latestAttendedMeetingNumber >= latestMeetingNumber) {
    println "You have already signed up for the latest JForum meeting number " + latestMeetingNumber
} else {
    println "You need to sign up for the latest JForum meeting number " + latestMeetingNumber
    // TODO : Sign up method
    attendee.latestAttendedMeetingNumber = latestMeetingNumber    
}



public class Attendee {

    private static final String DATA_FILE = "./meetingnumber.txt"
    
    private Integer latestAttendedMeetingNumber
    
    public void writeToFile(def data) {
        File file = new File(DATA_FILE)
        file.write data.toString()
    }
    
    public String readFromFile() {
        File file = new File(DATA_FILE) 
        if (!file.exists()) {
            return 0;
        } 
        file.text
    }
    
    public Integer getLatestAttendedMeetingNumber() {
       String meetingNumber = readFromFile()
       if(meetingNumber == null) {
           return 0    
       }
       meetingNumber.toInteger()     
    }
    
    public void setLatestAttendedMeetingNumber(Integer meetingNumber) {
        latestAttendedMeetingNumber = meetingNumber
        writeToFile latestAttendedMeetingNumber 
    }
}

public class JForum {

    private static final String JFORUM_URL = "http://www.jforum.se"
    private static final Integer GROUP_POSITION = 1;
    
    private String jforumData
    private Integer latestMeetingNumber

    public JForum() {
        jforumData = fetchWebpageData()    
    }
    
    private String fetchWebpageData() {
        new URL(JFORUM_URL).text    
    }

    public Integer getLatestMeetingNumber() {
        String meetingNumber
        jforumData.eachLine {
            (it =~ /.*meeting=(.*?)'/).each{
               meetingNumber = it[GROUP_POSITION]
            }
        }
        meetingNumber.toInteger()        
    }

}