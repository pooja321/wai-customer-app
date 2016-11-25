package com.waiapp.Utility;

import com.google.firebase.auth.FirebaseAuth;
import com.waiapp.R;

import java.util.Random;

/**
 * Created by keviv on 08/08/2016.
 */
public class Utilities {

    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static int[] tabIcons = {
            R.drawable.ic_map_white_24dp,
            R.drawable.ic_view_list_white_24dp
    };

    public  static String generateOrderId()
    {
        Random random = new Random();
        int randomNumber = random.nextInt(999999999 - 111111111) + 111111111;
        return (Constants.ORDER_ID_PREFIX).concat(String.valueOf(randomNumber));
    }

    public  static String generateCustomerId()
    {
        Random random = new Random();
        int randomNumber = random.nextInt(999999999 - 111111111) + 111111111;
        return (Constants.CUSTOMER_ID_PREFIX).concat(String.valueOf(randomNumber));
    }


}
