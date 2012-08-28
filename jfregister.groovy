/*
 * Groovy Script that registers the set memberName for the latest JForum event
 * It stores the latest meeting number in a file to prevent spamming the site
 *
 * Only works on nix based systems since curl is required
 * @author Steve Carrigan 2012 
 *   
**/

JForum jforum = new JForum()
Attendee attendee = new Attendee()
String memberName = "carrigan.steve@gmail.com"

Integer latestMeetingNumber = jforum.latestMeetingNumber

if (latestMeetingNumber == -1) {

    println new Date().toString() + " - There are no meetings at this date"

    growlNotify("tools","No meetings available (JForum)",new Date().toString())   
    
    return
}

if (attendee.latestAttendedMeetingNumber >= latestMeetingNumber) {

    println new Date().toString() + " - You have already signed up for the latest JForum meeting number " + latestMeetingNumber

    growlNotify("tools","Already signed up (JForum)",new Date().toString())   

} else {

    jforum.registerForMeeting(memberName,latestMeetingNumber)
    println new Date().toString() + " - You are signed up for JForum meeting number " + latestMeetingNumber
    attendee.latestAttendedMeetingNumber = latestMeetingNumber
    
    growlNotify("tools","You have signed up (JForum)",new Date().toString())           

}

void growlNotify(category, title, message, Closure clos = null) {
    def cmd = [
        "/usr/local/bin/growlnotify",
        "-n", "Groovy",
        "--image", "/Users/Shared/groovy-1.8.6/groovy48.png", // Modify 
        "-d", category,
        "-m", message,
        "-t", title,
    ]
    if (clos) {
        cmd << "-w"
    }
    def exitValue = execute(cmd)
    if (isClicked(exitValue)) clos.call()
}

boolean isClicked(exitValue) {
    exitValue == 2
}

int execute(cmd) {
    def proc = cmd.execute()
    proc.waitFor()
    proc.exitValue()
}

public class Attendee {

    private static final String DATA_FILE = ".meetingnumber"
    
    private Integer latestAttendedMeetingNumber
    
    public void writeToFile(def data) {
        File file = new File(DATA_FILE)
        file.write data.toString()
    }
    
    public String readFromFile() {
        File file = new File(DATA_FILE) 
        if (!file.exists()) {
            return null
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
    private static final Integer GROUP_POSITION = 1
    
    private String jforumData
    private Integer latestMeetingNumber

    public JForum() {
        jforumData = fetchWebpageData()    
    }
    
    private String fetchWebpageData() {
        new URL(JFORUM_URL).text    
    }
    
    public String registerForMeeting(String memberName, Integer meetingNumber) {
        def command = "curl -d email="+memberName+"&meeting="+meetingNumber.toString() + "  http://www.jforum.se/jf/?meeting="+meetingNumber.toString() 
        command.execute().text     
    }

    public Integer getLatestMeetingNumber() {
        String meetingNumber
        jforumData.eachLine {
            (it =~ /.*meeting=(.*?)'/).each{
               meetingNumber = it[GROUP_POSITION]
            }
        }
        if (meetingNumber != null)
            meetingNumber.toInteger()
        -1       
    }

}