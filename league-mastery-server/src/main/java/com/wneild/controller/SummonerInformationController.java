package com.wneild.controller;

import com.wneild.domain.Champion;
import com.wneild.dto.Region;
import com.wneild.service.ApiQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class SummonerInformationController {
    private final ApiQueryService apiQueryService;

    @Autowired
    public SummonerInformationController(ApiQueryService apiQueryService) {this.apiQueryService = apiQueryService;}

    /**
     * Attempts to get the next best champion for a summoner name that hasn't been used to get a Hextech Crafting chest
     * using the Riot champion mastery API.
     *
     * @param region the region the summoner name is registered
     * @param summonerName the summoner name to look up champion mastery
     * @return champion information if information was succesfully looked up for the provided summoner name and region, otherwise returns an empty model with a 404 response
     */
    @RequestMapping(value = "getNextChampionChest/region/{region}/summonerName/{summonerName}", method = RequestMethod
            .GET)
    @ResponseBody
    public ResponseEntity<?> getGetNextChampionChest(@PathVariable String region, @PathVariable String summonerName) {
        Optional<Region> parsedRegion = parseRegion(region);
        return apiQueryService.getNextBestChampionForChest(parsedRegion.get(), summonerName).map(championName ->
                     new ResponseEntity<>(championName, HttpStatus.OK)).orElseGet(
                () -> new ResponseEntity<>(new Champion(null, null), HttpStatus.NOT_FOUND));
    }

    /**
     * Attempts to get the summoner id for a summoner name and region using the Riot summoner API.
     *
     * @param region the region the summoner name is registered
     * @param summonerName the summoner name to look up champion mastery
     * @return the summoner id of the provided summoner name and region if api requests were successful otherwise returns an error message with a 404 response
     */
    @RequestMapping(value = "getSummonerId/region/{region}/summonerName/{summonerName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getSummonerId(@PathVariable String region, @PathVariable String summonerName) {
        Optional<Region> parsedRegion = parseRegion(region);
        if (!parsedRegion.isPresent()) {
            return new ResponseEntity<>("Unrecognised region identifier", HttpStatus.BAD_REQUEST);
        }
        return apiQueryService.getSummonerId(parsedRegion.get(), summonerName).map(Object::toString)
                              .map(summonerId -> new ResponseEntity<>(summonerId, HttpStatus.OK))
                              .orElseGet(() -> new ResponseEntity<>("Unable to find summoner id for the summoner name " +
                                                                            "under that region",
                                                                    HttpStatus.NOT_FOUND));
    }

    private Optional<Region> parseRegion(String region) {
        return Region.fromString(region.toLowerCase());
    }
}
