package com.erichorvat.rvgnet.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.adapter.BaseInflaterAdapter;
import com.erichorvat.rvgnet.adapter.GameInflater;
import com.erichorvat.rvgnet.adapter.PlatformInflater;
import com.erichorvat.rvgnet.adapter.StoreInflater;
import com.erichorvat.rvgnet.interfaces.OnChange;
import com.erichorvat.rvgnet.interfaces.OnSearch;
import com.erichorvat.rvgnet.interfaces.OnStart;
import com.erichorvat.rvgnet.model.Answer;
import com.erichorvat.rvgnet.model.Game;
import com.erichorvat.rvgnet.model.Platform;
import com.erichorvat.rvgnet.model.Question;
import com.erichorvat.rvgnet.model.Store;
import com.erichorvat.rvgnet.tasks.AsyncTaskRunner;
import com.erichorvat.rvgnet.util.Constants;
import com.erichorvat.rvgnet.util.RvgApp;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

@EActivity
public class HomeActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener{

    SlidingMenu menu;

    RelativeLayout rlLeft, rlRight;

    BaseInflaterAdapter<Store> store_adapter;

    BaseInflaterAdapter<Platform> platform_adapter;

    BaseInflaterAdapter<Game> game_adapter;

    ListView lvStores, lvPlatforms, lvGames;

    View headerView, selectedView;

    private static String PREFS = "prefs";

    private static String PREFS_SELECTED_STORE = "store_selection";

    private static String PREFS_SELECTED_PLATFORM = "platform_selection";

    private static int NUM_GAMES_PER_PAGE = 10;

    int selectedStorePos, selectedPlatformPos, pageCount, increment;

    boolean start, store, onChange;

    String platform;

    Animation a;

    Button next, back, search;

    EditText searchBox;

    JSONArray posts;

    @InjectView(R.id.tvSelectedStore) TextView selectedStore;
    @InjectView(R.id.tvSelectedPlatform) TextView tvSelectedPlatform;

    TextView tvRecycle, tvBuilding, tvQuestion, currPage,tvSearch;

    Typeface awesome, lato;

    ArrayList<Game> games;
    ArrayList<Platform> platforms;

    AsyncLoad alg;

    DB snappydb;

    RvgApp app;

