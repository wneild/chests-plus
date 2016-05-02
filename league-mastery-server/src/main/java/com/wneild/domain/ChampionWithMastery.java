package com.wneild.domain;

public class ChampionWithMastery extends Champion {
    private final String highestGrade;

    public ChampionWithMastery(String name, String imageUrl, String highestGrade) {
        super(name, imageUrl);
        this.highestGrade = highestGrade;
    }

    public ChampionWithMastery(Champion champion, String highestGrade) {
        this(champion.getName(), champion.getImageUrl(), highestGrade);
    }

    public String getHighestGrade() {
        return highestGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChampionWithMastery that = (ChampionWithMastery) o;

        return highestGrade != null ? highestGrade.equals(that.highestGrade) : that.highestGrade == null;

    }

    @Override
    public int hashCode() {
        return highestGrade != null ? highestGrade.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ChampionWithMastery{" +
                "highestGrade='" + highestGrade + '\'' +
                '}';
    }
}
