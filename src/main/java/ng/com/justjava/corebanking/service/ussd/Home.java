package ng.com.justjava.corebanking.service.ussd;

import ng.com.justjava.corebanking.domain.Profile;

import java.util.ArrayList;
import java.util.List;

public class Home {
    private final Profile profile;
    List<String> menuList = new ArrayList<>();
    private ServiceResponse response;

    public Home(String requestString, Profile profile) {
        this.profile = profile;
        menuList.add("Check Balance");
        menuList.add("Send Money to Wallet");
        menuList.add("Send Money to Bank");
        menuList.add("Fund Wallet");
        menuList.add("Bill payment");
        menuList.add("Pay RRR");
        menuList.add("Cash out");
        menuList.add("Buy Airtime/Data");
        menuList.add("Request Money");
        menuList.add("Approve Request Money");
//        menuList.add("Share Receipt");
        menuList.add("Settings");

        step(requestString);

    }
/*
    public static void main(String[] args) {

        String requestString = "*700000#98";

        String substring = requestString.substring(0, requestString.length() - 3);
        System.out.println(substring);

        requestString = substring.substring(5);

        System.out.println("Re " + requestString);
    }*/

    private ServiceResponse step(String requestString) {
        System.out.println("step requestString.split(\"#\").length..." + requestString.split("#").length);

        response = new ServiceResponse();

        String[] entries = requestString.split("#");

        if (entries.length == 1) {
            String entry = entries[0];
            System.out.println("entry ====> " + entry);
            String pageNumber = entry.substring(5);

            System.out.println("page number === " + pageNumber);

            int pgNo = pageNumber.length();
            System.out.println("pgNo ====> " + pgNo);
            int count = pgNo * 4 + 1;

            StringBuilder content = new StringBuilder();
            for (int i = count; i < count + 4 && i <= menuList.size(); i++) {
                if (i == 1) {
                    content.append("Welcome to Remita Mobile Money : \n");
                }
                content.append(" ").append(i).append(". ").append(menuList.get(i - 1)).append("\n");
            }

            if (pgNo != 0) {
                content.append("98. Back\n");
            }

            if (pgNo != menuList.size() / 4 && menuList.size() > 4)
                content.append("99. Next\n");

            response.setContent(content.toString());
            response.setMsgType("1");

        }
        return response;
    }

    public ServiceResponse getResponse() {
//        if (profile == null) {
//        }
        return response;
    }
}
