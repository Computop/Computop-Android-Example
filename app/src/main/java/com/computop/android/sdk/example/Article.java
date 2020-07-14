package com.computop.android.sdk.example;

import androidx.annotation.DrawableRes;

import org.parceler.Parcel;

/**
 * Created by paul.sprotte on 11.11.16.
 */
@Parcel
public class Article {

    public String name;
    public String color;
    public String price;

    @DrawableRes
    public int image;

    public String getName() {
        return name;
    }

    public Article setName(String name) {
        this.name = name;
        return this;
    }

    public String getColor() {
        return color;
    }

    public Article setColor(String color) {
        this.color = color;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public Article setPrice(String price) {
        this.price = price;
        return this;
    }

    public int getImage() {
        return image;
    }

    public Article setImage(int image) {
        this.image = image;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (image != article.image) return false;
        if (name != null ? !name.equals(article.name) : article.name != null) return false;
        if (color != null ? !color.equals(article.color) : article.color != null) return false;
        return price != null ? price.equals(article.price) : article.price == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + image;
        return result;
    }

    @Override
    public String toString() {
        return "Article{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", price='" + price + '\'' +
                ", image=" + image +
                '}';
    }
}
