package br.com.anteros.social.google.entities;

import android.graphics.Bitmap;

import br.com.anteros.social.core.AgeRange;
import br.com.anteros.social.core.SocialNetworkType;
import br.com.anteros.social.core.SocialProfile;

/**
 * Created by edson on 25/03/16.
 */
public class GoogleProfile implements SocialProfile{


    private String firstName;

    private String middleName;

    private String lastName;

    private String gender;

    private String birthday;

    private String email;

    private String image;

    private String link;

    private Bitmap imageBitmap;

    private String userName="";

    private String id="";

    public String getLink() {
        return link;
    }

    public GoogleProfile(String firstName, String middleName, String lastName, String gender, String birthday, String email, String image, String link, GoogleAgeRange ageRange) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.image = image;
        this.link = link;
        this.ageRange = ageRange;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public AgeRange getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(GoogleAgeRange ageRange) {
        this.ageRange = ageRange;
    }

    private GoogleAgeRange ageRange;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String getFullName() {
        StringBuffer sb = new StringBuffer();
        sb.append(getFirstName());
        if (getMiddleName()!=null){
            sb.append(" ");
            sb.append(getMiddleName());
        }
        sb.append(" ");
        sb.append(getLastName());
        return sb.toString();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getImageUrl() {
        return getImage();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "GoogleProfile{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                ", ageRange=" + ageRange +
                '}';
    }

    public Bitmap getImageBitmap(){
        return this.imageBitmap;
    }

    @Override
    public SocialNetworkType getProfileType() {
        return SocialNetworkType.GOOGLE;
    }

    @Override
    public String getProfileName() {
        return "Google";
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.imageBitmap = bitmap;
    }
}
