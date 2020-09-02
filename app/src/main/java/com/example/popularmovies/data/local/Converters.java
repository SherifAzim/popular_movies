package com.example.popularmovies.data.local;

import androidx.room.TypeConverter;

import com.example.popularmovies.data.models.Genre;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Converters {

    private Gson gson = new Gson();

    @TypeConverter
    public String listOfIntegersToString(List<Integer> list) {

        return gson.toJson(list);
    }


    @TypeConverter
    public List<Integer> StringToListOfIntegers(String data) {

        if (data == null) return Collections.emptyList();

        Type listType = new TypeToken<List<Integer>>(){}.getType();
        return gson.fromJson(data,listType);

    }
    @TypeConverter
    public String listOfGenresToString(List<Genre> list) {

        return gson.toJson(list);
    }


    @TypeConverter
    public List<Genre> StringToListOfGenres(String data) {

        if (data == null) return Collections.emptyList();

        Type listType = new TypeToken<List<Genre>>(){}.getType();
        return gson.fromJson(data,listType);

    }
}
