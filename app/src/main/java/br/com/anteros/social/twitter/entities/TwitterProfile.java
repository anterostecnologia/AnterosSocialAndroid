package br.com.anteros.social.twitter.entities;

import android.graphics.Bitmap;

/**
 * Created by edson on 28/03/16.
 */
public class TwitterProfile {

    private String firstName;

    private String middleName;

    private String lastName;

    private String gender;

    private String birthday;

    private String email;

    private String image;

    private String link;
    private Bitmap imageBitmap;

    public String getLink() {
        return link;
    }

    public TwitterProfile(String firstName, String middleName, String lastName, String gender, String birthday, String email, String image, String link, TwitterAgeRange ageRange) {
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

    public TwitterAgeRange getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(TwitterAgeRange ageRange) {
        this.ageRange = ageRange;
    }

    private TwitterAgeRange ageRange;


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
        return "TwitterProfile{" +
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

    public void setImageBitmap(Bitmap bitmap) {
        this.imageBitmap = bitmap;
    }
}
