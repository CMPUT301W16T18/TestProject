package ca.ualberta.cmput301w16t18.gamexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class SearchListActivity extends AppCompatActivity {

    private ListView drawerListView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private SearchListListViewArrayAdapter adapter;
    private ListView listView;
    private SearchListActivity searchListActivity;
    FloatingActionButton fab;
    private String type;

    private View mProgressView;
    private View mListViewView;

    public GameList games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchListActivity = this;
        games = new GameList();
        handleIntent(getIntent());
        setContentView(R.layout.activity_search_list);


        mListViewView = findViewById(R.id.searchListAllView);
        mProgressView = findViewById(R.id.search_progress);

        //Initialise FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchListActivity.this, GameProfileEditActivity.class);
                intent.putExtra(Constants.GAME_ID, "");
                startActivity(intent);
            }
        });

        if(type == null) {
            Log.d("Null Pointer", "Intent for SearchListActivity was started without the SEARCH_LIST_ACTIVITY_ACTION added");
        } else if(type.equals(Constants.BORROWED_GAMES)) {
            setTitle("Borrowed Games");
            fab.setVisibility(View.GONE);
        } else if(type.equals(Constants.WATCH_LIST)) {
            setTitle("Watch List");
            fab.setVisibility(View.GONE);
        } else {
            //Default to my Games
            setTitle("My Games");
            fab.setVisibility(View.VISIBLE);
        }

        showProgress(true);
        ElasticSearcher.receiveGames(type, this);

        //Create Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        // set adapter for drawer ListView
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.navigation_drawer_items_section)));
        //Set onclick listener
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch(NullPointerException ex) {
            Log.d("Navigation Drawer", "Got a null pointer from get support action bar. " + ex.getMessage());
        }
        getSupportActionBar().setHomeButtonEnabled(true);

        //mTitle = mDrawerTitle = getTitle().toString();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //Initialize ListView
        listView = (ListView) findViewById(R.id.searchListActivityListView);
        adapter = new SearchListListViewArrayAdapter(this, games.getGames());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchListActivity.this, GameProfileViewActivity.class);
                String gameID = games.getGames().get(position).getId();
                intent.putExtra(Constants.GAME_ID, gameID);
                startActivity(intent);
            }
        });

        //Initialize Gesture detector
        CustomGestureDetector mDetector = new CustomGestureDetector(this, SearchListActivity.this, listView);
        listView.setOnTouchListener(mDetector);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.tutorialTextView))
                .setDismissText("GOT IT")
                .setContentText("Touch a game to get details !!! " +
                        "Touch the hamburger icon in the top left corner to navigate around " +
                        "the app!! Also swipe left on a game to delete!!")
                .setDelay(1) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("list view")// provide a unique ID used to ensure it is only shown once
                .show();
        TextView view = (TextView) findViewById(R.id.tutorialTextView);
        view.setVisibility(View.GONE);
    }

    /**
     * Used to create the search bar and use it
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        type = intent.getStringExtra(Constants.SEARCH_LIST_ACTIVITY_ACTION);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            ElasticSearcher.receiveGamesBySearchTerm(query, searchListActivity);
            setTitle("Results for \"" + query + "\"");
        }
    }

    @Override
    public void onBackPressed() {
        //Modified from http://stackoverflow.com/questions/2257963/how-to-show-a-dialog-to-confirm-that-the-user-wishes-to-exit-an-android-activity
        new AlertDialog.Builder(this).setMessage(R.string.alert_dialog_message)
                .setPositiveButton(R.string.alert_dialog_logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constants.CURRENT_USER = new User();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
        .show();
        }


    /**
     * Updates the primary ArrayList with a new list of games.
     * @param gameList list of games.
     */
    public void setDisplayedList (GameList gameList) {
        games.clear();
        games.addAll(gameList);
        adapter.notifyDataSetChanged();
        showProgress(false);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        // Fixed as per lint.
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * adds a game to the elastic search database along with the owner.
     * @param mygame game to be added
     */
    public void addGame(Game mygame) {
        //implements US 01.01.01
        //add a game to a user's list
        games.add(mygame);
    }

    /**
     * deletes a game from Elastic search by game ID
     * @param id game id to be deleted
     */
    public void deleteGame(String id) {
        games.removeGame(id);
        adapter.notifyDataSetChanged();
    }

    /**
     * Deletes a game from Elastic search by position
     * @param position position in the gameslist to be deleted
     */
    public void deleteGameByPosition(int position) {
        //implements US 01.05.01
        //deletes a game from a user's list
        Game mygame = games.getGames().get(position);
        ElasticSearcher.deleteGame(mygame.getId(), this);
    }

    /**
     * Listener for navigation drawer item clicks
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            //Use position to navigate to the right dataset.
            //Added Switch cases to control where the buttons will go
            Intent intent;
            switch (position){
                case 0:
                    setTitle("My Games");
                    fab.setVisibility(View.VISIBLE);
                    showProgress(true);
                    ElasticSearcher.receiveGames(Constants.MY_GAMES, searchListActivity);
                    mDrawerLayout.closeDrawers();
                    break;
                case 1:
                    setTitle("Borrowed Games");
                    fab.setVisibility(View.GONE);
                    showProgress(true);
                    ElasticSearcher.receiveGames(Constants.ALL_GAMES, searchListActivity);
                    mDrawerLayout.closeDrawers();
                    break;
                case 2:
                    setTitle("Watch List");
                    fab.setVisibility(View.GONE);
                    showProgress(true);
                    ElasticSearcher.receiveGames(Constants.WATCH_LIST, searchListActivity);
                    mDrawerLayout.closeDrawers();
                    break;
                case 3:
                    setTitle("Notifications");
                    fab.setVisibility(View.GONE);
                    mDrawerLayout.closeDrawers();
                    Toast.makeText(SearchListActivity.this,"No notifications for you",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    intent = new Intent(SearchListActivity.this, UserProfileViewActivity.class);
                    intent.putExtra(Constants.USER_ID, Constants.CURRENT_USER.getId());
                    startActivity(intent);
                    break;
                case 5:
                    Constants.CURRENT_USER = new User();
                    finish();
                    break;
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mListViewView.setVisibility(show ? View.GONE : View.VISIBLE);
            mListViewView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mListViewView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mListViewView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
