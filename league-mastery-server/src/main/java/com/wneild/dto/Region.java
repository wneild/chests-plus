package com.wneild.dto;

import java.util.Optional;
import java.util.stream.Stream;

public enum Region {
    BR("br", "br1"), EUNE("eune", "eun1"), EUW("euw", "euw1"), JP("jp", "jp1"), KR("kr", "kr"), LAN("lan", "la1"), LAS("las", "la2"), NA("na", "na1"), OCE("oce", "oc1"), RU("ru", "ru"),
    TR("tr", "tr1");
    final public String apiValue, masteryApiValue;

    Region(String apiValue, String masteryApiValue) {this.apiValue = apiValue; this.masteryApiValue=masteryApiValue;}

    public static Optional<Region> fromString(String str) {
        return Stream.of(values()).filter(region -> str.equals(region.apiValue)).findAny();
    }
}
