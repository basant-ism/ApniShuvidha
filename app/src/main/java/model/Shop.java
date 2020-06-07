package model;

public class Shop
{
    private String shopStatus,sellerName,shopAddress,sellerPhone,sellerEmail,shopCatagory,sellerUid;

    public Shop() {
    }

    public Shop(String shopStatus, String sellerName, String shopAddress, String sellerPhone, String sellerEmail) {
        this.shopStatus = shopStatus;
        this.sellerName = sellerName;
        this.shopAddress = shopAddress;
        this.sellerPhone = sellerPhone;
        this.sellerEmail = sellerEmail;
    }

    public String getShopCatagory() {
        return shopCatagory;
    }

    public void setShopCatagory(String shopCatagory) {
        this.shopCatagory = shopCatagory;
    }

    public String getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(String shopStatus) {
        this.shopStatus = shopStatus;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerUid() {
        return sellerUid;
    }

    public void setSellerUid(String sellerUid) {
        this.sellerUid = sellerUid;
    }
}
