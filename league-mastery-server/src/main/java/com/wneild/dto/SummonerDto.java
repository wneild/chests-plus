package com.wneild.dto;

public class SummonerDto {
    private int id;
    private String name;
    private int profileIconId;
    private int summonerLevel;
    private long revisionDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public long getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(long revisionDate) {
        this.revisionDate = revisionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SummonerDto that = (SummonerDto) o;

        if (id != that.id) return false;
        if (profileIconId != that.profileIconId) return false;
        if (summonerLevel != that.summonerLevel) return false;
        if (revisionDate != that.revisionDate) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + profileIconId;
        result = 31 * result + summonerLevel;
        result = 31 * result + (int) (revisionDate ^ (revisionDate >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "SummonerDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", profileIconId=" + profileIconId +
                ", summonerLevel=" + summonerLevel +
                ", revisionDate=" + revisionDate +
                '}';
    }
}