    OnStart onStart = new OnStart(){

        //Initial query callback

        @Override
        public void onStart(String data) {

            JSONObject main = null;

            try {
                main = new JSONObject(data);

                int success = main.getInt("success");

                if(success != 0){

                    posts = main.getJSONArray("posts");

                        int val = posts.length()%NUM_GAMES_PER_PAGE;
                        val = val==0?0:1;
                        pageCount = posts.length()/NUM_GAMES_PER_PAGE+val;

                        alg = new AsyncLoad(posts);
                        alg.execute();



                    next.setEnabled(true);


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };

    OnChange change = new OnChange(){

        @Override
        public void onChange(String data) {

            JSONObject main = null;

            try {
                main = new JSONObject(data);

                int success = main.getInt("success");

                if(success != 0){

                    posts = main.getJSONArray("posts");


                    int val = posts.length()%NUM_GAMES_PER_PAGE;
                    val = val==0?0:1;
                    pageCount = posts.length()/NUM_GAMES_PER_PAGE+val;

                    alg = new AsyncLoad(posts);
                    alg.execute();

                    if(store){
                       store_adapter.notifyDataSetChanged();
                    }else{
                       platform_adapter.notifyDataSetChanged();
                    }

                    next.setEnabled(true);

                    tvRecycle.setVisibility(View.GONE);
                    tvRecycle.clearAnimation();

                }else{
                    tvRecycle.clearAnimation();
                    tvRecycle.setText("Out of stock");
                    tvRecycle.setTextColor(Color.RED);
                    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    OnSearch searching = new OnSearch() {

        @Override
        public void onNoMatch(ArrayList<Game> values, int count) {
            loadList(0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            DB snappydb = DBFactory.open(HomeActivity.this); //create or open an existing databse using the default name
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        start = true;
        onChange = false;
        awesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        lato = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");

        LayoutInflater inflator=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = inflator.inflate(R.layout.header, null);

        app = (RvgApp)getApplicationContext();


            AsyncTaskRunner runner = new AsyncTaskRunner(AsyncTaskRunner.GET_TASK, getApplicationContext()
                    , HomeActivity.this, Constants.ON_START, onStart);

            runner.execute(Constants.URL_DEFAULT_DATA + "?store=nb_qty&platform=Playstation%202");

        ButterKnife.inject(this, headerView);

        tvRecycle = (TextView) findViewById(R.id.tvRecycleIcon);
        tvRecycle.setTypeface(awesome);

        tvSearch = (TextView) findViewById(R.id.tvSearch);
        tvSearch.setTypeface(awesome);

        searchBox = (EditText) findViewById(R.id.etSearchBox);
        searchBox.setTypeface(lato);
        searchBox.setHint("Search a Playstation 2 game...");

        a = AnimationUtils.loadAnimation(this, R.anim.custom_spinner);
        currPage = (TextView) findViewById(R.id.title);

        store_adapter = new BaseInflaterAdapter<Store>(new StoreInflater(getApplicationContext()), null);
        platform_adapter = new BaseInflaterAdapter<Platform>(new PlatformInflater(getApplicationContext()),null);


        tvRecycle.setVisibility(View.VISIBLE);
        tvRecycle.startAnimation(a);

        back = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);

        back.setEnabled(false);
        next.setEnabled(false);
        next.setTypeface(awesome);
        back.setTypeface(awesome);

        /********* Right menu layout **********/
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rlRight = (RelativeLayout) layoutInflater.inflate(R.layout.rl_right, null);
        tvQuestion = (TextView) rlRight.findViewById(R.id.tvQuestion);
        tvQuestion.setOnClickListener(this);
        tvQuestion.setTypeface(awesome);

        /********* Left menu layout **********/
        rlLeft = (RelativeLayout) layoutInflater.inflate(R.layout.rl_left, null);
        tvBuilding = (TextView)rlLeft.findViewById(R.id.tvStores);
        tvBuilding.setTypeface(awesome);

        lvStores = (ListView) rlLeft.findViewById(R.id.lvStores);
        lvStores.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lvPlatforms = (ListView) rlRight.findViewById(R.id.lvPlatforms);
        lvPlatforms.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lvGames = (ListView) findViewById(R.id.lvGames);


        lvGames.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView listView, int scrollState) {
                // Pause disk cache access to ensure smoother scrolling
                Picasso picasso = Picasso.with(getApplicationContext());
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    picasso.pauseTag("img");
                } else {
                    picasso.resumeTag("img");
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
            }
        });


        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT_RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setBehindOffset(400);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(rlLeft);
        menu.setSecondaryMenu(rlRight);


        selectedStore.setOnClickListener(this);
        tvSelectedPlatform.setOnClickListener(this);


        // Set up your ActionBar
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(headerView);
        a.setDuration(1000);

        new AsyncLoadSideMenus().execute();



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();


        next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(pageCount!=1){
                    increment++;
                    CheckEnable();
                    loadList(increment);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(pageCount!=1){
                    increment--;
                    CheckEnable();
                    loadList(increment);
                }
            }
        });

        menu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                if (game_adapter != null && onChange) {


                    onChange = false;
                    games.clear();
                    game_adapter.clear(true);

                    AsyncTaskRunner runner = new AsyncTaskRunner(AsyncTaskRunner.GET_TASK, getApplicationContext()
                            , HomeActivity.this, Constants.ON_CHANGE, change);

                    runner.execute(Constants.URL_DEFAULT_DATA + "?store=" + store_adapter.getTItem(selectedStorePos).getSqlName().trim()
                            + "&platform=" + platform);

                }
            }
        });

        searchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    //this is for backspace

                    if(searchBox.getText().toString().length() == 0){

                        String phrase = searchBox.getText().toString().trim();

                        game_adapter.getFilter().filter(phrase);




                    }

                }
                return false;
            }
        });

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEARCH) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    if(searchBox.getText().length() > 0) {

                            String phrase = searchBox.getText().toString().trim();


                            game_adapter.getFilter().filter(phrase, new Filter.FilterListener() {
                                @Override
                                public void onFilterComplete(final int count) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            currPage.setText(count + " results");
                                        }
                                    });
                                }
                            });


                }
                        handled = true;
                    }

                return handled;

            }
        });








    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        next.setClickable(true);
        next.setEnabled(true);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                tvRecycle.setText(getResources().getString(R.string.icon_recycle));
                tvRecycle.setTextColor(getResources().getColor(R.color.emerald));

                tvRecycle.setVisibility(View.VISIBLE);
                tvRecycle.startAnimation(a);
                tvRecycle.bringToFront();


            }
        });
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        onChange=true;

        if(!start){
            selectedView.setBackgroundColor(Color.TRANSPARENT);
        }

        if(view!=null){
            selectedView = view;
            selectedView.setBackgroundColor(getResources().getColor(R.color.almost_aqua));
        }



        next.setEnabled(false);
        back.setEnabled(false);

        increment = 0;

        Store s2 = null;

        Platform p2 = null;

        switch(parent.getId()) {

            case R.id.lvStores:

                store = true;

                Store s1 = (Store) store_adapter.getItem(selectedStorePos);
                s1.setHighlighted(false);

                selectedStorePos = position;

                s2 = (Store) store_adapter.getItem(position);
                s2.setHighlighted(true);


                selectedStore.setText(s2.getStore_name());


                //store_adapter.notifyDataSetChanged();


                break;

            case R.id.lvPlatforms:
                store = false;


                Platform p1 = (Platform) platform_adapter.getItem(selectedPlatformPos);
                p1.setHighlighted(false);

                selectedPlatformPos = position;

                p2 = (Platform) platform_adapter.getItem(position);
                p2.setHighlighted(true);

                //platform_adapter.notifyDataSetChanged();


                break;

        }

        platform = platform_adapter.getTItem(selectedPlatformPos).getName().trim().replace(" ","%20");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchBox.setText("");
                searchBox.setHint("Search a " + platform_adapter.getTItem(selectedPlatformPos).getName().trim() + " game...");
            }
        });

        menu.toggle();



    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.tvSelectedStore:

                menu.showMenu();

                break;


            case R.id.tvSelectedPlatform:

                menu.showSecondaryMenu();

                break;

            case R.id.tvQuestion:

                //loadQuestions();

                //Start questions activity
                Intent i = new Intent(HomeActivity.this, ForumActivity_.class);
                startActivity(i);
                finish();

                overridePendingTransition(R.anim.right_slide_in,
                        R.anim.left_slide_out);

        }

    }

    @Background
    void loadQuestions(){
        // The connection URL


        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Add the String message converter
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Make the HTTP GET request, marshaling the response to a String
        String response = restTemplate.getForObject(Constants.URL_QUESTIONS, String.class, "Android");

        try {
            JSONObject r = new JSONObject(response);

            int success = r.getInt("success");

            if(success != 0){


                snappydb = DBFactory.open(HomeActivity.this); //create or open an existing databse using the default name

                //snappydb.put("username", r.getString("username"));

                JSONArray jQuestions = r.getJSONArray("posts");
                Question questions [] = new Question[jQuestions.length()];

                for(int i = 0; i<jQuestions.length(); i++){
                    JSONObject obj = jQuestions.getJSONObject(i);
                    Question question = new Question(obj.getInt("qid"),obj.getString("title"), obj.getString("body"),
                            obj.getString("platform"), obj.getString("username"),
                            obj.getString("tstamp"));

                    if(obj.getJSONArray("answers").length() > 0){
                        ArrayList<Answer> answers = new ArrayList<Answer>();

                        JSONArray jsonAnswers = obj.getJSONArray("answers");

                        for(int j = 0; j<jsonAnswers.length(); j++){

                            JSONObject jsonAnswer = jsonAnswers.getJSONObject(j);

                            Answer answer = new Answer(jsonAnswer.getInt("aid"), jsonAnswer.getString("answer"),
                                    jsonAnswer.getInt("qid"), jsonAnswer.getString("username"), jsonAnswer.getString("tstamp"));

                            answers.add(answer);
                        }

                        question.setAnswers(answers);

                    }

                    questions[i] = question;

                }



                snappydb.put("questions", questions);


                snappydb.close();


                //Start questions activity
                Intent i = new Intent(HomeActivity.this, ForumActivity_.class);
                startActivity(i);


                overridePendingTransition(R.anim.right_slide_in,
                        R.anim.left_slide_out);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Start questions activity
            Intent i = new Intent(HomeActivity.this, ForumActivity_.class);
            startActivity(i);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    private class AsyncLoad extends AsyncTask<Void, Void, Void>{


        Store store = null;

        Platform platform = null;

        JSONArray posts = null;

        public AsyncLoad(JSONArray posts){
            this.posts=posts;
        }


        public AsyncLoad(Store store, Platform platform){
            this.store = store;
            this.platform = platform;
        }



        @Override
        protected Void doInBackground(Void... params) {
            try {
                games = new ArrayList<Game>();
                for(int i=0; i<posts.length();i++){
                    JSONObject o = posts.getJSONObject(i);
                    Game game = new Game(o.getString("title"), o.getString("platform"),
                            o.getDouble("price"), o.getDouble("trade"), o.getInt("nbqty"),
                            o.getInt("npqty"), o.getInt("bkqty"), o.getInt("tmqty"),
                            Constants.IMG_PARENT_DIR + "/"+  o.getInt("skuid") + ".jpg", o.getString("picture"));
                    games.add(game);
                }
                app.setGames(games);
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            loadList(0);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    tvRecycle.setVisibility(View.GONE);
                    tvRecycle.clearAnimation();
                }
            });

            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();


        }
    }


    /**
     * Method for enabling and disabling Buttons
     */
    private void CheckEnable()
    {
        if(increment+1 == pageCount)
        {
            next.setEnabled(false);
            next.setClickable(false);
        }
        else if(increment == 0)
        {
            back.setEnabled(false);
        }
        else
        {
            back.setEnabled(true);
            next.setEnabled(true);
            next.setClickable(true);
        }
    }

    /**
     * Method for loading data in listview
     * @param number
     */
    private void loadList(final int number){

        AsyncLoadList loader = new AsyncLoadList(number);
        loader.execute();
    }

    private class AsyncLoadList extends AsyncTask<Void, Void, Void>{

        int number;

        public AsyncLoadList(int number){
            this.number=number;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            lvGames.setAdapter(game_adapter);

            if (games.size() != 0) {
                next.setEnabled(true);
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            final ArrayList<Game> sort = new ArrayList<Game>();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currPage.setText("Page " + (number + 1) + " of " + pageCount);


                    int start = number * NUM_GAMES_PER_PAGE;
                    for (int i = start; i < (start) + NUM_GAMES_PER_PAGE; i++) {
                        if (i < games.size()) {
                            sort.add(games.get(i));
                        } else {
                            break;
                        }
                    }

                    game_adapter = new BaseInflaterAdapter<Game>(new GameInflater(getApplicationContext()), games, searching);

                    game_adapter.addItems(sort, true);

                }
            });
            return null;
        }
    }


    private class AsyncLoadSideMenus extends AsyncTask<Void, Void, ArrayList<Store>>{

        @Override
        protected ArrayList<Store> doInBackground(Void... params) {

            ArrayList<Store> stores = new ArrayList<Store>();
            stores.add(0,new Store(Constants.STORE_RVG, Constants.ADDRESS_RVG, Constants.PHONE_RVG,true, Constants.ABBRV_RVG));
            stores.add(1,new Store(Constants.STORE_101, Constants.ADDRESS_101, Constants.PHONE_101,false, Constants.ABBRV_101));
            stores.add(2,new Store(Constants.STORE_AWESOME, Constants.ADDRESS_AWESOME, Constants.PHONE_AWESOME,false, Constants.ABBRV_AWESOME));
            stores.add(3,new Store(Constants.STORE_ABC, Constants.ADDRESS_ABC, Constants.PHONE_ABC,false, Constants.ABBRV_ABC));

            platforms = new ArrayList<Platform>();
            platforms.add(0, new Platform("Playstation 2", true));
            platforms.add(1, new Platform("Playstation 3", false));
            platforms.add(2, new Platform("Playstation 4", false));
            platforms.add(3, new Platform("Xbox", false));
            platforms.add(4, new Platform("Xbox 360", false));
            platforms.add(5, new Platform("Xbox One", false));
            platforms.add(6, new Platform("Gamecube", false));
            platforms.add(7, new Platform("Nintendo DS", false));
            platforms.add(8, new Platform("Nintendo 3DS",false));
            platforms.add(9, new Platform("Nintendo Wii", false));
            platforms.add(10, new Platform("Nintendo Wii U", false));
            platforms.add(11, new Platform("Sony PSP", false));
            platforms.add(12, new Platform("Gameboy Advanced", false));
            platforms.add(13, new Platform("PC Games", false));
            platforms.add(14, new Platform("DVD Movies", false));
            platforms.add(15, new Platform("Universal Media Disc",false));
            platforms.add(16, new Platform("Blue-Ray Disc", false));
            platforms.add(17, new Platform("Dreamcast", false));
            platforms.add(18, new Platform("Gameboy", false));
            platforms.add(19, new Platform("Gameboy Color", false));
            platforms.add(20, new Platform("Gamegear", false));
            platforms.add(21, new Platform("Genesis", false));
            platforms.add(22, new Platform("Nintendo", false));
            platforms.add(23, new Platform("Nintendo 64", false));
            platforms.add(24, new Platform("Playstation", false));
            platforms.add(25, new Platform("Super Nintendo", false));




            store_adapter.addItems(stores, true);

            platform_adapter.addItems(platforms, true);

            lvStores.setOnItemClickListener(HomeActivity.this);
            lvPlatforms.setOnItemClickListener(HomeActivity.this);

            SharedPreferences pref = getSharedPreferences(PREFS, MODE_PRIVATE);
            selectedStorePos = pref.getInt(PREFS_SELECTED_STORE, 0);
            selectedPlatformPos = pref.getInt(PREFS_SELECTED_PLATFORM, 0);



            return stores;
        }

        @Override
        protected void onPostExecute(ArrayList<Store> stores) {
            super.onPostExecute(stores);
            selectedStore.setTypeface(lato);
            selectedStore.setText(stores.get(0).getStore_name());

            tvSelectedPlatform.setTypeface(awesome);

            lvStores.setAdapter(store_adapter);
            lvPlatforms.setAdapter(platform_adapter);

            tvRecycle.setVisibility(View.GONE);


        }
    }

    @Override
    public void onBackPressed() {


        if(!menu.isMenuShowing() && !menu.isSecondaryMenuShowing()){

            AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
            final AlertDialog alert = builder.create();
            builder.setTitle("Are you sure you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alert.cancel();
                }
            });
            builder.show();
        }else{
            menu.toggle();
        }

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(snappydb!=null){
                snappydb.destroy();
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

}