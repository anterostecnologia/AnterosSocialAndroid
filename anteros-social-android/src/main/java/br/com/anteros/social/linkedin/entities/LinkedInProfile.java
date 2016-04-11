package br.com.anteros.social.linkedin.entities;

import android.graphics.Bitmap;
import android.os.Parcel;

import br.com.anteros.social.AgeRange;
import br.com.anteros.social.SocialNetworkType;
import br.com.anteros.social.SocialProfile;

/**
 * Created by edson on 10/04/16.
 */
public class LinkedInProfile implements SocialProfile {


    private String id="";

    private String userName="";

    private String firstName="";

    private String middleName="";

    private String lastName="";

    private String gender="";

    private String birthday="";

    private String email="";

    private String image="";

    private String link="";

    private Bitmap imageBitmap;

    public LinkedInProfile(){

    }

    public LinkedInProfile(String id, String userName, String firstName, String middleName, String lastName, String gender, String birthday, String email, String image, String link, Bitmap imageBitmap) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.image = image;
        this.link = link;
        this.imageBitmap = imageBitmap;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getMiddleName() {
        return middleName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getFullName() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getFirstName());
        if (this.getMiddleName()!="")
            sb.append(" ").append(this.getMiddleName());
        sb.append(" ").append(this.getLastName());
        return sb.toString();
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public AgeRange getAgeRange() {
        return new LinkedInAgeRange();
    }

    @Override
    public String getBirthday() {
        return birthday;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getImageUrl() {
        return image;
    }

    @Override
    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    @Override
    public SocialNetworkType getProfileType() {
        return SocialNetworkType.LINKEDIN;
    }

    @Override
    public String getProfileName() {
        return "LinkedIn";
    }
}
