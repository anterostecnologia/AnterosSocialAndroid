/*
 * ******************************************************************************
 *  * Copyright 2016 Anteros Tecnologia
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package br.com.anteros.social.instagram.entities;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import br.com.anteros.social.core.AgeRange;
import br.com.anteros.social.core.SocialNetworkType;
import br.com.anteros.social.core.SocialProfile;

/**
 * Created by edson on 25/03/16.
 */
public class InstagramProfile implements SocialProfile, Parcelable{

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

    protected InstagramProfile(Parcel in) {
        id = in.readString();
        userName = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        gender = in.readString();
        birthday = in.readString();
        email = in.readString();
        image = in.readString();
        link = in.readString();
    }

    public static final Creator<InstagramProfile> CREATOR = new Creator<InstagramProfile>() {
        @Override
        public InstagramProfile createFromParcel(Parcel in) {
            return new InstagramProfile(in);
        }

        @Override
        public InstagramProfile[] newArray(int size) {
            return new InstagramProfile[size];
        }
    };

    public String getLink() {
        return link;
    }

    public InstagramProfile(){

    }

    public InstagramProfile(String firstName, String middleName, String lastName, String gender, String birthday, String email, String image, String link, InstagramAgeRange ageRange) {
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

    public void setAgeRange(InstagramAgeRange ageRange) {
        this.ageRange = ageRange;
    }

    private InstagramAgeRange ageRange;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(String id) {
        this.id = id;
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
        return "InstagramProfile{" +
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
        return SocialNetworkType.INSTAGRAM;
    }

    @Override
    public String getProfileName() {
        return "Instagram";
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.imageBitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userName);
        dest.writeString(firstName);
        dest.writeString(middleName);
        dest.writeString(lastName);
        dest.writeString(gender);
        dest.writeString(birthday);
        dest.writeString(email);
        dest.writeString(image);
        dest.writeString(link);
    }
}
