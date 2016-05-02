package com.wneild.dto;

public class ChampionMasteryDto {
    private String highestGrade;
    private int championPoints;
    private int playerId;
    private int championPointsUntilNextLevel;
    private boolean chestGranted;
    private int championLevel;
    private int championId;
    private int championPointsSinceLastLevel;
    private long lastPlayTime;

    public String getHighestGrade() {
        return highestGrade;
    }

    public void setHighestGrade(String highestGrade) {
        this.highestGrade = highestGrade;
    }

    public int getChampionPoints() {
        return championPoints;
    }

    public void setChampionPoints(int championPoints) {
        this.championPoints = championPoints;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getChampionPointsUntilNextLevel() {
        return championPointsUntilNextLevel;
    }

    public void setChampionPointsUntilNextLevel(int championPointsUntilNextLevel) {
        this.championPointsUntilNextLevel = championPointsUntilNextLevel;
    }

    public boolean isChestGranted() {
        return chestGranted;
    }

    public void setChestGranted(boolean chestGranted) {
        this.chestGranted = chestGranted;
    }

    public int getChampionLevel() {
        return championLevel;
    }

    public void setChampionLevel(int championLevel) {
        this.championLevel = championLevel;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public int getChampionPointsSinceLastLevel() {
        return championPointsSinceLastLevel;
    }

    public void setChampionPointsSinceLastLevel(int championPointsSinceLastLevel) {
        this.championPointsSinceLastLevel = championPointsSinceLastLevel;
    }

    public long getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChampionMasteryDto that = (ChampionMasteryDto) o;

        if (championPoints != that.championPoints) return false;
        if (playerId != that.playerId) return false;
        if (championPointsUntilNextLevel != that.championPointsUntilNextLevel) return false;
        if (chestGranted != that.chestGranted) return false;
        if (championLevel != that.championLevel) return false;
        if (championId != that.championId) return false;
        if (championPointsSinceLastLevel != that.championPointsSinceLastLevel) return false;
        if (lastPlayTime != that.lastPlayTime) return false;
        return highestGrade != null ? highestGrade.equals(that.highestGrade) : that.highestGrade == null;

    }

    @Override
    public int hashCode() {
        int result = highestGrade != null ? highestGrade.hashCode() : 0;
        result = 31 * result + championPoints;
        result = 31 * result + playerId;
        result = 31 * result + championPointsUntilNextLevel;
        result = 31 * result + (chestGranted ? 1 : 0);
        result = 31 * result + championLevel;
        result = 31 * result + championId;
        result = 31 * result + championPointsSinceLastLevel;
        result = 31 * result + (int) (lastPlayTime ^ (lastPlayTime >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ChampionMasteryDto{" +
                "highestGrade='" + highestGrade + '\'' +
                ", championPoints=" + championPoints +
                ", playerId=" + playerId +
                ", championPointsUntilNextLevel=" + championPointsUntilNextLevel +
                ", chestGranted=" + chestGranted +
                ", championLevel=" + championLevel +
                ", championId=" + championId +
                ", championPointsSinceLastLevel=" + championPointsSinceLastLevel +
                ", lastPlayTime=" + lastPlayTime +
                '}';
    }
}
