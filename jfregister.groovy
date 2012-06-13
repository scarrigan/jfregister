

JForumHelper.meetingNumber

class JForumHelper {

    static Integer getMeetingNumber() {
        String meetingNumber
        new URL("http://www.jforum.se").eachLine {
            (it =~ /.*meeting=(.*?)'/).each{
               meetingNumber = it[1]
            }
        }
        meetingNumber.toInteger()
    }

}