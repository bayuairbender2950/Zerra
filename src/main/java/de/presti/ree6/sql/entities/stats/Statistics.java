package de.presti.ree6.sql.entities.stats;

import com.google.gson.JsonObject;
import de.presti.ree6.sql.base.annotations.Property;
import de.presti.ree6.sql.base.annotations.Table;
import de.presti.ree6.sql.base.entities.SQLEntity;

/**
 * SQL Entity for statistics.
 */
@Table(name = "Statistics")
public class Statistics extends SQLEntity {

    /**
     * The day of the statistic.
     */
    @Property(name = "day")
    int day;

    /**
     * The month of the statistic.
     */
    @Property(name = "month")
    int month;

    /**
     * The year of the statistic.
     */
    @Property(name = "year")
    int year;

    /**
     * The {@link JsonObject} with statistics.
     */
    @Property(name = "stats", keepOriginalValue = false, updateQuery = true)
    JsonObject statsObject;

    /**
     * Constructor.
     */
    public Statistics() {
    }

    /**
     * Constructor.
     * @param day the day of the Statistic.
     * @param month the month of the Statistic.
     * @param year the year of the Statistic.
     * @param statsObject the Object of the Statistic.
     */
    public Statistics(int day, int month, int year, JsonObject statsObject) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.statsObject = statsObject;
    }

    /**
     * Getter for the day of the statistic.
     * @return the day.
     */
    public int getDay() {
        return day;
    }

    /**
     * Getter for the month of the statistic.
     * @return the month.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Getter for the year of the statistic.
     * @return the year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Getter for the {@link JsonObject} with statistics.
     * @return the {@link JsonObject}
     */
    public JsonObject getStatsObject() {
        return statsObject;
    }

    /**
     * Setter for the {@link JsonObject} with new statistics.
     * @param statsObject the n ew statistics Object.
     */
    public void setStatsObject(JsonObject statsObject) {
        this.statsObject = statsObject;
    }
}