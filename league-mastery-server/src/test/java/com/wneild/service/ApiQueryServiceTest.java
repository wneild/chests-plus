package com.wneild.service;

import com.wneild.domain.Champion;
import com.wneild.domain.ChampionWithMastery;
import com.wneild.dto.Region;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


public class ApiQueryServiceTest {

    private static final String API_KEY = "mypreciousapikey";
    private static final String API_KEY_PARAM_NAME = "api_key";
    private static final String IMAGE_URL = "urf_has_been_withdrawn.png";
    private static final String CHAMPION_NAME = "Urf";
    private static final String LATEST_VERSION = "10.10.10";
    private static final Region REGION = Region.EUW;
    private static final String SUMMONER_NAME = "Johnny";
    private static final Integer CHAMPION_ID = 9001;
    private static final Integer SUMMONER_ID = 19596625;
    private static final String HIGHEST_GRADE = "A+";
    private static final String EXPECTED_SUMMONER_API_CALL = String.format("https://%s.api.pvp.net/api/lol/%s/v1.4/summoner/by-name/%s?%s=%s", REGION.apiValue,
            REGION.apiValue, SUMMONER_NAME, API_KEY_PARAM_NAME, API_KEY);
    private static final String EXPECTED_CHAMPION_API_CALL = String.format("https://global.api.pvp.net/api/lol/static-data/%s/v1.2/champion?champData=image&%s=%s", REGION.apiValue, API_KEY_PARAM_NAME, API_KEY);
    private static final String EXPECTED_VERSIONS_API_CALL = String.format("https://global.api.pvp.net/api/lol/static-data/%s/v1.2/versions?%s=%s", REGION.apiValue, API_KEY_PARAM_NAME, API_KEY);
    private static final String EXPECTED_CHAMPION_IMAGE_API_CALL = String.format("https://ddragon.leagueoflegends.com/cdn/%s/img/champion/%s", LATEST_VERSION, IMAGE_URL);
    private static final String EXPECTED_CHAMPION_MASTERY_API_CALL = String.format("https://%s.api.pvp.net/championmastery/location/%s/player/%s/champions?%s=%s",
    REGION.apiValue, REGION.masteryApiValue, SUMMONER_ID, API_KEY_PARAM_NAME, API_KEY);;
    private byte[] VERSION_API_RESPONSE;
    private byte[] CHAMPION_MASTERY_API_RESPONSE;
    private byte[] SUMMONER_API_RESPONSE;
    private byte[] CHAMPION_API_RESPONSE;

    private MockRestServiceServer mockServer;
    private ApiQueryService apiQueryService;

    @Before
    public void setUp() throws Exception {
        loadTestResources();
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        apiQueryService = new ApiQueryService(API_KEY, restTemplate);
    }

    @Test
    public void shouldGetNextBestChampionForChestForGetNextBestChampionForChestWhenChampionMasteryApiRequestsSucceed() {
        mockSummonerApiRequest(true);
        mockChampionMasteryApiRequest(true);
        mockChampionApiRequest(true);
        mockVersionApiRequest(true);

        Optional<ChampionWithMastery> nextBestChampionForChest = apiQueryService.getNextBestChampionForChest(REGION, SUMMONER_NAME);

        ChampionWithMastery expectedChampion = new ChampionWithMastery(CHAMPION_NAME, EXPECTED_CHAMPION_IMAGE_API_CALL, HIGHEST_GRADE);
        assertEquals(expectedChampion, nextBestChampionForChest.get());
    }

    @Test
    public void shouldGetEmptyOptionalForGetNextBestChampionForChestWhenChampionMasteryApiRequestsFails() {
        mockSummonerApiRequest(true);
        mockChampionMasteryApiRequest(false);
        mockChampionApiRequest(true);
        mockVersionApiRequest(true);

        Optional<ChampionWithMastery> nextBestChampionForChest = apiQueryService.getNextBestChampionForChest(REGION, SUMMONER_NAME);

        assertEquals(Optional.empty(), nextBestChampionForChest);
    }

    @Test
    public void shouldGetEmptyOptionalForGetNextBestChampionForChestWhenSummonerApiRequestsFails() {
        mockSummonerApiRequest(false);
        mockChampionMasteryApiRequest(true);
        mockChampionApiRequest(true);
        mockVersionApiRequest(true);

        Optional<ChampionWithMastery> nextBestChampionForChest = apiQueryService.getNextBestChampionForChest(REGION, SUMMONER_NAME);

        assertEquals(Optional.empty(), nextBestChampionForChest);
    }

    @Test
    public void shouldGetEmptyOptionalForGetNextBestChampionForChestWhenChampionApiRequestsFails() {
        mockSummonerApiRequest(true);
        mockChampionMasteryApiRequest(true);
        mockChampionApiRequest(false);
        mockVersionApiRequest(true);

        Optional<ChampionWithMastery> nextBestChampionForChest = apiQueryService.getNextBestChampionForChest(REGION, SUMMONER_NAME);

        assertEquals(Optional.empty(), nextBestChampionForChest);
    }


    @Test
    public void shouldGetChampionWithoutImageUrlForGetNextBestChampionForChestWhenVersionApiRequestsFails() {
        mockSummonerApiRequest(true);
        mockChampionMasteryApiRequest(true);
        mockChampionApiRequest(true);
        mockVersionApiRequest(false);

        Optional<ChampionWithMastery> nextBestChampionForChest = apiQueryService.getNextBestChampionForChest(REGION, SUMMONER_NAME);

        ChampionWithMastery expectedChampion = new ChampionWithMastery(CHAMPION_NAME, null, HIGHEST_GRADE);
        assertEquals(expectedChampion, nextBestChampionForChest.get());
    }

