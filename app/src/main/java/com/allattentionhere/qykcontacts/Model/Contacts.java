package com.allattentionhere.qykcontacts.Model;

/**
 * Created by krupen on 11/07/16.
 */
public class Contacts {
    private String id;
    private Phone phone;
    private String address;
    private String email;
    private String thumbnailUrl;
    private String name;
    private String gender;
    private String isFavorite;
    private String isToBeReminded;

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getIsToBeReminded() {
        return isToBeReminded;
    }

    public void setIsToBeReminded(String isToBeReminded) {
        this.isToBeReminded = isToBeReminded;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Phone getPhone ()
    {
        return phone;
    }

    public void setPhone (Phone phone)
    {
        this.phone = phone;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getThumbnailUrl ()
    {
        return thumbnailUrl;
    }

    public void setThumbnailUrl (String thumbnailUrl)
    {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

}
