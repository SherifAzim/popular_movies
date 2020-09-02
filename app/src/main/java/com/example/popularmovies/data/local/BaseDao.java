package com.example.popularmovies.data.local;


import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;


//BaseDao has a common functions tobe implemented in each dao class
public interface BaseDao<T> {


    //Insert one object
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(T entity);

    //Insert a list of objects
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<T> entities);
    //update one object
    @Update
    void update(T entity);
    //delete one object
    @Delete
    void delete(T entity);
}
