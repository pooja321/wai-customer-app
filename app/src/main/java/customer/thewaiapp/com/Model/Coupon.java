package customer.thewaiapp.com.Model;

/**
 * Created by jagriti kedia on 29/12/2016.
 */

public class Coupon {
    private String couponCode,discountType,categories,status,usesType;
    private float discount;
    private String lastDateFrom,lastDateTo;

    public Coupon() {
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsesType() {
        return usesType;
    }

    public void setUsesType(String usesType) {
        this.usesType = usesType;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getLastDateFrom() {
        return lastDateFrom;
    }

    public void setLastDateFrom(String lastDateFrom) {
        this.lastDateFrom = lastDateFrom;
    }

    public String getLastDateTo() {
        return lastDateTo;
    }

    public void setLastDateTo(String lastDateTo) {
        this.lastDateTo = lastDateTo;
    }
}
