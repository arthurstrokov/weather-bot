package com.gmail.arthurstrokov.weather.dto;

public record City(int id, String name, Coord coord, String country, int population, 
                   int timezone, int sunrise, int sunset) {
}
