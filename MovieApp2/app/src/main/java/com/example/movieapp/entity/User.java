package com.example.movieapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.movieapp.model.Movie;
import com.example.movieapp.utils.CONSTANT;

@Entity(tableName = CONSTANT.NAME_OF_USER_TABLE)
public class User implements Comparable, Cloneable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CONSTANT.USER_ID)
    public Long idUser;

    @ColumnInfo(name = CONSTANT.NAME_OF_USER)
    public String name;

    @ColumnInfo(name = CONSTANT.EMAIL_OF_USER)
    public String email;

    @ColumnInfo(name = CONSTANT.USER_BIRTHDAY)
    public String birthday;

    @ColumnInfo(name = CONSTANT.SEX_OF_USER_COLUMN)
    public String sex;

    @ColumnInfo(name = CONSTANT.AVATAR_OF_USER_COLUMN)
    public String avatar;

//    public User(Long idUser, String name, String email, String birthday, String sex, String avatar) {
//        this.idUser = idUser;
//        this.name = name;
//        this.email = email;
//        this.birthday = birthday;
//        this.sex = sex;
//        this.avatar = avatar;
//    }

    public User(String name, String email, String birthday, String sex, String avatar) {
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.sex = sex;
        this.avatar = avatar;
    }

    @Override
    public int compareTo(Object o) {
        User compare = (User) o;
        if (compare.idUser == this.idUser && compare.name.equals(this.name)
                && compare.email.equals(this.email) && compare.birthday.equals(this.birthday)){
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        User clone;
        try {
            clone = (User) super.clone();
        }catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
        return clone;
    }

    @NonNull
    @Override
    public String toString() {
        return "User: id = " + idUser.toString() + ", name = " + name
                + ", email = " + email + ", birthday = " + birthday
                + ", sex = " + sex;
    }
}
