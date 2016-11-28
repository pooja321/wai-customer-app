package customer.thewaiapp.com.Model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by keviv on 29/08/2016.
 */
public class OrderAmount extends RealmObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderId;
    private String ResourceId;
    private int baseAmount;
    private int membersCount, mainCourseCount;
    private int membersAmount, mainCourseAmount;
    private double totalAmount, serviceTaxAmount;

    public OrderAmount() {
    }

    public OrderAmount(String orderId, String resourceId, int baseAmount, int mainCourseAmount, int mainCourseCount, int membersAmount, int membersCount, double serviceTaxAmount, double totalAmount) {
        this.baseAmount = baseAmount;
        this.mainCourseAmount = mainCourseAmount;
        this.mainCourseCount = mainCourseCount;
        this.membersAmount = membersAmount;
        this.membersCount = membersCount;
        this.orderId = orderId;
        ResourceId = resourceId;
        this.serviceTaxAmount = serviceTaxAmount;
        this.totalAmount = totalAmount;
    }

    public int getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getResourceId() {
        return ResourceId;
    }

    public void setResourceId(String resourceId) {
        ResourceId = resourceId;
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
}
