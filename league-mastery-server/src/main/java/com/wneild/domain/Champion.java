package com.wneild.domain;

public class Champion {
    private final String name;
    private final String imageUrl;

    public Champion(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Champion champion = (Champion) o;

        if (name != null ? !name.equals(champion.name) : champion.name != null) return false;
        return imageUrl != null ? imageUrl.equals(champion.imageUrl) : champion.imageUrl == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Champion{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
