package com.wneild.service;

import com.wneild.domain.Champion;
import com.wneild.domain.ChampionWithMastery;
import com.wneild.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Service for querying Riot APIs
 */
@Service
public class ApiQueryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiQueryService.class);

    private static final String API_KEY_PARAM_NAME = "api_key";
    private final String riotApiKey;
    private final RestTemplate restTemplate;

    @Autowired
    public ApiQueryService(@Value("${riot.api.key}") String riotApiKey, RestTemplate restTemplate) {
        this.riotApiKey = riotApiKey;
        this.restTemplate = restTemplate;
    }

    /**
     * Attempts to get the next best champion for a summoner name that hasn't been used to get a Hextech Crafting chest
     * using the Riot champion mastery API.
     *
     * @param region the region the summoner name is registered
     * @param summonerName the summoner name to look up champion mastery
     * @return an Optional with a present champion if api requests were successful otherwise an empty Optional
     */
    @Cacheable("nextChampionForSummoner")
    public Optional<ChampionWithMastery> getNextBestChampionForChest(Region region, String summonerName) {
        Optional<Integer> summonerId = getSummonerId(region, summonerName);

        if(!summonerId.isPresent()) {
            return Optional.empty();
        }

        String url = String.format("https://%s.api.pvp.net/championmastery/location/%s/player/%s/champions?%s=%s",
                region.apiValue, region.masteryApiValue, summonerId.get(), API_KEY_PARAM_NAME, riotApiKey);
        List<ChampionMasteryDto> responseEntity = makeRequest(url, Collections.emptyList(), new ParameterizedTypeReference<List<ChampionMasteryDto>>() {
        });
        Optional<ChampionMasteryDto> oChampionMasteryDto = responseEntity.stream().filter(o -> !o.isChestGranted()).findFirst();

        return oChampionMasteryDto.flatMap(championMasteryDto ->
                getChampion(region, championMasteryDto.getChampionId())
                        .map(champion -> new ChampionWithMastery(champion, championMasteryDto.getHighestGrade())));
    }

    /**
     * Attempts to get the summoner id for a summoner name and region using the Riot summoner API.
     *
     * @param region the region the summoner name is registered
     * @param summonerName the summoner name to look up champion mastery
     * @return an Optional with a present summoner id if api requests were successful otherwise an empty Optional
     */
    @Cacheable("summonerId")
    public Optional<Integer> getSummonerId(Region region, String summonerName) {
        String url = String.format("https://%s.api.pvp.net/api/lol/%s/v1.4/summoner/by-name/%s?%s=%s", region.apiValue,
                region.apiValue, summonerName, API_KEY_PARAM_NAME, riotApiKey);
        Map<String, SummonerDto> responseEntity = makeRequest(url, Collections.<String, SummonerDto>emptyMap(), new ParameterizedTypeReference<Map<String, SummonerDto>>() {
        });
        return Optional.ofNullable(responseEntity.get(summonerName.toLowerCase().replaceAll(" ", ""))).map
                (SummonerDto::getId);
    }

    /**
     * Attempts to get champion information for a given champion id and region using the Riot summoner API.
     *
     * @param region the region to query for champion information
     * @param championId the champion id to get information for
     * @return an Optional with a present champion if api requests were successful otherwise an empty Optional
     */
    public Optional<Champion> getChampion(Region region, int championId) {
        Optional<ChampionListDto> championListDto = getChampionListDto(region);
        return championListDto.flatMap(dto -> dto.getData().entrySet().stream().filter(entry -> entry.getValue().getId()
                == championId).findAny().map(Map.Entry::getValue).map(champ -> new Champion(champ.getName(), getChampionImageUrl(region, champ).orElse(null))));
    }


    /**
     * Attempts to get the latest deployed League of Legends version for a region using the Riot static data API.
     *
     * @param region the region to query for version information
     * @return an Optional with a present latest version String if api requests were successful otherwise an empty Optional
     */
    @Cacheable("clientVersion")
    public Optional<String> getLatestClientVersionForRegion(Region region) {
        String url = String.format("https://global.api.pvp.net/api/lol/static-data/%s/v1.2/versions?%s=%s",
                region.apiValue, API_KEY_PARAM_NAME, riotApiKey);
        List<String> versions = makeRequest(url, Collections.emptyList(), new ParameterizedTypeReference<List<String>>() {
        });
        return versions.stream().findFirst();
    }

    /**
     * Attempts to get static champion information for all champion in a given region using the Riot static data API.
     *
     * @param region the region to query for champion information
     * @return an Optional with champion information if api requests were successful otherwise an empty Optional
     */
    @Cacheable("champions")
    private Optional<ChampionListDto> getChampionListDto(Region region) {
        String url = String.format("https://global.api.pvp.net/api/lol/static-data/%s/v1.2/champion?champData=image&%s=%s",
                region.apiValue, API_KEY_PARAM_NAME, riotApiKey);

        return Optional.ofNullable(makeRequest(url, null, new ParameterizedTypeReference<ChampionListDto>() {
        }));
    }

    /**
     * Attempts to get champion image url for a given champion and region.
     *
     * @param region the region to query for a the latest client version that is used in the URL
     * @param champ the champion information that includes the image file name
     * @return an Optional with champion image URL if api requests were successful otherwise an empty Optional
     */
    private Optional<String> getChampionImageUrl(Region region, ChampionDto champ) {
        String imageFileName = champ.getImage().getFull();
        Optional<String> oLatestVersion = getLatestClientVersionForRegion(region);
        return oLatestVersion.map(latestVersion -> String.format("https://ddragon.leagueoflegends.com/cdn/%s/img/champion/%s", latestVersion, imageFileName));
    }

    /**
     * Attempts to make a GET request to the provided URL and map the result to type T. If the request fails it gracefully returns the provided default value.
     *
     * @param url the URL to query
     * @param defaultValue default value to return if something goes wrong the with request or mapping
     * @param typeReference type reference to that captures more complicated generic types
     * @param <T> the type of object to attempt to map the request response to
     * @return the request response mapped to the provided type otherwise returns the provided default value
     */
    private <T> T makeRequest(String url, T defaultValue, ParameterizedTypeReference<T> typeReference) {
        try {
            LOGGER.info("Making API request {}", url);
            return restTemplate.exchange(url, HttpMethod.GET, null, typeReference).getBody();
        } catch (Exception e) {
            LOGGER.warn("Request {} failed with error {}", url, e.getMessage());
            return defaultValue;
        }
    }
}
