import com.baizhi.utils.SMSUtils;
import org.apache.commons.lang.RandomStringUtils;

public class TestSMS {


    public static void main(String[] args) {



        String code = RandomStringUtils.randomNumeric(6);
        System.out.println(code);
        SMSUtils.sendMsg("13141054521", code);


    }

}
