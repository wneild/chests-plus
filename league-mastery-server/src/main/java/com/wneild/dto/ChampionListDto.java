package com.wneild.dto;

import java.util.Map;

public class ChampionListDto {
    private Map<String, ChampionDto> data;
    private String format;
    private Map<String, String> keys;
    private String type;
    private String version;

    public Map<String, ChampionDto> getData() {
        return data;
    }

    public void setData(Map<String, ChampionDto> data) {
        this.data = data;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Map<String, String> getKeys() {
        return keys;
    }

    public void setKeys(Map<String, String> keys) {
        this.keys = keys;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChampionListDto that = (ChampionListDto) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (format != null ? !format.equals(that.format) : that.format != null) return false;
        if (keys != null ? !keys.equals(that.keys) : that.keys != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return version != null ? version.equals(that.version) : that.version == null;

    }

    @Override
    public int hashCode() {
        int result = data != null ? data.hashCode() : 0;
        result = 31 * result + (format != null ? format.hashCode() : 0);
        result = 31 * result + (keys != null ? keys.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChampionListDto{" +
                "data=" + data +
                ", format='" + format + '\'' +
                ", keys=" + keys +
                ", type='" + type + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
