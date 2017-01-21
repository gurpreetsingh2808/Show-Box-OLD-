package com.popular_movies.database;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

/**
 * Created by Gurpreet on 16-01-2017.
 */

@SimpleSQLConfig(
        name = "MovieProvider",
        authority = "com.capstone.showbox",
        database = "movie.db",
        version = 2)

public class MovieProviderConfig implements ProviderConfig {
    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}
