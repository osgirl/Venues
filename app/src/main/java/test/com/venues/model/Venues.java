package test.com.venues.model;

import org.json.JSONArray;


public class Venues {
    private String id, name, url, ratingcolor, rating, storeId, ratingSignals, photoUrl, address, postalcode, city,
            state, country, location, facebook, facebookName, twitter, phone;
    private double latitude, longitude;
    private boolean verified, isFavorite;
    private JSONArray photos, Contacts;
    private Float distance;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setRatingcolor(String ratingcolor) {
        this.ratingcolor = ratingcolor;
    }

    public String getRatingcolor() {
        return ratingcolor;
    }

    public void setRatingSignals(String ratingSignals) {
        this.ratingSignals = ratingSignals;
    }

    public String getRatingSignals() {
        return ratingSignals;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isVerified() {
        return verified;
    }


    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFacebook() {
        return facebook;
    }


    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getTwitter() {
        return twitter;
    }

    public JSONArray getPhotos() {
        return photos;
    }

    public void setPhotos(JSONArray photos) {
        this.photos = photos;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setContacts(JSONArray contacts) {
        Contacts = contacts;
    }

    public JSONArray getContacts() {
        return Contacts;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Float getDistance() {
        return distance;
    }
}
