package com.jacobmosehansen.themeproject.Post.PostListUtil;

import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.jacobmosehansen.themeproject.R;

import java.util.ArrayList;

//public class PostDetailActivity extends ActionBarActivity implements PostSelectorInterface{
//revurder det hele.
    /*
    public enum PhoneMode {PORTRAIT, LANDSCAPE}
    public enum UserMode {LIST_VIEW, DETAIL_VIEW, SPECIAL_VIEW}

    private static final String LIST_FRAG = "list_fragment";
    private static final String DETAILS_FRAG = "details_fragment";
    private static final String SPECIALS_FRAG = "specials_fragment";

    private PostListFragment postList;
    private PostDetailFragment postDetails;
    private SpecialDetailsFragment specialDetails;
    private ArrayList<Post> posts;

    private PhoneMode phoneMode;
    private UserMode userMode;

    private int selectedPostIndex;

    private LinearLayout listContainer;
    private LinearLayout detailsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multipane_main);

        listContainer = (LinearLayout)findViewById(R.id.list_container);
        detailsContainer = (LinearLayout)findViewById(R.id.details_container);

        posts = new PostLoader(this).getMovieList();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            phoneMode = PhoneMode.PORTRAIT;
        } else {
            phoneMode = PhoneMode.LANDSCAPE;
        }

        if(savedInstanceState == null) {

            selectedPostIndex = 0;
            userMode = UserMode.LIST_VIEW;

            postList = new PostListFragment();
            postDetails = new PostDetailsFragment();
            specialDetails = new SpecialDetailsFragment();

            movieList.setMovies(movies);
            movieDetails.setMovie(movies.get(selectedMovieIndex));

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_container, movieList, LIST_FRAG)
                    .add(R.id.details_container, specialDetails, SPECIALS_FRAG)
                    .replace(R.id.details_container, movieDetails, DETAILS_FRAG)
                    .commit();
        } else {
            //got restarted, probably due to orientation change

            selectedMovieIndex = savedInstanceState.getInt("movie_position");
            userMode = (UserMode) savedInstanceState.getSerializable("user_mode");

            if(userMode == null){
                userMode = UserMode.LIST_VIEW;  //default
            }

            movieList = (MovieListFragment)getSupportFragmentManager().findFragmentByTag(LIST_FRAG);
            if(movieList==null){
                movieList = new MovieListFragment();
            }
            movieDetails = (MovieDetailsFragment)getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG);
            if(movieDetails==null){
                movieDetails = new MovieDetailsFragment();
            }
            specialDetails = (SpecialDetailsFragment)getSupportFragmentManager().findFragmentByTag(SPECIALS_FRAG);
            if(specialDetails==null){
                specialDetails = new SpecialDetailsFragment();
            }

        }

        updateFragmentViewState(userMode);
    }

    @Override
    public void onBackPressed() {
        if(phoneMode == PhoneMode.LANDSCAPE){
            //finish on back press in landscape unless specialDetails view, in that case go to standard list view
            if(userMode == UserMode.SPECIAL_VIEW) {
                updateFragmentViewState(UserMode.LIST_VIEW);
            } else {
                finish();
            }
        } else {
            if (userMode == UserMode.LIST_VIEW) {
                //quit app on back press if in list view mode
                finish();
            } else if (userMode == UserMode.SPECIAL_VIEW) {
                //go to list mode from specialDetails mode on back
                updateFragmentViewState(UserMode.LIST_VIEW);
            } else {
                //go back to list mode if in details mode
                updateFragmentViewState(UserMode.LIST_VIEW);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("movie_position", selectedMovieIndex);
        outState.putSerializable("user_mode", userMode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateFragmentViewState(UserMode targetMode){


        if(targetMode == UserMode.DETAIL_VIEW) {
            userMode = UserMode.DETAIL_VIEW;
            switchFragment(targetMode);
        } if(targetMode == UserMode.LIST_VIEW) {
            userMode = UserMode.LIST_VIEW;
            switchFragment(targetMode);
        } if(targetMode == UserMode.SPECIAL_VIEW) {
            userMode = UserMode.SPECIAL_VIEW;
            switchFragment(targetMode);
        } else {
            //ignore
        }

    }

    private boolean switchFragment(UserMode targetMode){
        if(phoneMode == PhoneMode.PORTRAIT) {
            if (targetMode == UserMode.LIST_VIEW) {
                listContainer.setVisibility(View.VISIBLE);
                detailsContainer.setVisibility(View.GONE);
                changeDetailContainerFragment(UserMode.DETAIL_VIEW);
            } else if (targetMode == UserMode.DETAIL_VIEW) {
                listContainer.setVisibility(View.GONE);
                detailsContainer.setVisibility(View.VISIBLE);
                changeDetailContainerFragment(targetMode);
            } else if (targetMode == UserMode.SPECIAL_VIEW) {
                listContainer.setVisibility(View.GONE);
                detailsContainer.setVisibility(View.VISIBLE);
                changeDetailContainerFragment(targetMode);
            }
        } else {
            if(targetMode==UserMode.LIST_VIEW){
                changeDetailContainerFragment(UserMode.DETAIL_VIEW);
            } else {
                changeDetailContainerFragment(targetMode);
            }
        }
        return true;
    }

    private void changeDetailContainerFragment(UserMode targetMode){
        switch(targetMode) {
            case DETAIL_VIEW:

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.slide_in, R.animator.slide_out)
                        .replace(R.id.details_container, movieDetails, DETAILS_FRAG)
                        .commit();

                break;

            case SPECIAL_VIEW:

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.slide_in, R.animator.slide_out)
                        .replace(R.id.details_container, specialDetails, SPECIALS_FRAG)
                        .commit();

                break;
        }
    }

    public void onPostSelected(int position) {
        if(postDetails!=null){
            Post selectedPost = posts.get(position);
            if(selectedPost!=null) {
                selectedPostIndex = position;
                movieDetails.setPost(selectedPost);
            }
        }
        updateFragmentViewState(UserMode.DETAIL_VIEW);
    }

    public ArrayList<Post> getPostList() {
        return posts;
    }

    public void viewSpecial(){
        if(userMode !=UserMode.SPECIAL_VIEW) {
            updateFragmentViewState(UserMode.SPECIAL_VIEW);
        } else {
            updateFragmentViewState(UserMode.LIST_VIEW);
        }
    }

    public Post getCurrentSelection(){
        if(posts!=null) {
            return posts.get(selectedPostIndex);
        } else {
            return null;
        }
    }

    public ArrayList<Post> loadPostList() {

        ArrayList<Post> movies;

        PostLoader loader = new PostLoader(this);
        posts = loader.getPostList();

        if(posts == null) {
            //if null create fake list
            posts = new ArrayList<Post>();

            for (int i = 0; i < 20; i++) {
                posts.add(new Post("ArnieMovie " + (i + 1), 1980 + i, "Role" + (i+1)));
            }
        }

        return movies;
    }*/
//}