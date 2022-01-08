package com.recordstore.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "Item.queryAll", query = "select i from Item i")
})
public class Item implements DBEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "artistid")
    private int artistid;

    @Basic
    @Column(name = "format")
    private String format;

    @Basic
    @Column(name = "genre")
    private String genre;

    @Basic
    @Column(name = "year")
    private int year;

    @Basic
    @Column(name = "nounits")
    private int nounits;

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getArtistid() {
        return artistid;
    }

    public void setArtistid(int artistid) {
        this.artistid = artistid;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNounits() {
        return nounits;
    }

    public void setNounits(int nounits) {
        this.nounits = nounits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return year == item.year && nounits == item.nounits && Objects.equals(id, item.id) && Objects.equals(title, item.title) && Objects.equals(artistid, item.artistid) && Objects.equals(format, item.format) && Objects.equals(genre, item.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, artistid, format, genre, year, nounits);
    }

    @Override
    public String toString() {
        return "Item " + id + ": " + title;
    }

    @Override
    public String getInfo() {
        return """
                Item (ID: %d)
                Title: %s
                Artist ID: %d
                Format: %s
                Genre: %s
                Year: %d
                No. Units: %d
                """.formatted(id, title, artistid, format, genre, year, nounits);
    }
}
