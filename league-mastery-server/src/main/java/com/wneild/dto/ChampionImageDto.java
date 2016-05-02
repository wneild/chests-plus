package com.wneild.dto;

public class ChampionImageDto {
    private int w;
    private String full;
    private String sprite;
    private String group;
    private int h;
    private int y;
    private int x;

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChampionImageDto that = (ChampionImageDto) o;

        if (w != that.w) return false;
        if (h != that.h) return false;
        if (y != that.y) return false;
        if (x != that.x) return false;
        if (full != null ? !full.equals(that.full) : that.full != null) return false;
        if (sprite != null ? !sprite.equals(that.sprite) : that.sprite != null) return false;
        return group != null ? group.equals(that.group) : that.group == null;

    }

    @Override
    public int hashCode() {
        int result = w;
        result = 31 * result + (full != null ? full.hashCode() : 0);
        result = 31 * result + (sprite != null ? sprite.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + h;
        result = 31 * result + y;
        result = 31 * result + x;
        return result;
    }

    @Override
    public String toString() {
        return "ChampionImageDto{" +
                "w=" + w +
                ", full='" + full + '\'' +
                ", sprite='" + sprite + '\'' +
                ", group='" + group + '\'' +
                ", h=" + h +
                ", y=" + y +
                ", x=" + x +
                '}';
    }
}
