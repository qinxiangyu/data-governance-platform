import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import static ch.qos.logback.classic.Level.INFO

StringBuilder p = new StringBuilder();
//%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}
p.append("data-governance-platform");
p.append(">%d{yyyy-MM-dd/HH:mm:ss:SSS}");
p.append(" %-5p");
if(System.getProperty("PID")){
    p.append(" pid-" + System.getProperty("PID"))
} else {
    p.append(" pid-")
}
p.append("[%X]")
p.append("[%10.10t]")
p.append("%-40.40class{39}[%-5.5line]");
p.append("%m%n");
def USER_DIR = System.getProperty("user.dir");
def product = System.getProperty("product","false");
//Boolean isLog = Boolean.parseBoolean(product);
Boolean isLog = true;
appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = p.toString();
    }
}

if(isLog) {
    appender("FILE", RollingFileAppender) {
        file = "${USER_DIR}/log/data_governance_platform.log"
        rollingPolicy(TimeBasedRollingPolicy){
            fileNamePattern = "${USER_DIR}/log/data_gobernance_platform_%d{yyyy_MM_dd}.zip"
            maxHistory = 60
        }
        //append = true
        //prudent = true
        encoder(PatternLayoutEncoder) {
            pattern = p.toString();
        }
    }
}
if(isLog) {
    logger("com.weiyi.hlj.mapper", DEBUG, ["CONSOLE","FILE"])
    root(INFO, ["CONSOLE","FILE"])
} else {
    logger("com.weiyi.hlj.mapper", DEBUG, ["CONSOLE"])
    root(INFO, ["CONSOLE"])
}