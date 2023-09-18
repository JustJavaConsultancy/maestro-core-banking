package ng.com.systemspecs.apigateway;

import ng.com.systemspecs.apigateway.util.StringEncryptionConverter;
import ng.com.systemspecs.apigateway.util.TimeGranularity;

import java.util.Date;

import static java.lang.System.out;

public class TestingCodeSegment {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub 1381134873
		String uuid = "NbXqvtHxjM1fDbCVI4y77A==";

//        String startDate = "2022-04-13 00:00:00";
//        String endDate = "2022-04-14 00:00:00";
//        Date s=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate);
//        Date e=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endDate);
//
//        Instant sd = s.toInstant();
//        Instant ed = e.toInstant();
//
//        System.out.println(s+" === "+e);
        StringEncryptionConverter s = new StringEncryptionConverter();
        out.println(s.convertToEntityAttribute(uuid));

//        System.out.println(calculateTimeAgoByTimeGranularity(new Date(), new Date(new Date().getTime() - (1 * 5 * 60 * 1000)), TimeGranularity.MINUTES));

//        String splitStr = "Ibile Pay/63149661153: Bulk reference 63170441-1038541885158-514/SUCCESSFUL";
//        String f[] = splitStr.split(" ");
//        String d[] = f[4].split("/");
//
//        out.println("Split String  "+ d[0]);
//        String l[] = d[0].split("-");
//        String tr = l[0].substring(0, l[0].lastIndexOf("1"));
//        out.println("Final:: "+tr);

	}

    static long calculateTimeAgoByTimeGranularity(Date currentTime, Date pastTime, TimeGranularity granularity) {
        long timeDifferenceInMillis = currentTime.getTime() - pastTime.getTime();
        return timeDifferenceInMillis / granularity.toMillis();
    }

}
