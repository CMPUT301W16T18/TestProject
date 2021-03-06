package ca.ualberta.cmput301w16t18.gamexchange;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Adam on 2016-03-14.
 */
public class GameTest extends TestCase {

    public void testGetId() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        assertEquals("Game-ID was not equal","Game-ID",game.getId());
    }

    public void testGetStatus() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        assertEquals("Status was not equal","Available",game.getStatus());
    }

    public void testSetStatus() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        game.setStatus("not");
        assertEquals("Status was not equal","not",game.getStatus());
    }

    public void testGetTitle() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        assertEquals("Title was not equal","Title",game.getTitle());
    }

    public void testSetTitle() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        game.setTitle("New Title");
        assertEquals("Title was not equal", "New Title", game.getTitle());
    }

    public void testGetDeveloper() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        assertEquals("Developer was not equal","Developer",game.getDeveloper());
    }

    public void testSetDeveloper() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        game.setDeveloper("new dev");
        assertEquals("Developer was not equal", "new dev", game.getDeveloper());
    }

    public void testGetPlatform() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        assertEquals("Platform was not equal","platform",game.getPlatform());
    }

    public void testSetPlatform() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        game.setPlatform("new platform");
        assertEquals("Platform was not equal", "new platform", game.getPlatform());
    }

    public void testGetGenres() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        assertEquals("Genres were not equal",genres,game.getGenres());
    }

    public void testSetGenres() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        genres.add("another one");
        game.setGenres(genres);
        assertEquals("Genres were not equal",genres,game.getGenres());
    }

    public void testGetDescription() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        assertEquals("Description was not equal","description",game.getDescription());
    }

    public void testSetDescription() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        game.setDescription("new Desc");
        assertEquals("Description was not equal", "new Desc", game.getDescription());
    }

    public void testGetPicture() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        assertEquals("Picture was not equal","Picture",game.getPicture());
    }

    public void testSetPicture() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);

        game.setPicture("new pic");
        assertEquals("Picture was not equal","new pic",game.getPicture());
    }
}