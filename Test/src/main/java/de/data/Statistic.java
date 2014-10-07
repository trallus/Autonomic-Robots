package de.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A Simple Statistic about won and lost Battles
 * 
 * @author Mike Kiekebusch
 * @version 0.1
 * @since 07.10.2014
 */
@Entity
public class Statistic {

    /**
     * The Id for the Persistence System, DO NOT TOUCH THIS!
     */
    @Id
    @GeneratedValue
    private long id;
    /**
     * The Number of won Battles
     */
    private int wonBattles;
    /**
     * The Number of lost Battles
     */
    private int lostBattles;

    /**
     * @return The number of Battles won
     */
    public int getWonBattles() {
	return wonBattles;
    }

    /**
     * @return The number of Battles lost
     */
    public int getLostBattles() {
	return lostBattles;
    }

    /**
     * Increases the number of Battles won by one
     */
    public void increaseWonBattles() {
	wonBattles++;
    }

    /**
     * Increase the number of Battles lost by one
     */
    public void increaseLostBattles() {
	lostBattles++;
    }
}