    @Test
    public void shouldGetSummonerIdForGetSummonerIdWhenApiRequestSucceeds() {
        mockSummonerApiRequest(true);

        Optional<Integer> summonerId = apiQueryService.getSummonerId(REGION, SUMMONER_NAME);

        assertEquals(SUMMONER_ID, summonerId.get());
    }

    @Test
    public void shouldGetEmptyOptionalForGetSummonerIdWhenSummonerApiRequestSucceeds() {
        mockSummonerApiRequest(false);

        Optional<Integer> summonerId = apiQueryService.getSummonerId(REGION, SUMMONER_NAME);

        assertEquals(Optional.empty(), summonerId);
    }

    @Test
    public void shouldGetChampionForGetChampionWhenApiRequestSucceeds() {
        mockChampionApiRequest(true);
        mockVersionApiRequest(true);

        Optional<Champion> actualChampion = apiQueryService.getChampion(REGION, CHAMPION_ID);

        Champion expectedChampion = new Champion(CHAMPION_NAME, EXPECTED_CHAMPION_IMAGE_API_CALL);
        assertEquals(expectedChampion, actualChampion.get());
    }

    @Test
    public void shouldGetEmptyOptionalForGetChampionWhenChampionApiRequestFails() {
        mockChampionApiRequest(false);
        mockVersionApiRequest(true);

        Optional<Champion> actualChampion = apiQueryService.getChampion(REGION, CHAMPION_ID);

        assertEquals(Optional.empty(), actualChampion);
    }

    @Test
    public void shouldGetChampionWithoutImageUrlForGetChampionWhenVersionApiRequestFails() {
        mockChampionApiRequest(true);
        mockVersionApiRequest(false);

        Optional<Champion> actualChampion = apiQueryService.getChampion(REGION, CHAMPION_ID);

        Champion expectedChampion = new Champion(CHAMPION_NAME, null);
        assertEquals(expectedChampion, actualChampion.get());
    }

    @Test
    public void shouldGetLatestClientVersionForGetLatestClientVersionForRegionWhenApiRequestSucceeds() {
        mockVersionApiRequest(true);
        Optional<String> latestClientVersionForRegion = apiQueryService.getLatestClientVersionForRegion(REGION);
        mockServer.verify();
        assertEquals(LATEST_VERSION, latestClientVersionForRegion.get());
    }

    @Test
    public void shouldGetEmptyOptionalForGetLatestClientVersionForRegionWhenApiRequestFails() {
        mockVersionApiRequest(false);
        Optional<String> latestClientVersionForRegion = apiQueryService.getLatestClientVersionForRegion(REGION);
        mockServer.verify();
        assertEquals(Optional.empty(), latestClientVersionForRegion);
    }


    private void mockChampionMasteryApiRequest(boolean succeed) {
        if(succeed) {
            mockServer.expect(requestTo(EXPECTED_CHAMPION_MASTERY_API_CALL)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(CHAMPION_MASTERY_API_RESPONSE, MediaType.APPLICATION_JSON));
        } else {
            mockServer.expect(requestTo(EXPECTED_CHAMPION_MASTERY_API_CALL)).andExpect(method(HttpMethod.GET))
                    .andRespond(withServerError());
        }
    }

    private void mockSummonerApiRequest(boolean succeed) {
        if(succeed) {
            mockServer.expect(requestTo(EXPECTED_SUMMONER_API_CALL)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(SUMMONER_API_RESPONSE, MediaType.APPLICATION_JSON));
        } else {
            mockServer.expect(requestTo(EXPECTED_SUMMONER_API_CALL)).andExpect(method(HttpMethod.GET))
                    .andRespond(withServerError());
        }
    }

    private void mockChampionApiRequest(boolean succeed) {
        if(succeed) {
            mockServer.expect(requestTo(EXPECTED_CHAMPION_API_CALL)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(CHAMPION_API_RESPONSE, MediaType.APPLICATION_JSON));
        } else {
            mockServer.expect(requestTo(EXPECTED_CHAMPION_API_CALL)).andExpect(method(HttpMethod.GET))
                    .andRespond(withServerError());
        }
    }

    private void mockVersionApiRequest(boolean succeed) {
        if(succeed) {
            mockServer.expect(requestTo(EXPECTED_VERSIONS_API_CALL)).andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(VERSION_API_RESPONSE, MediaType.APPLICATION_JSON));
        } else {
            mockServer.expect(requestTo(EXPECTED_VERSIONS_API_CALL)).andExpect(method(HttpMethod.GET)).andRespond(withServerError());
        }
    }

    private void loadTestResources() throws IOException {
        SUMMONER_API_RESPONSE = getBytesForFile("apiresponse/summoner_response.json");
        CHAMPION_MASTERY_API_RESPONSE = getBytesForFile("apiresponse/champion_mastery_response.json");
        CHAMPION_API_RESPONSE = getBytesForFile("apiresponse/champion_response.json");
        VERSION_API_RESPONSE = getBytesForFile("apiresponse/version_response.json");
    }

    private byte[] getBytesForFile(String file) throws IOException {
        return Files.readAllBytes(new File(getClass().getClassLoader().getResource(file).getFile()).toPath());
    }
}