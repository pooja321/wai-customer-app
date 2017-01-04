package customer.thewaiapp.com.Model;

/**
 * Created by jagriti kedia on 29/12/2016.
 */

public class Coupon {
    private String CouponCode,DiscountType,Categories,Status,UsesType;
    private float Discount;
    private String Lastdatefrom,Lastdateto;


    public Coupon() {
    }

    public Coupon(String couponCode, String discountType, String categories, String status, String usesType, float discount, String lastdatefrom, String lastdateto) {
        CouponCode = couponCode;
        DiscountType = discountType;
        Categories = categories;
        Status = status;
        UsesType = usesType;
        Discount = discount;
        Lastdatefrom = lastdatefrom;
        Lastdateto = lastdateto;
    }

    public String getCouponCode() {
        return CouponCode;
    }

    public void setCouponCode(String couponCode) {
        CouponCode = couponCode;
    }

    public String getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(String discountType) {
        DiscountType = discountType;
    }

    public String getCategories() {
        return Categories;
    }

    public void setCategories(String categories) {
        Categories = categories;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUsesType() {
        return UsesType;
    }

    public void setUsesType(String usesType) {
        UsesType = usesType;
    }

    public float getDiscount() {
        return Discount;
    }

    public void setDiscount(float discount) {
        Discount = discount;
    }

    public String getLastdatefrom() {
        return Lastdatefrom;
    }

    public void setLastdatefrom(String lastdatefrom) {
        Lastdatefrom = lastdatefrom;
    }

    public String getLastdateto() {
        return Lastdateto;
    }

    public void setLastdateto(String lastdateto) {
        Lastdateto = lastdateto;
    }
}
