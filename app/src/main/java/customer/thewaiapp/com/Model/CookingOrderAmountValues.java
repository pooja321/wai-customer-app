package customer.thewaiapp.com.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by keviv on 18/10/2016.
 */

public class CookingOrderAmountValues extends RealmObject {

    @PrimaryKey
    private String OrderId;
    private int baseAmount, membersCount, mainCourseCount,membersAmount, mainCourseAmount;
    private double totalAmount, serviceTaxAmount;

    public CookingOrderAmountValues() {
    }

    public CookingOrderAmountValues(String orderId, int baseAmount, int mainCourseAmount, int mainCourseCount, int membersAmount, int membersCount, double serviceTaxAmount, double totalAmount) {
        this.baseAmount = baseAmount;
        this.mainCourseAmount = mainCourseAmount;
        this.mainCourseCount = mainCourseCount;
        this.membersAmount = membersAmount;
        this.membersCount = membersCount;
        OrderId = orderId;
        this.serviceTaxAmount = serviceTaxAmount;
        this.totalAmount = totalAmount;
    }

    public int getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
    }

    public double getServiceTaxAmount() {
        return serviceTaxAmount;
    }

    public void setServiceTaxAmount(double serviceTaxAmount) {
        this.serviceTaxAmount = serviceTaxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public int getMainCourseAmount() {
        return mainCourseAmount;
    }

    public void setMainCourseAmount(int mainCourseAmount) {
        this.mainCourseAmount = mainCourseAmount;
    }

    public int getMainCourseCount() {
        return mainCourseCount;
    }

    public void setMainCourseCount(int mainCourseCount) {
        this.mainCourseCount = mainCourseCount;
    }

    public int getMembersAmount() {
        return membersAmount;
    }

    public void setMembersAmount(int membersAmount) {
        this.membersAmount = membersAmount;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }
}
