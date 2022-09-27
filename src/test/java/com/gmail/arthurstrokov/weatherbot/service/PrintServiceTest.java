package com.gmail.arthurstrokov.weatherbot.service;

import com.gmail.arthurstrokov.weatherbot.dto.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrintServiceTest {
    /**
     * Method under test: {@link PrintService#formatMessage(WeatherForecastDto)}
     */
    @Test
    void testFormatMessage() {
        Coord coord = new Coord();
        coord.setLat(10.0d);
        coord.setLon(10.0d);

        City city = new City();
        city.setCoord(coord);
        city.setCountry("GB");
        city.setId(1);
        city.setName("Name");
        city.setPopulation(1);
        city.setSunrise(1);
        city.setSunset(1);
        city.setTimezone(1);

        WeatherForecastDto weatherForecastDto = new WeatherForecastDto();
        weatherForecastDto.setCity(city);
        weatherForecastDto.setCnt(1);
        weatherForecastDto.setCod("Cod");
        weatherForecastDto.setList(new ArrayList<>());
        weatherForecastDto.setMessage(1);
        assertEquals("\ncity: Name\n", PrintService.formatMessage(weatherForecastDto));
    }

    /**
     * Method under test: {@link PrintService#formatMessage(WeatherForecastDto)}
     */
    @Test
    void testFormatMessage2() {
        Coord coord = new Coord();
        coord.setLat(10.0d);
        coord.setLon(10.0d);

        City city = new City();
        city.setCoord(coord);
        city.setCountry("GB");
        city.setId(1);
        city.setName("Name");
        city.setPopulation(1);
        city.setSunrise(1);
        city.setSunset(1);
        city.setTimezone(1);

        Clouds clouds = new Clouds();
        clouds.setAll(1);

        Main main = new Main();
        main.setFeels_like(10.0d);
        main.setGrnd_level(1);
        main.setHumidity(1);
        main.setPressure(1);
        main.setSea_level(1);
        main.setTemp(10.0d);
        main.setTemp_kf(10.0d);
        main.setTemp_max(10.0d);
        main.setTemp_min(10.0d);

        Rain rain = new Rain();
        rain.set_3h(10.0d);

        Sys sys = new Sys();
        sys.setPod("\n");

        Wind wind = new Wind();
        wind.setDeg(1);
        wind.setGust(10.0d);
        wind.setSpeed(10.0d);

        List list = new List();
        list.setClouds(clouds);
        list.setDt(1);
        list.setDt_txt("\n");
        list.setMain(main);
        list.setPop(10.0d);
        list.setRain(rain);
        list.setSys(sys);
        list.setVisibility(1);
        list.setWeather(new ArrayList<>());
        list.setWind(wind);

        ArrayList<List> listList = new ArrayList<>();
        listList.add(list);

        WeatherForecastDto weatherForecastDto = new WeatherForecastDto();
        weatherForecastDto.setCity(city);
        weatherForecastDto.setCnt(1);
        weatherForecastDto.setCod("Cod");
        weatherForecastDto.setList(listList);
        weatherForecastDto.setMessage(1);
        assertEquals("\ncity: Name\n---\nTemp: 10.0 Feels_like: 10.0\n[]: []\nClouds(all=1)\nRain: Rain(_3h=10.0)\n\n\n",
                PrintService.formatMessage(weatherForecastDto));
    }

    /**
     * Method under test: {@link PrintService#formatMessage(WeatherForecastDto)}
     */
    @Test
    void testFormatMessage3() {
        Coord coord = new Coord();
        coord.setLat(10.0d);
        coord.setLon(10.0d);

        City city = new City();
        city.setCoord(coord);
        city.setCountry("GB");
        city.setId(1);
        city.setName("Name");
        city.setPopulation(1);
        city.setSunrise(1);
        city.setSunset(1);
        city.setTimezone(1);

        Clouds clouds = new Clouds();
        clouds.setAll(1);

        Main main = new Main();
        main.setFeels_like(10.0d);
        main.setGrnd_level(1);
        main.setHumidity(1);
        main.setPressure(1);
        main.setSea_level(1);
        main.setTemp(10.0d);
        main.setTemp_kf(10.0d);
        main.setTemp_max(10.0d);
        main.setTemp_min(10.0d);

        Rain rain = new Rain();
        rain.set_3h(10.0d);

        Sys sys = new Sys();
        sys.setPod("\n");

        Wind wind = new Wind();
        wind.setDeg(1);
        wind.setGust(10.0d);
        wind.setSpeed(10.0d);

        List list = new List();
        list.setClouds(clouds);
        list.setDt(1);
        list.setDt_txt("\n");
        list.setMain(main);
        list.setPop(10.0d);
        list.setRain(rain);
        list.setSys(sys);
        list.setVisibility(1);
        list.setWeather(new ArrayList<>());
        list.setWind(wind);

        Clouds clouds1 = new Clouds();
        clouds1.setAll(1);

        Main main1 = new Main();
        main1.setFeels_like(10.0d);
        main1.setGrnd_level(1);
        main1.setHumidity(1);
        main1.setPressure(1);
        main1.setSea_level(1);
        main1.setTemp(10.0d);
        main1.setTemp_kf(10.0d);
        main1.setTemp_max(10.0d);
        main1.setTemp_min(10.0d);

        Rain rain1 = new Rain();
        rain1.set_3h(10.0d);

        Sys sys1 = new Sys();
        sys1.setPod("\n");

        Wind wind1 = new Wind();
        wind1.setDeg(1);
        wind1.setGust(10.0d);
        wind1.setSpeed(10.0d);

        List list1 = new List();
        list1.setClouds(clouds1);
        list1.setDt(1);
        list1.setDt_txt("\n");
        list1.setMain(main1);
        list1.setPop(10.0d);
        list1.setRain(rain1);
        list1.setSys(sys1);
        list1.setVisibility(1);
        list1.setWeather(new ArrayList<>());
        list1.setWind(wind1);

        ArrayList<List> listList = new ArrayList<>();
        listList.add(list1);
        listList.add(list);

        WeatherForecastDto weatherForecastDto = new WeatherForecastDto();
        weatherForecastDto.setCity(city);
        weatherForecastDto.setCnt(1);
        weatherForecastDto.setCod("Cod");
        weatherForecastDto.setList(listList);
        weatherForecastDto.setMessage(1);
        assertEquals(
                "\n" + "city: Name\n" + "---\n" + "Temp: 10.0 Feels_like: 10.0\n" + "[]: []\n" + "Clouds(all=1)\n"
                        + "Rain: Rain(_3h=10.0)\n" + "\n" + "\n" + "---\n" + "Temp: 10.0 Feels_like: 10.0\n" + "[]: []\n"
                        + "Clouds(all=1)\n" + "Rain: Rain(_3h=10.0)\n" + "\n" + "\n",
                PrintService.formatMessage(weatherForecastDto));
    }
}
